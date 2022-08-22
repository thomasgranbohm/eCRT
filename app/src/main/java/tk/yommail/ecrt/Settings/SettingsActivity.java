package tk.yommail.ecrt.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import tk.yommail.ecrt.Login.LoginActivity;
import tk.yommail.ecrt.MainActivityFragments.ProfileFragment;
import tk.yommail.ecrt.Misc.AddCreditcardDialog;
import tk.yommail.ecrt.R;

public class  SettingsActivity extends AppCompatActivity implements AddCreditcardDialog.AddCredidcardDialogListener {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences loginPreferences = getSharedPreferences(
                "LOGIN",
                Context.MODE_PRIVATE
        );

        Switch darkModeSwitch = (Switch) findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences("SETTINGS", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("DARKMODE", isChecked);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(settings.getBoolean("DARKMODE", false) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });

        Button changeCardCredentials = (Button) findViewById(R.id.changeCredentialsButton);
        changeCardCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCreditcardDialog().show(getSupportFragmentManager(), "wowsa");
            }
        });

        Button signOutButton = (Button) findViewById(R.id.signoutbutton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putString("EMAIL", null);
                editor.putString("PASSWORD", null);
                editor.putString("CREDIT_CARD", null);
                editor.putString("EXPIRY_DATE", null);
                editor.putString("CVC", null);
                editor.putBoolean("CARD_SET", false);
                editor.apply();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void applyTexts(String holder, String number, String expiry, String cvc) {
        SharedPreferences loginPreferences = getSharedPreferences(
                "LOGIN",
                MODE_PRIVATE
        );
        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.putString("HOLDER", holder);
        editor.putString("CREDIT_CARD", number);
        editor.putString("EXPIRY_DATE", expiry);
        editor.putString("CVC", cvc);
        editor.putBoolean("CARD_SET", true);
        editor.apply();
        ProfileFragment.refreshSharedPreferences();
    }
}
