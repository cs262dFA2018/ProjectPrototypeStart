package edu.calvin.cs262.cs262d.eventconnect.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.calvin.cs262.cs262d.eventconnect.R;
import edu.calvin.cs262.cs262d.eventconnect.data.Event;
import edu.calvin.cs262.cs262d.eventconnect.data.MockDatabase;

public class AddEvent extends AppCompatActivity {
    private EditText eventTitle, eventDescription, eventHost, eventDate, eventLocation, eventCost, eventThreshold, eventCapacity;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private DatePicker datePicker;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        //access the UI Edit Texts

        // initialize a DatePickerDialog set to name date for the onClickListener
        calendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //datePicker.setMinDate(System.currentTimeMillis());
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        eventTitle = (EditText) findViewById(R.id.title);
        eventDescription = (EditText) findViewById(R.id.description);
        eventHost = (EditText) findViewById(R.id.host);
        eventDate = (EditText) findViewById(R.id.date);

        // onClick listener for eventDate to pull up the calendar widget
        eventDate.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick for activating the calendar widget
             * once the date EditText is clicked
             * @param view
             */
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEvent.this, date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        eventLocation = (EditText) findViewById(R.id.location);
        eventCost = (EditText) findViewById(R.id.cost);
        eventThreshold = (EditText) findViewById(R.id.threshold);
        eventCapacity = (EditText) findViewById(R.id.capacity);

        //setup toolbar bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //then set up toolbar/actionbar's up navigation
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }

    }

    /*unless access to the settings activity is added from here,
     * onOptionItemSelected really needs to care about only the back arrow.
     */

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param view
     */
    public void onCreateEventClicked(View view) {
        //access the data from the UI elements
        //TODO: Enforce a title and description??
        String title = eventTitle.getText().toString();
        String desc = eventDescription.getText().toString();
        String loc;
        String host;
        Long date2;
        String date = eventDate.getText().toString();
        Event event = new Event();

        double cost;
        try {
            cost = Double.parseDouble(eventCost.getText().toString());
        }
        catch (java.lang.NumberFormatException e) {
            cost = 0;
        }

        int threshold;
        final int capacity;

        try {
            threshold = (int) Math.floor(Double.parseDouble(eventThreshold.getText().toString()));
            event.setMinThreshold(threshold);
            }
        catch (java.lang.NumberFormatException e) {
            eventThreshold.setError(getString(R.string.error_invalid_number));
            return;
        }
        catch (RuntimeException e){
            eventThreshold.setError(getString(R.string.error_invalid_MinNumber));
            return;
        }

        try {
            String capacityText = eventCapacity.getText().toString();
            // if the capacity is not an empty string, turn it into a number
            if (!capacityText.equals("")){
                capacity = (int) Math.floor(Double.parseDouble(capacityText));
                event.setMaxCapacity(capacity);
            }
            // other wise set to -1 which mean there is no max capacity
            else {
                capacity = -1;
            }
        }
        catch (java.lang.NumberFormatException e) {
            eventCapacity.setError(getString(R.string.error_invalid_number));
            return;
        }
        catch (RuntimeException e){
            eventCapacity.setError(getString(R.string.error_invalid_MaxNumber));
            return;
        }

        //store the data from the UI elements
        event.setTitle(title);
        event.setDescription(desc);

        try {
            host = eventHost.getText().toString();
            if (!host.equals("")){
                event.setHost(host);
            } else {
                eventHost.setError(getString(R.string.error_empty_host));
                return;
            }
        }
        catch (RuntimeException e){} // remember this catch block if we ever throw a runtimeException in the Event class for event.setHost(host)

        try {
            loc = eventLocation.getText().toString();
            if(!loc.equals("")){
                event.setLocation(loc);
            } else{
                eventLocation.setError(getString(R.string.error_empty_location));
                return;
            }
        }
        catch (RuntimeException e){}// remember this catch block if we ever throw a runtimeException in the Event class for event.setLocation(loc)

        //todo: compare for valid dates and set toast to alert user
        /**
        try{
            date2 = datePicker.getMinDate();
            if (calendar.getTime() > calendar.getMinimum(Calendar.getInstance()) && !date.equals("")) {
                Toast.makeText(this, context.getString(R.string.error_invalid_date),
                        Toast.LENGTH_LONG).show();
            } else {
                eventDate.setError(getString(R.string.error_invalid_date));
                return;
            }

        }

        catch (RuntimeException e){}
         **/

        event.setCost(cost);
        event.setDate(date);
        //access and update the database.

        MockDatabase database = MockDatabase.getInstance();
        database.addEvent(event);
        finish();
    }

    /**
     * This method updates the onClick listener for the calendar
     * widget, updating it to the MM/dd/yy format and Local US date
     **/
    private void updateLabel(){
        String DateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat, Locale.US);
        eventDate.setText(sdf.format(calendar.getTime()));

    }

}
