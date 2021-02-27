package com.example.simplelogin;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.jsonparsing.HttpHandler;


public class CARSHOW2 extends AppCompatActivity {
    private static final String TAG = CARSHOW2.class.getSimpleName();
    ArrayList<String> makerNamesArray  = new ArrayList<>();
    ArrayList<String> modelNamesArray  = new ArrayList<>();
    Spinner makerSpinner;
    Spinner modelSpinner;

    HashMap<String,String> MakersandModels = new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carshow2);

        makerSpinner =  findViewById(R.id.makerSpinner);
        FetchDataMaker process = new FetchDataMaker();
        process.execute();
        modelSpinner = findViewById(R.id.modelSpinner);

        makerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             modelNamesArray.clear();
             FetchModelData modelData = new FetchModelData();
             modelData.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                        MakersandModels.put(tempJSONOBJECT.getString("vehicle_make"),tempJSONOBJECT.getString("id"));

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

            makerSpinner.setAdapter(makerAdapter);


        }
    }



    public class FetchModelData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String modelURL ="https://thawing-beach-68207.herokuapp.com/carmodelmakes/"+MakersandModels.get(makerSpinner.getSelectedItem());
            info.androidhive.jsonparsing.HttpHandler modelHTTPHandler = new info.androidhive.jsonparsing.HttpHandler();
            String jsonModelHandler = modelHTTPHandler.makeServiceCall(modelURL);
            if (jsonModelHandler != null)
                try{
                    JSONArray modelsNames = new JSONArray(jsonModelHandler);
                    for (int j = 0; j < modelsNames.length();j++)
                    {
                        JSONObject tempModelObject = modelsNames.getJSONObject(j);
                        modelNamesArray.add(tempModelObject.getString("model"));
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
            modelSpinner.setAdapter(modelAdapter);
        }
    }
}

