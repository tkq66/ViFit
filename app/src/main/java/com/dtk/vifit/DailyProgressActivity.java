package com.dtk.vifit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DailyProgressActivity extends Activity
{
    private TextView welcome_text, time_text;
    private Spinner select_date;
    private TableLayout schedule_table;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_progress);

        JSONArray scheduleJSONArray;

        try
        {
            scheduleJSONArray = getScheduleJSONArray();
        }
        catch(JSONException exception)
        {
            Toast.makeText(getApplicationContext(),
                    "Failed to parse JSON schedule!",
                    Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        welcome_text = (TextView) findViewById(R.id.welcome_text);
        time_text = (TextView) findViewById(R.id.time_text);
        select_date = (Spinner) findViewById(R.id.select_date);
        schedule_table = (TableLayout) findViewById(R.id.schedule_table);

        populateWelcomeText(welcome_text, getUserNameString());
        populateTimeText(time_text, getTimeString());
        try
        {
            populateDateScheduleSelector(schedule_table, select_date, scheduleJSONArray);
        }
        catch(JSONException exception)
        {
            Toast.makeText(getApplicationContext(),
                    "Accessing schedule JSON failed!",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private JSONArray createMockScheduleJSON() throws JSONException
    {
            String mockScheduleJSONString = "{\"Schedule\":" +
                                                "[" +
                                                    "{" +
                                                        "\"Monday\":[" +
                                                            "{" +
                                                                "\"08:00 AM\":\"Push up\"" +
                                                            "}," +
                                                            "{" +
                                                                "\"09:00 AM\":\"Sit up\"" +
                                                            "}," +
                                                            "{" +
                                                                "\"10:00 AM\":\"Walk\"" +
                                                            "}" +
                                                        "]" +
                                                    "}," +
                                                    "{" +
                                                        "\"Tuesday\":[" +
                                                            "{" +
                                                                "\"11:00 AM\":\"Cardio\"" +
                                                            "}," +
                                                            "{" +
                                                                "\"12:00 PM\":\"Run\"" +
                                                            "}," +
                                                            "{" +
                                                                "\"10:00 PM\":\"Walk\"" +
                                                            "}" +
                                                        "]" +
                                                    "}," +
                                                    "{" +
                                                        "\"Wednesday\":[" +
                                                            "{" +
                                                                "\"12:00 AM\":\"Sacrifice 3 virgins to the dark lord\"" +
                                                            "}" +
                                                        "]" +
                                                    "}" +
                                                "]" +
                                            "}";

        JSONObject mockScheduleJSON = new JSONObject(mockScheduleJSONString);

        return mockScheduleJSON.getJSONArray("Schedule");
    }

    private String getUserNameString()
    {
        return "Dan";
    }

    private String getTimeString()
    {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        date.setTimeZone(TimeZone.getDefault());

        return date.format(currentTime);
    }

    private JSONArray getScheduleJSONArray() throws JSONException
    {
        return createMockScheduleJSON();
    }

    private void populateWelcomeText(TextView textView, String nameString)
    {
        String welcomeText = "Hello " + nameString + "! The time is";
        textView.setText(welcomeText);
    }

    private void populateTimeText(TextView textView, String timeString)
    {
        textView.setText(timeString);
    }

    private void populateDateScheduleSelector(TableLayout table,
                                              Spinner dateSelector,
                                              JSONArray dateListJSON)
                                                throws JSONException
    {
        //  Get all dates into a list
        List<String> dateList = new ArrayList<String>();
        for(int i = 0; i < dateListJSON.length(); i++)
        {
            dateList.add(dateListJSON.getJSONObject(i).keys().next());
        }

        //  Add each date as a member of the dropdown selector
        ArrayAdapter<String> dateListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                dateList);
        dateListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSelector.setAdapter(dateListAdapter);

        //  Add listener to each date selection, to display respective daily schedule
        final TableLayout tableLayout = table;
        final JSONArray dailySchedule = dateListJSON;
        dateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                JSONArray dailyScheduleJSON;
                try
                {
                    dailyScheduleJSON = dailySchedule
                            .getJSONObject(position)
                            .getJSONArray(selectedItem);
                    populateDailyScheduleTableFromDailyScheduleJSON(tableLayout, dailyScheduleJSON);
                }
                catch(JSONException exception)
                {
                    Toast.makeText(getApplicationContext(),
                            "Accessing daily schedule JSON failed!",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void populateDailyScheduleTableFromDailyScheduleJSON(TableLayout table,
                                                                 JSONArray dailyScheduleJSON)
                                                                    throws JSONException
    {
        table.removeAllViews();

        for(int i = 0; i < dailyScheduleJSON.length(); i++)
        {
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView time = new TextView(getApplicationContext());
            TextView routine = new TextView(getApplicationContext());

            JSONObject task = dailyScheduleJSON.getJSONObject(i);
            String timeText = task.keys().next();
            String routineText = task.getString(timeText);
            time.setText(timeText);
            routine.setText(routineText);
            time.setTextColor(Color.BLACK);
            routine.setTextColor(Color.BLACK);

            row.addView(time);
            row.addView(routine);
            table.addView(row);
        }
    }
}
