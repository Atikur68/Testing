package com.example.flarzehashstash.data;

public class Hash_List {

    private int images, dates,friendlist;
    private String hashcomment, times,userId,date,friendsName;
    boolean toggleCheck;
    private long cmtTime;
    private int voteCount;
    private String profileImage;
    private String hashImage,stashImage;
    private long expiration;

    public Hash_List(int friendlist,String friendsName) {
        this.friendlist = friendlist;
        this.friendsName = friendsName;
    }

    public Hash_List(int images, String hashcomment, String date, String time){
        this.images = images;
        this.hashcomment = hashcomment;
        this.date = date;
        this.times = time;
    }



    public Hash_List(int images, String hashcomment, long cmtTime, String time, boolean check, String stashImage, int voteCount, long expiration) {
        this.images = images;
        this.hashcomment = hashcomment;
        this.cmtTime = cmtTime;
        this.times = time;
        this.toggleCheck = check;
        this.stashImage=stashImage;
        this.voteCount = voteCount;
        this.expiration = expiration;

    }

    public String getFriendsName() {
        return friendsName;
    }

    public int getFriendlist() {
        return friendlist;
    }

    public String getUserId() {
        return userId;
    }

    public String getHashImage() {
        return hashImage;
    }

    public String getStashImage() {
        return stashImage;
    }

    public boolean isToggleCheck() {
        return toggleCheck;
    }

    public int getImage() {
        return images;
    }

    public String getHashcomment() {
        return hashcomment;
    }

    public long getCmtTime() {
        return cmtTime;
    }

    public String getTime() {
        return times;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public long getExpiration(){return  expiration;}

    public String getDate() {
        return date;
    }

    public int getImages() {
        return images;
    }
}
