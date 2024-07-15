

package com.example.assign_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class newEvent extends AppCompatActivity {

    TextView topPanel; // Retrieve Current Date to present to the user

    int selectedYear; // year selected for date format
    int selectedMonth; // month selected for date format
    int selectedDate; // day selected for date format

    String meetingName;

    String meetingDescription;

    Button back_btn; //button to go back to main menu

    Button add_btn; // button to add an event

    TimePicker timePicker; // Pointer to timepicker

     Context context;
    DataHelper db; // Database helper instance

    Spinner contactSpinner; // Reference to Spinner Component in the GUI

    String Contact; // Contact Variable

    List<String> contactsList = new ArrayList<>(); // List to store The Names of Contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        topPanel = findViewById(R.id.topPanel);
        this.back_btn = findViewById(R.id.Back_Button);

        this.add_btn = findViewById(R.id.Add_meeting);
        timePicker = findViewById(R.id.select_time);
        this.contactSpinner = findViewById(R.id.contact_selection);

        context = this;

        db = new DataHelper(context);

        loadContacts();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactsList); // assign spinner to contain elements in the contact list
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // set elements in the drop down menu
        contactSpinner.setAdapter(adapter); // set Adapter

        presentSelectedDate();
        goBack();
        addEvent();

    }
    /*
        Function to present the date in which the user wishes to create a meeting on
     */
    void presentSelectedDate() {

        this.selectedYear = getIntent().getIntExtra("SelectedYear", 0);
        this.selectedMonth = getIntent().getIntExtra("SelectedMonth", 0);
        this.selectedDate = getIntent().getIntExtra("SelectedDay", 0);
        this.topPanel.setText(selectedYear + "/" + selectedMonth + "/" + selectedDate); // format the date for the user
    }


    /**
     * Function to load all the contacts stored on the device
     */
    void loadContacts(){

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null); // query data

        if(cursor!=null && cursor.getCount()>0){ // iterate through contacts

            while( cursor.moveToNext()) {

                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // retrieve contact name

                contactsList.add(contactName); // add to contacts list
            }

        }

        cursor.close();

    }


    /**
     * Function to save the Event the user stores
     */
    void saveEvent(){

        long currentStatus; // status of insertion into the Database
        meetingName = ((EditText) findViewById(R.id.Meeting_name)).getText().toString(); // retrieve meeting name
        meetingDescription = ((EditText) findViewById(R.id.Meeting_Description)).getText().toString(); // retrieve meeting description
        int hour= this.timePicker.getHour(); // restive hour from user
        int minute = this.timePicker.getMinute(); // retrieve minute from user
        String InputDate = selectedYear + "/" + selectedMonth + "/" + selectedDate; // build string for date

        String meetingTime = String.format("%02d:%02d", hour, minute); // format time of the meeting


                contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // for selected contact in the list

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String selectedContact = (String) parent.getItemAtPosition(position); // assign contact to what the user chooses in the spinner
                        Contact = selectedContact;

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { // case where the user does not choose a contact
                        if(parent.getCount()>0){
                            parent.setSelection(0);
                            String selectedContact = (String) parent.getItemAtPosition(0);

                            Contact = selectedContact;

                        }
                    }
                });

        Event addedEvent = new Event(meetingName,InputDate,meetingTime,meetingDescription,Contact); // create event object
        currentStatus = db.addMeeting(addedEvent); // call add Meeting in the Database Class

        if(this.meetingDescription == " " || this.meetingName == " "){

            AlertDialog.Builder builder = new AlertDialog.Builder(newEvent.this);
            builder.setTitle("Whoops");
            builder.setMessage("Fill in the required information ");
            builder.setPositiveButton("OK",null);

        }else{

            if(currentStatus == -1){ // error status when adding an event to the database

                AlertDialog.Builder builder = new AlertDialog.Builder(newEvent.this);
                builder.setTitle("Whoops");
                builder.setMessage("There was an Error ");
                builder.setPositiveButton("OK",null);
                builder.show();

            }else{

                // other wise, alert the user that the insertion into the database was successful

                AlertDialog.Builder builder = new AlertDialog.Builder(newEvent.this);
                builder.setTitle("Alert");
                builder.setMessage("Event Added  ");
                builder.setPositiveButton("OK",null);
                builder.show();
            }
        }
    }

    /**
     * Function to call whene the add meetings button is called
     */
    void addEvent(){

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
    }

    /**
     * Function for the user to be able to go back to the main menu
     */
    void goBack(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(newEvent.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }

}