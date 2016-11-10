package com.anshul.pokemongopokedex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hotchemi.stringpicker.StringPicker;

/**
 * Created by anshul on 8/13/2016.
 */
public class showpoke extends DialogFragment{


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Integer PriCol = getArguments().getInt("PriCol");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content =  inflater.inflate(R.layout.dialogpoke, null);
        final LinearLayout linlay = (LinearLayout)content.findViewById(R.id.dialay);
        linlay.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        linlay.setBackgroundColor(PriCol);
        final StringPicker stringPicker = (StringPicker) content.findViewById(R.id.string_picker);
        ArrayList pokevals = getArguments().getStringArrayList("pokevals");
        AlertDialog.Builder showPoke = new AlertDialog.Builder(getActivity());


        showPoke.setView(content)


                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        attacks.onUserSelectValue(stringPicker.getCurrentValue());
                    }
                })


                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showpoke.this.getDialog().cancel();
                    }
                });



        stringPicker.setValues(pokevals);
        return showPoke.create();
    }

}
