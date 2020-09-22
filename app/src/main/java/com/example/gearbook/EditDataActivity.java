package com.example.gearbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

/**
 * Created by Jason Zhao on Sept/20/2020.
 * Copyright is reserved
 */

// This class is similar to Add_item class when it comes to add the info of an item
// The differences are it will retrieve the previous entry, and it can delete an item
// Modified and referenced to https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/blob/master/SaveAndDisplaySQL/app/src/main/java/com/tabian/saveanddisplaysql/EditDataActivity.java

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button updateButton,deleteButton;
    private EditText entry_price, entry_maker, entry_description,entry_comment;


    //for the date picker
    private String date;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    public DatabaseHelper mDatabaseHelper;

    private String selectedPrice, selectedMaker, selectedDescription, selectedComment, selectedDate;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        // declare everything
        entry_price =(EditText) findViewById(R.id.entry_price);
        entry_maker =(EditText) findViewById(R.id.entry_maker);
        entry_description =(EditText) findViewById(R.id.entry_description);
        entry_comment =(EditText) findViewById(R.id.entry_comment);

        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the all the data we passed as an extra
        selectedMaker = receivedIntent.getStringExtra("maker");
        selectedPrice = receivedIntent.getStringExtra("price");
        selectedDescription = receivedIntent.getStringExtra("description");
        selectedComment = receivedIntent.getStringExtra("comment");
        selectedDate = receivedIntent.getStringExtra("date");
        date = selectedDate;

        //set the text to show the current selected item
        entry_price.setText(selectedPrice);
        entry_maker.setText(selectedMaker);
        entry_description.setText(selectedDescription);
        entry_comment.setText(selectedComment);

        //save the new info to database
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price =entry_price.getText().toString();
                String maker =entry_maker.getText().toString();
                String description =entry_description.getText().toString();
                String comment =entry_comment.getText().toString();

                if(price.length() != 0 && maker.length() !=0 && date.length() !=0 && description.length() !=0){

                    // handle the comment entry, since it could be null.
                    if (comment.length() ==0){
                        comment = "[nothing enter]";
                    }
                    mDatabaseHelper.updateName(selectedPrice,selectedID,date,maker,description,price,comment);
                    Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                    startActivity(intent);

                }else{
                    toastMessage("You must enter a name");
                }
            }
        });


        // Delete the Item from database with help from SQL
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteItem(selectedID);
                entry_price.setText("");
                toastMessage("removed from database");
                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                startActivity(intent);

            }
        });




        // datepicker from Youtube https://www.youtube.com/watch?v=hwe1abDO2Ag&ab_channel=CodingWithMitch

        mDisplayDate = (TextView) findViewById(R.id.entry_date);
        mDisplayDate.setText(selectedDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditDataActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                // added all these zeros in front of the numbers
                String month_s = (month < 10 ? "0" : "") +month;
                String day_s = (day < 10 ? "0" : "") + day;


                date = year + "-" + month_s + "-" + day_s;
                mDisplayDate.setText(date);
            }

        };


    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}

