package com.games;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeView extends View {
    private static int width = 10;
    private static int height = 15;
    private Pair<Integer, Integer> direction_of_movement;
    private List<Point> snakePositions = new ArrayList<Point>();
    private Point head;
    private Point fruitPosition;
    private boolean fruit_caught = false;
    private RefreshHandler mRedrawHandler = new RefreshHandler();
    private int X_start = -1;
    private int Y_start = -1;
    private int X_current = -1;
    private int Y_current = -1;
    private int sleep_time = 500;
    private int score = 0;

    private boolean game_over = false;
    Random generator = new Random();
    private static final String TAG = "MyActivity";

    public SnakeView(Context context) {
            super(context);
            initSnakeView();
        }

    private void initSnakeView() {
        direction_of_movement = new Pair<Integer,Integer>(1,0);
        // Add a snake. Need to add the head first (so the tail is the last element in the list).
        head = new Point(0,3);
        snakePositions.add(head);
        snakePositions.add(new Point(0,2));
        snakePositions.add(new Point(0,1));

        assignFruit();
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int X, Y;
        X = (int)event.getX();
        Y = (int)event.getY();

        if ((X_start == -1) || (event.getAction() == MotionEvent.ACTION_DOWN)){
            X_start = X;
            Y_start = Y;
        }
        X_current = X;
        Y_current = Y;
        return true;
    }

    protected void onMeasure(int widthSpec, int heightSpec)
    {
        int measuredWidth = MeasureSpec.getSize(widthSpec);
        int measuredHeight = MeasureSpec.getSize(heightSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private void rePosition() {
        Point new_head = new Point(head.getX() + direction_of_movement.first,
                                   head.getY() + direction_of_movement.second);
        // Add a new head to the snake.
        snakePositions.add(0,new_head);
        head = new Point(new_head.getX(), new_head.getY());
        // Remove the tail of the snake.
        if (!fruit_caught) {
            snakePositions.remove(snakePositions.size() - 1);
        }
        fruit_caught = false;
    }

    private void updateDirectionOfMovement() {
        int X_change, Y_change;
        X_change = X_current - X_start;
        Y_change = Y_current - Y_start;

        if ((Math.abs(X_change) > Math.abs(Y_change))) {
            if ((X_change < 0) && (!direction_of_movement.equals(new Pair<Integer, Integer>(1,0)))) {
                direction_of_movement = new Pair<Integer, Integer>(-1,0);
            }
            else if (!direction_of_movement.equals(new Pair<Integer, Integer>(-1,0))) {
                direction_of_movement = new Pair<Integer, Integer>(1,0);
            }
        }
        else
        {
            if ((Y_change < 0) && (!direction_of_movement.equals(new Pair<Integer, Integer>(0,1)))) {
                direction_of_movement = new Pair<Integer, Integer>(0,-1);
            }
            else if (!direction_of_movement.equals(new Pair<Integer, Integer>(0,-1))) {
                direction_of_movement = new Pair<Integer, Integer>(0,1);
            }
        }
    }

    private void checkFruit() {
        // Look to see if the head is now over the fruit. If it is, then make the snake longer and assign a new fruit.
        if (head.equals(fruitPosition)) {
            fruit_caught = true;
            score++;
            assignFruit();
            sleep_time *= 0.95;
        }
    }

    private void assignFruit() {
        Point possible_point = new Point(generator.nextInt(width), generator.nextInt(height));
        while (snakePositions.contains(possible_point)) {
            possible_point = new Point(generator.nextInt(width), generator.nextInt(height));
        }
        fruitPosition = new Point(possible_point.getX(), possible_point.getY());
    }

    private void isGameOver() {
        // first check that the head hasn't gone over one of the edges.
        Point head = new Point(snakePositions.get(0).getX(), snakePositions.get(0).getY());
        if ((head.getX() < 0) || (head.getX() >= width) || (head.getY() < 0) || (head.getY() >= height)) {
            game_over = true;
        }
        snakePositions.remove(0);
        if (snakePositions.contains(head)) {
            game_over = true;
        }
        snakePositions.add(0, head);
    }

    protected void onDraw(Canvas canvas){
        updateDirectionOfMovement();
        rePosition();
        checkFruit();
        isGameOver();
//get the size of your control based on last call to onMeasure
        int full_height = getMeasuredHeight();
        int full_width = getMeasuredWidth();
// Now create a paint brush to draw your widget
        Paint mTextPaint=new Paint();
        mTextPaint.setColor(Color.GREEN);
// set’s paint’s text size
        mTextPaint.setTextSize(full_width/width);
// Define the string you want to paint
        String displayText= "X";
// Measure width of your text string
        Float textWidth = mTextPaint.measureText(displayText);

        if (!game_over) {
            for (Point point : snakePositions) {
                canvas.drawText(displayText, (point.getX())*(full_width/width), (point.getY() + 1)*(full_height/height), mTextPaint);
            }

            mTextPaint.setColor(Color.RED);
            canvas.drawText("O", (fruitPosition.getX()) * (full_width/width), (fruitPosition.getY() + 1)*(full_height/height), mTextPaint);

            mRedrawHandler.sleep(sleep_time);
        }
        else {
            canvas.drawText("GAME OVER!!!", 0, (6 * (full_height/height)), mTextPaint);
            canvas.drawText("Score: " + Integer.toString(score), 0, (7 * (full_height/height)), mTextPaint);
        }
    }

    class RefreshHandler extends Handler  {

        public void handleMessage(Message msg) {
            SnakeView.this.invalidate();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }
}
