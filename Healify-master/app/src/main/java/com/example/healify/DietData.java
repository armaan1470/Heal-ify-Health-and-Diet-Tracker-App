package com.example.healify;

public class DietData {

    private String carbs;
    private String desc;
    private String fats;
    private String image;
    private String meal1;
    private String meal2;
    private String meal3;
    private String meal4;
    private String name;
    private String protiens;
    private int ratings;

    public DietData(){

    }

    public DietData(String carbs, String desc, String fats, String image, String meal1, String meal2, String meal3, String meal4, String name, String protiens, int ratings) {
        this.carbs = carbs;
        this.desc = desc;
        this.fats = fats;
        this.image = image;
        this.meal1 = meal1;
        this.meal2 = meal2;
        this.meal3 = meal3;
        this.meal4 = meal4;
        this.name = name;
        this.protiens = protiens;
        this.ratings = ratings;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getDesc() {
        return desc;
    }

    public String getFats() {
        return fats;
    }

    public String getImage() {
        return image;
    }

    public String getMeal1() {
        return meal1;
    }

    public String getMeal2() {
        return meal2;
    }

    public String getMeal3() {
        return meal3;
    }

    public String getMeal4() {
        return meal4;
    }

    public String getName() {
        return name;
    }

    public String getProtiens() {
        return protiens;
    }

    public int getRatings() {
        return ratings;
    }
}
