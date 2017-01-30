package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters.PageFragmentAdapter;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.SpecializationModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;


/**
 * Created by Damian Deska on 2017-01-10.
 */

public class DoctorPageActivity extends AppCompatActivity {

    int doctorID;
    DoctorModel doctor;
    Toolbar doctorPageToolbar;
    DatabaseConnectionController databaseConnectionController;
    TextSliderView textSliderView;
    SliderLayout sliderShow;
    ImageView doctorAvatar;
    TextView doctorName;
    TextView specName;
    RatingBar doctorPageRatingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_page_activity);

        Bundle bundle = getIntent().getExtras();
        doctorID = bundle.getInt("doctorID");
        bundle.putString("doctorID", Integer.toString(doctorID));

        initToolbar();

        UserSession.getSession().setIsAvailable(false);

        databaseConnectionController = DatabaseConnectionController.getInstance();
        doctor = databaseConnectionController.getDoctorInfo(doctorID);

        doctorAvatar = (ImageView)findViewById(R.id.doctorPageAvatar);
        Picasso.with(getApplicationContext()).load(doctor.getDoctorAvatarURL()).into(doctorAvatar);

        doctorName = (TextView)findViewById(R.id.doctorPageDoctorName);
        doctorName.setText(doctor.getDoctorName() + " "  + doctor.getDoctorSurname());

        specName = (TextView)findViewById(R.id.doctorPageSpecName);
        SpecializationModel specializationModel = databaseConnectionController.getSpecializationInfo(doctor.getSpecjalizationID());
        specName.setText(specializationModel.getSpecializationName());

        //doctorPageRatingBar = (RatingBar)findViewById(R.id.doctorPageRatingBar);
        //doctorPageRatingBar.setRating(doctor.getDoctorRating());

        initGallery();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(),
                DoctorPageActivity.this, doctorID));

/*        ViewPager wrapContentViewPager = (ViewPager) findViewById(R.id.viewpager);
        wrapContentViewPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(),
                DoctorPageActivity.this, doctorID));*/
/*
        wrapContentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                wrapContentViewPager.reMeasureCurrentPage(wrapContentViewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

/*        docAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View treatmentsListView) {
                Intent intent = new Intent(DoctorPageActivity.this, DatePickerActivity_To_Remove.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        //list.setAdapter(null);
        super.onDestroy();
    }


    public void initGallery() {
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, String> doctorGalleryUrlsList = databaseConnectionController.getDoctorGalleryUrlsList(doctorID);
        Set set = doctorGalleryUrlsList.entrySet();
        Iterator iterator =  set.iterator();

        while(iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry)iterator.next();
            textSliderView = new TextSliderView(this);
            if(mapEntry.getValue() != null) {
                textSliderView.description(mapEntry.getValue().toString());
            }
            textSliderView.image(mapEntry.getKey().toString());
            sliderShow.addSlider(textSliderView);
        }
    }

    public void initToolbar() {
        doctorPageToolbar = (Toolbar)findViewById(R.id.doctorPageToolbar);
        setSupportActionBar(doctorPageToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        doctorPageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecializationModel specializationModel = databaseConnectionController.getSpecializationInfo(doctor.getSpecjalizationID());
                Intent intent = new Intent(getApplicationContext(), DoctorsListActivity.class);
                intent.putExtra("selectedSpecializationName", specializationModel.getSpecializationName());
                startActivity(intent);
                finish();
            }
        });
    }

}
