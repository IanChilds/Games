package com.games;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 16/03/13
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class Dice extends Activity {

    private Bitmap[] pic = new Bitmap[6];
    public int current_side = 0;
    private int diceRollsLeft;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dice);

        ImageView diceImage = (ImageView) findViewById(R.id.diceView);

        Bitmap diceSides = BitmapFactory.decodeResource(getResources(), R.drawable.dice_sides);
        for(int side = 0; side < 6; side++)  // Split the single alphabet image into separate bitmaps
        {
            pic[side] = Bitmap.createBitmap(diceSides, side * 100, 0, 100, diceSides.getHeight());
        }
        diceImage.setImageBitmap(pic[current_side]);
    }

    public void setDiceImage() {
        ImageView diceImage = (ImageView) findViewById(R.id.diceView);
        diceImage.setImageBitmap(pic[current_side]);
    }

    public void rollDiceSeq (View view) {
        diceRollsLeft = 10;
        rollDiceOnce();
    }

    private void rollDiceOnce() {
        int rand;
        Random random = new Random();
        do {
            rand = random.nextInt(6);
        } while (rand == current_side);
        current_side = rand;

        Handler handler = new Handler();
        handler.postDelayed(runRollDice, 250);
        diceRollsLeft--;
    }

    private Runnable runRollDice = new Runnable() {
        public void run() {
            setDiceImage();
            if (diceRollsLeft >= 0) rollDiceOnce();
        }
    };



}