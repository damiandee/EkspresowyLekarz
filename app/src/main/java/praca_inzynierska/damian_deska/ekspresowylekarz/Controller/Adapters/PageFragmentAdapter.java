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
    /*informacje o fragmentach w adapterze; ilosc zakladek i ich nazwy*/
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Usługi", "O Lekarzu", "Opinie" };
    private Context context;
    int doctorID;

    public PageFragmentAdapter(FragmentManager fm, Context context, int doctorID) {
        super(fm);
        this.context = context;
        this.doctorID = doctorID;
    }

    /*funkcja zwraca ilosc zakladek*/
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /*funkcja zwraca wybrany przez uzytkownika fragment*/
    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1, doctorID);
    }

    /*funkcja zwraca nazwe odpowiedniej zakladki*/
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
