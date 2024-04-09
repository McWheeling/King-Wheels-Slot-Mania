package com.example.cst214_final_project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Jared Striemer CST214
 * COSC195 Final Project - King Wheels Mania
 * Game Screen Activity
 */
public class GameScreen extends AppCompatActivity implements View.OnClickListener
{
    Button btnSpin;

    //Counter to track how many times through the loop and how many times the images have changed
    private int nCounter;

    //Random variable to be used later
    private Random obRandom = new Random();

    //ImageViews from game_activity
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;

    //Spin Trackers to be used with the random values
    int nSpin1 = 0;
    int nSpin2 = 0;
    int nSpin3 = 0;

    //Image Array - If matches seem far apart comment out some images to increase matches
    private int[] imgArray = {
                                R.drawable.img_1,
                                R.drawable.img_2,
                                R.drawable.img_3,
                                R.drawable.img_4,
                                R.drawable.img_5,
                                R.drawable.img_6,
                                R.drawable.img_7,
                                R.drawable.img_8,
                                R.drawable.img_9
                             };

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);

        btnSpin = findViewById(R.id.btnSpin);

    } //End of onCreate

    /**
     * When the SPIN! (Play) Button is pressed it should play the game and spin the imageViews
     * When the game is finished it will switch to the ResultScreen Activity
     *  and set the Win Message if any on that screen
     */
    @Override
    public void onClick(View view) {

        //Timer to handle the image spinning's, originally had a Thread but ran into UI Thread issues
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        spinImages();
                    }
                });
            }
        },150,150);
    } //End of onClick

    /**
     * This method handles the actual game code, and randomly sets images and spins them
     */
    private synchronized void spinImages()
    {
        btnSpin.setOnClickListener(null);
        nCounter += 100;

        //These are milliseconds
        if (nCounter <= 2000)
        {
            nSpin1 = (int) (Math.random() * imgArray.length);
            iv1.setImageDrawable(getResources().getDrawable( imgArray[nSpin1]));
        }

        if (nCounter <= 3000)
        {
            nSpin2 = (int) (Math.random() * imgArray.length);
            iv2.setImageDrawable(getResources().getDrawable( imgArray[nSpin2]));
        }

        if (nCounter <= 4000)
        {
            nSpin3 = (int) (Math.random() * imgArray.length);
            iv3.setImageDrawable(getResources().getDrawable( imgArray[nSpin3]));
        }
        else if (nCounter >= 4300)
        {
            timer.cancel();
            nCounter = 0;

            // ---- Win Check Statements ----
            //Congrats! All 3 Match
            if (nSpin1 == nSpin2 && nSpin1 == nSpin3)
            {
                //All 3 message
                endGame("Congrats! All Match!");
                return;
            }

            //Doubles!
            if(nSpin1 == nSpin2 || nSpin2 == nSpin3 || nSpin1 == nSpin3)
            {
                //Doubles message
                endGame("Congrats! Doubles!");
                return;
            }

            //You Lost - None Match
            endGame("You Lost! - None Match!");
        }
    }

    /**
     * End Game Helper Method that takes in a String of what the winning/ losing message will be
     * This message will be displayed on the Result Screen TextView
     * @param value
     */
    private void endGame(String value)
    {
        Intent intent = new Intent(this, ResultScreen.class);
        intent.putExtra("message", value);
        this.startActivity(intent);
    }

    /**
     * Go Home Button Event Handler
     * Incase the player wants to switch their picture and/ or name
     * Reason: So, the player doesn't need to close the app to change those
     * @param view
     */
    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class); //Opens the Main (Home) Java File
        this.startActivity(intent);
    }
} //End of file