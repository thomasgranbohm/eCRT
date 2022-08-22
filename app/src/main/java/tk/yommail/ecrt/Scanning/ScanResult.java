package tk.yommail.ecrt.Scanning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import tk.yommail.ecrt.MainActivity;
import tk.yommail.ecrt.Misc.DatabaseHelper;
import tk.yommail.ecrt.Misc.Item;
import tk.yommail.ecrt.R;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScanResult extends AppCompatActivity {

    private static final String TAG = "ScanResult";

    private TextView itemTitle;
    private ImageView itemImage;
    private TextView itemPrice;

    private ImageButton buttonMinus;
    private ImageButton buttonPlus;
    private FlexboxLayout buttonAddToCart;
    private TextView itemCount;

    private int amount;

    private int id;
    private String title;
    private double price;
    private String image;

    private int leftToAdd = 10;

    private final String PRODUCT_URL = "https://php.yommail.tk/?item=";
    private RelativeLayout loadingSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        final DatabaseHelper db = new DatabaseHelper(this);

        loadingSpinner = (RelativeLayout) findViewById(R.id.product_progressbar);

        itemTitle = (TextView) findViewById(R.id.itemName);
        itemImage = (ImageView) findViewById(R.id.itemImage);
        itemPrice = (TextView) findViewById(R.id.itemPrice);

        buttonPlus = (ImageButton) findViewById(R.id.buttonPlus);
        buttonMinus = (ImageButton) findViewById(R.id.buttonMinus);
        buttonAddToCart = (FlexboxLayout) findViewById(R.id.buttonAddToCart);
        itemCount = (TextView) findViewById(R.id.itemCount);
        amount = Integer.valueOf(itemCount.getText().toString());

        if (getIntent().hasExtra("barcode")){

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    PRODUCT_URL + getIntent().getStringExtra("barcode"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray items = new JSONArray(response);
                        if (items.length() != 0) {
                            JSONObject item = items.getJSONObject(0);
                            id = item.getInt("id");
                            title = item.getString("name");
                            price = item.getDouble("price");
                            image = "https:" + item.getString("image");

                            ArrayList<Item> dbItems =  db.getItems();

                            for (Item dbItem : dbItems) {
                                if (id == dbItem.getId()) {
                                    leftToAdd -= dbItem.getAmount();
                                    if (leftToAdd == 0) {
                                        Toast.makeText(
                                                ScanResult.this,
                                                "You can't buy more than 10 of the same product at once. Sorry!",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        startActivity(new Intent(ScanResult.this, MainActivity.class));
                                    }
                                }
                            }


                            itemTitle.setText(title);
                            Picasso.get().load(image).into(itemImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    loadingSpinner.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.d(TAG, "onError: failed to load image.");
                                }
                            });
                            itemPrice.setText(String.format("%s%s", Math.floor(price * amount * 100) / 100, getString(R.string.currency)));

                        } else {
                            Toast.makeText(ScanResult.this, "That barcode isn't valid.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ScanResult.this, MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: ", error);
                    Toast.makeText(ScanResult.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(ScanResult.this).add(stringRequest);
        }

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount < leftToAdd){
                    amount++;
                    itemCount.setText(String.valueOf(amount));
                    itemPrice.setText(String.format("%s%s", Math.floor(price * amount * 100) / 100, getString(R.string.currency)));
                    if (!buttonAddToCart.isEnabled()) {
                        buttonAddToCart.setEnabled(true);
                    }
                }
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount > 0){
                    amount--;
                    itemCount.setText(String.valueOf(amount));
                    itemPrice.setText(String.format("%s%s", Math.floor(price * amount * 100) / 100, getString(R.string.currency)));
                    if (amount == 0) {
                        buttonAddToCart.setEnabled(false);
                    }
                }
            }
        });

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(ScanResult.this);

                dbHelper.addItem(id, title, price, amount);

                startActivity(new Intent(ScanResult.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ScanResult.this, MainActivity.class));
    }

}

