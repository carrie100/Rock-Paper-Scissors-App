package com.example.rockpaperscissors.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.rockpaperscissors.R;
import com.example.rockpaperscissors.lib.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
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

    private final String mCOMPUTER = "Computer", mPLAYER = "Player", mTIE = "Tie";

    private ImageView mImageViewPlayer, mImageViewComputer;
    private View[] mRPSImageViews;
    private TextView mTvGameWinner;

    private TextView[] tvRoundsComputer, tvRoundsPlayer, tvRoundsWinner;

    private String[] mRPSStrings;
    private int mCurrentRound;
    private int[] mRPSImagesIDs;
    private int mCurrentRoundComputerPick, mCurrentRoundPlayerPick;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENT_ROUND", mCurrentRound);
        outState.putInt("CURRENT_PLAYER_PICK", mCurrentRoundPlayerPick);
        outState.putInt("CURRENT_COMPUTER_PICK", mCurrentRoundComputerPick);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentRound = savedInstanceState.getInt("CURRENT_ROUND");
        mCurrentRoundPlayerPick = savedInstanceState.getInt("CURRENT_PLAYER_PICK");
        mCurrentRoundComputerPick = savedInstanceState.getInt("CURRENT_COMPUTER_PICK");

        if (mCurrentRoundPlayerPick !=-1)
            mImageViewPlayer.setImageResource(mRPSImagesIDs[mCurrentRoundPlayerPick]);

        if (mCurrentRoundComputerPick !=-1)
            mImageViewComputer.setImageResource(mRPSImagesIDs[mCurrentRoundComputerPick]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFields();

        mRPSStrings = new String[]{"Rock", "Paper", "Scissors"};
        startNewGame();

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
        mImageViewPlayer = findViewById(R.id.img_player_choice);
        mImageViewComputer = findViewById(R.id.img_computer_choice);
        mTvGameWinner = findViewById(R.id.tv_board_winner);

        mRPSImageViews = new View[]{findViewById(R.id.image_rock),
                findViewById(R.id.image_paper),
                findViewById(R.id.image_scissors)};
        mRPSImagesIDs = new int[]
                {R.drawable.rock, R.drawable.paper, R.drawable.scissors};

        TextView tvRound1Computer = findViewById(R.id.tv_data_round1_computer);
        TextView tvRound1Player = findViewById(R.id.tv_data_round1_player);
        TextView tvRound1Winner = findViewById(R.id.tv_data_winner_round1);
        TextView tvRound2Computer = findViewById(R.id.tv_data_round2_computer);
        TextView tvRound2Player = findViewById(R.id.tv_data_round2_player);
        TextView tvRound2Winner = findViewById(R.id.tv_data_winner_round2);
        TextView tvRound3Computer = findViewById(R.id.tv_data_round3_computer);
        TextView tvRound3Player = findViewById(R.id.tv_data_round3_player);
        TextView tvRound3Winner = findViewById(R.id.tv_data_winner_round3);

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
        } else if (id == R.id.action_new_game) {
            startNewGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startNewGame() {
        // reset round to 1
        mCurrentRound = 1;

        // reset the game board
        mImageViewComputer.setImageResource(0);
        mImageViewPlayer.setImageResource(0);
        mTvGameWinner.setText(R.string.welcome_to_a_new_game);

        // reset the rounds board
        for (int i = 0; i < tvRoundsWinner.length; i++) {
            tvRoundsPlayer[i].setText("");
            tvRoundsComputer[i].setText("");
            tvRoundsWinner[i].setText("");
        }

        mCurrentRoundComputerPick = -1;
        mCurrentRoundPlayerPick = -1;
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
        if (mCurrentRound <= 3) {
            mCurrentRoundPlayerPick = getHumanNumber(view);
            updateHumanDrawable((ImageButton) view);

            mCurrentRoundComputerPick = (int) Math.floor(Math.random() * 3);
            updateComputerDrawable();

            updateWinnerInfo(view);
        } else {
            Snackbar.make(view, getString(R.string.game_already_over), Snackbar.LENGTH_SHORT).show();
        }
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
        Drawable currentDrawable = view.getDrawable();
        mImageViewPlayer.setImageDrawable(currentDrawable);

        int index = Integer.parseInt((String) view.getTag());
        String rps = mRPSStrings[index];
        TextView currentRoundHuman = tvRoundsPlayer[mCurrentRound - 1];

        currentRoundHuman.setText(rps);
    }

    private void updateComputerDrawable() {
        mImageViewComputer.setImageResource(mRPSImagesIDs[mCurrentRoundComputerPick]);
        String rps = mRPSStrings[mCurrentRoundComputerPick];
        TextView currentRoundComputer = tvRoundsComputer[mCurrentRound - 1];

        currentRoundComputer.setText(rps);
    }

    private void updateWinnerInfo(View view) {
        String result, computerWins = "Computer won.", playerWins = "You won!", winner;
        if (mCurrentRoundPlayerPick == mCurrentRoundComputerPick) {
            // display tie
            result = "Tie Game! Repeat this round";
            winner = "Tie";
        } else if (mCurrentRoundPlayerPick == 0 && mCurrentRoundComputerPick == 1) {
            // Rock vs Paper
            // Computer is winner
            result = computerWins;
            winner = mCOMPUTER;
        } else if (mCurrentRoundPlayerPick == 0 && mCurrentRoundComputerPick == 2) {
            // Rock vs Scissors
            // Human is the winner
            result = playerWins;
            winner = mPLAYER;
        } else if (mCurrentRoundPlayerPick == 1 && mCurrentRoundComputerPick == 0) {
            // Paper vs Rock
            // Human is the winner
            result = playerWins;
            winner = mPLAYER;
        } else if (mCurrentRoundPlayerPick == 1 && mCurrentRoundComputerPick == 2) {
            // Paper vs Scissors
            // Computer is winner
            result = computerWins;
            winner = mCOMPUTER;
        } else if (mCurrentRoundPlayerPick == 2 && mCurrentRoundComputerPick == 0) {
            // Scissors vs Rock
            // Computer is winner
            result = computerWins;
            winner = mCOMPUTER;
        } else if (mCurrentRoundPlayerPick == 2 && mCurrentRoundComputerPick == 1) {
            // Scissors vs Paper
            // Human is the winner
            result = playerWins;
            winner = mPLAYER;
        } else {
            result = "Unknown";
            winner = result;
        }

        // set the round winner in the score board
        TextView currentRoundWinner = tvRoundsWinner[mCurrentRound - 1];
        currentRoundWinner.setText(winner);

        // Tell the user the results of this round
        Snackbar.make(view,
                "Round " + mCurrentRound + ": " + result, Snackbar.LENGTH_LONG).show();

        // set main board text after all three rounds
        if (mCurrentRound == 3 && !winner.equals(mTIE))
            mTvGameWinner.setText(getString(R.string.game_winner_colon).concat(getGameWinner()));

        // go to next round in current game
        if (!winner.equals(mTIE))
            mCurrentRound++;
    }

    private String getGameWinner() {
        int computerWinCount = 0;
        for (int i = 0; i < tvRoundsWinner.length; i++) {
            computerWinCount = tvRoundsWinner[i].getText().toString().equals(mCOMPUTER)
                    ? computerWinCount + 1 : computerWinCount;
        }

        return computerWinCount >=2 ? mCOMPUTER : mPLAYER;
    }
}