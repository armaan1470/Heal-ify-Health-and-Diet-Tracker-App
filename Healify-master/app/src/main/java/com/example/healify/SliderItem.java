package com.example.healify;

public class SliderItem {
    private String Description;
    private String ImageUrl;
    private String Longdefine;


    public SliderItem(String Description,String ImageUrl,String Longdefine){
        this.Description=Description;
        this.ImageUrl=ImageUrl;
        this.Longdefine=Longdefine;
    }

    public SliderItem(){
    }

    public String getDescription() {
        return Description;
    }
    public String getImageUrl() {
        return ImageUrl;
    }
    public String getLongdefine() {
        return Longdefine;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setLongdefine(String longdefine) {
        Longdefine = longdefine;
    }
}
