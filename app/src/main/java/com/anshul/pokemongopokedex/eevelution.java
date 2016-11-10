package com.anshul.pokemongopokedex;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class eevelution extends AppCompatActivity {
    public JSONObject pokemonInfo;
    public String name;
    public ArrayList<Double> muls = new ArrayList<Double>();
    JSONArray nextevoldata;
    public JSONObject pokeDetail1;
    public JSONObject pokeDetail2;
    public JSONObject pokeDetail3;
    public Integer maxCP1;
    public Integer maxCP2;
    public Integer maxCP3;
    public Integer PriCol;
    public Integer SecCol;
    public Integer TextCol;
    Integer Cpvalue =0;
    Integer selector=0;

    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eevelution);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        final int position = extras.getInt("MyData");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        RelativeLayout mainshell = (RelativeLayout)findViewById(R.id.mainshell);

        try{
            JSONArray obj = new JSONArray(loadJSONFromAsset("Poke.json"));
            pokemonInfo = obj.getJSONObject(position);
            name  = pokemonInfo.getString("Name");
            nextevoldata = pokemonInfo.getJSONArray("Next evolution(s)");

            JSONArray val = pokemonInfo.getJSONArray("multiplier");
            for(int i=0;i<val.length();i++){
                muls.add(i,val.getDouble(i));
            }


            pokeDetail1 = obj.getJSONObject(position+1);
            pokeDetail2 = obj.getJSONObject(position+2);
            maxCP1 = pokeDetail1.getInt("MaxCP");
            maxCP2 = pokeDetail2.getInt("MaxCP");
            String PriCols = pokemonInfo.getString("primaryColor");
            PriCol = Color.parseColor(PriCols);
            String SecCols = pokemonInfo.getString("secondaryColor");
            SecCol = Color.parseColor(SecCols);
            String TextCols = pokemonInfo.getString("textColor");
            TextCol = Color.parseColor(TextCols);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        mainshell.setBackgroundColor(PriCol);
        TextView enc = (TextView)findViewById(R.id.Cppointshead);
        TextView mxc = (TextView)findViewById(R.id.maxcp);
        enc.setTextColor(TextCol);
        enc.setAlpha(0.7f);
        mxc.setTextColor(TextCol);
        mxc.setAlpha(0.7f);

        TextView poke1 = (TextView)findViewById(R.id.pokeName1);
        poke1.setTextColor(TextCol);
        poke1.setText(name);

        final GradientDrawable drawbox = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
        drawbox.setCornerRadius(5);

        ImageView gifImage1 = (ImageView) findViewById(R.id.gifImage1);
        int relPos = position + 1;
        int id1 = getResources().getIdentifier("p" + relPos, "mipmap", getPackageName());
        Picasso.with(context).load(id1).into(gifImage1);

        final ImageView gifImage2 = (ImageView) findViewById(R.id.evolimg1);
        int relPos1 = position + 2;
        int id2 = getResources().getIdentifier("p" + relPos1, "mipmap", getPackageName());
        Picasso.with(context).load(id2).into(gifImage2);
        gifImage2.setBackground(drawbox);

        final ImageView gifImage3 = (ImageView) findViewById(R.id.evolimg2);
        int relPos2 = position + 3;
        int id3 = getResources().getIdentifier("p" + relPos2, "mipmap", getPackageName());
        Picasso.with(context).load(id3).into(gifImage3);

        final ImageView gifImage4 = (ImageView) findViewById(R.id.evolimg3);
        int relPos3 = position + 4;
        int id4 = getResources().getIdentifier("p" + relPos3, "mipmap", getPackageName());
        Picasso.with(context).load(id4).into(gifImage4);

        final ProgressBar prog1 = (ProgressBar)findViewById(R.id.progressBar1);
        final ProgressBar prog2 = (ProgressBar)findViewById(R.id.progressBar2);

        prog2.setMax(maxCP1);
        prog1.setMax(maxCP2);



        final EditText CPval = (EditText)findViewById(R.id.cpVallay);
        GradientDrawable draw = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
        draw.setCornerRadius(5);
        CPval.setBackground(draw);
        CPval.setPadding(0,2,0,2);
        CPval.setTextColor(TextCol);



        final TextView poke2 = (TextView)findViewById(R.id.pokeName2);
        poke2.setTextColor(TextCol);
        poke2.setText("Vaporeon");

        final TextView pokeevol = (TextView)findViewById(R.id.pokeevolcp);
        pokeevol.setTextColor(TextCol);




        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                Editable editable = CPval.getText();
                String CPval = String.valueOf(editable);

                Cpvalue=0;
                if(CPval.isEmpty()){
                    Cpvalue=0;
                }else {
                    if (CPval.length() > 0) {
                        Cpvalue = Integer.parseInt(CPval);
                    }
                }
                prog2.setProgress(Cpvalue);

                double mul = muls.get(selector);

                double mulval = Cpvalue * mul;

                prog1.setProgress((int)mulval);
                pokeevol.setText(String.valueOf(Math.round(mulval)));


            }
        };

        CPval.addTextChangedListener(textWatcher);


        gifImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pokename = "";
                gifImage4.setBackground(null);
                gifImage3.setBackground(null);
                gifImage2.setBackground(drawbox);
                try{
                    JSONArray obj = new JSONArray(loadJSONFromAsset("Poke.json"));
                    pokemonInfo = obj.getJSONObject(position+1);
                    Pokename  = pokemonInfo.getString("Name");
                    maxCP3 = pokemonInfo.getInt("MaxCP");
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                prog1.setMax(maxCP3);
                poke2.setText(Pokename);
                poke2.setTextColor(TextCol);
                selector =0;


                double mul = muls.get(selector);
                double mulval = Cpvalue * mul;

                Log.d("cal",String.valueOf(mulval));

                prog1.setProgress((int)mulval);
                pokeevol.setText(String.valueOf(Math.round(mulval)));

            }
        });

        gifImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pokename="";
                gifImage4.setBackground(null);
                gifImage2.setBackground(null);
                gifImage3.setBackground(drawbox);
                try{
                    JSONArray obj = new JSONArray(loadJSONFromAsset("Poke.json"));
                    pokemonInfo = obj.getJSONObject(position+2);
                    Pokename  = pokemonInfo.getString("Name");
                    maxCP3 = pokemonInfo.getInt("MaxCP");
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                prog1.setMax(maxCP3);

                poke2.setText(Pokename);
                poke2.setTextColor(TextCol);
                selector =1;

                double mul = muls.get(selector);

                double mulval = Cpvalue * mul;
                Log.d("cal",String.valueOf(mulval));

                prog1.setProgress((int)mulval);
                pokeevol.setText(String.valueOf(Math.round(mulval)));
            }
        });

        gifImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pokename="";
                gifImage3.setBackground(null);
                gifImage2.setBackground(null);
                gifImage4.setBackground(drawbox);
                try{
                    JSONArray obj = new JSONArray(loadJSONFromAsset("Poke.json"));
                    pokemonInfo = obj.getJSONObject(position+3);
                    Pokename  = pokemonInfo.getString("Name");
                    maxCP3 = pokemonInfo.getInt("MaxCP");
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                prog1.setMax(maxCP3);
                poke2.setText(Pokename);
                poke2.setTextColor(TextCol);
                selector =2;

                double mul = muls.get(selector);
                double mulval = Cpvalue * mul;
                Log.d("cal",String.valueOf(mulval));

                prog1.setProgress((int)mulval);
                pokeevol.setText(String.valueOf(Math.round(mulval)));


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
