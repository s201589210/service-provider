package com.serveic_provider.service_provider;


public class Provider {
    private String names;
    private String proffision;
    private String numberReviews;
    private String descriptions;
    private String cities;
    private String locations;
    private boolean selected;


    private int id = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED =-1;

    public Provider(String name,String Proffison,String numberReview,String Description,String City, String Location,int ID){
        names= name  ;
        proffision=Proffison;
        numberReviews=numberReview;
        descriptions= Description;
        cities=City;
        id=ID;
        locations=Location;
    }

    public Provider(String name,String Proffison,String numberReview,String Description,String City, String Location){
        selected = false;
        names= name  ;
        proffision=Proffison;
        numberReviews=numberReview;
        descriptions= Description;
        cities=City;
        locations=Location;


    }



    public String getNames() {
        return names;
    }
    public String getProffision() {
        return proffision;
    }
    public String getNumberReviews() {
        return numberReviews;
    }
    public String getDescriptions() {
        return descriptions;
    }
    public String getCities() {
        return cities;
    }
    public String getLocations() {
        return locations;
    }


    public int getId() {
        return id;
    }



    public boolean hasImage(){
        return id != NO_IMAGE_PROVIDED;

    }


}
