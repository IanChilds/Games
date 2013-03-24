package com.games;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 26/09/12
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class Snake extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make it full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout layout = new LinearLayout(this);
    //    Button but1 = new Button(this);
        SnakeView custom_view = new SnakeView(this);
    //    Button but2 = new Button(this);
    //    layout.addView(but1);
        layout.addView(custom_view);
    //    layout.addView(but2);
        setContentView(layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.snake_menu, menu);
        return true;
    }
}