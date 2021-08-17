package com.jpr.spacex_hello;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private ImageView crew_Image;
    private TextView crew_name, crew_Agency, crew_Status, crew_Wikipedia;
    private Crew crew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        crew_name = findViewById(R.id.id_crew_name);
        crew_Image = findViewById(R.id.id_crew_image);
        crew_Agency = findViewById(R.id.id_crew_agency);
        crew_Status = findViewById(R.id.id_crew_ststus);
        crew_Wikipedia = findViewById(R.id.id_crew_wikipedia);

        Object obj = getIntent().getSerializableExtra("detail");
        if(obj instanceof Crew){
            crew = (Crew) obj;
        }
        Glide.with(getApplicationContext()).load(crew.getImage()).into(crew_Image);
        crew_name.setText(crew.getName());
        crew_Agency.setText(crew.getAgency());
        crew_Status.setText(crew.getStatus());

        crew_Wikipedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = crew.getWikipedia();

                CustomTabsIntent.Builder customTabIntent = new CustomTabsIntent.Builder();
                customTabIntent.setToolbarColor(Color.parseColor("#292D36"));
                openCustomTabs(DetailActivity.this,customTabIntent.build(),Uri.parse(url));

            }
        });

    }

    public static void openCustomTabs(Activity activity, CustomTabsIntent customTabsIntent, Uri uri){

        String packageName="com.android.chrome";
        if(packageName!=null)
        {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity,uri);
        }
        else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
    }


}