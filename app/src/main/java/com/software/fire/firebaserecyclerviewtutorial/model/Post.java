package com.software.fire.firebaserecyclerviewtutorial.model;

/**
 * Created by Brad on 12/15/2016.
 */

public class Post {
    private String imageUrl;
    private long numLikes;
    private String UID;

    public Post() {
    }

    public Post(String imageUrl, long numLikes, String UID) {
        this.imageUrl = imageUrl;
        this.numLikes = numLikes;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }
}
