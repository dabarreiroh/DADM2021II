package com.example.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    private boolean mGameOver;
    private int human_wins;
    private int android_wins;
    private int ties;
    private TextView mInfoTextView;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 2;
    static final int DIALOG_ABOUT_ID = 1;

    private BoardView mBoardView;

    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPLayer;

    private int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPLayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);

        setContentView(R.layout.activity_tic_tac_toe);



        mGame = new TicTacToeGame();
        mGameOver = false;

        human_wins = 0;
        android_wins = 0;
        ties = 0;
        mBoardView = (BoardView) findViewById(R.id.board);//findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        startNewGame();
    }

    @Override
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id){
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                int selected = 1;
                builder.setSingleChoiceItems(levels, mGame.getmDifficultyLevel().ordinal(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();
                                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.valueOf(String.valueOf(levels[item])));
                                Toast.makeText(getApplicationContext(), levels[item],Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                TicTacToeActivity.this.finish();

                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
            case DIALOG_ABOUT_ID:
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog,null);
                builder.setView(layout);
                builder.setPositiveButton("Ok",null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return true;
    }

    private void startNewGame() {
        mGame.clearBoard();
        mBoardView.invalidate();
        mGameOver = false;
        mInfoTextView=(TextView) findViewById(R.id.info_text);
        Random rand = new Random();
        int n = rand.nextInt(100);
        if(n < 50){
            mInfoTextView.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);

        }
        mInfoTextView.setText(R.string.turn_human);
        turn = TicTacToeGame.HUMAN_PLAYER;
    }

    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location){
            this.location = location;
        }
        @Override
        public void onClick(View v) {
            if(mBoardButtons[location].isEnabled() && !mGameOver){
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                int winner = mGame.checkForWinner();
                if (winner == 0){
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if(winner == 0){
                    mInfoTextView.setText(R.string.turn_human);
                } else if(winner == 1) {
                    mGameOver = true;
                    ties+=1;
                } else if (winner == 2){
                    mGameOver = true;
                    human_wins+=1;
                } else {
                    mGameOver = true;
                    android_wins+=1;
                }
            }
        }
    }

    private boolean setMove(char player, int location){
        if(player==TicTacToeGame.HUMAN_PLAYER){
            mHumanMediaPlayer.start();
        }else{
            mComputerMediaPLayer.start();
        }
        if(mGame.setMove(player,location)){
            mBoardView.invalidate();
            return true;
        }
        return false;

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(turn == TicTacToeGame.HUMAN_PLAYER) {
                int col = (int) event.getX() / mBoardView.getBoardCelWidth();
                int row = (int) event.getY() / mBoardView.getBoardCelHeight();
                int pos = row * 3 + col;
                if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView.setText(R.string.turn_computer);
                        turn = TicTacToeGame.COMPUTER_PLAYER;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int move = mGame.getComputerMove();
                                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                                int winner = mGame.checkForWinner();
                                if (winner == 0) {
                                    turn = TicTacToeGame.HUMAN_PLAYER;
                                } else if (winner == 1) {
                                    mGameOver = true;
                                    ties += 1;
                                } else if (winner == 2) {
                                    mGameOver = true;
                                    human_wins += 1;
                                } else {
                                    mGameOver = true;
                                    android_wins += 1;
                                }
                            }
                        }, 1000);
                    } else if (winner == 1) {
                        mInfoTextView.setText(R.string.result_tie);
                        mGameOver = true;
                        ties += 1;
                    } else if (winner == 2) {
                        mGameOver = true;
                        human_wins += 1;
                    } else {
                        mGameOver = true;
                        android_wins += 1;
                    }
                }
            }
            return false;
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPLayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPLayer.release();
    }
}

