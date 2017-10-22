package com.example.nirvansharma.ttsecure;

/**
 * Created by Nirvan Sharma on 10/21/2017.
 */

public class Data {

    private String Score;
    private String URL;

    public Data(String score, String URL) {
        Score = score;
        this.URL = URL;
    }

    public Data() {
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
