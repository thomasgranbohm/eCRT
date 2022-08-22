package tk.yommail.ecrt;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import tk.yommail.ecrt.MainActivityFragments.FrontPageFragment;
import tk.yommail.ecrt.MainActivityFragments.ProfileFragment;
import tk.yommail.ecrt.Misc.AddCreditcardDialog;
import tk.yommail.ecrt.Misc.MainActivitySliderAdapter;

import tk.yommail.ecrt.R;

import tk.yommail.ecrt.Scanning.ScanResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements AddCreditcardDialog.AddCredidcardDialogListener {

    private static final String TAG = "MainActivity";
    private static final String PRODUCT_URL = "https://php.yommail.tk/?item=";

    private ViewPager viewPager;
    private MainActivitySliderAdapter sliderAdapter;

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_main:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_shopping_list:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return true;
        }
    };
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        viewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);

        navigation = (BottomNavigationView) findViewById(R.id.mainActivityNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_main);


        sliderAdapter = new MainActivitySliderAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sliderAdapter);
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int fragID = 0;
                switch (position) {
                    case 0:
                        fragID = R.id.navigation_profile;
                        break;
                    case 1:
                        fragID = R.id.navigation_main;
                        break;
                    case 2:
                        fragID = R.id.navigation_shopping_list;
                        break;
                }
                navigation.setSelectedItemId(fragID);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            Intent intent = new Intent(MainActivity.this, ScanResult.class);
            intent.putExtra("barcode", result.getContents());
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void applyTexts(String holder, String number, String expiry, String cvc) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                "LOGIN",
                MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("HOLDER", holder);
        editor.putString("CREDIT_CARD", number);
        editor.putString("EXPIRY_DATE", expiry);
        editor.putString("CVC", cvc);
        editor.putBoolean("CARD_SET", true);
        editor.apply();
        ProfileFragment.refreshSharedPreferences();
    }


    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(this, "No NDEF messages found.", Toast.LENGTH_SHORT).show();
            }

        }

        super.onNewIntent(intent);
    }

    private void readTextFromMessage(NdefMessage parcelable) {

        NdefRecord[] ndefRecords = parcelable.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];

            String text = getTextFromNdefRecord(ndefRecord);

            try {
                JSONObject jsonObject = new JSONObject(text);
                FrontPageFragment.connectedShoppingCart(jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "readTextFromMessage: " + e.getMessage(), e);
            }
        } else {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }

    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagText = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagText = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "getTextFromNdefRecord: " + e.getMessage(), e);
        }
        return tagText;
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    public void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    public void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }


}
