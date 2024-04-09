package com.example.cst214_final_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Jared Striemer CST214
 * COSC195 Final Project - King Wheels Mania
 * Main Activity - Home Screen
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    ImageView ivPhoto;
    Button btnCamera;
    Button btnPlay;
    TextView tvLabelEnterName;
    EditText etName;

    //---- Request Permission String Array containing the Manifest for Camera and Location ----
    private static final String[] REQ_PERMS = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign Variables
        ivPhoto = findViewById(R.id.ivPhoto);
        btnCamera = findViewById(R.id.btnCamera);
        btnPlay = findViewById(R.id.btnPlay);
        tvLabelEnterName = findViewById(R.id.tvLabelEnterName);
        etName = findViewById(R.id.etName);

        btnPlay.setOnClickListener(this); //Make the button a click listener

        // ---- Requests Permissions - Checking if false then the perms are not set ---
        if (!(checkPermissions(this, REQ_PERMS)))
        {
            ActivityCompat.requestPermissions(this, REQ_PERMS, PERMISSION);
        }

        //Camera Button Click Listener
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Open Camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100); //I know this is deprecated but, it's currently working for me
            }
        });
        //---- End of Camera Code ----

        //---- Scheduled Notification Code ----
        //Schedule a notification to happen 7 seconds in the future
        Intent intent = new Intent(this, FireNotification.class);

        //Put inside a pending intent again
        PendingIntent pendingIntent = PendingIntent.getService(this, 1000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Use alarm manager to schedule into the future
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //86400000 ms = 1 Day
        //432000000 ms = 5 days
        //10000 = 10 Seconds (Testing)
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
    } //End of onCreate

    //---- Permission - Checks the Permissions  ----
    public static boolean checkPermissions(Context context, String[] permissions)
    {
        for (String sPerms : permissions)
        {
            if (ActivityCompat.checkSelfPermission(context, sPerms) != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * On Activity Result for Camera
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //Get Capture Image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");

            //Set Capture Image to ImageView
            ivPhoto.setImageBitmap(captureImage);
        }
    }

    //---- File Storage Code ----
    /**
     * File Write/ Read Functions
     * @param view
     */
    //****NEED TO FIGURE OUT HOW TO GET PERMISSION POPUP LIKE CAMERA****
    //Write Name Event Handler/ File Handling
    public void write(View view)
    {
        //Edit Text Name
        EditText txtMsg = findViewById(R.id.etName);
        String sName = txtMsg.getText().toString();

        //If the text field length is greater than 0 write the name. Else display a error message
        if (sName.length() > 0)
        {
            try
            {
                //For writing to internal Storage
                FileOutputStream fileOut = this.openFileOutput("finalProject.txt", MODE_PRIVATE);

                //Output Writer - Writes the Name
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);
                outputWriter.write(txtMsg.getText().toString());
                outputWriter.close();

                Toast.makeText(this, "Name: " + txtMsg.getText().toString(), Toast.LENGTH_LONG).show();
                etName.setText(txtMsg.getText().toString()); //Fills the EditText with your name, it will stay there until cleared
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            //New label for the error
            //st the txt view to be a error message and change the color to red
            tvLabelEnterName.setText("Please Enter A Name - Can't Be Blank");
            tvLabelEnterName.setTextColor(Color.RED);
        }
    } //End of write method

    // ---- Read Name Event Handler/ File Handling ----
    public void read(View view)
    {
        int size = 100;

        try
        {
            //For writing to internal storage
            FileInputStream fileIn = openFileInput("finalProject.txt");

            //Input Reader - Reads the Name
            InputStreamReader inputReader = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[size];
            String name = "";
            int charRead;
            while ( (charRead = inputReader.read(inputBuffer)) > 0)
            {
                name += String.copyValueOf(inputBuffer, 0, charRead);
            }

            inputReader.close();
            Toast.makeText(this, "Last Name: " + name, Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }// End of read method

    //Switches to the Game Screen when the Play button is pressed
    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(this, GameScreen.class); //Opens the Games Java File
        this.startActivity(intent);
    }
} //End of File