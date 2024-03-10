package solvesudoku;

import java.lang.System;
import java.util.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class SolveSudokuApplication {

  public static void main(String[] args) {
    SpringApplication.run(SolveSudokuApplication.class);
  }
  //@CrossOrigin(origins = "http://localhost:63342")
  @RestController
  class SudokuController {

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/solve")
    public int[] solveSudoku(@RequestBody int[] board) {
      int[][] realBoard = new int[9][9];
      for(int i = 0; i < 9; i++) {
        for(int j = 0; j < 9; j++) {
          realBoard[i][j] = board[9 * i + j];
        }
      }
      solve(realBoard);
      for(int i = 0; i < 9; i++) {
        for(int j = 0; j < 9; j++) {
          board[9 * i + j] = realBoard[i][j];
        }
      }
      return board;
    }
    public boolean solve(int[][] board) {
        int minAmountOfValues = 10;
        for(int i = 0; i < 9; i++) {
          for(int j = 0; j < 9; j++) {
            int amountOfCell = checkCell(board, i, j).size();
            if(board[i][j] == 0 && amountOfCell < minAmountOfValues) {
              minAmountOfValues = amountOfCell;
              if(minAmountOfValues == 0) return false;
            }
          }
        }
        for (int i = 0; i < 9; i++) {
          for (int j = 0; j < 9; j++) {
            if(board[i][j] == 0) {
              ArrayList<Integer> possibleValue = checkCell(board, i, j);
              if (possibleValue.size() == minAmountOfValues) {
                for(int k : possibleValue) {
                  board[i][j] = k;
                  if(solve(board)) return true;
                }
                board[i][j] = 0;
                return false;
              }
            }
          }
        }
        return true;
    }

    public ArrayList checkCell(int[][] board, int i, int j) {
      int[] possibleValues = new int[10];
      Arrays.fill(possibleValues, 1);
      for(int k = 0; k < 9; k++) {
        if(board[i][k] != 0) possibleValues[board[i][k]] = 0;
      }
      for(int k = 0; k < 9; k++) {
        if(board[k][j] != 0) possibleValues[board[k][j]] = 0;
      }
      for(int k = i - i % 3; k < i - i % 3 + 3; k++) {
        for(int s = j - j % 3; s < j - j % 3 + 3; s++) {
          if(board[k][s] != 0) possibleValues[board[k][s]] = 0;
        }
      }
      ArrayList<Integer> allPossibleValues = new ArrayList<Integer>();
      for(int k = 1; k < 10; k++) {
        if(possibleValues[k] == 1) allPossibleValues.add(k);
      }
      return allPossibleValues;
    }
  }
}
