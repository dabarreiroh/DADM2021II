package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Random;

public class TicTacToeActivity extends AppCompatActivity {

    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;
    static final int COMPUTER_DELAY = 600;

    private TicTacToeGame mGame;
    private BoardView mBoardView;
    private TextView mInfoTextView, mScoreTextView;
    private boolean mGameOver;
    private boolean mFirstHuman;
    private boolean mHumanTurn;
    private boolean mSoundOn;
    private int mCntHumanWins;
    private int mCntTies;
    private int mCntAndroidWins;
    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPlayer;
    private SharedPreferences mPrefs;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if( mGameOver || !mHumanTurn ) return false;

            int col = (int) motionEvent.getX() / mBoardView.getBoardCellWidth();
            int row = (int) motionEvent.getY() / mBoardView.getBoardCellHeight();
            int pos = 3*row + col;

            if( mGame.getBoardOccupant( pos ) == TicTacToeGame.COMPUTER_PLAYER || mGame.getBoardOccupant( pos ) == TicTacToeGame.HUMAN_PLAYER ) return false;

            setMove( TicTacToeGame.HUMAN_PLAYER, pos );

            int winner = mGame.checkForWinner();
            if( winner == 0 ){
                makeComputerMove();
            }
            else endGame( winner );
            return false;
        }
    };

    private void makeComputerMove(){
        mHumanTurn = false;
        mInfoTextView.setText( R.string.turn_computer );
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setMove( TicTacToeGame.COMPUTER_PLAYER, mGame.getComputerMove() );
                mInfoTextView.setText(R.string.turn_human);
                mHumanTurn = true;
                int winner = mGame.checkForWinner();
                if( winner == 0 ) mInfoTextView.setText( R.string.turn_human );
                else endGame( winner );
            }
        }, COMPUTER_DELAY);
    }

    private void startGame(){
        mGame.clearBoard();
        mGameOver = false;
        mBoardView.invalidate();
        updateWinsCnt();
        if( mFirstHuman ) {
            mInfoTextView.setText(R.string.first_human);
            mHumanTurn = true;
        }
        else{
            makeComputerMove();
        }
    }

    public void endGame( int winner ){
        mGameOver = true;
        if (winner == 1){
            ++mCntTies;
            mInfoTextView.setText( getResources().getString( R.string.result_tie ) );
        }
        else if (winner == 2){
            ++mCntHumanWins;
            mInfoTextView.setText( mPrefs.getString( "victory_message", getResources().getString( R.string.result_human_wins ) ) );
        }
        else{
            ++mCntAndroidWins;
            mInfoTextView.setText( getResources().getString( R.string.result_computer_wins ) );
        }
        updateWinsCnt();
        mFirstHuman = !mFirstHuman;
    }

    private void setMove( char player, int location ){
        if( mSoundOn ) {
            try {
                if (player == TicTacToeGame.COMPUTER_PLAYER) mComputerMediaPlayer.start();
                else mHumanMediaPlayer.start();
            } catch (IllegalStateException e) {
            }
        }
        mGame.setMove( player, location );
        mBoardView.invalidate();
    }

    private void updateWinsCnt(){
        mScoreTextView.setText(String.format("Score Human:%dTie:%dAndroid:%d", mCntHumanWins, mCntTies, mCntAndroidWins));
            }

    private String getDifficultyLevelString( TicTacToeGame.DifficultyLevel diffLevel ){
        if( diffLevel == TicTacToeGame.DifficultyLevel.Easy )
            return getResources().getString( R.string.difficulty_easy );
        else if( diffLevel == TicTacToeGame.DifficultyLevel.Harder )
            return getResources().getString( R.string.difficulty_harder );
        else
            return getResources().getString( R.string.difficulty_expert );
    }

    private void setGameDifficulty( String difficultyLevel ){
        if ( difficultyLevel.equals( getResources().getString( R.string.difficulty_easy ) ) )
            mGame.setDifficultyLevel( TicTacToeGame.DifficultyLevel.Easy );
        else if ( difficultyLevel.equals( getResources().getString( R.string.difficulty_harder ) ) )
            mGame.setDifficultyLevel( TicTacToeGame.DifficultyLevel.Harder );
        else
            mGame.setDifficultyLevel( TicTacToeGame.DifficultyLevel.Expert );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        mBoardView = (BoardView) findViewById( R.id.board );
        mBoardView.setOnTouchListener( mTouchListener );
        mInfoTextView = (TextView) findViewById(R.id.info_text);
        mScoreTextView = (TextView) findViewById(R.id.score_text);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mCntHumanWins = mPrefs.getInt( "mCntHumanWins", 0 );
        mCntAndroidWins = mPrefs.getInt( "mCntAndroidWins", 0 );
        mCntTies = mPrefs.getInt( "mCntTies", 0 );
        mFirstHuman = mPrefs.getBoolean( "mFirstHuman", true );
        mSoundOn = mPrefs.getBoolean( "sound", true );

        if( savedInstanceState == null ) {
            mGame = new TicTacToeGame();
            setGameDifficulty( mPrefs.getString( "difficulty_level", getResources().getString(R.string.difficulty_expert) ) );
            mBoardView.setGame( mGame );
            mHumanTurn = mFirstHuman;
            startGame();
        }
        else{
            mGame = new TicTacToeGame();
            setGameDifficulty( mPrefs.getString( "difficulty_level", getResources().getString(R.string.difficulty_expert) ) );
            mGame.setBoardState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            mHumanTurn = savedInstanceState.getBoolean("mHumanTurn");
            mBoardView.setGame( mGame );
            mBoardView.invalidate();
            updateWinsCnt();
            if( !mHumanTurn ) makeComputerMove();
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putInt("mCntHumanWins", Integer.valueOf(mCntHumanWins));
        outState.putInt("mCntAndroidWins", Integer.valueOf(mCntAndroidWins));
        outState.putInt("mCntTies", Integer.valueOf(mCntTies));
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putBoolean("mHumanTurn", mHumanTurn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate( R.menu.options_menu, menu );
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mCntHumanWins", mCntHumanWins);
        ed.putInt("mCntAndroidWins", mCntAndroidWins);
        ed.putInt("mCntTies", mCntTies);
        String difficultyLevel;
        ed.putString("difficulty_level", getDifficultyLevelString(mGame.getDifficultyLevel()));
        ed.putBoolean("mFirstHuman", mFirstHuman);
        ed.putBoolean("sound", mSoundOn);
        ed.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ){
            case R.id.new_game:
                startGame();
                return true;
            case R.id.settings:
                startActivityForResult( new Intent( this, Settings.class ), 0 );
                return true;
            //case R.id.about:
            //   showDialog( DIALOG_ABOUT_ID );
            //  return true;
            case R.id.reset:
                mCntAndroidWins = mCntTies = mCntHumanWins = 0;
                startGame();
//            case R.id.quit:
//                showDialog( DIALOG_QUIT_ID );
//                return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_CANCELED ){
            mSoundOn = mPrefs.getBoolean( "sound", true );
            String difficultyLevel = mPrefs.getString( "difficulty_level", getResources().getString( R.string.difficulty_expert ) );
            setGameDifficulty( difficultyLevel );
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

}