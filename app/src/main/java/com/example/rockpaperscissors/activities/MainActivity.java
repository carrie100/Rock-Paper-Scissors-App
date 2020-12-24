package com.example.rockpaperscissors.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.rockpaperscissors.R;
import com.example.rockpaperscissors.lib.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    private View [] imagesRPS;
    private TextView tvWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFields();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupFields() {
        imgHuman = findViewById(R.id.img_human_choice);
        imgComputer = findViewById(R.id.img_computer_choice);
        imagesRPS = new View[]{findViewById(R.id.image_rock),
                findViewById(R.id.image_paper),
                findViewById(R.id.image_scissors)};
        tvWinner = findViewById(R.id.tv_board_winner);
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
        if (requestCode==1) {
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
        int humanNumber=-1;
        for (int i = 0; i < imagesRPS.length; i++) {
            if (view == imagesRPS[i])
                humanNumber = i;
        }
        return humanNumber;
    }

    private void updateHumanDrawable(ImageButton view) {
        ImageButton currentView = view;
        imgHuman.setImageDrawable(currentView.getDrawable());
    }

    private void updateComputerDrawable(int computerNumber) {
        int[] drawables = {R.drawable.rock, R.drawable.paper, R.drawable.scissors};
        imgComputer.setImageResource(drawables[computerNumber]);
    }

    private void updateWinnerInfo(View view, int humanNumber, int computerNumber) {
        // TODO: compare human number to computer number to determine winner
        String result, computerWins = "Computer won.", humanWins = "You won!";
        if (humanNumber == computerNumber)
        {
            // display tie
            result = "Tie Game!";
        }
        else if(humanNumber == 0 && computerNumber == 1)
        {
            // Rock vs Paper
            // Computer is winner
            result = computerWins;
        }
        else if (humanNumber == 0 && computerNumber == 2)
        {
            // Rock vs Scissors
            // Human is the winner
            result = humanWins;
        }
        else if (humanNumber == 1 && computerNumber == 0)
        {
            // Paper vs Rock
            // Human is the winner
            result = humanWins;
        }
        else if(humanNumber ==1 && computerNumber == 2)
        {
            // Paper vs Scissors
            // Computer is winner
            result = computerWins;
        }
        else if(humanNumber ==2 && computerNumber == 0)
        {
            // Scissors vs Rock
            // Computer is winner
            result = computerWins;
        }
        else if(humanNumber == 2 && computerNumber == 1)
        {
            // Scissors vs Paper
            // Human is the winner
            result = humanWins;
        }
        else
        {
            result="Unknown";
        }

        tvWinner.setText(result);
    }
}