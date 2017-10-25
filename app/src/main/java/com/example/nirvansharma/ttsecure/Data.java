package com.example.nirvansharma.ttsecure;



public class Data {

    private String Location;
    private String Score;
    private String URL;
    private String time;

    public Data(String location, String score, String URL, String time) {
        Location = location;
        Score = score;
        this.URL = URL;
        this.time = time;
    }

    public Data() {
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
