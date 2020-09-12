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

public class ListDataActivity extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
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

        populateListView();

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

    private void populateListView() {
        Log.d(TAG,"populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();

//        ArrayList<String> listData = new ArrayList<>();
//        while(data.moveToNext()){
//            listData.add(data.getString(1));
//            listData.add(data.getString(2));
//
//
//        }
        ArrayList<ArrayList<String>> listData = new ArrayList<ArrayList<String>>();
        while(data.moveToNext()){
            ArrayList<String> oneitem =new ArrayList<String>();

            oneitem.add(data.getString(1));
            oneitem.add(data.getString(3));
            oneitem.add(data.getString(4));
            listData.add(oneitem);


        }
        //for debugging
        Log.d(TAG,"populateListView: Displaying"+listData+" in the ListView.");
        ListAdapter adapter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listData);
        mListView.setAdapter(adapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> selectedItem = (ArrayList<String>) adapterView.getItemAtPosition(i);
                Log.d(TAG, "onItemClick: You Clicked on " + selectedItem);

                Cursor data = mDatabaseHelper.getItemID(selectedItem); //get the id associated with that name

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
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String name = adapterView.getItemAtPosition(i).toString();
//                Log.d(TAG, "onItemClick: You Clicked on " + name);
//
//                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
//
//                //some error handling
//                int itemID = -1;
//                while(data.moveToNext()){
//                    itemID = data.getInt(0);
//                }
//                if(itemID > -1){
//                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
//                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
//                    editScreenIntent.putExtra("id",itemID);
//                    editScreenIntent.putExtra("date",name);
//                    startActivity(editScreenIntent);
//                }
//                else{
//                    toastMessage("No ID associated with that name");
//                }
//            }
//        });








    }
    private void toastMessage(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.START, 0, 0);
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
