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

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
/**
 * Created by Jason Zhao on Sept/20/2020.
 * Copyright is reserved
 */

//This class handles all the adding when users is trying to add an item.
//The thinking is, on this page, you can hint save button to save it, it will bring up a toast message
//for you to indicate whether it went successful. And it won't bring you to another activity, this way
// it will maximize efficiency of adding items.
//Modified and reference to https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/blob/master/SaveAndDisplaySQL/app/src/main/java/com/tabian/saveanddisplaysql/MainActivity.java

public class add_item extends AppCompatActivity {

    private static final String TAG = "Add_item";

    //for the datepicker
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //for the database

    public DatabaseHelper mDatabaseHelper;
    private Button saveButton,viewButton;
    private EditText entry_price, entry_maker, entry_description,entry_comment;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        // declare everything
        entry_price =(EditText) findViewById(R.id.entry_price);
        entry_maker =(EditText) findViewById(R.id.entry_maker);
        entry_description =(EditText) findViewById(R.id.entry_description);
        entry_comment =(EditText) findViewById(R.id.entry_comment);


        saveButton =(Button) findViewById(R.id.updateButton);
        viewButton =(Button) findViewById(R.id.deleteButton);
        mDatabaseHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String price =entry_price.getText().toString();
                String maker =entry_maker.getText().toString();
                String description =entry_description.getText().toString();
                String comment =entry_comment.getText().toString();



                // input checking
                if (price.length() != 0 && maker.length() !=0 && date.length() !=0 && description.length() !=0) {
                    //Since Comment is not required entry, we need to handle this
                    if (comment.length() ==0){
                        comment = "[nothing entered]";
                    }
                        AddData(date,maker,description,price,comment);
                 //   editText.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add_item.this, ListDataActivity.class);
                startActivity(intent);
            }
        });




        // datepicker from Youtube https://www.youtube.com/watch?v=hwe1abDO2Ag&ab_channel=CodingWithMitch
        mDisplayDate = (TextView) findViewById(R.id.entry_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        add_item.this,
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
    //adding everything to Database
    public void AddData ( String date,String maker,String description,String price, String comment ){
        boolean insertData =mDatabaseHelper.addData(date,maker,description,price,comment);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }

    }


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
