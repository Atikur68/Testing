package com.flarze.hashstash.data;


import com.flarze.hashstash.activity.UserProfileActivity;
import com.flarze.hashstash.data.instagram_login.AppPreferences;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    //  String BASE_URL = "http://192.168.43.178:8080/mavenFileUpload/rest/upload/";
    // String BASE_URL = "http://192.168.43.178/hashstash/hashOrStash.postman_collection.json";
    String BASE_URL = "http://139.59.74.201:8080/hashorstash-0.0.1-SNAPSHOT/users/10/";
    //String BASE_URL = new UserProfileActivity().HttpUrl;
    @Multipart
    @PUT("images")
    Call<String> UploadImage(@Part MultipartBody.Part image
//                             @Part("path") String path
 //                            @Part("name") String name
//                             @Part("name") String name,
//                             @Part("phone") String phone,
//                             @Part("email") String email,
//                             @Part("country") String country
    );

    @PUT("images")
    Call<String> UploadImageHashorStash(@Part MultipartBody.Part image
//                             @Part("path") String path
//                             @Part("name") String name
//                             @Part("name") String name,
//                             @Part("phone") String phone,
//                             @Part("email") String email,
//                             @Part("country") String country
    );

    @PUT("votes")
    Call<String> VoteForHash(
    );

    @PUT("stashtohash/{stash_id}")
    Call<String> StashToHash(@Path("stash_id") String stash_id);


    @DELETE("stashes")
    Call<String> DeleteStash(@Path("stash_id") String stash_id,
                             @Path("user_id") String user_id);

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

