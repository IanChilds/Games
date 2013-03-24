package com.games;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 26/09/12
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */

public class NoughtsAndCrosses extends Activity implements RestartDialogFragment.RestartDialogListener
{
    private String player1;
    private String player2;
    private boolean play_against_computer;
    private boolean player1Turn = true;
    private boolean game_over = false;
    private static final String no_entry = "";
    public String[] myStringArray = {no_entry, no_entry, no_entry, no_entry, no_entry, no_entry, no_entry, no_entry, no_entry};
    GridView gridView;
    TextView textView;
    Button button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        player1 = intent.getStringExtra(PlayerMenu.PLAYER_1_NAME);
        player2 = intent.getStringExtra(PlayerMenu.PLAYER_2_NAME);
        play_against_computer = intent.getBooleanExtra(PlayerMenu.PLAY_AGAINST_COMPUTER, false);
        if (player1.equals("")) player1 = "Player1";
        if (play_against_computer) player2 = "Computer";
        else if (player2.equals("")) player2 = "Player2";

        setContentView(R.layout.ncgrid);
        drawGrid();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (myStringArray[position].equals(no_entry)) {
                    if (player1Turn) myStringArray[position] = "X";
                    else myStringArray[position] = "O";
                    player1Turn = !player1Turn;
                    drawGrid();

                    if (!game_over && play_against_computer && player1Turn == false) {
                        // Probably want a sleep here.
                        int next_move = bestNextMove(myStringArray, "O", "X");
                        myStringArray[next_move] = "O";
                        player1Turn = !player1Turn;
                        drawGrid();
                    }
                }
            }
        });
    }

    private void drawGrid() {
        gridView = (GridView) findViewById(R.id.grid_view);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                                                        R.layout.gridcell,
                                                        myStringArray);
        gridView.setAdapter(adapter);
        textView = (TextView) findViewById(R.id.player_name);
        button = (Button) findViewById(R.id.restart);
        if (hasWon("X")) {
            textView.setText(player1 + " Wins!!!");
            game_over = true;
            gridView.setOnItemClickListener(null);
            button.setText("New Game");
        }
        else if (hasWon("O")) {
            textView.setText(player2 + " Wins!!!");
            game_over = true;
            gridView.setOnItemClickListener(null);
            button.setText("New Game");
        }
        else if (gameFinished(myStringArray)) {
            textView.setText("It's a draw!!!");
            game_over = true;
            gridView.setOnItemClickListener(null);
            button.setText("New Game");
        }
        else {
            if (player1Turn) textView.setText(player1 + "'s Turn:");
            else textView.setText(player2 + "'s Turn:");
        }
    }

    private boolean gameFinished(String[] s) {
        for (String elem : s) {
            if (elem.equals(no_entry)) return false;
        }
        return true;
    }

    private boolean hasWon (String s) {
        boolean success;
        for (int ii = 0; ii < 3; ii++) {
            success = true;
            for (int jj = 0; jj < 3; jj++) {
                if (!myStringArray[ii * 3 + jj].equals(s)) success = false;
            }
            if (success) return true;
            success = true;
            for (int jj = 0; jj < 3; jj++) {
                if (!myStringArray[jj * 3 + ii].equals(s)) success = false;
            }
            if (success) return true;
        }
        success = true;
        for (int ii = 0; ii <= 8; ii += 4) {
            if (!myStringArray[ii].equals(s)) success = false;
        }
        if (success) return true;
        success = true;
        for (int ii = 2; ii <= 6; ii += 2)  {
            if (!myStringArray[ii].equals(s)) success = false;
        }
        if (success) return true;
        return false;
    }

    private void restartCurrentGame () {
        game_over = false;
        for (int ii = 0; ii < 9; ii++) myStringArray[ii] = no_entry;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (myStringArray[position] == no_entry) {
                    if (player1Turn) myStringArray[position] = "X";
                    else myStringArray[position] = "O";
                    player1Turn = !player1Turn;
                    drawGrid();

                    if (!game_over && play_against_computer && player1Turn == false) {
                        // Probably want a sleep here.
                        int next_move = bestNextMove(myStringArray, "O", "X");
                        myStringArray[next_move] = "O";
                        player1Turn = !player1Turn;
                        drawGrid();
                    }
                }
            }
        });
        button.setText("Restart");
        drawGrid();

        if (play_against_computer && player1Turn == false) {
            // Probably want a sleep here.
            int next_move = bestNextMove(myStringArray, "O", "X");
            myStringArray[next_move] = "O";
            player1Turn = !player1Turn;
            drawGrid();
        }
    }

    public void buttonPressed(View view) {
        if (game_over) restartCurrentGame();
        else showRestartDialog();
    }

    private void showRestartDialog() {
        DialogFragment dialog = new RestartDialogFragment();
        dialog.show(getFragmentManager(), "RestartDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        // We do want to restart the current game.
        this.restartCurrentGame();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button - do nothing.
    }

    // Some methods for playing as the computer.
    // The computer uses Minimax to find the best next move.

    // This should never be called for a game that is over.
    private int bestNextMove (String[] s, String curr_player, String other_player) {
        int current_best_move = -1;
        int current_best_score = -10;

        assert(!gameFinished(s));

        for (int ii = 0; ii < s.length; ii++) {
            if (s[ii] == no_entry) {
                s[ii] = curr_player;
                int best_score_from_this_pos = minimaxMin(s, other_player, curr_player, current_best_score);
                if (best_score_from_this_pos > current_best_score) {
                    current_best_score = best_score_from_this_pos;
                    current_best_move = ii;
                }
                s[ii] = no_entry;
            }
        }

        return current_best_move;
    }

    private int minimaxMax(String[] s, String curr_player, String other_player, int alpha) {
        int current_best_score = -10;

        if (hasWon(other_player)) return -1;
        else if (gameFinished(s)) return 0;
        else {
            for (int ii = 0; ii < s.length; ii++) {
                // Do some alpha pruning.
                if (current_best_score >= alpha) break;
                if (s[ii].equals(no_entry)) {
                    s[ii] = curr_player;
                    int best_score_from_this_pos = minimaxMin(s, other_player, curr_player, current_best_score);
                    if (best_score_from_this_pos > current_best_score) {
                        current_best_score = best_score_from_this_pos;
                    }
                    s[ii] = no_entry;
                }
            }
            return current_best_score;
        }
    }

    private int minimaxMin(String[] s, String curr_player, String other_player, int beta) {
        int current_lowest_score = 10;
        if (hasWon(other_player)) return 1;
        else if (gameFinished(s)) return 0;
        else {
            for (int ii = 0; ii < s.length; ii++) {
                // Do some beta pruning first
                if (current_lowest_score <= beta) break;
                if (s[ii].equals(no_entry)) {
                    s[ii] = curr_player;
                    int lowest_score_from_this_pos = minimaxMax(s, other_player, curr_player, current_lowest_score);
                    if (lowest_score_from_this_pos < current_lowest_score) {
                        current_lowest_score = lowest_score_from_this_pos;
                    }
                    s[ii] = no_entry;
                }
            }
            return current_lowest_score;
        }
    }
}