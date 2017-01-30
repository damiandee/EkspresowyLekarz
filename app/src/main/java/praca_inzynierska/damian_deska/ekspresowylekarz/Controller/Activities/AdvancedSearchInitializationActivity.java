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

public class AdvancedSearchInitializationActivity extends TabActivity implements AppCompatCallback{

    private AppCompatDelegate delegate;
    Toolbar advancedSearchToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search_initialization_activity);

        TabHost tabHost = getTabHost();

        //initToolbar();

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

        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);

        //we use the delegate to inflate the layout
        //delegate.setContentView(R.layout.advanced_search_initialization_activity);

        //Finally, let's add the Toolbar
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
        //let's leave this empty, for now
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        // let's leave this empty, for now
    }

    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    };

}
