package tk.yommail.ecrt.Misc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tk.yommail.ecrt.MainActivityFragments.FrontPageFragment;
import tk.yommail.ecrt.MainActivityFragments.ListFragment;
import tk.yommail.ecrt.MainActivityFragments.ProfileFragment;

public class MainActivitySliderAdapter extends FragmentPagerAdapter {

    private final static int NUM_ITEMS = 3;

    public MainActivitySliderAdapter(FragmentManager fragmentManager) {
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
                return new ProfileFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new FrontPageFragment();
            case 2: // Fragment # 1 - This will show SecondFragment
                return new ListFragment();
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
