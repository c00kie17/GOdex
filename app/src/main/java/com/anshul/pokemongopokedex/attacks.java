package com.anshul.pokemongopokedex;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;

import hotchemi.stringpicker.StringPicker;

public class attacks extends AppCompatActivity {
    public static JSONArray obj;
    public static JSONArray obj2;
    public JSONObject pokemonInfo;
    public Integer PriCol;
    public Integer SecCol;
    public Integer TextCol;
    public JSONArray FA;
    public JSONArray CA;
    public static Integer Faval =-1;
    public static Integer Caval=-1;
    public static String Name;
    public ArrayList <String> pokesvals = new ArrayList<String>();
    public ArrayList <attackdata> attackdata = new ArrayList<attackdata>();
    public static TextView oppoval;
    public static String Opponent="";
    public static ProgressBar efecprog;
    public static JSONArray obj3;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attacks);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        final int position = extras.getInt("MyData");

        RelativeLayout main = (RelativeLayout)findViewById(R.id.main);
        LinearLayout LinLayoutFA = (LinearLayout) findViewById(R.id.fa);
        LinearLayout LinLayoutCA = (LinearLayout) findViewById(R.id.ca);

        try {
            obj = new JSONArray(loadJSONFromAsset("Poke.json"));
            pokemonInfo = obj.getJSONObject(position);
            Name = pokemonInfo.getString("Name");
            String PriCols = pokemonInfo.getString("primaryColor");
            PriCol = Color.parseColor(PriCols);
            String SecCols = pokemonInfo.getString("secondaryColor");
            SecCol = Color.parseColor(SecCols);
            String TextCols = pokemonInfo.getString("textColor");
            TextCol = Color.parseColor(TextCols);
            FA = pokemonInfo.getJSONArray("Fast Attack(s)");
            CA = pokemonInfo.getJSONArray("Charge Attack(s)");
            for(int i=0;i<obj.length();i++){
                JSONObject pokobj = obj.getJSONObject(i);
                if(pokobj.getString("Name") != pokemonInfo.getString("Name")) {
                    pokesvals.add(pokobj.getString("Name"));
                }
            }
            obj2 = new JSONArray(loadJSONFromAsset("PokeAttacks.json"));
            obj3 = new JSONArray(loadJSONFromAsset("pokemax.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        main.setBackgroundColor(PriCol);

        ImageView mainimg = (ImageView)findViewById(R.id.pokeimg);
        int relPos = position + 1;
        int id = getResources().getIdentifier("p" + relPos, "mipmap", getPackageName());
        Picasso.with(context).load(id).into(mainimg);


        TextView attacks = (TextView) findViewById(R.id.att);
        attacks.setTextColor(TextCol);
        attacks.setAlpha(0.7f);

        efecprog = (ProgressBar)findViewById(R.id.efecprog);


        for(int i=0;i<FA.length();i++){
            String attackValue="";
            try {
                attackValue = FA.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!attackValue.isEmpty()){
                final TextView attacking = new TextView(this);
                attacking.setTextSize(20);
                attacking.setId(i);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20,10,20,10);
                attacking.setPadding(8, 3, 8, 3);
                attacking.setTextColor(Color.parseColor("#ffffff"));
                LinLayoutFA.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
                attacking.setLayoutParams(layoutParams);
                attacking.setText(attackValue);
                attacking.setTextColor(TextCol);
                final GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
                drawable.setCornerRadius(3);
                LinLayoutFA.addView(attacking);
                attacking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer id = attacking.getId();
                        Faval = id;
                        Double val = computeval(Name,Opponent,Caval,Faval);
                        efecprog.setProgress(val.intValue());
                        attacking.setBackground(drawable);
                        for(int i=0;i<FA.length();i++){
                            if(i!=id){
                                TextView holder = (TextView) findViewById(i);
                                holder.setBackgroundResource(0);
                            }
                        }
                    }
                });
            }
        }

        TextView chaattacks = (TextView) findViewById(R.id.chatt);
        chaattacks.setTextColor(TextCol);
        chaattacks.setAlpha(0.7f);

        for(int i=0;i<CA.length();i++){
            String attackValue="";
            try {
                attackValue = CA.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!attackValue.isEmpty()){
                final TextView attacking = new TextView(this);
                attacking.setTextSize(20);
                attacking.setId(100+i);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20,10,20,10);
                attacking.setPadding(8, 3, 8, 3);
                attacking.setTextColor(Color.parseColor("#ffffff"));
                LinLayoutCA.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
                attacking.setLayoutParams(layoutParams);
                attacking.setText(attackValue);
                attacking.setTextColor(TextCol);
                final GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
                drawable.setCornerRadius(3);
                LinLayoutCA.addView(attacking);
                attacking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer id = attacking.getId()-100;
                        Caval = id;
                        Double val = computeval(Name,Opponent,Caval,Faval);
                        efecprog.setProgress(val.intValue());
                        attacking.setBackground(drawable);
                        for(int i=0;i<CA.length();i++){
                            if(i!=id){
                                TextView holder = (TextView) findViewById(100+i);
                                holder.setBackgroundResource(0);
                            }
                        }
                    }
                });
            }
        }

        TextView opponent = (TextView) findViewById(R.id.opo);
        opponent.setTextColor(TextCol);
        opponent.setAlpha(0.7f);



        oppoval = (TextView)findViewById(R.id.opoval);
        final GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
        drawable.setCornerRadius(3);

        oppoval.setText("Select the Opponent");
        oppoval.setTextColor(TextCol);
        oppoval.setBackground(drawable);
        oppoval.setPadding(8, 8, 8, 8);
        final DialogFragment diags = new showpoke();
        Bundle args = new Bundle();
        args.putStringArrayList("pokevals", pokesvals);
        args.putInt("PriCol",PriCol);
        diags.setArguments(args);



        oppoval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diags.show(getFragmentManager(),"pokeshappy");

            }
        });



    }
    public  String loadJSONFromAsset(String name) {
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

    public static void onUserSelectValue(String selectedValue) {
        oppoval.setText(selectedValue);
        Opponent = selectedValue;
        Double val = computeval(Name,Opponent,Caval,Faval);

        attacks at = new attacks();
        Integer maxval = 0;
        for (int i = 0; i < obj3.length() ; i++) {
            try {
                JSONObject poke = obj3.getJSONObject(i);
                if(poke.getString("name").equals( Opponent)){
                    Log.d("weak",String.valueOf(poke));
                    maxval = poke.getInt("val");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("weak",String.valueOf(maxval));
        efecprog.setProgress(val.intValue());


    }

    public static Double computeval(String Name,String opponent ,Integer Caval,Integer Faval) {
        Double totalDPS = 0.0;
        if (!opponent.isEmpty() && Caval >= 0 && Faval >= 0) {
            JSONObject oppo = null;
            JSONObject me = null;
            JSONObject FAattack = null;
            JSONObject CAattack = null;
            ArrayList<String> weakne = new ArrayList<String>();
            ArrayList<String> oppotype = new ArrayList<String>();
            ArrayList<String> metype = new ArrayList<String>();
            Boolean typecounter = false;
            String FaValue = "";
            String CaValue = "";
            Boolean faSTAB = false;
            Boolean caSTAB = false;
            Double Seconds = 0.0;
            Double EnergyMeter = 0.0;


            try {
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject poke = obj.getJSONObject(i);
                    if (poke.getString("Name") == Name) {
                        me = poke;
                    } else if (poke.getString("Name") == opponent) {
                        oppo = poke;
                    }
                }

                JSONArray FA = me.getJSONArray("Fast Attack(s)");
                for (int i = 0; i < FA.length(); i++) {
                    FaValue = FA.getString(Faval);

                }
                JSONArray CA = me.getJSONArray("Charge Attack(s)");
                for (int i = 0; i < CA.length(); i++) {
                    CaValue = CA.getString(Caval);

                }
                for (int i = 0; i < obj2.length(); i++) {
                    JSONObject attack = obj2.getJSONObject(i);
                    if (attack.getString("Move").equals(FaValue)) {
                        FAattack = attack;
                    } else if (attack.getString("Move").equals(CaValue)) {
                        CAattack = attack;
                    }
                }
                JSONArray weakness = me.getJSONArray("Weaknesses");
                for (int i = 0; i < weakness.length(); i++) {
                    weakne.add(weakness.getString(i));
                }
                JSONArray holder = oppo.getJSONArray("Type I");
                oppotype.add(holder.getString(0));
                if (oppo.has("Type II")) {
                    JSONArray holder1 = oppo.getJSONArray("Type II");
                    oppotype.add(holder1.getString(0));
                }

                JSONArray holderme = me.getJSONArray("Type I");
                metype.add(holderme.getString(0));
                if (me.has("Type II")) {
                    JSONArray holderme1 = me.getJSONArray("Type II");
                    oppotype.add(holderme1.getString(0));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < oppotype.size(); i++) {
                for (int j = 0; j < weakne.size(); j++) {
                    String oppoty = oppotype.get(i);
                    String wea = weakne.get(j);
                    if (oppoty.equals(wea)) {
                        typecounter = true;
                    }
                }
            }
            for (int i = 0; i < metype.size(); i++) {
                try {
                    if (metype.get(i) == FAattack.getString("Type")) {
                        faSTAB = true;
                    } else if (metype.get(i) == CAattack.getString("Type")) {
                        caSTAB = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

           while(Seconds <10){
                Double FAenergy = 0.0;
                Double CAenergy = 0.0;
                Double FAdps  = 0.0;
                Double CAdps = 0.0;
                Double FAseconds = 0.0;
                Double CAseconds= 0.0;




                try {
                    FAenergy = FAattack.getDouble("Energy");
                     CAenergy = CAattack.getDouble("Energy");
                    FAdps = FAattack.getDouble("DPS");
                    FAseconds = FAattack.getDouble("Sec");
                    CAdps = CAattack.getDouble("DPS");
                    CAseconds = CAattack.getDouble("Sec");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                EnergyMeter = EnergyMeter +  FAenergy;
                if(faSTAB == true){
                    totalDPS += (FAdps*FAseconds)*1.25;
                }else {
                    totalDPS += FAdps * FAseconds;
                }
                Seconds += FAseconds;
                if(EnergyMeter >= Math.abs(CAenergy)){
                    if(caSTAB == true){
                        totalDPS += (CAdps*CAseconds)*1.25;
                    }else {
                        totalDPS += CAdps * CAseconds;
                    }
                    Seconds += CAseconds;
                    EnergyMeter += CAenergy;
                }



            }

            Double SingleDPS = totalDPS/Seconds;
            if(typecounter == true){
                SingleDPS = SingleDPS*0.8;
            }
            totalDPS = SingleDPS*10;






            




        }
            return totalDPS;
        }






}
