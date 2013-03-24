package com.games;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 26/09/12
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class ShowPath extends Activity
{
    DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Create an instance of the dialog fragment and show it
        drawView = new DrawView(this);
        setContentView(drawView);
        drawView.requestFocus();
    }
}
