package com.anshul.pokemongopokedex;

/**
 * Created by anshul on 8/16/2016.
 */
public class attackdata {
    private String Name = "";
    private Integer FA;
    private Integer CA;

    public attackdata(String n,Integer f,Integer c){
        Name = n;
        FA = f;
        CA = c;
    }

    public String getName(){
        return Name;
    }

    public Integer getFA(){
        return FA;
    }

    public Integer getCA(){
        return CA;
    }

}
