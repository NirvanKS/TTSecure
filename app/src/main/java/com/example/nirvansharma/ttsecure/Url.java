package com.example.nirvansharma.ttsecure;

import android.app.Application;

/**
 * Created by Nirvan Sharma on 10/21/2017.
 */

public class Url extends Application {

    private String url;


    public String getGlobalUrl(){
        return url;
    }

    public void setGlobalUrl(String url){
        this.url = url;
    }
}
