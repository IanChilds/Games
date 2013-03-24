package com.games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowAllGames extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList<Class> games = new ArrayList<Class>();
    public final static String GAME_TYPE = "com.example.games.GAME_TYPE";
    public final static String CONNECT_FOUR = "Connect Four";
    public final static String NOUGHTS_AND_CROSSES = "Noughts and Crosses";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<String> gamesList = new ArrayList<String>();
        gamesList.add("Snake");
        games.add(Snake.class);
        gamesList.add("Blackjack");
        games.add(Blackjack.class);
        gamesList.add("ShowPath");
        games.add(ShowPath.class);
        gamesList.add("Noughts and Crosses");
        games.add(PlayerMenu.class);
        gamesList.add("Connect Four");
        games.add(PlayerMenu.class);
        gamesList.add("Mental Maths");
        games.add(MentalMaths.class);
        gamesList.add("Dice");
        games.add(Dice.class);
        gamesList.add("Pub Quiz");
        games.add(PubQuiz.class);

        ListView gamesListView = (ListView)findViewById(R.id.games_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowAllGames.this, android.R.layout.simple_list_item_1, gamesList);
        gamesListView.setAdapter(adapter);
        gamesListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(games.size() > i){
            Intent intent = new Intent(this, games.get(i));
            if (i == 3) intent.putExtra(GAME_TYPE, NOUGHTS_AND_CROSSES);
            if (i == 4) intent.putExtra(GAME_TYPE, CONNECT_FOUR);
            startActivity(intent);
        }
    }
}