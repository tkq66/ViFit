package com.dtk.vifit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class InputFormActivity extends Activity
{
    private RadioGroup sex_group, weight_unit_group;
    private EditText age_form, weight_form, email_form, pwd_form;
    private Button done_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);

        sex_group = (RadioGroup) findViewById(R.id.sex_radio_group);
        weight_unit_group = (RadioGroup) findViewById(R.id.weight_unit_radio_group);
        age_form = (EditText) findViewById(R.id.age_form);
        weight_form = (EditText) findViewById(R.id.weight_form);
        email_form = (EditText) findViewById(R.id.email_regis_form);
        pwd_form = (EditText) findViewById(R.id.password_regis_form);
        done_btn = (Button) findViewById(R.id.done_button);

        done_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                submitFormHandle();
            }
        });
    }

    private JSONObject parseDataToJSON() throws JSONException, NullPointerException
    {
        JSONObject userData_json = new JSONObject();

        int sexRadioBtnId = sex_group.getCheckedRadioButtonId();
        int weightUnitRadioBtnId = weight_unit_group.getCheckedRadioButtonId();
        RadioButton sex_btn = (RadioButton) findViewById(sexRadioBtnId);
        RadioButton weight_unit_btn = (RadioButton) findViewById(weightUnitRadioBtnId);

        userData_json.put("email", email_form.getText().toString());
        userData_json.put("password", pwd_form.getText().toString());
        userData_json.put("age", age_form.getText().toString());
        userData_json.put("sex", sex_btn.getText().toString());
        userData_json.put("weight", weight_form.getText().toString());
        userData_json.put("weight_unit", weight_unit_btn.getText().toString());

        return userData_json;
    }

    /**
     * Validate each data point provided by the user to make sure that they are valid. If they
     * are valid, return true, if they are not valid, return false.
     * @return boolean Boolean asserting if data entered are valid or not.
     */
    private boolean validateData()
    {
        return true;
    }

    private boolean submitDatatoServer()
    {
        return true;
    }

    private void proceedToMainActivity()
    {
        Intent i = new Intent(getApplicationContext(), DailyProgressActivity.class);
        startActivity(i);
        setContentView(R.layout.activity_daily_progress);
    }

    /**
     * Handle the data that was prepped through this page, then proceed to the next activity, which
     * is the presentation of daily goals.
     */
    private void submitFormHandle()
    {
        // Parse form data to JSON format. Do nothing if parsing failed.
        JSONObject userData_json;
        try
        {
            userData_json = parseDataToJSON();
        }
        catch(NullPointerException exception)
        {
            Toast.makeText(getApplicationContext(),
                    "Error reading user data!",
                    Toast.LENGTH_SHORT)
                    .show();

            return;
        }
        catch (JSONException exception)
        {
            Toast.makeText(getApplicationContext(),
                    "Parsing form data to JSON failed!",
                    Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        // If JSON parsing of form data is successful, validate that the data provided
        // is in correct format. Do nothing if format is incorrect.
        if(!validateData())
        {
            Toast.makeText(getApplicationContext(),
                    "Information provided is invalid! Please try again.",
                    Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        // If JSON data is valid, process it and report success.
        submitDatatoServer();
        Toast.makeText(getApplicationContext(),
                "Form sent!",
                Toast.LENGTH_SHORT)
                .show();

        proceedToMainActivity();
    }
}
