package com.cookie_computing.wastenomore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class TrashCalc extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.cookie-computing.wastenomore.TOTAL_GAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_calc);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trash_calc, menu);
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

    /** Called when the user clicks the Calculate button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, TrashResults.class);

        // Get the edit text views for the bags
        EditText smallEditText = (EditText) findViewById(R.id.small_bags_number);
        EditText medEditText = (EditText) findViewById(R.id.med_bags_number);
        EditText largeEditText = (EditText) findViewById(R.id.large_bags_number);

        // Get the number of bags
        String smallBagString = smallEditText.getText().toString();
        String medBagString = medEditText.getText().toString();
        String largeBagString = largeEditText.getText().toString();

        int smallBags, medBags, largeBags;
        if (smallBagString.equals("")) {
            smallBags = 0;
        } else {
            smallBags = Integer.parseInt(smallBagString);
        }
        if (medBagString.equals("")) {
            medBags = 0;
        } else {
            medBags = Integer.parseInt(medBagString);
        }
        if (largeBagString.equals("")) {
            largeBags = 0;
        } else {
            largeBags = Integer.parseInt(largeBagString);
        }

        // Calculate the number of gallons ad weight used for that week and send it with the Intent
        int smallBagWeight = smallBags * 9;
        System.out.println("small bag weight " + smallBagWeight);
        int medBagWeight = medBags * 15;
        System.out.println("med bag weight " + medBagWeight);
        int largeBagWeight = largeBags * 24;
        System.out.println("large bag weight " + largeBagWeight);
        int totalWeight = smallBagWeight + medBagWeight + largeBagWeight;
        intent.putExtra(EXTRA_MESSAGE, "" + totalWeight);
        startActivity(intent);
    }
}
