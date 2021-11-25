package com.example.tictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;



public class BoardView extends View {

    public static final int GRID_WIDTH = 6;

    private Bitmap mHumanBitmap;
    private Bitmap mComputerBitmap;
    private Paint mPaint;
    private TicTacToeGame mGame;

    public BoardView( Context context ){
        super( context );
        initialize();
    }

    public BoardView( Context context, AttributeSet attrs ){
        super( context, attrs );
        initialize();
    }

    public BoardView( Context context, AttributeSet attrs, int defStyle ){
        super( context, attrs, defStyle );
        initialize();
    }

    public void initialize(){
        mHumanBitmap = BitmapFactory.decodeResource( getResources(), R.drawable.x_img );
        mComputerBitmap = BitmapFactory.decodeResource( getResources(), R.drawable.o_img );
        mPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int boardWidth = getWidth();
        int boardHeight = getHeight();

        mPaint.setColor( Color.LTGRAY );
        mPaint.setStrokeWidth( GRID_WIDTH );

        int cellWidth = boardWidth / 3;
        canvas.drawLine( cellWidth, 0, cellWidth, boardHeight, mPaint );
        canvas.drawLine( 2*cellWidth, 0, 2*cellWidth, boardHeight, mPaint );

        int cellHeight = boardHeight / 3;
        canvas.drawLine( 0, cellHeight, boardWidth, cellHeight, mPaint );
        canvas.drawLine( 0, 2*cellHeight, boardWidth, 2*cellHeight, mPaint );

        for( int i = 0; i < TicTacToeGame.BOARD_SIZE; ++i ){
            int row = i/3;
            int col = i%3;

            int left = (col != 0) ? col*cellWidth + GRID_WIDTH : 0;
            int right = (col != 2) ?  (col+1)*cellWidth - GRID_WIDTH : (col+1)*cellWidth;
            int top = (row != 0) ? row*(cellHeight) + GRID_WIDTH : 0;
            int bottom = (row != 2) ? (row+1)*(cellHeight) - GRID_WIDTH : (row+1)*(cellHeight);

            if( mGame != null ){
                if( mGame.getBoardOccupant( i ) == TicTacToeGame.COMPUTER_PLAYER ){
                    canvas.drawBitmap( mComputerBitmap, null, new Rect( left, top, right, bottom ), null );
                }
                else if( mGame.getBoardOccupant( i ) == TicTacToeGame.HUMAN_PLAYER ){
                    canvas.drawBitmap( mHumanBitmap, null, new Rect( left, top, right, bottom ), null );
                }
            }
        }

    }

    public int getBoardCellWidth(){
        return getWidth()/3;
    }

    public int getBoardCellHeight(){
        return getHeight()/3;
    }

    public void setGame( TicTacToeGame game ){
        mGame = game;
    }
}
