package tk.yommail.ecrt.MainActivityFragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import tk.yommail.ecrt.R;
import tk.yommail.ecrt.Scanning.PortraitCaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

public class FrontPageFragment extends Fragment {

    private static final String TAG = "FrontPageFragment";
    private static final int REQUEST_CAMERA = 1;
    private View scanButton;
    private static ConstraintLayout connected;
    private static View disconnectButton;
    private static TextView shoppingCartName;
    private static TextView shoppingCartBattery;
    private static SharedPreferences sharedPreferences;
    private static CountDownTimer countDownTimer;

    private static final long DIFF = 60 * 60 * 1000;
    private RelativeLayout hint;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frontpage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        sharedPreferences = getContext().getSharedPreferences(
                "SHOPPING_CART",
                getContext().MODE_PRIVATE
        );

        connected = view.findViewById(R.id.connected_shopping_cart);
        hint = view.findViewById(R.id.nfcHint);
        scanButton = view.findViewById(R.id.frontPageScanButton);

        shoppingCartName = view.findViewById(R.id.shopping_cart_name);
        shoppingCartBattery = view.findViewById(R.id.shopping_cart_battery);
        disconnectButton = view.findViewById(R.id.disconnect_button);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connected.setVisibility(View.GONE);
                hint.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            }
        });

        countDownTimer = new CountDownTimer(DIFF, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int percent = (int) (100 - Math.floor(((double) (DIFF - millisUntilFinished) / DIFF) * 100));
                shoppingCartBattery.setText(String.valueOf(percent) + " %");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("PERCENT", percent);
                editor.apply();
                /*
                TODO gör en background task som ändrar i SP
                sen gör så att FPF läser in värdet från SP varje gång den callas
                 */
            }

            @Override
            public void onFinish() {

            }
        };

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanOnClick();
            }
        });

        if (sharedPreferences.getBoolean("CONNECTED", false)) {
            shoppingCartName.setText(sharedPreferences.getString("NAME", ""));
            shoppingCartBattery.setText("Battery Level: 96%");
            connected.setVisibility(View.VISIBLE);
            hint.setVisibility(View.GONE);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private void scanOnClick() {
        // Check permission
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            // Scan for barcode
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setPrompt("Scan a barcode");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setOrientationLocked(true);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setBeepEnabled(true);
            integrator.setCaptureActivity(PortraitCaptureActivity.class);
            integrator.initiateScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanOnClick();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static void connectedShoppingCart(JSONObject jsonObject) {
        if (!sharedPreferences.getBoolean("CONNECTED", false)) {
            connected.setVisibility(View.VISIBLE);

            try {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String name = jsonObject.getString("name");
                long time = System.currentTimeMillis();
                editor.putBoolean("CONNECTED", true);
                editor.putString("NAME", name);
                editor.putLong("TIME", time);
                editor.apply();
                shoppingCartName.setText(name);
                shoppingCartBattery.setText("Battery Level: 100%");
                /*countDownTimer.start();*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
