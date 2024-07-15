// David Martin
//6995948
//dm20zo
// April 3rd, 2024

package com.example.assign_2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {

    static final String  DATABASENAME = "Meetings"; // Name of Table
    static final int DATABASE_VERSION = 5; // Version Number
    static final String COLUMN_ID = "_id"; //ID
    static final String COLUMN_NAME = "Name"; // NAme of meeting

    public static final String COLUMN_DESCRIPTION = "description"; // Meeting Description
    public static final String COLUMN_DATE = "date"; // Meeting Date
    public static final String COLUMN_TIME = "time"; // Time of Meeting

    public static final String COLUMN_CONTACT = "contact";

    // String Command to Create the Database
     static final String SQL_CREATE_TABLE_COMMAND =
             "CREATE TABLE " + DATABASENAME +" (" +
                     COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                     COLUMN_NAME + " TEXT, " +
                     COLUMN_DATE + " TEXT, " +
                     COLUMN_TIME + " TEXT, " +
                     COLUMN_DESCRIPTION + " TEXT, " +
                     COLUMN_CONTACT + " TEXT)";


    public DataHelper( Context context){
        super(context,DATABASENAME, null, DATABASE_VERSION );
    }

    /**\
     *
     * @param e Take an Object Event to add to the Database
     * @return
     */
    public long addMeeting(Event e ){

        SQLiteDatabase db =this.getWritableDatabase();
        long status;
        ContentValues inputValues = new ContentValues();
        inputValues.put(DataHelper.COLUMN_NAME,e.getEventName());
        inputValues.put(DataHelper.COLUMN_DATE,e.getDate());
        inputValues.put(DataHelper.COLUMN_TIME,e.getTime());
        inputValues.put(DataHelper.COLUMN_DESCRIPTION,e.getDescription());
        inputValues.put(DataHelper.COLUMN_CONTACT,e.getContact());
        status = db.insert(DATABASENAME,null,inputValues);
        System.out.println(status);
        return status;

    }


    /**
     * Function that returns a list of meetings Currently in the Table
     */
    public List <Event> getAllMeetings(){
        List<Event> meetings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "  + DATABASENAME, null); //query the data

        try{ // Iterate through data
            if(cursor.moveToFirst()){
                do {
                    @SuppressLint("Range") Event temp =  new Event( // Create New Event Object to store
                            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT))
                    );
                    meetings.add(temp); // add it to meetings

                }while(cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }

        return meetings;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASENAME);
        onCreate(db);
    }


    /**
     *
     * @param date take in the current date and push today's Meeting to the appropriate date
     */
    public void pushMeetings(String date ){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "  + DATABASENAME, null); // Perform query on the database
        try{
            if(cursor.moveToFirst()){
                do {

                    @SuppressLint("Range") String temp_date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

                    if(temp_date.equals(date)){

                        System.out.println("in the if");
                        Calendar calendar = Calendar.getInstance(); // get the current day of the week
                        int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
                        int daysToadd;

                        switch (dayOfWeek){
                            case Calendar.FRIDAY: // Move to next Monday
                                daysToadd = 3;
                                break;

                            case Calendar.SATURDAY: // If its Saturday move event to Sunday
                                daysToadd = 1;
                                break;

                            case Calendar.SUNDAY: // if its sunday add 6
                                daysToadd = Calendar.SATURDAY - dayOfWeek + 7;
                                break;

                            default: // if its a week day, add the current date by 1
                                daysToadd =1;

                        }

                        calendar.add(Calendar.DAY_OF_MONTH,daysToadd);
                        ContentValues newinput = new ContentValues();

                        // Convert the new date to a formatted string (assuming your date is stored as text)
                        String newDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.DAY_OF_MONTH);

                        newinput.put(COLUMN_DATE, newDate);
                        db.update(DATABASENAME, newinput, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                    }
                }while(cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }
    }

    /**
     * @param date Take in the Current date and Remove Today's Meetings
     */

    @SuppressLint("Range")
    public void ClearMeetingsToday(String date){

        SQLiteDatabase db = this.getWritableDatabase(); // Query Each entry in the table
        Cursor cursor = db.rawQuery("SELECT * FROM "  + DATABASENAME, null);

        try{

            if(cursor.moveToFirst()){ // Iterate through the table
                do {

                    String temp_date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE)); // Grab the Date

                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

                    if(temp_date.equals(date)){ // If its the same date as today, Remove the meeting

                        db.delete(DATABASENAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

                    }
                }while(cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }
    }

    /**
     * Function that removes the all entries Database
     */
    public void ClearMeetings(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASENAME,null,null);
        db.close();


    }
}

