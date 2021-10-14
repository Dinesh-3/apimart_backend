package com.codingmart.api_mart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {

    public long possiblePathsInGrid(int count) {
        return countTotalPaths(count, 0, 0, new int[count+1][count+1]);
    }

    public int countPathsWithoutMem(int count) {
        return countPathsWithoutMem(count, 0, 0);
    }

    private static int countTotalPaths(int n, int row, int column, int[][] mem) {
        if(row > n || column > n) return 0;
        if(row == n && column == n) return 1;
        if(mem[row][column] == 0){
            mem[row][column] = countTotalPaths(n,row+1, column, mem) + countTotalPaths(n, row, column+1, mem);
        }

        return mem[row][column];
    }

    private static int countPathsWithoutMem(int n, int row, int column) {
        if(row > n || column > n) return 0;
        if(row == n && column == n) return 1;
            return countPathsWithoutMem(n,row+1, column) + countPathsWithoutMem(n, row, column+1);
    }
}
