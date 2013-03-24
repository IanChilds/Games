package com.games;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 16/03/13
 * Time: 22:42
 * To change this template use File | Settings | File Templates.
 */
public class ConnectFour extends Activity implements RestartDialogFragment.RestartDialogListener {
    private String player1;
    private String player2;
    private boolean play_against_computer;
    private boolean player1Turn = true;
    private boolean game_over = false;
    private static final String red = "X";
    private static final String yellow = "O";
    private static final String empty = "";
    private static final int grid_width = 7;
    private static final int grid_height = 6;
    public String[] myStringArray = new String[grid_width * grid_height];
    private int depth = 0;
    private int computerNextMove;
    GridView gridView;
    TextView textView;
    Button button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        player1 = intent.getStringExtra(PlayerMenu.PLAYER_1_NAME);
        player2 = intent.getStringExtra(PlayerMenu.PLAYER_2_NAME);
        play_against_computer = intent.getBooleanExtra(PlayerMenu.PLAY_AGAINST_COMPUTER, false);
        String level = intent.getStringExtra(PlayerMenu.LEVEL);
        if (level.equals("Easy")) depth = 3;
        if (level.equals("Medium")) depth = 5;
        if (level.equals("Hard")) depth = 7;
        if (player1.equals("")) player1 = "Player1";
        if (play_against_computer) player2 = "Computer";
        else if (player2.equals("")) player2 = "Player2";

        setContentView(R.layout.c4grid);
        for (int ii = 0; ii < myStringArray.length; ii++) myStringArray[ii] = empty;
        drawGrid();
        setListener(gridView);
    }

    private void drawGrid() {
        gridView = (GridView) findViewById(R.id.grid_view);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.gridcell,
                myStringArray);

        gridView.setAdapter(adapter);
        textView = (TextView) findViewById(R.id.player_name);
        button = (Button) findViewById(R.id.restart);
        if (hasWon(red)) {
            textView.setText(player1 + " Wins!!!");
            game_over = true;
            gridView.setOnItemClickListener(null);
            button.setText("New Game");
        }
        else if (hasWon(yellow)) {
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
            if (elem.equals(empty)) return false;
        }
        return true;
    }

    private int availableColumnSpace(String[] s, int column) {
        for (int ii = grid_height - 1; ii >= 0; ii--) {
            if (s[ii*grid_width + column].equals(empty)) return ii;
        }
        // Returns -1 if no space in the column is available.
        return -1;
    }

    // This assume that the last move was taken by s.
    // Remember that the grid is effectively upside-down
    private boolean hasWon (String s, int lastMove) {
        int lastMoveRow = lastMove / grid_width;
        int lastMoveCol = lastMove % grid_width;
        int numRight = 0;
        int numLeft = 0;
        int numUp = 0;
        int numDown = 0;
        int numUpRight = 0;
        int numUpLeft = 0;
        int numDownRight = 0;
        int numDownLeft = 0;

        int ii = 1;
        while ((lastMoveCol + ii < grid_width) &&
               (ii < 4) &&
               (myStringArray[lastMoveRow * grid_width + lastMoveCol + ii].equals(s))) { numRight += 1; ii += 1; }

        ii = 1;
        while ((lastMoveCol - ii >= 0) &&
                (ii < 4) &&
                (myStringArray[lastMoveRow * grid_width + lastMoveCol - ii].equals(s))) { numLeft += 1; ii += 1; }

        ii = 1;
        while ((lastMoveRow + ii < grid_height) &&
                (ii < 4) &&
                (myStringArray[(lastMoveRow + ii) * grid_width + lastMoveCol].equals(s))) { numUp += 1; ii += 1; }

        ii = 1;
        while ((lastMoveRow - ii >= 0) &&
                (ii < 4) &&
                (myStringArray[(lastMoveRow - ii) * grid_width + lastMoveCol].equals(s))) { numDown += 1; ii += 1; }

        ii = 1;
        while ((lastMoveCol + ii < grid_width) &&
               (lastMoveRow + ii < grid_height) &&
                (ii < 4) &&
                (myStringArray[(lastMoveRow + ii) * grid_width + lastMoveCol + ii].equals(s))) { numUpRight += 1; ii += 1; }

        ii = 1;
        while ((lastMoveCol - ii >= 0) &&
                (lastMoveRow + ii < grid_height) &&
                (ii < 4) &&
                (myStringArray[(lastMoveRow + ii) * grid_width + lastMoveCol - ii].equals(s))) { numUpLeft += 1; ii += 1; }

        ii = 1;
        while ((lastMoveCol + ii < grid_width) &&
                (lastMoveRow - ii >= 0) &&
                (ii < 4) &&
                (myStringArray[(lastMoveRow - ii) * grid_width + lastMoveCol + ii].equals(s))) { numDownRight += 1; ii += 1; }

        ii = 1;
        while ((lastMoveRow - ii >= 0) &&
                (lastMoveCol - ii >= 0) &&
                (ii < 4) &&
                (myStringArray[(lastMoveRow - ii) * grid_width + lastMoveCol - ii].equals(s))) { numDownLeft += 1; ii += 1; }

        return ((numUp + numDown >= 3) ||
                (numLeft + numRight >= 3) ||
                (numUpRight + numDownLeft >= 3) ||
                (numUpLeft + numDownRight >= 3));
    }

    private boolean hasWon (String s) {
        boolean success;
        // vertical
        for (int ii = 0; ii < 3 * grid_width; ii++) {
            success = true;
            for (int jj = 0; jj < 4; jj++) {
                if (!myStringArray[ii + jj * grid_width].equals(s)) success = false;
            }
            if (success) return true;
        }
        // horizontal
        for (int ii = 0; ii < grid_width * grid_height; ii++) {
            if ((ii % 7) < 4) {
                success = true;
                for (int jj = 0; jj < 4; jj++) {
                    if (!myStringArray[ii + jj].equals(s)) success = false;
                }
                if (success) return true;
            }
        }
        // diagonals
        for (int ii = 0; ii < grid_width * 3; ii++) {
            if ((ii % 7) < 4) {
                success = true;
                for (int jj = 0; jj < 4; jj++) {
                    if (!myStringArray[ii + jj*(grid_width+1)].equals(s)) success = false;
                }
                if (success) return true;
            }
            if ((ii % 7) > 2) {
                success = true;
                for (int jj = 0; jj < 4; jj++) {
                    if (!myStringArray[ii + jj*(grid_width-1)].equals(s)) success = false;
                }
                if (success) return true;
            }
        }
        return false;
    }

    private int evaluatePosition (String s) {
        return 0;
//        int ret_val = 0;
//        for (int ii = 0; ii < grid_width * grid_height; ii++) {
//            if (myStringArray[ii].equals(s)) ret_val += (Math.min(ii % 7, 7 - (ii % 7)));
//            else if (!myStringArray[ii].equals(empty)) ret_val -= (Math.min(ii % 7, 7 - (ii % 7)));
//        }
//        return ret_val;
    }

    private void restartCurrentGame () {
        game_over = false;
        for (int ii = 0; ii < myStringArray.length; ii++) myStringArray[ii] = empty;
        setListener(gridView);
        button.setText("Restart");
        drawGrid();

        if (play_against_computer && !player1Turn) {
            // Probably want a sleep here.
            int next_move = bestNextMove(myStringArray, yellow, red);
            myStringArray[next_move] = yellow;
            player1Turn = !player1Turn;
            drawGrid();
        }
    }

    private void setListener(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int rowNum = availableColumnSpace(myStringArray, position % grid_width);
                if (rowNum >= 0) {
                    if (player1Turn) myStringArray[rowNum * grid_width + (position % grid_width)] = red;
                    else myStringArray[rowNum * grid_width + (position % grid_width)] = yellow;
                    player1Turn = !player1Turn;
                    drawGrid();

                    if (!game_over && play_against_computer && !player1Turn) {
                        // Put in a little delay so that the user's move gets shown straight away.
                        Handler handler = new Handler();
                        handler.postDelayed(doComputerMove, 5);
                    }
                }
            }
        });
    }

    private Runnable doComputerMove = new Runnable() {
        public void run() {
            int bestMove = bestNextMove(myStringArray, yellow, red);
            myStringArray[bestMove] = yellow;
            player1Turn = !player1Turn;
            drawGrid();
        }
    };

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
    //    int[] current_best_moves = new int[grid_width];
        int current_best_score = -100000;
        int current_best_move = -1;

    //    for (int ii = 0; ii < current_best_moves.length; ii++) current_best_moves[ii] = -1;

        assert(!gameFinished(s));

        int[] ordered = {3, 2, 4, 5, 1, 6, 0};

        for (int jj = 0; jj < grid_width; jj++) {
            int ii = ordered[jj];
            int rowNum = availableColumnSpace(s, ii);
            if (rowNum != -1) {
                s[rowNum * grid_width + ii] = curr_player;
                int best_score_from_this_pos = minimaxMin(s, other_player, curr_player, current_best_score, depth, rowNum * grid_width + ii);
                if (best_score_from_this_pos > current_best_score) {
                    current_best_score = best_score_from_this_pos;
                    current_best_move = rowNum * grid_width + ii;
                    //current_best_moves[ii] = rowNum * grid_width + ii;
                }
                s[rowNum * grid_width + ii] = empty;
            }
        }
//        int rand;
//        Random random = new Random();
//        do {
//            rand = random.nextInt(grid_width);
//        } while (current_best_moves[rand] == -1);
//        return current_best_moves[rand];
        return current_best_move;
    }

    private int minimaxMax(String[] s, String curr_player, String other_player, int alpha, int depth, int lastMove) {
        int current_best_score = -100000;

        //if (hasWon(curr_player)) return 1;
        if (hasWon(other_player, lastMove)) return -10000;
        else if (gameFinished(s)) return 0;
        else if (depth == 0) return evaluatePosition(curr_player);
        else {
            for (int ii = 0; ii < grid_width; ii++) {
                // Do some alpha pruning.
                if (current_best_score >= alpha) break;
                int rowNum = availableColumnSpace(s, ii);
                if (rowNum != -1) {
                    s[rowNum * grid_width + ii] = curr_player;
                    int best_score_from_this_pos = minimaxMin(s, other_player, curr_player, current_best_score, depth-1, rowNum * grid_width + ii);
                    if (best_score_from_this_pos > current_best_score) {
                        current_best_score = best_score_from_this_pos;
                    }
                    s[rowNum * grid_width + ii] = empty;
                }
            }
            return current_best_score;
        }
    }

    private int minimaxMin(String[] s, String curr_player, String other_player, int beta, int depth, int lastMove) {
        int current_lowest_score = 100000;
        //if (hasWon(curr_player)) return -1;
        if (hasWon(other_player, lastMove)) return 10000;
        else if (gameFinished(s)) return 0;
        else if (depth == 0) return evaluatePosition(curr_player);
        else {
            for (int ii = 0; ii < grid_width; ii++) {
                // Do some beta pruning first
                if (current_lowest_score <= beta) break;
                int rowNum = availableColumnSpace(s, ii);
                if (rowNum != -1) {
                    s[rowNum * grid_width + ii] = curr_player;
                    int lowest_score_from_this_pos = minimaxMax(s, other_player, curr_player, current_lowest_score, depth-1, rowNum * grid_width + ii);
                    if (lowest_score_from_this_pos < current_lowest_score) {
                        current_lowest_score = lowest_score_from_this_pos;
                    }
                    s[rowNum * grid_width + ii] = empty;
                }
            }
            return current_lowest_score;
        }
    }
}