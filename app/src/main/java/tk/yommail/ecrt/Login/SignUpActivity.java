package tk.yommail.ecrt.Login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import tk.yommail.ecrt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextInputLayout name = (TextInputLayout) findViewById(R.id.signupName);
        final TextInputLayout email = (TextInputLayout) findViewById(R.id.signupEmail);
        final TextInputLayout password = (TextInputLayout) findViewById(R.id.signupPassword);
        final TextInputLayout confirmPassword = (TextInputLayout) findViewById(R.id.signupConfirmPassword);

        final Button signupButton = (Button) findViewById(R.id.signupButton);

        final RelativeLayout spinner = (RelativeLayout) findViewById(R.id.signup_spinner);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO gör om
                if (name.getEditText().getText().toString().length() != 0 &&
                    email.getEditText().getText().toString().length() != 0 &&
                    password.getEditText().getText().toString().length() != 0 &&
                    confirmPassword.getEditText().getText().toString().length() != 0) {
                    if (password.getEditText().getText().toString().length() >= 6) {
                        if (password.getEditText().getText().toString().equals(confirmPassword.getEditText().getText().toString())) {
                            name.setError(null);
                            email.setError(null);
                            password.setError(null);
                            confirmPassword.setError(null);
                            spinner.setVisibility(View.VISIBLE);
                            SignUpRequest registerRequest = new SignUpRequest(

                                    name.getEditText().getText().toString(),
                                    email.getEditText().getText().toString().toLowerCase(),
                                    password.getEditText().getText().toString(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);

                                                if (jsonResponse.getBoolean("success")) {
                                                    startActivity(new Intent(SignUpActivity.this,
                                                            LoginActivity.class));
                                                } else {
                                                    spinner.setVisibility(View.INVISIBLE);
                                                    new AlertDialog.Builder(SignUpActivity.this)
                                                            .setTitle("Registration failed")
                                                            .setMessage(jsonResponse.getString("desc"))
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

                            RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                            queue.add(registerRequest);
                        } else {
                            confirmPassword.setError("The passwords doesn't match.");
                            confirmPassword.getEditText().setText("");
                        }
                    } else {
                        password.setError("Your password needs to be 6 characters or longer.");
                        confirmPassword.setError(null);
                    }
                } else {
                    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email.getEditText().getText().toString());
                    if (name.getEditText().getText().toString().length() == 0) {
                        name.setError("Please input a name.");
                    } else {
                        name.setError(null);
                    }
                    if (email.getEditText().getText().toString().length() == 0 || !matcher.find()){
                        if (email.getEditText().getText().toString().length() == 0) {
                            Log.d(TAG, "onClick: " + email.getEditText().getText().toString().length());
                        } else if (!matcher.find()) {
                            Log.d(TAG, "onClick: Matcher " + matcher.find());
                        }
                        email.setError("Please input a valid email.");
                    } else {
                        Log.d(TAG, "onClick: setting null");
                        email.setError(null);
                    }
                    if (password.getEditText().getText().toString().length() == 0) {
                        password.setError("Your password needs to be 6 characters or longer.");
                    } else {
                        password.setError(null);
                    }
                }
            }
        });
    }
}
