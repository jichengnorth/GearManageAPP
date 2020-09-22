package com.example.gearbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Jason Zhao on Sept/20/2020.
 * Copyright is reserved
 */

// this class will handle: 1.list all the items, 2.display only 3 of 5 info, 3.click on each of it
// 4. get the total price.
// it is based on List view sturction

public class ListDataActivity extends AppCompatActivity {

    // declare
    private static final String TAG = "ListDataActivity";
    public DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    private String makerGot;
    private String commentGot;
    private Button calculateButton;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView =(ListView)findViewById(R.id.listView);
        calculateButton =(Button) findViewById(R.id.calculateButton);
        mDatabaseHelper = new DatabaseHelper(this);

        //displace the List view
        populateListView();


        // get the total price with a toast message for better protection of privacy
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor totalprice = mDatabaseHelper.getTotalPrice();
                if(totalprice.moveToFirst())
                {
                    String  totalPrice = "Total CAD$ "+totalprice.getString(0);
                    toastMessage(totalPrice);

                }

            }
        });

    }


    /**
     * Display the data, and use putExtra to save the data when user click on the row
     * Modified and reference to https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/blob/master/SaveAndDisplaySQL/app/src/main/java/com/tabian/saveanddisplaysql/ListDataActivity.java
     *
     */

    private void populateListView() {
        Log.d(TAG,"populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();

        //An ArrayList inside of ArrayList, since we are displaying 3 info of an item
        ArrayList<ArrayList<String>> listData = new ArrayList<ArrayList<String>>();
        while(data.moveToNext()){
            ArrayList<String> oneitem =new ArrayList<String>();
            //The info we want to display are date, price and description
            oneitem.add(data.getString(1));
            oneitem.add(data.getString(3));
            oneitem.add(data.getString(4));
            listData.add(oneitem);


        }
        //for debugging
        Log.d(TAG,"populateListView: Displaying"+listData+" in the ListView.");
        //Display it with adapter
        ListAdapter adapter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listData);
        mListView.setAdapter(adapter);

        //when a click happens:
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> selectedItem = (ArrayList<String>) adapterView.getItemAtPosition(i);
                Log.d(TAG, "onItemClick: You Clicked on " + selectedItem);

                Cursor data = mDatabaseHelper.getItemID(selectedItem); //get the id associated with that item

                Log.d(TAG, "onItemClick: data retrived Clicked on " + data);
                //some error handling
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    int myId=itemID;



                    Cursor makerComGot = mDatabaseHelper.getMakerCom(selectedItem);
                    Log.d(TAG, "onItemClick: maker and commet are:  " + makerComGot);

                    while(makerComGot.moveToNext()){
                        makerGot = makerComGot.getString(2);
                        commentGot = makerComGot.getString(5);
                    }


                    //save of the data, since we will need it later for updating
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("date", selectedItem.get(0));
                    Log.d(TAG, "onItemClick: date is :  " + selectedItem.get(2));
                    editScreenIntent.putExtra("description", selectedItem.get(1));
                    editScreenIntent.putExtra("price", selectedItem.get(2));

                    editScreenIntent.putExtra("maker",  makerGot);
                    Log.d(TAG, "onItemClick: maker is :  " +makerGot );
                    editScreenIntent.putExtra("comment", commentGot);


                    Log.d(TAG, "onItemClick: saved values " + itemID+ " " +makerGot +commentGot );



                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });



    }
    private void toastMessage(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        //Position it
        toast.setGravity(Gravity.BOTTOM|Gravity.LEFT, 100, 200);
        toast.show();
    }
    //Called when add item button is clicked
    public void add_item_button(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, add_item.class);
        startActivity(intent);
    }
}
