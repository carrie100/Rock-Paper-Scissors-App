package com.example.rockpaperscissors.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.rockpaperscissors.R;
import com.example.rockpaperscissors.classes.GameRPS;
import com.example.rockpaperscissors.classes.RoundRPS;
import com.example.rockpaperscissors.enums.ItemRPS;
import com.example.rockpaperscissors.enums.WinType;
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

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageViewPlayer, mImageViewComputer;
    private View[] mRPSImageViews;
    private TextView mTvGameWinner;

    private TextView[] tvRoundsComputer, tvRoundsPlayer, tvRoundsWinner;

    private int[] mRPSImagesIDs;

    private boolean mUseAutoSave;

    private GameRPS mGame;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("GAME", mGame.toJSONString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGame = GameRPS.fromJSONString(savedInstanceState.getString("GAME"));

        ItemRPS currentRoundComputerPick = mGame.getCurrentRoundComputerItem();
        ItemRPS currentRoundPlayerPick = mGame.getCurrentRoundPlayerItem();

        if (currentRoundPlayerPick != null)
            mImageViewPlayer.setImageResource(mRPSImagesIDs[currentRoundPlayerPick.ordinal()]);

        if (currentRoundComputerPick != null)
            mImageViewComputer.setImageResource(mRPSImagesIDs[currentRoundComputerPick.ordinal()]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mUseAutoSave) {
            SharedPreferences defaultSP = getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = defaultSP.edit();

            editor.putString("GAME", mGame.toJSONString());

            editor.apply();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFields();
        SharedPreferences defaultSP = getDefaultSharedPreferences(this);

        mGame = new GameRPS();

        setupFAB();

        mUseAutoSave = defaultSP.getBoolean(getString(R.string.auto_save_key), true);

        restoreFromPrefsOnNewRun(savedInstanceState, defaultSP);

    }

    private void restoreFromPrefsOnNewRun(Bundle savedInstanceState, SharedPreferences defaultSP) {
        // if we're starting a new run (not on rotation) and auto save is on then restore from prefs
        if (savedInstanceState == null && mUseAutoSave) {
            String gameInPrefs = defaultSP.getString("GAME", null);
            if (gameInPrefs != null) {
                mGame = GameRPS.fromJSONString(gameInPrefs);

                for (int round = 1; round <= mGame.getCurrentRoundNumber(); round++) {
                    RoundRPS thisRound = mGame.getRoundNumber(round);

                    if (thisRound.getComputerItem() != null) {
                        tvRoundsComputer[round - 1].setText(thisRound.getComputerItem().toString());
                    }
                    if (thisRound.getPlayerItem() != null) {
                        tvRoundsPlayer[round - 1].setText(thisRound.getPlayerItem().toString());
                    }
                    if (thisRound.getComputerItem() != null && thisRound.getPlayerItem() != null)
                        tvRoundsWinner[round - 1].setText(
                                RoundRPS.getRoundWinType(
                                        thisRound.getComputerItem(), thisRound.getPlayerItem()).toString());
                }
            }

            // restore the current round images, if any
            if (mGame.getCurrentRoundPlayerItem() != null)
                mImageViewPlayer.setImageResource(
                        mRPSImagesIDs[mGame.getCurrentRoundPlayerItem().ordinal()]);

            if (mGame.getCurrentRoundComputerItem() != null)
                mImageViewComputer.setImageResource(
                        mRPSImagesIDs[mGame.getCurrentRoundComputerItem().ordinal()]);
        }
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showInfoDialog(MainActivity.this,
                        "Info", "Rock Paper Scissors Game\n" +
                                "The goal of the game is to beat the computer. " +
                                "Rock beats Scissors. Scissors beats Paper. Paper beats Rock");
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
        TextView tvRound1Winner = findViewById(R.id.tv_data_round1_winner);
        TextView tvRound2Computer = findViewById(R.id.tv_data_round2_computer);
        TextView tvRound2Player = findViewById(R.id.tv_data_round2_player);
        TextView tvRound2Winner = findViewById(R.id.tv_data_round2_winner);
        TextView tvRound3Computer = findViewById(R.id.tv_data_round3_computer);
        TextView tvRound3Player = findViewById(R.id.tv_data_round3_player);
        TextView tvRound3Winner = findViewById(R.id.tv_data_round3_winner);

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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == R.id.action_new_game) {
            startNewGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startNewGame() {
        mGame.startNewGame();
        resetViewForNewGame();
    }

    private void resetViewForNewGame() {
        resetGameBoard();
        resetRoundsBoard();
    }

    private void resetGameBoard() {
        mImageViewComputer.setImageResource(0);
        mImageViewPlayer.setImageResource(0);
        mTvGameWinner.setText(R.string.welcome_to_a_new_game);
    }

    private void resetRoundsBoard() {
        for (int i = 0; i < tvRoundsWinner.length; i++) {
            tvRoundsPlayer[i].setText("");
            tvRoundsComputer[i].setText("");
            tvRoundsWinner[i].setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Utils.setNightModeOnOffFromPreferenceValue(
                    getApplicationContext(), getString(R.string.night_mode_key));
        }

        // reset images on button bar buttons due to unexpected display corruption
        for (int i = 0; i < mRPSImageViews.length; i++) {
            ImageView imageView = (ImageView) mRPSImageViews[i];
            imageView.setImageResource(mRPSImagesIDs[i]);
        }
    }

    public void pickRPS(View view) {
        if (!mGame.isGameOver()) {
            // in MVC View, get picks from player and computer
            int currentRoundPlayerPick = getPlayerPickNumberFromImageClicked(view);
            int currentRoundComputerPick = (int) Math.floor(Math.random() * 3);

            // go to next round in model and add in those picks to that round
            mGame.advanceToAndSetNextRound(
                    ItemRPS.values()[currentRoundComputerPick],
                    ItemRPS.values()[currentRoundPlayerPick]);

            updateBoardsWithComputerAndPlayerMoves((ImageButton) view, currentRoundComputerPick);
            updateScoreboardWithRoundWinner(view);
            updateScoreboardWithGameWinnerIfGameOver();
        } else {
            Snackbar.make(view, getString(R.string.game_already_over), Snackbar.LENGTH_SHORT).show();
        }
    }

    private int getPlayerPickNumberFromImageClicked(View view) {
        int humanNumber = -1;
        for (int i = 0; i < mRPSImageViews.length; i++) {
            if (view == mRPSImageViews[i])
                humanNumber = i;
        }
        return humanNumber;
    }

    private void updateBoardsWithComputerAndPlayerMoves(ImageButton view, int currentRoundComputerPick) {
        updatePlayerDrawable(view);
        updateComputerDrawable(currentRoundComputerPick);
    }

    private void updatePlayerDrawable(ImageButton view) {
        Drawable currentDrawable = view.getDrawable();
        mImageViewPlayer.setImageDrawable(currentDrawable);

        int index = Integer.parseInt((String) view.getTag());
        String rps = ItemRPS.values()[index].toString();
        TextView currentRoundPlayer = tvRoundsPlayer[mGame.getCurrentRoundNumber() - 1];

        currentRoundPlayer.setText(rps);
    }

    private void updateComputerDrawable(int currentRoundComputerPick) {
        mImageViewComputer.setImageResource(mRPSImagesIDs[currentRoundComputerPick]);
        String rps = ItemRPS.values()[currentRoundComputerPick].toString();
        TextView currentRoundComputer = tvRoundsComputer[mGame.getCurrentRoundNumber() - 1];

        currentRoundComputer.setText(rps);
    }

    private void updateScoreboardWithRoundWinner(View view) {
        int[] results = {R.string.win_computer, R.string.win_player, R.string.win_tie};
        WinType winType = mGame.getCurrentRound().getRoundWinType();

        // set the round winner in the score board
        TextView currentRoundWinner = tvRoundsWinner[mGame.getCurrentRoundNumber() - 1];
        currentRoundWinner.setText(winType.toString());

        // Tell the user the results of this round
        Snackbar.make(view, "Round " + (mGame.getCurrentRoundNumber()) + ": "
                + getString(results[winType.ordinal()]), Snackbar.LENGTH_LONG)
                .show();
    }

    private void updateScoreboardWithGameWinnerIfGameOver() {
        if (mGame.isGameOver()) {
            WinType winType = mGame.getWinner();

            // in case there are an even number of rounds, there could then be a tie...
            String msg = winType.equals(WinType.TIE)
                    ? getString(R.string.game_winner_no_winner)
                    : getString(R.string.game_winner_colon).concat(winType.toString());
            mTvGameWinner.setText(msg);
        }
    }
}