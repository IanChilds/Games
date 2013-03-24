package com.games;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 27/02/13
 * Time: 20:30
 * To change this template use File | Settings | File Templates.
 */
public class PlayerMenu extends Activity implements LevelSelectionDialogFragment.LevelSelectionDialogListener {
    public final static String PLAYER_1_NAME = "com.example.games.PLAYER1";
    public final static String PLAYER_2_NAME = "com.example.games.PLAYER2";
    public final static String PLAY_AGAINST_COMPUTER = "com.example.games.PLAY_AGAINST_COMPUTER";
    public final static String LEVEL = "com.example.games.LEVEL";
    private String gameType;
    private String level = "Easy";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent receivedIntent = getIntent();
        gameType = receivedIntent.getStringExtra(ShowAllGames.GAME_TYPE);
        setContentView(R.layout.ncmenu);
        Button difficulty = (Button) findViewById(R.id.level);
        difficulty.setText("Difficulty: " + level);
        difficulty.setVisibility(View.GONE);
    }

    public void startGame(View view) {
        // Do something in response to play button
        Intent intent;

        if (gameType.equals(ShowAllGames.NOUGHTS_AND_CROSSES)) intent = new Intent(this, NoughtsAndCrosses.class);
        else {
            assert(gameType.equals(ShowAllGames.CONNECT_FOUR));
            intent = new Intent(this, ConnectFour.class);
        }

        EditText player1Name = (EditText) findViewById(R.id.player1_name);
        String name = player1Name.getText().toString();
        intent.putExtra(PLAYER_1_NAME, name);

        EditText player2Name = (EditText) findViewById(R.id.player2_name);
        name = player2Name.getText().toString();
        intent.putExtra(PLAYER_2_NAME, name);

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_computer_play);
        boolean play_against_computer = checkBox.isChecked();
        intent.putExtra(PLAY_AGAINST_COMPUTER, play_against_computer);

        intent.putExtra(LEVEL, level);

        startActivity(intent);
    }

    public void onCheckboxClicked (View view) {
        //Change whether we can see the textbox for player 2's name or not
        boolean checked = ((CheckBox)view).isChecked();
        EditText player2Name = (EditText) findViewById(R.id.player2_name);
        Button difficulty = (Button) findViewById(R.id.level);
        if (checked) {
            player2Name.setVisibility(View.GONE);
            difficulty.setVisibility(View.VISIBLE);
        }
        else {
            player2Name.setVisibility(View.VISIBLE);
            difficulty.setVisibility(View.GONE);
        }
    }

    public void selectLevel (View view) {
        DialogFragment dialog = new LevelSelectionDialogFragment();
        dialog.show(getFragmentManager(), "LevelSelectionDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int position) {
        // User touched the dialog's positive button
        // Set the level of the game.
        if (position == 0) level = "Easy";
        else if (position == 1) level = "Medium";
        else level = "Hard";

        Button difficulty = (Button) findViewById(R.id.level);
        difficulty.setText("Difficulty: " + level);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button - do nothing.
    }

    public int getCurrentPosition() {
        if (level.equals("Easy")) return 0;
        if (level.equals("Medium")) return 1;
        else return 2;
    }

}
