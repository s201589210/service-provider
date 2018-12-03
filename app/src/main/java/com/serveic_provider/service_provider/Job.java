package com.serveic_provider.service_provider;

import com.google.firebase.database.DataSnapshot;
import com.serveic_provider.service_provider.serviceProvider.Profession;

import java.util.ArrayList;

public class Job {

    public String title;
    public int price;

    public Job(){

    }

    public Job(String title , int price){
        this.title = title;
        this.price = price;
    }

    public void setTitle(String title) { this.title = title; }

    public void setPrice(int price) { this.price = price; }

    public int getPrice(){return price;}

    public String getTitle() {return title;}


    public double total(ArrayList<Job> x){
        double total = 0;
        for(int i=1; i < x.size() ; i++){
            total= total+ x.get(i).getPrice();
        }
        return total;
    }



}
