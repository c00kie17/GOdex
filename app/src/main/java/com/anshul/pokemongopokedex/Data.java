package com.anshul.pokemongopokedex;

/**
 * Created by anshul on 7/28/2016.
 */
public class Data {

    private String Name = "";
    private Integer position;

    public Data(String n,Integer p){
        Name = n;
        position = p;
    }

    public String getName(){
        return Name;
    }

    public Integer getPos(){
        return position;
    }
}
