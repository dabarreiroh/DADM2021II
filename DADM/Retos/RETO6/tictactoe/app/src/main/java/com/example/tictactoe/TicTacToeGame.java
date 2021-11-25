package com.example.tictactoe;


import android.util.Log;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class TicTacToeGame {

    private char mBoard[] = {' ',' ',' ',' ',' ',' ',' ',' ',' '};
    public static final int BOARD_SIZE = 9;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';

    private Random mRand;
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    public enum DifficultyLevel { Easy, Harder, Expert  };

    public DifficultyLevel getDifficultyLevel(){
        return mDifficultyLevel;
    }

    public void setDifficultyLevel( DifficultyLevel level ){
        mDifficultyLevel = level;
    }

    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();
    }

    public char getBoardOccupant( int idx ){
        return mBoard[ idx ];
    }

    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard(){
        for( int i = 0; i < BOARD_SIZE; ++i )
            mBoard[ i ] = ' ';
    }


    /** Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public void setMove(char player, int location){
        mBoard[ location ] = player;
    }


    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    public int getComputerMove( )
    {
        int move;
        if( mDifficultyLevel == DifficultyLevel.Easy )
            move = getRandomMove();
        else if( mDifficultyLevel == DifficultyLevel.Harder ){
            move = getWinningMove();
            if( move == -1 )
                move = getRandomMove();
        }
        else{
            move = getWinningMove();
            if( move == -1 ) move = getBlockingMove();
            if( move == -1 ) move = getRandomMove();
        }
        return move;
    }

    // Generate random move
    private int getRandomMove() {
        int move;
        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);

        mBoard[move] = COMPUTER_PLAYER;
        return move;
    }

    // See if there's a move O can make to block X from winning
    private int getBlockingMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == OPEN_SPOT) {
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    return i;
                } else
                    mBoard[i] = OPEN_SPOT;
            }
        }
        return -1;
    }

    // See if there's a move O can make to win
    private int getWinningMove(){
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == OPEN_SPOT) {
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    return i;
                }
                else
                    mBoard[i] = OPEN_SPOT;
            }
        }
        return -1;
    }


    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    public char[] getBoardState(){
        return mBoard.clone();
    }

    public void setBoardState( char[] board ){
        for( int i = 0; i < BOARD_SIZE; ++i )
            mBoard[ i ] = board[ i ];
    }

}