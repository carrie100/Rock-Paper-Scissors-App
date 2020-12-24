package com.example.rockpaperscissors;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imgHuman, imgComputer;
    private View [] imagesRPS;

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pickRPS(View view) {
        int[] drawables = {R.drawable.rock, R.drawable.paper, R.drawable.scissors};
        int randomNumber, humanNumber;

        // Human
        ImageButton currentView = (ImageButton) view;
        imgHuman.setImageDrawable(currentView.getDrawable());

        for (int i = 0; i < imagesRPS.length; i++) {
            if (view == imagesRPS[i])
                humanNumber = i;
        }

        // Do computer
        randomNumber = (int) Math.floor(Math.random() * 3);
        //Toast.makeText(getApplicationContext(),"Random: " + randomNumber, Toast.LENGTH_SHORT).show();
        imgComputer.setImageResource(drawables[randomNumber]);

        // TODO: compare human number to computer number to determine winner
        if (humanNumber == randomNumber)
        {
            //display tie
        }
        else if(humanNumber == 0 && randomNumber == 1)
        {
            //Rock vs Paper
            // Computer is winner
        }
        else if (humanNumber == 0 && randomNumber == 2)
        {
            //Rock vs Scissors
            // Human is the winner
        }
        else if (humanNumber == 1 && randomNumber == 0)
        {
            //Paper vs Rock
            //Human is the winner
        }
        else if(humanNumber ==1 && randomNumber == 2)
        {
            //Paper vs Scissors
            //Computer is winner
        }
        else if(humanNumber ==2 && randomNumber == 0)
        {
            //Scissors vs Rock
            //Computer is winner
        }
        else if(humanNumber == 2 && randomNumber == 1)
        {
            //Scissors vs Paper
            //Human is the winner
        }
    }
}