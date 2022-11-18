package com.example.knowyourgovernment;

import java.util.ArrayList;

public class Official {
    private String office;
    private String name;
    private String party;
    private String address;
    private ArrayList<String> phones;
    private String email;
    private ArrayList<String> website;
    private Channels channels;
    private String photoUrl;

    public Official(String office, String name, String party, String address, ArrayList<String> phones, String email, ArrayList<String> website, Channels channels, String photoUrl) {
        this.office = office;
        this.name = name;
        this.party = party;
        this.address = address;
        this.phones = phones;
        this.email = email;
        this.website = website;
        this.channels = channels;
        this.photoUrl = photoUrl;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getWebsite() {
        return website;
    }

    public void setWebsite(ArrayList<String> website) {
        this.website = website;
    }

    public Channels getChannels() {
        return channels;
    }

    public void setChannels(Channels channels) {
        this.channels = channels;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}