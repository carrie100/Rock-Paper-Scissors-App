package com.example.rockpaperscissors.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.rockpaperscissors.R;
import com.example.rockpaperscissors.lib.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView imgHuman, imgComputer;
    private View[] mRPSImageViews;
    private TextView tvCurrentRoundWinner;
    private TextView tvRound1Computer, tvRound1Player, tvRound1Winner;
    private TextView tvRound2Computer, tvRound2Player, tvRound2Winner;
    private TextView tvRound3Computer, tvRound3Player, tvRound3Winner;

    private TextView[] tvRoundsComputer, tvRoundsPlayer, tvRoundsWinner;

    private String[] mRPSStrings;
    private int currentRound;
    private int[] mRPSImagesIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFields();

        currentRound = 1;
        mRPSStrings = new String[]{"Rock", "Paper", "Scissors"};

        setupFAB();
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fill in Info or About
                Utils.showInfoDialog(MainActivity.this,
                        "Info", "Information to show user");
            }
        });
    }

    private void setupFields() {
        imgHuman = findViewById(R.id.img_human_choice);
        imgComputer = findViewById(R.id.img_computer_choice);

        mRPSImageViews = new View[]{findViewById(R.id.image_rock),
                findViewById(R.id.image_paper),
                findViewById(R.id.image_scissors)};
        mRPSImagesIDs = new int[]
                {R.drawable.rock, R.drawable.paper, R.drawable.scissors};

        tvCurrentRoundWinner = findViewById(R.id.tv_board_winner);

        tvRound1Computer = findViewById(R.id.tv_data_round1_computer);
        tvRound1Player = findViewById(R.id.tv_data_round1_player);
        tvRound1Winner = findViewById(R.id.tv_data_winner_round1);
        tvRound2Computer = findViewById(R.id.tv_data_round2_computer);
        tvRound2Player = findViewById(R.id.tv_data_round2_player);
        tvRound2Winner = findViewById(R.id.tv_data_winner_round2);
        tvRound3Computer = findViewById(R.id.tv_data_round3_computer);
        tvRound3Player = findViewById(R.id.tv_data_round3_player);
        tvRound3Winner = findViewById(R.id.tv_data_winner_round3);

        tvRoundsComputer = new TextView[]{tvRound1Computer, tvRound2Computer, tvRound3Computer};
        tvRoundsPlayer = new TextView[]{tvRound1Player, tvRound2Player, tvRound3Player};
        tvRoundsWinner = new TextView[]{tvRound1Winner, tvRound2Winner, tvRound3Winner};
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Utils.setNightModeOnOffFromPreferenceValue(
                    getApplicationContext(), getString(R.string.night_mode_key));
        }
    }

    public void pickRPS(View view) {
        int humanNumber = getHumanNumber(view);
        updateHumanDrawable((ImageButton) view);

        int computerNumber = (int) Math.floor(Math.random() * 3);
        updateComputerDrawable(computerNumber);

        updateWinnerInfo(view, humanNumber, computerNumber);
    }

    private int getHumanNumber(View view) {
        int humanNumber = -1;
        for (int i = 0; i < mRPSImageViews.length; i++) {
            if (view == mRPSImageViews[i])
                humanNumber = i;
        }
        return humanNumber;
    }

    private void updateHumanDrawable(ImageButton view) {
        ImageButton currentView = view;
        Drawable currentDrawable = currentView.getDrawable();
        imgHuman.setImageDrawable(currentDrawable);

        int index = Integer.parseInt((String) currentView.getTag());
        String rps = mRPSStrings[index];
        TextView currentRoundHuman = tvRoundsPlayer[currentRound-1];

        currentRoundHuman.setText(rps);
    }

    private void updateComputerDrawable(int computerNumber) {
        imgComputer.setImageResource(mRPSImagesIDs[computerNumber]);
        String rps = mRPSStrings[computerNumber];
        TextView currentRoundComputer = tvRoundsComputer[currentRound-1];

        currentRoundComputer.setText(rps);
    }

    private void updateWinnerInfo(View view, int humanNumber, int computerNumber) {

        final String COMPUTER = "Computer", PLAYER = "Player";

        String result, computerWins = "Computer won.", playerWins = "You won!", winner;
        if (humanNumber == computerNumber) {
            // display tie
            result = "Tie Game!";
            winner = "Tie";
        } else if (humanNumber == 0 && computerNumber == 1) {
            // Rock vs Paper
            // Computer is winner
            result = computerWins;
            winner = COMPUTER;
        } else if (humanNumber == 0 && computerNumber == 2) {
            // Rock vs Scissors
            // Human is the winner
            result = playerWins;
            winner = PLAYER;
        } else if (humanNumber == 1 && computerNumber == 0) {
            // Paper vs Rock
            // Human is the winner
            result = playerWins;
            winner = PLAYER;
        } else if (humanNumber == 1 && computerNumber == 2) {
            // Paper vs Scissors
            // Computer is winner
            result = computerWins;
            winner = COMPUTER;
        } else if (humanNumber == 2 && computerNumber == 0) {
            // Scissors vs Rock
            // Computer is winner
            result = computerWins;
            winner = COMPUTER;
        } else if (humanNumber == 2 && computerNumber == 1) {
            // Scissors vs Paper
            // Human is the winner
            result = playerWins;
            winner = PLAYER;
        } else {
            result = "Unknown";
            winner = result;
        }

        // set main board text
        tvCurrentRoundWinner.setText(result);

        // set the winner in the score board
        TextView currentRoundWinner = tvRoundsWinner[currentRound-1];
        currentRoundWinner.setText(winner);

        // go to next round
        currentRound = currentRound == 3 ? 1 : currentRound + 1;
    }
}