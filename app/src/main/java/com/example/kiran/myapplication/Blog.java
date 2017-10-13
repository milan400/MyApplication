package com.example.kiran.myapplication;

/**
 * Created by kiran on 10/5/2017.
 */
public class Blog {

    private String title;
    private String description;
    private String image;

    public Blog(){

    }

    public Blog(String image, String description, String title) {
        this.image = image;
        this.description = description;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
