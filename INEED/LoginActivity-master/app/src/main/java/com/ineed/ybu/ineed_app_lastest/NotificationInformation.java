package com.ineed.ybu.ineed_app_lastest;

/**
 * Created by SERPİL on 12.05.2017.
 */

public class NotificationInformation {

     public String userid;
     public String token;




    public NotificationInformation(){}
    public NotificationInformation(String userid,String token)
    {
        this.userid = userid;
        this.token = token;
    }
    public NotificationInformation(String userid,String token,String notification_body)
    {
        this.userid = userid;
        this.token = token;
    }

}
