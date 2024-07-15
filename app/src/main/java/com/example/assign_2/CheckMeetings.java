

package com.example.assign_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class CheckMeetings extends AppCompatActivity {

    Button go_back; // Go Back Button
    DataHelper db; // Instance of Database Class

    List<Event> listOfMeetings; // Array List of meetings

    TextView mainView; // Main View for Presenting Meetings

    Button deleteALL; // Delete all meetings Button

    Button deleteToday; // Delete meetings Today button
    Button pushMeeting; // Push meetings Button
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_meetings);
        go_back = findViewById(R.id.Back_Button_check);
        mainView = findViewById(R.id.Meetings_section);
        deleteALL = findViewById(R.id.delete_all_meetings);
        deleteToday = findViewById(R.id.delete_meetings_today);
        pushMeeting = findViewById(R.id.push_meeting);

        db = new DataHelper(this);

        backtoMain();
        showMeetings();
        ClearAllMeetings();
        ClearMeetingsToday();
        pushMeeting();

    }


    /**
     * Function to present all meetings to the User
     */
    void showMeetings(){

        this.listOfMeetings = db. getAllMeetings(); // assign the List to the data from the query in the Datahelper Class

        StringBuilder temp = new StringBuilder(); // Build String

        for( Event e: listOfMeetings){ // Loop through each entry and append it to a String

            temp.append("Meeting Name: ").append(e.getEventName()).append("\n");
            temp.append("Meeting Date : ").append(e.getDate()).append("\n");
            temp.append("Meeting Time: ").append(e.getTime()).append("\n");
            temp.append("Meeting Description: ").append(e.getDescription()).append("\n");
            temp.append("Contact: ").append(e.getContact()).append("\n").append("\n");

        }
        mainView.setText(temp.toString()); // Set the MainView to the String
    }

    /**
     * Function to clear All Meetings
     */
    void ClearAllMeetings(){
        deleteALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.ClearMeetings();
                showMeetings();
            }
        });
    }

    /**
     * Function to Clear Meetings Just for Today
     */
    void ClearMeetingsToday(){

        deleteToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar CurrentDate =  Calendar.getInstance();
                int currentyear = CurrentDate.get(Calendar.YEAR);
                int currentMonth = CurrentDate.get(Calendar.MONTH);
                int Currentday =CurrentDate.get(Calendar.DAY_OF_MONTH);
                String Date = currentyear + "/"+currentMonth+"/" + Currentday;
                db.ClearMeetingsToday(Date);
                showMeetings();
            }
        });

    }
    void pushMeeting(){
        pushMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar CurrentDate =  Calendar.getInstance();
                int currentyear = CurrentDate.get(Calendar.YEAR);
                int currentMonth = CurrentDate.get(Calendar.MONTH);
                int Currentday =CurrentDate.get(Calendar.DAY_OF_MONTH);
                String Date = currentyear + "/"+currentMonth+"/" + Currentday;
                
                db.pushMeetings(Date);
                showMeetings();
            }
        });
    }
    /**
     * Function to back to the main Screen
     */
    void backtoMain(){
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckMeetings.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}