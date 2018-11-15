package com.serveic_provider.service_provider;


public class word {
    private String defult;
    private String names;
    private String Price;
    private String FromUser;
    private int id = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED =-1;

    public word(String deflt,String name,int ID){
        defult= deflt ;
        names= name  ;
        id=ID;
    }

    public word(String deflt,String name,String price,String fromUser){

        defult= deflt ;
        names= name  ;
        Price = price;
        FromUser = fromUser;

    }

    public word(String deflt,String name,String fromUser){

        defult= deflt ;
        names= name  ;
        FromUser = fromUser;


    }

    public String todefult(){
        return defult;
    }

    public String toNames() {
        return names;
    }

    public String ToPrice(){
        return Price;
    }

    public String getFromUser() {
        return FromUser;
    }

    public int getId() {
        return id;
    }



    public boolean hasImage(){
        return id != NO_IMAGE_PROVIDED;

    }


}
