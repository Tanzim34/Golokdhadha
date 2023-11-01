package com.example.myapplication;

public class Listdata {
    String name;
    //String image;
    String ID;
    int type;
//    public Listdata(String name, String image) {
//        this.name = name;
//        this.image = image;
//    }
    public Listdata(String name,String ID){
        //this.image = image;
        this.name = name;
        this.ID = ID;
        this.type = -1;
    }
//    public Listdata(String name, String ID){
//        //this.image = "";
//        this.ID = ID;
//        this.name = name;
//    }
    public Listdata(String name, String ID, int type)
    {
        this.name = name;
        this.ID = ID;
        this.type =type;
    }

    public String getId() {
        return this.ID;
    }
   public String getName(){ return this.name;}

    public int getType() {
        return this.type;
    }
}