package com.anshul.pokemongopokedex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.anshul.pokemongopokedex.MESSAGE";
    public static String[] pokedata;
    public ArrayList<Data> pokes = new ArrayList<Data>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences settings = getSharedPreferences("alert", Activity.MODE_PRIVATE);
        Boolean alert = settings.getBoolean("alert",false);
        if(alert == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("                    Notice");
            builder.setMessage("PokemonGO,Pokemon, Nintendo,Niantic,and character names or imagery are trademarks or registered trademarks of their respective holders. Use of such trademarks does not imply any affiliation with or endorsements by mark holders ");
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.show();
            TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            SharedPreferences val = getSharedPreferences("alert", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = val.edit();
            editor.putBoolean("alert", true);
            editor.commit();

        }


        getSupportActionBar().hide();

        final InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        try{
            JSONArray obj = new JSONArray(loadJSONFromAsset("Poke.json"));
            for(int i=0;i<obj.length();i++){
                JSONObject pokemonInfo = obj.getJSONObject(i);
                pokes.add(new Data(pokemonInfo.getString("Name"),pokemonInfo.getInt("number")));

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        final SearchView search = (SearchView)findViewById(R.id.searchView);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        final ImageAdapter adapter = new ImageAdapter(MainActivity.this,pokes);
        gridview.setAdapter(adapter);






        search.setQueryHint("Pokemon name or number");
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) search.findViewById(id);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setHintTextColor(Color.parseColor("#354AA5"));

        int searchPlateId = search.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = search.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(0xFF1A237E);
        }

        search.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(false){
                    adapter.clear();
                    search.clearFocus();
                }
            }
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });



    }

    public String loadJSONFromAsset(String name) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
