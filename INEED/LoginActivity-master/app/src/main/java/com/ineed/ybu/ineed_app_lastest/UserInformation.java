package com.ineed.ybu.ineed_app_lastest;

/**
 * Created by SERPİL on 12.05.2017.
 */

public class UserInformation {

    public String name;
    public String surname;
    public String clas;
    public String department;
    public String location;
    public float pleasure_rate;
    public String id;

    public UserInformation(){

    }
    public UserInformation(String location){
        this.location=location;
    }


    public UserInformation(String id,String name, String surname,String department, String clas,String location,float prate) {
        this.id=id;
        this.name = name;
        this.surname = surname;
        this.department=department;
        this.clas=clas;
        this.location=location;
       pleasure_rate= prate;
    }


}
