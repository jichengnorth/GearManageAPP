package com.example.gearbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Created by Jason Zhao on Sept/20/2020.
 * Copyright is reserved
 */

//The MainActivity class includes all the functionality of the starting page of the app, which are
//just 2 buttons presses. I used 2 different ways to implement the button click. First, I used setOnClickListener
//to implement viewing all item. Second, I make a new method that links to add item click. I made this public
//so other classes can access it, since I want to make sure adding items function always appends to the bottom end of the list.

public class MainActivity extends AppCompatActivity {
    private Button viewButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewButton =(Button) findViewById(R.id.viewButton);

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

    }

    //Called when add item floating button is clicked,
    // this method of calling can be found in https://developer.android.com/training/basics/firstapp/starting-activity.html
    public void add_item_button(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, add_item.class);
        startActivity(intent);
    }


}
