package tk.yommail.ecrt.Purchase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tk.yommail.ecrt.MainActivity;
import tk.yommail.ecrt.Misc.DatabaseHelper;
import tk.yommail.ecrt.Misc.Item;
import tk.yommail.ecrt.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class RecieptActivity extends AppCompatActivity {

    private static final String TAG = "RecieptActivity";
    private View rootView;
    private SlideUp slideUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<Item> items = databaseHelper.getItems();
        String input = "";
        for (Item item : items) {
            input += item.toString();
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String hash = new String(digest.digest(input.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(hash, BarcodeFormat.AZTEC,256,256);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView image = findViewById(R.id.reciept_code);
            image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        rootView = findViewById(R.id.reciept_root_view);
        final LinearLayout dim = findViewById(R.id.dim);

        View swipeUpReciept = findViewById(R.id.swipe_up_reciept);
        slideUp = new SlideUpBuilder(swipeUpReciept)
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView)
                .withListeners(new SlideUp.Listener.Slide() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                        if (percent == 100) {
                            dim.setVisibility(View.GONE);
                        } else if (dim.getVisibility() != View.VISIBLE) {
                            dim.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .build();
        databaseHelper.clearItems();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
