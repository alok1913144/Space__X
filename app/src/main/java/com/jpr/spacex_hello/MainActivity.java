package com.jpr.spacex_hello;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton refreshBtn, deleteBtn,openBtn;
    Float translationYaxis =100f;
    Boolean menuOpen = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    private List<Crew> crewList;
    private CrewAdapter crewAdapter;
    private RecyclerView crewRecycler;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteBtn = findViewById(R.id.button_delete);
        refreshBtn = findViewById(R.id.button_refresh);
        crewRecycler = findViewById(R.id.recyclerView);
        openBtn = findViewById(R.id.fab_main);

        if(setUpDB().dao().checkCrew() == 0) {
            fetchData();
        }
        setUpRecycler();

        deleteBtn.setAlpha(0f);
        refreshBtn.setAlpha(0f);

        deleteBtn.setTranslationY(translationYaxis);
        refreshBtn.setTranslationY(translationYaxis);

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuOpen){
                    CloseMenu();
                }else {
                    OpenMenu();
                }
            }
        });







        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshBtn.setEnabled(false);
                refreshBtn.postDelayed(() -> {
                    refreshBtn.setEnabled(true);
                }, 2000);

                if(setUpDB().dao().checkCrew() == 0) {
                    fetchData();
                } else {
                    Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteBtn.setEnabled(false);
                deleteBtn.postDelayed(() -> deleteBtn.setEnabled(true), 2000);

                setUpDB().dao().deleteAll();
                crewList.clear();
                crewAdapter.notifyDataSetChanged();

                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void OpenMenu(){
        menuOpen =! menuOpen;
        openBtn.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
        deleteBtn.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        refreshBtn.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void CloseMenu(){

        menuOpen =! menuOpen;
        openBtn.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
        deleteBtn.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        refreshBtn.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }



    public CrewDatabase setUpDB() {
        CrewDatabase database = Room.databaseBuilder(
                MainActivity.this, CrewDatabase.class, "CrewDatabase")
                .allowMainThreadQueries().build();

        return database;
    }



    public void fetchData() {

        String URL = "https://api.spacexdata.com/v4/crew";

        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);

                                String name = object.get("name").toString();
                                String agency = object.get("agency").toString();
                                String image = object.get("image").toString();
                                String wikipedia = object.get("wikipedia").toString();
                                String status = object.get("status").toString();

                                Crew crew = new Crew(name, agency, image, wikipedia, status);

                                setUpDB().dao().crewInsertion(crew);
                                setUpRecycler();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    public void setUpRecycler() {
        crewList = new ArrayList<>();
        crewList = setUpDB().dao().getAllCrew();
        crewAdapter = new CrewAdapter(MainActivity.this, crewList);
        crewRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        crewRecycler.setAdapter(crewAdapter);
        crewAdapter.notifyDataSetChanged();
    }
}