package com.gchatzip.JsonSuggestionAPI;

/**
 * Created by George on 2/8/2016.
 */
public class Suggestion {

    private long _id;
    private String name;
    private String type;
    private Double latitude;
    private Double longitude;


    public Suggestion(long id,String name,String type,Double latitude,Double longitude){
        this._id=id;
        this.name=name;
        this.type=type;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public void setId(long id){
        this._id=id;
    }
    public long getId(){
        return this._id;
    }
    public void setType(String type){
        this.type=type;
    }
    public String getType(){
        return this.type;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setLatitude(Double latitude){
        this.latitude=latitude;

    }
    public Double getLatitude(){
        return this.latitude;
    }
    public void setLongitude(Double longitude){
        this.longitude=longitude;
    }
    public Double getLongitude(){
        return this.longitude;
    }



}
