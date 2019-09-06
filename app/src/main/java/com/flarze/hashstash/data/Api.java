package com.flarze.hashstash.data;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    //  String BASE_URL = "http://192.168.43.178:8080/mavenFileUpload/rest/upload/";
   // String BASE_URL = "http://192.168.43.178/hashstash/hashOrStash.postman_collection.json";
    String BASE_URL = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/";

    @Multipart
    @POST("image/")
    Call<String> UploadImage(@Part MultipartBody.Part image, @Part("file") RequestBody file,
                             @Part("path") String path,
                             @Part("name") String name,
                             @Part("phone") String phone,
                             @Part("email") String email,
                             @Part("country") String country
    );

    @Multipart
    @POST("savehashwithimage/")
    Call<String> SaveHashUploadImage(@Part MultipartBody.Part image, @Part("file") RequestBody file,
                                     @Part("path") String path,
                                     @Part("userid") String userid,
                                     @Part("comments") String comments,
                                     @Part("cmtTime") long cmtTime,
                                     @Part("location") String location,
                                     @Part("locationId") String locationId,
                                     @Part("latitute") String latitute,
                                     @Part("longitute") String longitute,
                                     @Part("duration") long duration,
                                     @Part("hashOrStash") String hashOrStash
    );

    @GET("savehashorstash/{userid}/{comments}/{cmtTime}/{location}/{locationId}/{latitute}/{longitute}/{duration}/{hashOrStash}")
    Call<String> SaveHashOrStash(@Path("userid") String userid,
                                 @Path("comments") String comments,
                                 @Path("cmtTime") long cmtTime,
                                 @Path("location") String location,
                                 @Path("locationId") String locationId,
                                 @Path("latitute") String latitute,
                                 @Path("longitute") String longitute,
                                 @Path("duration") long duration,
                                 @Path("hashOrStash") String hashOrStash);



}

