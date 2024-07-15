
package com.example.assign_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView top_panel;
    ArrayList<Event> listOfEvents;
    CalendarView MainCalendar;
    Button current_meetings;

    Button check_Contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.top_panel = findViewById(R.id.today_date);
        this.MainCalendar = findViewById(R.id.calendar);
        this.current_meetings = findViewById(R.id.Check_Meetings);
        this.check_Contacts = findViewById(R.id.check_contacts);

        this.listOfEvents = new ArrayList<>();

        // present the current date on the top panel
        presentDate();
        addEvent();
        goContacts();
        showcurrentMeetings();
    }


    /**
     * Functioon to direct the user to the Contacts Menu
     */
    void goContacts(){
        check_Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ManageContacts.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Function called to bring user to the menu to add an event
     */
    void addEvent(){
        MainCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar CurrentDate =  Calendar.getInstance();
                int currentyear = CurrentDate.get(Calendar.YEAR);
                int currentMonth = CurrentDate.get(Calendar.MONTH);
                int Currentday =CurrentDate.get(Calendar.DAY_OF_MONTH);
                int weekDay= CurrentDate.get(Calendar.DAY_OF_WEEK);


                // accounts for past dates
                if (year < currentyear || (year == currentyear && month < currentMonth) || (year == currentyear && month == currentMonth && dayOfMonth < Currentday)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Whoops");
                    builder.setMessage("Cannot add meeting in the past  ");
                    builder.setPositiveButton("OK",null);
                    builder.show();
                }else{

                    Intent intent = new Intent(MainActivity.this,newEvent.class);
                    intent.putExtra("SelectedYear", year);
                    intent.putExtra("SelectedMonth",month);
                    intent.putExtra("SelectedDay",dayOfMonth);
                    intent.putExtra("DayOfWeek",weekDay);
                    startActivity(intent);
                }

            }
        });
    }

    public void showcurrentMeetings(){
        current_meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CheckMeetings.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Function used to get the current date and present it to the user at the top of the screen
     */
    void presentDate(){

        String current_text = top_panel.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("EEE, MMM dd, yyyy");
        String currentDate = date.format(calendar.getTime());
        top_panel.setText(String.format("%s%s%s", current_text, "  ",currentDate));

    }




}