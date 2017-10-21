package com.example.nirvansharma.ttsecure;

/**
 * Created by Nirvan Sharma on 10/21/2017.
 */

public class Data
{
    public String score;
    public String url;

    public Data() {
    }

    public Data(String score, String url) {
        this.score = score;
        this.url = url;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
