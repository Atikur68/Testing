package com.flarze.hashstash.data;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
   // @SerializedName("name") for change name to bind another name
   private String name;
   private String username;
   private String phone;
   private String password;
   private String email;
   private String image;
   private String country;
   private String userId,popularUserId;

  public User(int id, String name, String username, String phone, String password, String email, String image, String country) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.image = image;
        this.country = country;
    }

    public User(String popularUserId) {
      this.popularUserId=popularUserId;
    }

    public User(int id, String name, String phone, String email, String image) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.image = image;
    }

    public User(String name, String email, String image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }


    public String getPopularUserId() {
        return popularUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getCountry() {
        return country;
    }
}
