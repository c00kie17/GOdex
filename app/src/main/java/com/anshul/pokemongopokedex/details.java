package com.anshul.pokemongopokedex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class details extends AppCompatActivity {
    public JSONObject pokemonInfo;
    public String TypeI;
    public String TypeII;
    public JSONArray weakness;
    public JSONArray attacks;
    public Double CaptureRate;
    public Double FleeRate;
    public ArrayList<Integer> Evolutions =new ArrayList<Integer>();
    public int highlight;
    public String color;
    public Integer PriCol;
    public Integer SecCol;
    public Integer TextCol;
    public JSONArray prevevoldata;
    public JSONArray nextevoldata;
    public String Egg;
    public boolean notAvail = true;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().hide();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.pokelayout);
        LinearLayout LinlayoutWeak = (LinearLayout) findViewById(R.id.weak);
        LinearLayout LinlayoutAtt = (LinearLayout) findViewById(R.id.attribute);
        LinearLayout LinlayoutAttack = (LinearLayout) findViewById(R.id.attacktext);
        LinearLayout LinlayoutCC = (LinearLayout) findViewById(R.id.capc);
        LinearLayout LinlayoutFC = (LinearLayout) findViewById(R.id.fleec);
        LinearLayout LinlayoutEvol = (LinearLayout) findViewById(R.id.evol);
        LinearLayout LinlayoutCalc = (LinearLayout) findViewById(R.id.calc);
        LinearLayout Linlayoutegg = (LinearLayout) findViewById(R.id.egg);
        ScrollView scroll = (ScrollView)findViewById(R.id.scrollView);
        Bundle extras = getIntent().getExtras();
        final int position = extras.getInt("MyData");

        scroll.scrollTo(0, scroll.getTop());

        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset("Poke.json"));
            pokemonInfo = obj.getJSONObject(position);
            weakness = pokemonInfo.getJSONArray("Weaknesses");
            attacks = pokemonInfo.getJSONArray("Fast Attack(s)");
            Egg = pokemonInfo.getString("pokeegg");
            String PriCols = pokemonInfo.getString("primaryColor");
            PriCol = Color.parseColor(PriCols);
            String SecCols = pokemonInfo.getString("secondaryColor");
            SecCol = Color.parseColor(SecCols);
            String TextCols = pokemonInfo.getString("textColor");
            TextCol = Color.parseColor(TextCols);
            JSONArray data = pokemonInfo.getJSONArray("Type I");
            TypeI = data.getString(0);
            if(pokemonInfo.has("Type II")){
                data = pokemonInfo.getJSONArray("Type II");
                TypeII = data.getString(0);
            }else{
                TypeII = null;
            }
            if(pokemonInfo.has("Next evolution(s)")){
                nextevoldata = pokemonInfo.getJSONArray("Next evolution(s)");
                for(int i=0;i<nextevoldata.length();i++){
                    JSONObject value = nextevoldata.getJSONObject(i);
                    Evolutions.add(Integer.parseInt(value.getString("Number")));

                }
                notAvail = false;
            }
            if(pokemonInfo.has("Previous evolution(s)")){
                prevevoldata = pokemonInfo.getJSONArray("Previous evolution(s)");
                for(int i=0;i<prevevoldata.length();i++){
                    JSONObject value = prevevoldata.getJSONObject(i);
                    Evolutions.add(Integer.parseInt(value.getString("Number")));

                }
            }
           Evolutions.add(position+1);


            CaptureRate = pokemonInfo.getDouble("BaseCaptureRate");
            FleeRate = pokemonInfo.getDouble("BaseFleeRate");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        layout.setBackgroundColor(PriCol);


        int checker;
        if(TypeII == null){
            checker = 1;
        }else{
            checker = 2;
        }
        for(int i=0;i<checker;i++){
            String imageValue;
            if(i==0){
                imageValue =TypeI;
            }else{
                imageValue =TypeII;
            }
            ImageView image = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(90, 70);
            layoutParams.setMargins (10,10,10,10);
            LinlayoutAtt.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            image.setLayoutParams(layoutParams);
            Picasso.with(context).load(getAttributeImage(imageValue)).into(image);
            LinlayoutAtt.addView(image);
        }

        for (int i = 0; i < weakness.length(); i++) {
            String imageValue = "";
            try {
                imageValue = weakness.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ImageView image = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(90, 70);
            layoutParams.setMargins (10,10,10,10);
            LinlayoutWeak.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            image.setLayoutParams(layoutParams);
            Picasso.with(context).load(getAttributeImage(imageValue)).into(image);
            LinlayoutWeak.addView(image);

        }

        for(int i=0;i<attacks.length();i++){
            String attackValue="";
            try {
                attackValue = attacks.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(attackValue.isEmpty()){
            }
            else{
                TextView attacking = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                attacking.setPadding(8, 3, 8, 3);
                attacking.setTextColor(Color.parseColor("#ffffff"));
                LinlayoutAttack.setGravity(Gravity.CENTER_HORIZONTAL);
                attacking.setLayoutParams(layoutParams);
                attacking.setText(attackValue);
                attacking.setTextColor(TextCol);
                GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
                drawable.setCornerRadius(3);
                attacking.setBackground(drawable);
                LinlayoutAttack.addView(attacking);
            }
        }

        //not capturing all cc and cf data from json
        getLightUpCC(CaptureRate);
        for(int i=0;i<12;i++){
            ImageView Cap = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 10,1.0f);
            LinlayoutCC.setGravity(Gravity.TOP | Gravity.LEFT);
            layoutParams.setMargins(5, 10, 5, 5);
            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 8 );
            if(i<highlight) {
                shape.setColor(Color.parseColor(color));
            }
            else {
                shape.setColor(SecCol);

            }
            Cap.setBackground(shape);
            Cap.setLayoutParams(layoutParams);
            LinlayoutCC.addView(Cap);
        }

        getLightUpCF(FleeRate);
        for(int i=0;i<12;i++){
            ImageView Cap = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 10,1.0f);
            LinlayoutFC.setGravity(Gravity.TOP | Gravity.LEFT);
            layoutParams.setMargins(5, 10, 5, 5);
            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 8 );
            if(i<highlight) {
                shape.setColor(Color.parseColor(color));
            }
            else{
                shape.setColor(SecCol);
            }
            Cap.setBackground(shape);
            Cap.setLayoutParams(layoutParams);
            LinlayoutFC.addView(Cap);
        }

        Collections.sort(Evolutions);
        for(int i=0;i<Evolutions.size();i++){
            final int offset = i;
            ImageView image = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(130, 130);
            layoutParams.setMargins (15,15,15,15);
            image.setPadding(10,10,10,10);
            GradientDrawable shape =  new GradientDrawable();
            if(position == Evolutions.get(i)-1){
                shape.setCornerRadius( 7 );
                shape.setColor(SecCol);
            }
            image.setBackground(shape);
            LinlayoutEvol.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            image.setLayoutParams(layoutParams);
            String val = "p"+String.valueOf(Evolutions.get(i));
            int id = context.getResources().getIdentifier(val, "mipmap", context.getPackageName());
            Picasso.with(context).load(id).into(image);
            LinlayoutEvol.addView(image);
            if(position != Evolutions.get(i)-1) {
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(details.this, details.class);
                        intent.putExtra("MyData", Evolutions.get(offset) - 1);
                        startActivity(intent);
                    }
                });
            }
        }

        int calcValue;
        if(notAvail == true){
           calcValue = 2;
        }
        else{
            calcValue = 3;
        }
        for(int i=0;i<calcValue;i++){
            final Button but = new Button(this);
            but.setTextSize(10);
            Typeface typeFace= Typeface.createFromAsset(getAssets(),"fonts/aba.ttf");
            GradientDrawable drawableback = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
            drawableback.setCornerRadius(5);
            but.setBackground(drawableback);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 100);
            layoutParams.setMargins (15,15,15,15);
            LinlayoutCalc.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            if(i == 0){
                but.setText("ATTACK CHECKER");
                but.setTextColor(TextCol);
                but.setTypeface(typeFace);
                but.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(details.this, attacks.class);
                        intent.putExtra("MyData", position);
                        startActivity(intent);
                    }
                });

            }else if(i==1){
                but.setText("POWER UP CALCULATOR");
                but.setTextColor(TextCol);
                but.setTypeface(typeFace);
                but.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(details.this, Powerup.class);
                        intent.putExtra("MyData", position);
                        startActivity(intent);
                    }
                });
            }
            else{
                but.setText("EVOLUTION MULTIPLIER");
                but.setTextColor(TextCol);
                but.setTypeface(typeFace);
                but.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(position+1 == 133){
                            Intent intent = new Intent(details.this, eevelution.class);
                            intent.putExtra("MyData", position);
                            startActivity(intent);

                        }else{
                            Intent intent = new Intent(details.this, Evolution.class);
                            intent.putExtra("MyData", position);
                            startActivity(intent);
                        }
                    }
                });
            }

            but.setLayoutParams(layoutParams);
            LinlayoutCalc.addView(but);
        }
        Log.d("val",Egg);
        if(!Egg.isEmpty()){
            ImageView eggimg = new ImageView(this);
            TextView eggval = new TextView(this);
            LinearLayout.LayoutParams layoutParamsval = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
            Linlayoutegg.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(eggimg);
            Glide.with(this).load(R.mipmap.egg).into(imageViewTarget);
            eggval.setText(Egg+"Km");
            eggval.setTextColor(TextCol);
            eggval.setTextSize(30);
            eggimg.setLayoutParams(layoutParams);
            eggval.setLayoutParams(layoutParamsval);
            Linlayoutegg.addView(eggimg);
            Linlayoutegg.addView(eggval);
        }





        TextView Name = (TextView) findViewById(R.id.pokeName);
        TextView Id = (TextView) findViewById(R.id.pokeId);
        TextView type = (TextView) findViewById(R.id.pokeType);
        TextView weaknesses = (TextView) findViewById(R.id.weaknesses);
        TextView attacks = (TextView) findViewById(R.id.attacks);
        TextView  CC = (TextView) findViewById(R.id.CC);
        TextView  FC= (TextView) findViewById(R.id.FC);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{SecCol,SecCol});
        drawable.setCornerRadius(3);
        type.setBackground(drawable);

        Name.setTextColor(TextCol);
        Id.setTextColor(TextCol);
        type.setTextColor(TextCol);
        weaknesses.setTextColor(TextCol);
        attacks.setTextColor(TextCol);
        CC.setTextColor(TextCol);
        FC.setTextColor(TextCol);
        weaknesses.setAlpha(0.7f);
        attacks.setAlpha(0.7f);
        CC.setAlpha(0.7f);
        FC.setAlpha(0.7f);


        try {
            Name.setText(pokemonInfo.getString("Name"));
            type.setText(pokemonInfo.getString("Classification"));
            Id.setText("#" + pokemonInfo.getString("number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView gifImage = (ImageView) findViewById(R.id.gifImage);
        int relPos = position + 1;
        int id = getResources().getIdentifier("p" + relPos, "mipmap", getPackageName());
        Picasso.with(context).load(id).into(gifImage);

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

    public int getAttributeImage(String name){
        int returnValue;
        switch(name){
            case "Beauty":
                returnValue =  R.mipmap.beauty;
                break;
            case "Bug":
                returnValue =  R.mipmap.bug;
            break;
            case "Cool":
                returnValue =  R.mipmap.cool;
            break;
            case "Cute":
                returnValue =  R.mipmap.cute;
            break;
            case "Dark":
                returnValue =  R.mipmap.dark;
            break;
            case "Dragon":
                returnValue =  R.mipmap.dragon;
            break;
            case "Electric":
                returnValue =  R.mipmap.electric;
            break;
            case "Fairy":
                returnValue =  R.mipmap.fairy;
            break;
            case "Fighting":
                returnValue =  R.mipmap.fight;
            break;
            case "Fire":
                returnValue = R.mipmap.fire;
            break;
            case "Flying":
                returnValue =  R.mipmap.flying;
            break;
            case "Ghost":
                returnValue=  R.mipmap.ghost;
            break;
            case "Grass":
                returnValue = R.mipmap.grass;
            break;
            case "Ground":
                returnValue =  R.mipmap.ground;
            break;
            case "Ice":
                returnValue = R.mipmap.ice;
            break;
            case "Normal":
                returnValue =  R.mipmap.normal;
            break;
            case "Poison":
                returnValue =  R.mipmap.poison;
            break;
            case "Psychic":
                returnValue =  R.mipmap.psychic;
            break;
            case "Rock":
                returnValue =  R.mipmap.rock;
            break;
            case "Smart":
                returnValue =  R.mipmap.smart;
            break;
            case "Steel":
                returnValue =  R.mipmap.steel;
            break;
            case "Tough":
                returnValue =  R.mipmap.tough;
            break;
            case "Water":
                returnValue =  R.mipmap.water;
            break;
            default:
                returnValue =  R.mipmap.wut;
        }
        return  returnValue;
    }

    public void getLightUpCC(double value){
        if( value <= 0.02) {
            highlight = 1;
            color = "#FF0000";
        }
        else if (value <= 0.04){
            highlight = 2;
            color = "#FF0000";
        }
        else if(value <= 0.08){
            highlight = 3;
            color = "#FF0000";
        }
        else if(value <= 0.11) {
            highlight = 4;
            color = "#FF0000";
        }
        else if(value <= 0.12) {
            highlight = 5;
            color = "#FF9900";
        }
        else if (value <= 0.16) {
            highlight = 6;
            color = "#FF9900";
        }
        else if (value <= 0.21) {
            highlight = 7;
            color = "#FF9900";
        }
        else if (value <= 0.24) {
            highlight = 8;
            color = "#FF9900";
        }
        else if (value <= 0.32) {
            highlight = 9;
            color = "#00E500";
        }
        else if (value <= 0.41) {
            highlight = 10;
            color = "#00E500";
        }
        else if (value <= 0.48) {
            highlight = 11;
            color = "#00E500";
        }
        else {
            highlight = 12;
            color = "#00E500";
        }
    }

    public void getLightUpCF(double value){
        if (value > 0.98) {
            highlight = 12;
            color = "#FF0000";
        }
        else if (value > 0.19) {
            highlight = 10;
            color = "#FF0000";
        }
        else if (value > 0.14) {
            highlight = 8;
            color = "#FF9900";
        }
        else if (value > 0.091) {
            highlight = 6;
            color = "#FF9900";
        }
        else if (value > 0.08) {
            highlight = 4;
            color = "#00E500";
        }
        else {
            highlight = 2;
            color = "#00E500";
        }
    }

}
