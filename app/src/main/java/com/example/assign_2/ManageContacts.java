

package com.example.assign_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class ManageContacts extends AppCompatActivity {

    Button back_btn;

    ArrayList<String> ContactsList = new ArrayList<>();

    static final int PERMISSION = 100;

    TextView MainView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contacts);
        back_btn = findViewById(R.id.Back_Button_Contacts);
        this.MainView = findViewById(R.id.Contacts_list);
        GoBack();


        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){ // Check Permissions
            ShowContacts(this);
        }else{
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSION);
        }
    }

    /**
     * Function to present all the Contacts to the user
     */
    @SuppressLint("Range")
    void ShowContacts(Context context){

        StringBuilder sb = new StringBuilder();
        ContentResolver contentResolver = context.getContentResolver();

        String contactName = null; //used to retrieve ContactName
        String phoneNumber =  null; // Used to retreiev

        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null); // query contacts

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){

                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                this.ContactsList.add(contactName);
                sb.append("CONTACT NAME: ").append(contactName).append("\n");
                sb.append("CONTACT PHONE: ").append(phoneNumber).append("\n").append("\n");
            }

        }

        String result = sb.toString(); // Set the String to present to the main view
        MainView.setText(result);
    }

    /**
     * Function to Go back to the Main Home Screen
     */
    void GoBack(){
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageContacts.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }



}