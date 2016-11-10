package com.anshul.pokemongopokedex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class Powerup extends AppCompatActivity {
    public Integer Level = 0;
    public JSONObject pokeDetail;
    public Integer maxCP;
    public double LevelCap;
    public Integer cpPerlevelup;
    public Integer PriCol;
    public Integer SecCol;
    public Integer TextCol;
    Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerup);
        getSupportActionBar().hide();



        SharedPreferences settings = getSharedPreferences("level",Activity.MODE_PRIVATE);
        Level = settings.getInt("Level",0);


        RelativeLayout layout = (RelativeLayout) findViewById(R.id.power);
        LinearLayout infolay = (LinearLayout)findViewById(R.id.info);
        final LinearLayout LinLayoutlev = (LinearLayout) findViewById(R.id.setLevel);
        Bundle extras = getIntent().getExtras();
        final int position = extras.getInt("MyData");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        try{
            JSONArray obj1 = new JSONArray(loadJSONFromAsset("Poke.json"));
            pokeDetail = obj1.getJSONObject(position);

            cpPerlevelup = pokeDetail.getInt("powerup");
            maxCP = pokeDetail.getInt("MaxCP");
            JSONArray obj2 = new JSONArray(loadJSONFromAsset("pokecap.json"));
            JSONObject capObj= obj2.getJSONObject(Level-1);
            LevelCap = capObj.getDouble("cap");
            String PriCols = pokeDetail.getString("primaryColor");
            PriCol = Color.parseColor(PriCols);
            String SecCols = pokeDetail.getString("secondaryColor");
            SecCol = Color.parseColor(SecCols);
            String TextCols = pokeDetail.getString("textColor");
            TextCol = Color.parseColor(TextCols);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        GradientDrawable infodraw = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
        infodraw.setCornerRadius(3);
        infolay.setBackground(infodraw);
        layout.setBackgroundColor(PriCol);
        ImageView image = (ImageView)findViewById(R.id.Image);
        int relPos = position + 1;
        int id = getResources().getIdentifier("p" + relPos, "mipmap", getPackageName());
        Picasso.with(context).load(id).into(image);

        LinLayoutlev.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        Button plus = (Button)findViewById(R.id.buttonUp);
        Button minus = (Button)findViewById(R.id.buttonDown);
        TextView trainlev = (TextView)findViewById(R.id.TrainerLevel);
        TextView maxcp = (TextView)findViewById(R.id.maxcp);
        TextView Currcp = (TextView)findViewById(R.id.currCp);
        TextView maxcpplace = (TextView)findViewById(R.id.maxcpplace);
        TextView levcapplace = (TextView)findViewById(R.id.levcapplace);
        TextView perlevplace = (TextView)findViewById(R.id.perlevplace);

        trainlev.setTextColor(TextCol);
        maxcp.setTextColor(TextCol);
        Currcp.setTextColor(TextCol);
        maxcpplace.setTextColor(TextCol);
        levcapplace.setTextColor(TextCol);
        perlevplace.setTextColor(TextCol);
        trainlev.setAlpha(0.7f);
        trainlev.setAlpha(0.7f);
        Currcp.setAlpha(0.7f);
        maxcpplace.setAlpha(0.7f);
        levcapplace.setAlpha(0.7f);
        perlevplace.setAlpha(0.7f);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
        drawable.setShape(GradientDrawable.OVAL);
        plus.setBackground(drawable);
        minus.setBackground(drawable);
        plus.setTextColor(TextCol);
        minus.setTextColor(TextCol);
        plus.setPadding(5,2,5,7);
        minus.setPadding(5,2,5,9);


        final EditText levelval = (EditText)findViewById(R.id.levelValue);
        levelval.setTextColor(TextCol);
        levelval.setText(String.valueOf(Level));
        plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Level = Level +1;
                if(Level>40){
                    Toast toast = Toast.makeText(context, "Level cannot be greater than 40", Toast.LENGTH_SHORT);
                    toast.show();
                    Level =40;
                }
               levelval.setText(String.valueOf(Level));

            }
        });
        minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Level = Level -1;
                if(Level<0){
                    Toast toast = Toast.makeText(context, "Level cannot be lesser than 0", Toast.LENGTH_SHORT);
                    toast.show();
                    Level =0;
                }
                levelval.setText(String.valueOf(Level));
            }
        });

        TextView maxCpVal = (TextView)findViewById(R.id.maxcpval);
        maxCpVal.setText(String.valueOf(maxCP));
        maxCpVal.setTextColor(TextCol);

        TextView levelCap = (TextView)findViewById(R.id.levcapval);
        levelCap.setTextColor(TextCol);
        long capVal = Math.round(LevelCap * maxCP);
        levelCap.setText(String.valueOf(capVal));
        levcapplace.setTextColor(TextCol);

        TextView cpperlevelcval = (TextView)findViewById(R.id.perlevval);
        cpperlevelcval.setTextColor(TextCol);
        cpperlevelcval.setText(String.valueOf(cpPerlevelup));

        final EditText CPval = (EditText)findViewById(R.id.cpVal);
        GradientDrawable draw = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
        draw.setCornerRadius(5);
        CPval.setBackground(draw);
        CPval.setTextColor(TextCol);
        CPval.setPadding(0,2,0,2);
        String Cpvalue = CPval.getText().toString();
        final Integer CPVal = Integer.parseInt(Cpvalue);



        ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBarCap);
        mProgress.setMax(maxCP);
        mProgress.setProgress((int)capVal);

        ProgressBar mProgressCP = (ProgressBar) findViewById(R.id.progressBarCP);
        mProgressCP.setMax(maxCP);
        mProgressCP.setProgress(CPVal);


        TextWatcher textWatcherCP = new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                EditText CP = (EditText)findViewById(R.id.cpVal);
                Editable editable = CP.getText();
                String CPval = String.valueOf(editable);
                Integer Cpvalue=0;
                if(CPval.isEmpty()){
                    Cpvalue = 0;
                }else{
                    if (CPval.length() > 0) {
                        Cpvalue = Integer.parseInt(CPval);
                    }
                    if (Cpvalue > maxCP) {
                        Cpvalue = maxCP;
                    }
                }

                ProgressBar mProgressCP = (ProgressBar) findViewById(R.id.progressBarCP);
                mProgressCP.setMax(maxCP);
                mProgressCP.setProgress(Cpvalue);

            }
        };


         TextWatcher textWatcher = new TextWatcher() {

             public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try{
                    JSONArray obj2 = new JSONArray(loadJSONFromAsset("pokecap.json"));
                    JSONObject capObj= obj2.getJSONObject(Level-1);
                    LevelCap = capObj.getDouble("cap");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView levelCap = (TextView)findViewById(R.id.levcapval);
                long capVal = Math.round(LevelCap * maxCP);
                levelCap.setText(String.valueOf(capVal));
                ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBarCap);
                mProgress.setProgress((int)capVal);

            }
        };

        levelval.addTextChangedListener(textWatcher);

        CPval.addTextChangedListener(textWatcherCP);





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

    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences("level", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Level", Level);
        editor.commit();
    }


}
