package com.example.myapplication;

public class Listdata {
    String name;
    //String image;
    String ID;
//    public Listdata(String name, String image) {
//        this.name = name;
//        this.image = image;
//    }
    public Listdata(String name,String ID){
        //this.image = image;
        this.name = name;
        this.ID = ID;
    }
//    public Listdata(String name, String ID){
//        //this.image = "";
//        this.ID = ID;
//        this.name = name;
//    }

    public String getId() {
        return this.ID;
    }
   public String getName(){ return this.name;}

}