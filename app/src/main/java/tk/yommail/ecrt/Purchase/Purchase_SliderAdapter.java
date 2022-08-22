package tk.yommail.ecrt.Purchase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tk.yommail.ecrt.Purchase.PurchaseFragments.Purchase_ConfirmFragment;
import tk.yommail.ecrt.Purchase.PurchaseFragments.Purchase_CredentialsFragment;
import tk.yommail.ecrt.Purchase.PurchaseFragments.Purchase_StartFragment;


public class Purchase_SliderAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public Purchase_SliderAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new Purchase_StartFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new Purchase_CredentialsFragment();
            case 2: // Fragment # 1 - This will show SecondFragment
                return new Purchase_ConfirmFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}