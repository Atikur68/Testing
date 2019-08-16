package com.flarze.hashstash.data;

public class HashStashList {

    private String hashStashId, hashStashComments, hashStashcmtTime ,hashStashlocation,hashStashlocationId,hashStashlatitude,
            hashStashlongitude,hashStashduration;


    public HashStashList(String hashStashId, String hashStashComments, String hashStashcmtTime, String hashStashlocation, String hashStashlocationId, String hashStashlatitude, String hashStashlongitude, String hashStashduration) {
        this.hashStashId = hashStashId;
        this.hashStashComments = hashStashComments;
        this.hashStashcmtTime = hashStashcmtTime;
        this.hashStashlocation = hashStashlocation;
        this.hashStashlocationId = hashStashlocationId;
        this.hashStashlatitude = hashStashlatitude;
        this.hashStashlongitude = hashStashlongitude;
        this.hashStashduration = hashStashduration;
    }

    public String getHashStashId() {
        return hashStashId;
    }

    public String getHashStashComments() {
        return hashStashComments;
    }

    public String getHashStashcmtTime() {
        return hashStashcmtTime;
    }

    public String getHashStashlocation() {
        return hashStashlocation;
    }

    public String getHashStashlocationId() {
        return hashStashlocationId;
    }

    public String getHashStashlatitude() {
        return hashStashlatitude;
    }

    public String getHashStashlongitude() {
        return hashStashlongitude;
    }

    public String getHashStashduration() {
        return hashStashduration;
    }
}
