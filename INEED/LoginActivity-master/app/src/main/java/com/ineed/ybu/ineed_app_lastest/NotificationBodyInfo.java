package com.ineed.ybu.ineed_app_lastest;

/**
 * Created by Büşra GÜL on 10.06.2017.
 */

public class NotificationBodyInfo {

    public  String user_id;
    public  String notification_body;
    public String token_id;



    public NotificationBodyInfo(){}

    public NotificationBodyInfo(String user_id,String notification_body,String token_id)
    {
        this.user_id = user_id;
        this.notification_body = notification_body;
    }
}
