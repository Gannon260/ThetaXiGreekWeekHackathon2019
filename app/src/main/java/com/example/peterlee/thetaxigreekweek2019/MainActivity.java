package com.example.peterlee.thetaxigreekweek2019;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Date;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        createFakeData();
    }
    public void createFakeData() {
        Meal meal1 = new Meal("20190401", 40, MealEnum.MONDAY_LUNCH, Cuisine.AMERICAN);
        Meal meal2 = new Meal("20190401", 60, MealEnum.MONDAY_DINNER, Cuisine.AMERICAN);
        Meal meal3 = new Meal("20190402", 45, MealEnum.TUESDAY_LUNCH, Cuisine.LATIN);
        Meal meal4 = new Meal("20190402", 55, MealEnum.TUESDAY_DINNER, Cuisine.ASIAN);
        Meal meal5 = new Meal("20190403", 50, MealEnum.WEDNESDAY_LUNCH, Cuisine.LATIN);
        Meal meal6 = new Meal("20190403", 70, MealEnum.WEDNESDAY_DINNER, Cuisine.AMERICAN);
        Meal meal7 = new Meal("20190404", 40, MealEnum.THURSDAY_LUNCH, Cuisine.AMERICAN);
        Meal meal8 = new Meal("20190404", 20, MealEnum.THURSDAY_DINNER, Cuisine.LATIN);
        Meal meal9 = new Meal("20190405", 40, MealEnum.FRIDAY_LUNCH, Cuisine.AMERICAN);
        addMeal(meal1);
        addMeal(meal2);
        addMeal(meal3);
        addMeal(meal4);
        addMeal(meal5);
        addMeal(meal6);
        addMeal(meal7);
        addMeal(meal8);
        addMeal(meal9);

        database = FirebaseDatabase.getInstance();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        updateMealInfoForWeek(year, month, day);

        setKeyString();
    }

    public void setKeyString() {
        TextView keyTextView = findViewById(R.id.keyTextView);
        keyTextView.setText(Html.fromHtml("<font color=\"#F44336\">American</font> | " +
                "<font color='#673AB7'>Asian</font> | <font color='#4CAF50'>European</font> | " +
                "<font color='#03A9F4'>Latin</font> | <font color='#FF9800'>Middle Eastern</font>"));
    }

    public void pickDate(MenuItem item) {
        Calendar calander = Calendar.getInstance();
        int curYear = calander.get(Calendar.YEAR);
        int curMonth = calander.get(Calendar.MONTH);
        int curDay = calander.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                MainActivity.this, curYear, curMonth, curDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        updateMealInfoForWeek(i, i1, i2);
    }

    protected String bool2str(Boolean b) {
        return b ? "1" : "0";
    }

    //add meal to firebase.
    protected void addMeal(Meal meal) {
        //Toast.makeText(getApplicationContext(),meal.toString(),Toast.LENGTH_SHORT).show();
        myRef = database.getReference(meal.getDate()
                + bool2str(meal.getMealEnum().isDinner()));
        myRef.setValue(meal);
    }

    public void updateMealInfoForWeek(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        while (cal.get(Calendar.DAY_OF_WEEK) != 1) {
            Log.v("REMOVE", getDate(cal) + ";" + cal.get(Calendar.DAY_OF_WEEK));
            cal.add(Calendar.DATE, -1);
            Log.v("REMOVED", getDate(cal));
        }

        TextView curWeek = findViewById(R.id.weekTextView);

        cal.add(Calendar.DATE, 1);
        String curWeekStr = "" + (cal.get(Calendar.MONTH) + 1) + "/"
                + (cal.get(Calendar.DAY_OF_MONTH)) + " - ";
        setMeal(getDate(cal) + "0", (TextView) findViewById(R.id.MLTextView));
        setMeal(getDate(cal) + "1", (TextView) findViewById(R.id.MDTextView));
        cal.add(Calendar.DATE, 1);
        setMeal(getDate(cal) + "0", (TextView) findViewById(R.id.TLTextView));
        setMeal(getDate(cal) + "1", (TextView) findViewById(R.id.TDTextView));
        cal.add(Calendar.DATE, 1);
        setMeal(getDate(cal) + "0", (TextView) findViewById(R.id.WLTextView));
        setMeal(getDate(cal) + "1", (TextView) findViewById(R.id.WDTextView));
        cal.add(Calendar.DATE, 1);
        setMeal(getDate(cal) + "0", (TextView) findViewById(R.id.TRLTextView));
        setMeal(getDate(cal) + "1", (TextView) findViewById(R.id.TRDTextView));
        cal.add(Calendar.DATE, 1);
        setMeal(getDate(cal) + "0", (TextView) findViewById(R.id.FLTextView));
        curWeekStr = curWeekStr + (cal.get(Calendar.MONTH) + 1) + "/"
                + (cal.get(Calendar.DAY_OF_MONTH));
        curWeek.setText(curWeekStr);
    }

    public String getDate(Calendar cal) {
        String newYear = "" + cal.get(Calendar.YEAR);

        String newMonth = "" + (cal.get(Calendar.MONTH) + 1);
        if (newMonth.length() == 1) {
            newMonth = "0" + newMonth;
        }

        String newDay = "" + (cal.get(Calendar.DAY_OF_MONTH));
        if (newDay.length() == 1) {
            newDay = "0" + newDay;
        }
        return newYear + newMonth + newDay;
    }

    protected void setMeal(String date, TextView view) {
        final TextView tv = view;
        myRef = database.getReference(date);
        ValueEventListener postListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Meal meal = dataSnapshot.getValue(Meal.class);
                if (meal != null) {
                    tv.setText("" + meal.getNumPeopleEating());
                    switch (meal.getCuisine()) {
                        case ASIAN:
                            //Set background
                            tv.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.circle_asian));
                            break;
                        case AMERICAN:
                            tv.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.circle_american));
                            break;
                        case LATIN:
                            tv.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.circle_latin));
                            break;
                    }
                } else {
                    tv.setText("-");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tv.setText("-");
            }
        };
        final ValueEventListener valueEventListener = myRef.addValueEventListener(postListener);
    }


    public void setupMeals() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
