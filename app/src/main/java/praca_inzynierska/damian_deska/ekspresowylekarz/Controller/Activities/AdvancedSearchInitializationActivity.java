package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class AdvancedSearchInitializationActivity extends TabActivity implements AppCompatCallback {

    private AppCompatDelegate delegate;
    Toolbar advancedSearchToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search_initialization_activity);

        TabHost tabHost = getTabHost();

        //stworzenie 2 zakladek w wyszukiwarce zaawansowanej
        Intent intent = new Intent(this, AdvancedSearchActivity.class);
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Lista");
        tabSpec1.setIndicator("Lista");
        tabSpec1.setContent(intent);

        Intent inteint = new Intent(this, MapsActivity.class);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Mapa").setContent(inteint);
        tabSpec2.setIndicator("Mapa");
        tabSpec2.setContent(inteint);

        tabHost.addTab(tabSpec1);
        tabHost.addTab(tabSpec2);

        delegate = AppCompatDelegate.create(this, this);

        delegate.onCreate(savedInstanceState);

        advancedSearchToolbar = (Toolbar) findViewById(R.id.advancedSearchInitToolbar);
        delegate.setSupportActionBar(advancedSearchToolbar);

        delegate.setSupportActionBar(advancedSearchToolbar);
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);

        advancedSearchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
    }

    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    ;

}
