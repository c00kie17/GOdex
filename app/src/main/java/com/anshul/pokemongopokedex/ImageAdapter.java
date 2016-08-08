package com.anshul.pokemongopokedex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by anshul on 7/22/2016.
 */
public class ImageAdapter extends ArrayAdapter<Data> implements Filterable {

    private Context mContext;
    ArrayList<Data> obj =  new ArrayList<Data>();
    ArrayList<Data> Orgobj =  new ArrayList<Data>();
    private LayoutInflater mInflater;

    private ItemFilter mFilter = new ItemFilter();




    public ImageAdapter(Context c,ArrayList<Data> pokes) {
        super(c,0,pokes);
        mContext = c;
        obj =pokes;
        Orgobj = pokes;
        mInflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return obj.size();
    }

    public Data getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        final Integer Pos = position;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(10, 10, 10, 10);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, details.class);

                int newPos = obj.get(Pos).getPos();
                intent.putExtra("MyData", newPos-1);
                mContext.startActivity(intent);

            }
        });
        int newPos = obj.get(position).getPos();
        int id = mContext.getResources().getIdentifier("p"+newPos, "mipmap", mContext.getPackageName());
        Picasso.with(mContext).load(id).into(imageView);
        return imageView;
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            final ArrayList<Data> list = Orgobj;
            int count = list.size();

            final ArrayList<Data> nlist = new ArrayList<Data>(count);

            String Name;
            Integer Pos;

            if (constraint == null || constraint.length() == 0) {
                results.count = list.size();
                results.values = list;
            }else {
                for (int i = 0; i < count; i++) {
                    Name = list.get(i).getName();
                    Pos = list.get(i).getPos();
                    String posString = String.valueOf(Pos);
                    if (Name.toLowerCase().contains(filterString)) {

                        nlist.add(new Data(Name, Pos));
                    }
                    if (posString.toLowerCase().contains(filterString)) {
                        nlist.add(new Data(Name, Pos));
                    }

                }
                results.values  = nlist;
                results.count = nlist.size();
            }

            return results;
        }
        protected void publishResults(CharSequence constraint, FilterResults results) {
            obj = (ArrayList<Data>) results.values;
            notifyDataSetChanged();
        }

    }

    public void clearAdapter()
    {
        obj.clear();
        Orgobj.clear();
        notifyDataSetChanged();
    }
}

