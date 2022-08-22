package tk.yommail.ecrt.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import tk.yommail.ecrt.MainActivity;
import tk.yommail.ecrt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout spinner;
    private ConstraintLayout rootView;

    private static final String TAG = "LoginActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences settings = getSharedPreferences(
                "SETTINGS",
                MODE_PRIVATE
        );

        AppCompatDelegate.setDefaultNightMode(settings.getBoolean("DARKMODE", false) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sharedPreferences = getSharedPreferences(
                "LOGIN",
                MODE_PRIVATE
        );

        spinner = (RelativeLayout)findViewById(R.id.progressBar1);
        rootView = (ConstraintLayout) findViewById(R.id.login_rootview);

        rootView.setVisibility(View.GONE);

        String loggedInEmail = sharedPreferences.getString("EMAIL", null);
        String loggedInPassword = sharedPreferences.getString("PASSWORD", null);

        if (loggedInEmail != null && loggedInPassword != null) {
            Log.d(TAG, "onCreate: Stored ");
            LoginRequest loginRequest = new LoginRequest(
                    loggedInEmail,
                    loggedInPassword,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                Log.d(TAG, "onResponse: " + jsonResponse.toString());

                                if (jsonResponse.getBoolean("success")) {


                                    String name = jsonResponse.getString("name");
                                    String email = jsonResponse.getString("email");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);

                                    startActivity(intent);

                                } else {
                                    spinner.setVisibility(View.GONE);
                                    rootView.setVisibility(View.VISIBLE);
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setMessage("Login Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );

            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        } else {
            Log.d(TAG, "onCreate: Not stored");
            spinner.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
        }

        final TextInputLayout email = (TextInputLayout) findViewById(R.id.loginEmail);
        final TextInputLayout password = (TextInputLayout) findViewById(R.id.loginPassword);

        final Button login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(email.getEditText().getText().toString());
                if (matcher.matches() && password.getEditText().getText().length() >= 6) {
                    spinner.setVisibility(View.VISIBLE);
                    LoginRequest loginRequest = new LoginRequest(
                            email.getEditText().getText().toString(),
                            password.getEditText().getText().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);

                                        if (jsonResponse.getBoolean("success")) {

                                            String name = jsonResponse.getString("name");
                                            String email = jsonResponse.getString("email");

                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString("NAME", name);
                                            editor.putString("EMAIL", email);
                                            editor.putString("PASSWORD", password.getEditText().getText().toString());
                                            editor.apply();

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("name", name);
                                            intent.putExtra("email", email);

                                            startActivity(intent);

                                        } else {
                                            spinner.setVisibility(View.GONE);
                                            rootView.setVisibility(View.VISIBLE);
                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setMessage("Login Failed")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );

                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                } else {
                    Log.d(TAG, "onClick: email: " + matcher.matches() + "  |  pass: " + password.getEditText().getText().toString().length());
                    if (!matcher.matches()) {
                        email.setError("Please input a valid email.");
                    } else {
                        email.setError(null);
                    }
                    if (password.getEditText().getText().toString().length() < 6) {
                        password.setError("Password needs to be longer than 6 characters long.");
                    } else {
                        password.setError(null);
                    }
                }
            }
        });

        final Button signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        final TextView loginProblems = (TextView) findViewById(R.id.loginProblem);
        loginProblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Too bad.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
