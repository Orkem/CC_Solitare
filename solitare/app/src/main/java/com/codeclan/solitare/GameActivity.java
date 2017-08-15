package com.codeclan.solitare;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {

    HashMap<Integer, Card> gameState;
    boolean isSelected;
    int selectedCard;
    GameLogic game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameState = new HashMap<>();
        isSelected = false;
        game = new GameLogic();
        game.newGame();


        //find the gameStacks view
        LinearLayout gameStack1 = (LinearLayout) findViewById(R.id.game_stacks);
        gameStack1.setOrientation(LinearLayout.HORIZONTAL);
        reDrawState(game, gameStack1);

    }

    public void reDrawState(GameLogic game, final LinearLayout gameStacks){
        final GameLogic redrawGame = game;
        gameStacks.removeAllViews();
        int count = 0;
        //create view for pile card
        if(game.getPile().size() > 0) {
            Button pileCard = (Button) findViewById(R.id.pile);
            Card pileCardObject = redrawGame.getPileCard();
            pileCard.setText(pileCardObject.getRank() + pileCardObject.getSuit());
        }
        //go through the gameStacks to display cards
        for(final ArrayList<Card> stack : game.getGameStacks()) {
            LinearLayout linearRow = new LinearLayout(this);
            linearRow.setOrientation(LinearLayout.VERTICAL);

            for (final Card card : stack) {
                //update hash
                gameState.put(count, card);
                LinearLayout linearColumn = new LinearLayout(this);
                linearColumn.setOrientation(LinearLayout.HORIZONTAL);

                Button cardButton = new Button(this);
                cardButton.setText(card.getRank() + card.getSuit());
                cardButton.setId(count);
                cardButton.setTextSize(10);
                //make the last items height larger
                if(stack.indexOf(card) == stack.size()-1) {
                    cardButton.setLayoutParams(new LinearLayoutCompat.LayoutParams(150, 200));
                }
                else{
                    cardButton.setLayoutParams(new LinearLayoutCompat.LayoutParams(150, 100));
                }
                linearColumn.addView(cardButton);
                linearRow.addView(linearColumn);
                final int index = count;
                cardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(isSelected){
                            Log.i("TAG", "index :" + index);
                            Toast.makeText(getApplicationContext(),
                                    "Clicked Button Rank :" + index,
                                    Toast.LENGTH_SHORT).show();
                            redrawGame.makeValidMove(gameState.get(selectedCard),redrawGame.getGameStacks().indexOf(stack));
                            reDrawState(redrawGame, gameStacks);
                            isSelected = false;
                        }
                        else{
                            Log.i("TAG", "index :" + index);
                            Toast.makeText(getApplicationContext(),
                                    "Selected Button Rank :" + index,
                                    Toast.LENGTH_SHORT).show();
                            selectedCard = index;
                            isSelected = true;
                        }
                    }
                });
                count++;
            }
            gameStacks.addView(linearRow);
        }
    }
}

