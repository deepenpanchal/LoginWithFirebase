package com.flash.apps.logmein;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView clickHere, forgotPassword;
    private EditText emailaddress;
    private EditText userPassword;
    private TextInputLayout txlemail;
    private TextInputLayout txlpassword;
    private ProgressBar progressBar;
    private CheckBox rememberMe;

    private FirebaseAuth firebaseAuth;
    private SharedPreferences loginRemember;
    private SharedPreferences.Editor loginRememberEditor;

    private boolean saveLogin;

    //To navigate to the login if remember me is checked
    public void loginNow(){
        startActivity(new Intent(MainActivity.this, Dashboard.class));
        MainActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.login);
        clickHere = findViewById(R.id.clickhere);
        forgotPassword = findViewById(R.id.forgotpassword);
        emailaddress = findViewById(R.id.emailaddress);
        userPassword = findViewById(R.id.password);
        txlemail = findViewById(R.id.text_input_layout);
        txlpassword = findViewById(R.id.text_input_layout2);
        progressBar = findViewById(R.id.progressBar);
        rememberMe = findViewById(R.id.rememberme);
        firebaseAuth = FirebaseAuth.getInstance();
        loginRemember = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginRememberEditor = loginRemember.edit();


        clickHere.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        btnLogin.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.buttonText));

        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPass.class));
            }
        });


        saveLogin = loginRemember.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            emailaddress.setText(loginRemember.getString("username", ""));
            userPassword.setText(loginRemember.getString("password", ""));
            rememberMe.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(emailaddress.getWindowToken(), 0);

                final String email = emailaddress.getText().toString();
                String password = userPassword.getText().toString();

                if (rememberMe.isChecked()) {
                    loginRememberEditor.putBoolean("saveLogin", true);
                    loginRememberEditor.putString("username", email);
                    loginRememberEditor.putString("password", password);
                    loginRememberEditor.commit();
                } else {
                    loginRememberEditor.clear();
                    loginRememberEditor.commit();
                }

                //doSomethingElse();

                if(TextUtils.isEmpty(email))
                {
                    txlemail.setError("Email address is required");
                    return;
                }

                else
                {
                    txlemail.setError(null);
                }

                if(TextUtils.isEmpty(password))
                {
                    txlpassword.setError("Password can't be empty");
                }

                else
                {
                    txlpassword.setError(null);
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, getString(R.string.authError), Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            Intent toDashboard = new Intent(MainActivity.this, Dashboard.class);
                            toDashboard.putExtra("email", email);
                            startActivity(toDashboard);
                            finish();
                        }



                    }
                });

            }
        });

    }
}
