package com.flarze.hashstash.data;

public class FriendList {
    private String friendname,friendnumber,invitefriendName;

    public FriendList(String invitefriendName, String friendnumber) {
        this.invitefriendName=invitefriendName;
        this.friendnumber=friendnumber;
    }

    public FriendList(String friendname){
        this.friendname=friendname;
    }

    public String getFriendname() {
        return friendname;
    }

    public String getFriendnumber() {
        return friendnumber;
    }

    public String getInvitefriendName() {
        return invitefriendName;
    }
}
