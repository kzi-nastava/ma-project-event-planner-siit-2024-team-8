package com.example.myapplication.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("number")
    private String number;

    @SerializedName("profileImageURL")
    private String profileImageURL;
    @SerializedName("address")
    private String address;

    @SerializedName("userType")
    private UserType userType;
    @SerializedName("companyName")
    private String companyName;
    @SerializedName("companyDescription")
    private String companyDescription;
    @SerializedName("companyImagesURL")
    private ArrayList<String> companyImagesURL;
    @SerializedName("isActive")
    private Boolean isActive;

    public User() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";
        this.number = "";
        this.profileImageURL = "";
        this.address = "";
        this.userType = UserType.USER;
        this.companyName = "";
        this.companyDescription = "";
        this.companyImagesURL = new ArrayList<String>();
        this.isActive = false;
    }
    public User(String firstName, String lastName, String email, String password, String number, String profileImageURL, String address, UserType userType, String companyName, String companyDescription, ArrayList<String> companyImagesURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.number = number;
        this.profileImageURL = profileImageURL;
        this.address = address;
        this.userType = userType;
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.companyImagesURL = companyImagesURL;
        this.isActive = false;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public ArrayList<String> getCompanyImagesURL() {
        return companyImagesURL;
    }

    public void setCompanyImagesURL(ArrayList<String> companyImagesURL) {
        this.companyImagesURL = companyImagesURL;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}