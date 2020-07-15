package com.example.krejcdav.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper  = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);

        configureGoToStatsButton();
        configureGoToSettingsButton();

        configureAddTramButton();
        configureAddBusButton();
    }

    private void configureGoToStatsButton() {
        ImageButton button = (ImageButton) findViewById(R.id.goToStatsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,StatsActivity.class));
            }
        });
    }
    private void configureGoToSettingsButton() {
        ImageButton button = (ImageButton) findViewById(R.id.goToSettingsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });
    }
    private void configureAddTramButton(){
        Button addTramButton = (Button) findViewById(R.id.addTramButton);
        addTramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you wish to add a tram ride?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        boolean isInserted = databaseHelper.saveToDatabase(MainActivity.this,TransportType.TRAM);
                        if (isInserted) {
                            toastMessage("Record added!");
                        }
                        else{
                            toastMessage("Error adding record.");
                        }
                    }
                });
                builder.show();
            }
        });
    }
    private void configureAddBusButton(){
        Button addBusButton = (Button) findViewById(R.id.addBusButton);
        addBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you wish to add a bus ride?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        boolean isInserted = databaseHelper.saveToDatabase(MainActivity.this,TransportType.BUS);
                        if (isInserted) {
                            toastMessage("Record added!");
                        }
                        else{
                            toastMessage("Error adding record.");
                        }
                    }
                });
                builder.show();
            }
        });
    }
    private void toastMessage(String message){
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
    }

}
