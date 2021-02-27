package com.example.simplelogin;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.jsonparsing.HttpHandler;

import static com.example.simplelogin.R.layout.activity_carshow2;
import static com.example.simplelogin.R.layout.activity_navigation_drawer;


public class NavigationDrawer extends AppCompatActivity {
    private static final String TAG = NavigationDrawer.class.getSimpleName();
    private   ArrayList<String> makerNamesArray = new ArrayList<>();
    private   ArrayList<String> modelNamesArray = new ArrayList<>();
    private   ArrayList<String> listofModelsArray = new ArrayList<>();

    private   Spinner makerSpinner2;
    private   Spinner modelSpinner2;
    private   TextView make_model;
    private   TextView price;
    private   TextView vehicleDescription;
    private   ImageView vehicleImage;
    private   DrawerLayout drawerLayout;
    private   String zipCode =  "92603";
    private ListView listOfModels;
    private  TextView lastUpdated;
    String imageUrl="";


    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    HashMap<String, String> MakersandModels = new HashMap<>();
    HashMap<String, String> ModelandVehicleID = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(activity_navigation_drawer);/*Setting Up Navigation Drawer*/
        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();                                                                                                       /* Setting and Instatiating all the Variables*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView =  findViewById(R.id.nv);
        lastUpdated = findViewById(R.id.lastUpdated);
        make_model = findViewById(R.id.make_model);
        price  = findViewById(R.id.price);
        vehicleDescription = findViewById(R.id.vehicleDescription);
        vehicleImage = findViewById(R.id.imageView2);


        listOfModels = findViewById(R.id.listOfModels);
        makerSpinner2 = findViewById(R.id.makerSpinner2);
        FetchDataMaker process = new FetchDataMaker();
        process.execute();
        modelSpinner2 = findViewById(R.id.modelSpinner2);

        makerSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                modelNamesArray.clear();
                FetchModelData modelData = new FetchModelData();
                modelData.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });                               //Loads Maker  Data From Json Array


        modelSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {                               //Loads Spinner Data based  off  of maker spinner
                listofModelsArray.clear();
                CarData carData = new CarData();
                carData.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listOfModels.setOnItemClickListener(new AdapterView.OnItemClickListener() {                                               //Loads Main Screen
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainDataLoader mainDataLoader = new MainDataLoader();
                mainDataLoader.execute();

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                                                   //Opens Hamburger Icon
        if(item.getItemId() == android.R.id.home){ // use android.R.id
           if( drawerLayout.isDrawerOpen(GravityCompat.START)){
               drawerLayout.closeDrawer(GravityCompat.START);
           }
            else
                drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchDataMaker extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            info.androidhive.jsonparsing.HttpHandler httpHandler = new info.androidhive.jsonparsing.HttpHandler();
            String makerURL = "https://thawing-beach-68207.herokuapp.com/carmakes";
            String jsonStrHandler = httpHandler.makeServiceCall(makerURL);
            if (jsonStrHandler != null)


                try {


                    JSONArray makersNames = new JSONArray(jsonStrHandler);
                    for (int i = 0; i < makersNames.length(); i++) {
                        JSONObject tempJSONOBJECT = makersNames.getJSONObject(i);
                        makerNamesArray.add(tempJSONOBJECT.getString("vehicle_make"));
                        MakersandModels.put(tempJSONOBJECT.getString("vehicle_make"), tempJSONOBJECT.getString("id"));

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();


                        }
                    });
                }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter<String> makerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, makerNamesArray);
            makerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

            makerSpinner2.setAdapter(makerAdapter);


        }
    }


    public class FetchModelData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String modelURL = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/" + MakersandModels.get(makerSpinner2.getSelectedItem());
            info.androidhive.jsonparsing.HttpHandler modelHTTPHandler = new info.androidhive.jsonparsing.HttpHandler();
            String jsonModelHandler = modelHTTPHandler.makeServiceCall(modelURL);
            if (jsonModelHandler != null)
                try {
                    JSONArray modelsNames = new JSONArray(jsonModelHandler);
                    for (int j = 0; j < modelsNames.length(); j++) {
                        JSONObject tempModelObject = modelsNames.getJSONObject(j);
                        modelNamesArray.add(tempModelObject.getString("model"));
                        ModelandVehicleID.put(tempModelObject.getString("model"),tempModelObject.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, modelNamesArray);
            modelAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            modelSpinner2.setAdapter(modelAdapter);
        }
    }

    public class CarData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String availableModels ="https://thawing-beach-68207.herokuapp.com/cars/"+  MakersandModels.get(makerSpinner2.getSelectedItem()) +"/" +  ModelandVehicleID.get(modelSpinner2.getSelectedItem()) +"/"+zipCode ;
            info.androidhive.jsonparsing.HttpHandler modelHTTPHandler = new info.androidhive.jsonparsing.HttpHandler();
            String jsonModelHandler = modelHTTPHandler.makeServiceCall(availableModels);
            if (jsonModelHandler != null)
                try {
                    JSONObject modelsJSONObject = new JSONObject(jsonModelHandler);
                    JSONArray listofModels = modelsJSONObject.getJSONArray("lists");
                    for (int k = 0; k < listofModels.length(); k++) {
                        JSONObject templistOfModelsObject = listofModels.getJSONObject(k);
                        listofModelsArray.add(templistOfModelsObject.getString("model")+"("+k+1+")");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter<String> listofModelsAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listofModelsArray);
            listOfModels.setAdapter(listofModelsAdapter);
        }
    }
    public class MainDataLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String availableModels ="https://thawing-beach-68207.herokuapp.com/cars/"+  MakersandModels.get(makerSpinner2.getSelectedItem()) +"/" +  ModelandVehicleID.get(modelSpinner2.getSelectedItem()) +"/"+zipCode ;
            info.androidhive.jsonparsing.HttpHandler modelHTTPHandler = new info.androidhive.jsonparsing.HttpHandler();
            String jsonModelHandler = modelHTTPHandler.makeServiceCall(availableModels);
            if (jsonModelHandler != null) {
                try {

                    JSONObject modelsJSONObject = new JSONObject(jsonModelHandler);
                    JSONArray listofModels = modelsJSONObject.getJSONArray("lists");
                    JSONObject selectedCarfromMenuItem = listofModels.getJSONObject(listOfModels.getSelectedItemPosition()+1);
                    String vehicleImageURL = selectedCarfromMenuItem.getString("image_url");
                    imageUrl = vehicleImageURL.trim();
                    lastUpdated.setText(selectedCarfromMenuItem.getString("created_at"));
                    price.setText("$");
                    price.append(Double.toString((Double)selectedCarfromMenuItem.get("price")));
                    vehicleDescription.setText(selectedCarfromMenuItem.getString("veh_description"));
                    make_model.setText(makerSpinner2.getSelectedItem()+"/"+modelSpinner2.getSelectedItem());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Picasso.get().load(imageUrl).into(vehicleImage);




        }

    }



}

