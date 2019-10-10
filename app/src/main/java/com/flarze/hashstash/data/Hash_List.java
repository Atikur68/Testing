package com.flarze.hashstash.data;

public class Hash_List {

    private int images, dates;
    private String hashcomment, times,userId,date,friendsName;
    boolean toggleCheck;
    private long cmtTime;
    private int voteCount,voteStatus;
    private String profileImage,hashId,votedHashId,userIdHashStash;
    private String hashStashImage,stashImage,friendImage;
    private long expiration;

    public Hash_List(String votedHashId) {
        this.votedHashId = votedHashId;
    }

    public Hash_List(String friendlist, String friendsName) {
        this.friendImage = friendlist;
        this.friendsName = friendsName;
    }

    public Hash_List(String profileImage,String hashId, String hashcomment, String date, String time,int voteStatus,String hashStashImage,String userIdHashStash){
        this.profileImage = profileImage;
        this.hashcomment = hashcomment;
        this.date = date;
        this.times = time;
        this.hashId=hashId;
        this.voteStatus=voteStatus;
        this.hashStashImage=hashStashImage;
        this.userIdHashStash=userIdHashStash;
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

    public String getUserIdHashStash() {
        return userIdHashStash;
    }

    public String getHashStashImage() {
        return hashStashImage;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public String getVotedHashId() {
        return votedHashId;
    }

    public String getHashId() {
        return hashId;
    }

    public String getFriendsName() {
        return friendsName;
    }

    public String getFriendlist() {
        return friendImage;
    }

    public String getUserId() {
        return userId;
    }

    public String getHashImage() {
        return hashStashImage;
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
