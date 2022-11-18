package com.example.knowyourgovernment;

public class Channels {
    private String youtube;
    private String google;
    private String twitter;
    private String facebook;

    public Channels(String youtube, String google, String twitter, String facebook) {
        this.youtube = youtube;
        this.google = google;
        this.twitter = twitter;
        this.facebook = facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
