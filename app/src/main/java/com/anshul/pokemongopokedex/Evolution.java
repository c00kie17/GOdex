package com.anshul.pokemongopokedex;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Evolution extends AppCompatActivity {

    public JSONObject pokemonInfo;
    public JSONObject pokeMul;
    public String evolName;
    public Integer evolPos;
    public String name;
    public JSONObject pokeDetail1;
    public JSONObject pokeDetail2;
    public Integer maxCP1;
    public Integer maxCP2;
    public double multiplier;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolution);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        final int position = extras.getInt("MyData");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try{
            JSONArray obj = new JSONArray(loadJSONFromAsset("pokemon.json"));
            pokemonInfo = obj.getJSONObject(position);
            name  = pokemonInfo.getString("Name");
            JSONArray nextevoldata = pokemonInfo.getJSONArray("Next evolution(s)");
            for(int i=0;i<1;i++){
                JSONObject value = nextevoldata.getJSONObject(i);
                evolName = value.getString("Name");
                evolPos = value.getInt("Number");
            }
            JSONArray obj1 = new JSONArray(loadJSONFromAsset("pokejson2.json"));
            for(int i=0;i<obj1.length();i++){
                JSONObject insideObj = obj1.getJSONObject(i);
                if(insideObj.getInt("PkMn") == position+1){
                    pokeDetail1 = insideObj;
                }
            }
            maxCP1 = pokeDetail1.getInt("MaxCP");
            for(int i=0;i<obj1.length();i++){
                JSONObject insideObj = obj1.getJSONObject(i);
                if(insideObj.getInt("PkMn") == evolPos){
                    pokeDetail2 = insideObj;
                }
            }
            maxCP2 = pokeDetail2.getInt("MaxCP");
            JSONArray obj2 = new JSONArray(loadJSONFromAsset("multiplier.json"));
            for(int i=0;i<obj2.length();i++){
                JSONObject insideObj = obj2.getJSONObject(i);
                if(insideObj.getInt("pokemon") == position+1){
                    pokeMul = insideObj;
                }
            }
            multiplier = pokeMul.getDouble("multiplier");



        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        TextView name1 =(TextView)findViewById(R.id.pokeName1);
        name1.setText(name);
        TextView name2 =(TextView)findViewById(R.id.pokeName2);
        name2.setText(evolName);

        ImageView gifImage1 = (ImageView) findViewById(R.id.gifImage1);
        int relPos = position + 1;
        int id1 = getResources().getIdentifier("p" + relPos, "mipmap", getPackageName());
        Picasso.with(context).load(id1).into(gifImage1);

        ImageView gifImage2 = (ImageView) findViewById(R.id.gifImage2);
        int id2 = getResources().getIdentifier("p" + evolPos, "mipmap", getPackageName());
        Picasso.with(context).load(id2).into(gifImage2);


        final EditText CPval = (EditText)findViewById(R.id.cpVallay);


        ProgressBar prog1 = (ProgressBar)findViewById(R.id.progressBar1);
        ProgressBar prog2 = (ProgressBar)findViewById(R.id.progressBar2);

        prog1.setMax(maxCP2);
        prog2.setMax(maxCP1);

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                EditText CP = (EditText)findViewById(R.id.cpVallay);
                Editable editable = CP.getText();
                String CPval = String.valueOf(editable);
                Integer Cpvalue=0;
                if(CPval.isEmpty()){
                    Cpvalue=0;
                }else {
                    if (CPval.length() > 0) {
                        Cpvalue = Integer.parseInt(CPval);
                    }
                }

                ProgressBar prog1 = (ProgressBar)findViewById(R.id.progressBar1);
                ProgressBar prog2 = (ProgressBar)findViewById(R.id.progressBar2);
                prog2.setProgress(Cpvalue);
                double prog2val = Cpvalue * multiplier;
                prog1.setProgress((int)prog2val);
                TextView twoVal = (TextView)findViewById(R.id.pokeevolcp);
                twoVal.setText(String.valueOf(Math.round(prog2val)));





            }
        };

        CPval.addTextChangedListener(textWatcher);





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
