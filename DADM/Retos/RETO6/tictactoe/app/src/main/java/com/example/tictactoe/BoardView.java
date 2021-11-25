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

    public BoardView(Context context) {
        super(context);
        initialize();
    }
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void initialize(){
        mHumanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x_img);
        mComputerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o_img);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int boardWidth = getWidth();
        int boardHeight = getHeight();

        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(GRID_WIDTH);

        int cellWidth = boardWidth / 3;
        canvas.drawLine(cellWidth,0,cellWidth,boardHeight,mPaint);
        canvas.drawLine(cellWidth*2,0,cellWidth*2,boardHeight,mPaint);

        canvas.drawLine(0,cellWidth,boardWidth,cellWidth,mPaint);
        canvas.drawLine(0,cellWidth*2,boardWidth,cellWidth*2,mPaint);

        for (int i = 0;i < TicTacToeGame.BOARD_SIZE; i++){
            int col = i % 3;
            int row = i / 3;

            int left = (cellWidth*col);
            int top = (cellWidth*row);
            int right = (cellWidth*(col+1));
            int bottom = (cellWidth*(row+1));

            if(mGame != null && mGame.getBoardOcupant(i) == TicTacToeGame.HUMAN_PLAYER){
                canvas.drawBitmap(mHumanBitmap,
                        null,
                        new Rect(left,top,right,bottom),
                        null);
            }else if(mGame != null && mGame.getBoardOcupant(i) == TicTacToeGame.COMPUTER_PLAYER){
                canvas.drawBitmap(mComputerBitmap,
                        null,
                        new Rect(left,top,right,bottom),
                        null);
            }
        }
    }

    public void setGame(TicTacToeGame game){
        this.mGame = game;
    }

    public int getBoardCelWidth(){
        return getWidth() / 3;
    }

    public int getBoardCelHeight(){
        return  getHeight() / 3;
    }

}

