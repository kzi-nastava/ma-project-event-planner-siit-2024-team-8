package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.Role;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

public class UserCreateRequest {
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
    @SerializedName("address")
    private String address;

    @SerializedName("profileImageURL")
    private String profileImageURL;
    @SerializedName("userType")
    private Role role;
    @SerializedName("companyName")
    private String companyName;
    @SerializedName("companyDescription")
    private String companyDescription;
    @SerializedName("companyImagesURL")
    private ArrayList<String> companyImagesURL;
    @SerializedName("isActive")
    private Boolean isActive;

    public UserCreateRequest() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";
        this.number = "";
        this.address = "";
        profileImageURL = "";
        this.role = Role.USER;
        this.companyName = "";
        this.companyDescription = "";
        this.companyImagesURL = new ArrayList<String>();
        this.isActive = false;
    }
    public UserCreateRequest(String firstName, String lastName, String email, String password, String number, String profileImageURL, String address, Role role, String companyName, String companyDescription, ArrayList<String> companyImagesURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.number = number;
        this.profileImageURL = profileImageURL;
        this.address = address;
        this.role = role;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getUserType() {
        return role;
    }

    public void setUserType(Role role) {
        this.role = role;
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

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}