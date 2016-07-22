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

    private Button signin_btn;
    private EditText email_form, pwd_form;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_form = (EditText) findViewById(R.id.signin_email);
        pwd_form = (EditText) findViewById(R.id.signin_password);
        signin_btn = (Button) findViewById(R.id.signin_button);

        signin_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signinHandler(email_form.getText().toString(), pwd_form.getText().toString());
            }
        });
    }

    /**
     *  Handles sign-in event prompted by user upon pressing the sign-in button by displaying
     *  Toast message whether sign in succeeds or failed. If success, app proceeds to next
     *  page showing user's main page. If fails, stay on the same page.
     *  @param email Email used to identify user and sign in.
     *  @param pwd Password used to authenticate user and sign in.
     */
    private void signinHandler(String email, String pwd)
    {
        if(this.authenticateUser(email, pwd))
        {
            Toast.makeText(getApplicationContext(),
                    R.string.msg_signin_success,
                    Toast.LENGTH_SHORT)
                    .show();

            Intent i = new Intent(getApplicationContext(), DailyProgressActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_daily_progress);
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    R.string.msg_signin_failed,
                    Toast.LENGTH_SHORT)
                    .show();
        }
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
}
