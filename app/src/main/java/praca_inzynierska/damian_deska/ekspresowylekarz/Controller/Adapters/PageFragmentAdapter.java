package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Fragments.PageFragment;

/**
 * Created by Damian Deska on 2017-01-12.
 */

public class PageFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Usługi", "O Lekarzu", "Opinie" };
    private Context context;
    int doctorID;

    public PageFragmentAdapter(FragmentManager fm, Context context, int doctorID) {
        super(fm);
        this.context = context;
        this.doctorID = doctorID;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1, doctorID);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
