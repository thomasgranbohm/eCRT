package tk.yommail.ecrt.Purchase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import tk.yommail.ecrt.R;

public class MakePurchaseActivity extends AppCompatActivity {

    private static final String TAG = "MakePurchaseActivity";

    private ViewPager viewPager;
    private LinearLayout progressLayout;
    private Purchase_SliderAdapter sliderAdapter;

    private Button backButton;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_purchase);

        viewPager = (ViewPager) findViewById(R.id.purchase_view_pager);

        sliderAdapter = new Purchase_SliderAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sliderAdapter);
        viewPager.setCurrentItem(0);
    }


    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.purchase_view_pager);
        }
        return viewPager;
    }

    public void chooseTab(int position){
        viewPager.setCurrentItem(position);
    }

    // Toolbar and stuff
    @Override
    public void onBackPressed(){
        if (getViewPager().getCurrentItem() == 0) {
            super.onBackPressed();
            finish();
        } else {
            chooseTab(getViewPager().getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
