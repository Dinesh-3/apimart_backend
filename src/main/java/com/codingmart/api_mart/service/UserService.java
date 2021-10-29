package com.codingmart.api_mart.service;

import com.codingmart.api_mart.ExceptionHandler.ClientErrorException;
import com.codingmart.api_mart.config.jwt_configure.JwtTokenProvider;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.model.Verification;
import com.codingmart.api_mart.repository.UserRepository;
import com.codingmart.api_mart.repository.VerificationRepository;
import com.codingmart.api_mart.utils.GetTokenPayload;
import com.codingmart.api_mart.utils.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.codingmart.api_mart.utils.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private VerificationRepository verificationRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private MailService mailService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public UserService(UserRepository userRepository, VerificationRepository verificationRepository, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, MailService mailService) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.mailService = mailService;
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public User getUserById(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String name = GetTokenPayload.getPayload(token, "sub");
        return userRepository.findByName(name);
    }

    public ResponseBody signup(User user) {
        boolean isEmailOrNameExists = userRepository.isEmailOrNameExists(user.getName(), user.getEmail());

        if( isEmailOrNameExists ) return new ResponseBody(false, 400,  "Email or Name Already Exist", null);

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        userRepository.save(user);
        sendVerifyLink(user.getUser_id());
        return new ResponseBody("Signup Success, Check mail to verify email", user);
    }


    public ResponseBody login(User user) {
        User savedUser = userRepository.findByEmail(user.getEmail());
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(savedUser.getName(), user.getPassword()));
        Verification verify = verificationRepository.findById(savedUser.getUser_id());
        if(!verify.isIs_email_verified()) throw new ClientErrorException(HttpStatus.FORBIDDEN, "Email Not Verified. Verify email to continue");
        HashMap<String, Object> body = new HashMap<>();
        body.put("token", tokenProvider.createToken(savedUser.getName(), savedUser.getEmail(), savedUser.getUser_id()));
        body.put("user", savedUser);
        return new ResponseBody("Login Success", body);
    }

    public boolean verifyEmail(String otp, String id) {
        Verification verification = verificationRepository.findById(id);
        if(verification.isIs_email_verified()) throw new ClientErrorException(HttpStatus.FORBIDDEN, "Email already verified");
        if(!verification.getOtp().equals(otp)) throw new ClientErrorException(HttpStatus.FORBIDDEN, "Invalid OTP. Try again");
        if(!OtpUtil.isValid(LocalDateTime.parse(verification.getOtp_created_at()))) {
            try {
                verification.setOtp(OtpUtil.getOtp());
                verification.setOtp_created_at(LocalDateTime.now().toString());
                User user = userRepository.findById(id);
                verificationRepository.findOneAndUpdate(verification.getUser_id(), verification);
                mailService.sendForVerify(user, verification);
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new ClientErrorException(HttpStatus.BAD_REQUEST, "Expired Link. New Verify link sent to mail");
        }
        verification.setIs_email_verified(true);
        verificationRepository.findOneAndUpdate(id, verification);
        return true;
    }

    private void sendVerifyLink(String id) {
        try {
            String otp = OtpUtil.getOtp();
            User user = userRepository.findById(id);
            Verification verification = new Verification(id, LocalDateTime.now().toString(), otp);
            verificationRepository.save(verification);
            mailService.sendForVerify(user, verification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVerifyEmail(String id) {
        Verification verification = verificationRepository.findById(id);
        User user = userRepository.findById(id);
        if(verification.isIs_email_verified()) throw new ClientErrorException(HttpStatus.FORBIDDEN, "Email already verified");
        String otp = OtpUtil.getOtp();
        verification.setOtp(otp);
        verification.setOtp_created_at(LocalDateTime.now().toString());
        verificationRepository.findOneAndUpdate(verification.getUser_id(), verification);
        mailService.sendForVerify(user, verification);
    }
}
