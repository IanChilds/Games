package com.games;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

/* Next task here is maybe to add a button allowing some kind of reset. Presumably this should just clear out the
list of points. Another thing to do is to learn about lists in java.
 */
public class DrawView extends View implements OnTouchListener {
    private static final String TAG = "DrawView";
    private RefreshHandler mRedrawHandler = new RefreshHandler();

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);

        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    public void clearScreen(View view){
        points.clear();
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (Point point : points) {
            canvas.drawCircle(point.getX(), point.getY(), 5, paint);
            // Log.d(TAG, "Painting: "+point);
        }
        mRedrawHandler.sleep(2000);
    }
    public boolean onTouch(View view, MotionEvent event) {
        // This is the standard version which follows you around.
        Point point = new Point((int)event.getX(), (int)event.getY());
        points.add(point);
        //invalidate();
        Log.d(TAG, "point: " + point);
        return true;
    }

/*
    public boolean onTouch(View view, MotionEvent event) {
        // This is the version that just prints where you touch down and pick up.
        if ((event.getAction() == MotionEvent.ACTION_DOWN) ||
            (event.getAction() == MotionEvent.ACTION_UP))
        {
            Point point = new Point();
            point.x = event.getX();
            point.y = event.getY();
            points.add(point);
            invalidate();
            Log.d(TAG, "point: " + point);
        }
        return true;
    }
*/

    class RefreshHandler extends Handler {

        public void handleMessage(Message msg) {
            DrawView.this.invalidate();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

}
