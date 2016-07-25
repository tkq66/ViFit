package com.dtk.vifit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{

    private Button signin_btn, register_btn;
    private EditText email_form, pwd_form;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_form = (EditText) findViewById(R.id.signin_email);
        pwd_form = (EditText) findViewById(R.id.signin_password);
        signin_btn = (Button) findViewById(R.id.signin_button);
        register_btn = (Button) findViewById(R.id.register_button);

        signin_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signinHandler(email_form.getText().toString(), pwd_form.getText().toString());
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerHandler();
            }
        });
    }

    /**
     * Sign-in logic to authenticate if user provided valid sign in information, returning 'true'
     * if user exist and 'false' if user doesn't exist.
     * @param email Email used to sign in.
     * @param pwd Password used to sign in.
     * @return boolean Status of whether user exist in database or not.
     */
    private boolean authenticateUser(String email, String pwd)
    {
        if(email.equals("admin") && pwd.equals("admin"))
        {
            return true;
        }

        return false;
    }

    /**
     * Handles sign-in event prompted by user upon pressing the sign-in button. If success, app
     * proceeds to next page showing user's main page. If fails, stay on the same page and
     * display Toast message.
     * @param email Email used to identify user and sign in.
     * @param pwd Password used to authenticate user and sign in.
     */
    private void signinHandler(String email, String pwd)
    {
        if(!authenticateUser(email, pwd))
        {
            Toast.makeText(getApplicationContext(),
                    R.string.msg_signin_failed,
                    Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        proceedToMainActivity();
    }

    private void proceedToMainActivity()
    {
        Intent i = new Intent(getApplicationContext(), DailyProgressActivity.class);
        startActivity(i);
        setContentView(R.layout.activity_daily_progress);
    }

    /**
     * Handles register event prompted by user upon pressing the register button. App proceeds to
     * the next page that asks user for registeration information.
     */
    private void registerHandler()
    {
        Intent i = new Intent(getApplicationContext(), InputFormActivity.class);
        startActivity(i);
        setContentView(R.layout.activity_input_form);
    }
}
