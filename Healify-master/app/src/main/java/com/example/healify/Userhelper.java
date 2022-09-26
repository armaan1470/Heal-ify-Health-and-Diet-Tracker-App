package com.example.healify;

public class Userhelper {

 String Email, Username, Password,Gender,Profile;
 Double Height,Weight,BMI;
 Integer Dietcount;


    public Userhelper(String Email, String Username, String Password, Double Height, Double Weight, Double BMI, String Gender,String Profile, Integer Dietcount) {
        this.Email = Email;
        this.Username = Username;
        this.Password = Password;
        this.Height = Height;
        this.Weight = Weight;
        this.BMI = BMI;
        this.Gender = Gender;
        this.Profile = Profile;
        this.Dietcount = Dietcount;
    }

    public Userhelper() {

    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public Double getHeight() {
        return Height;
    }

    public Double getWeight() {
        return Weight;
    }

    public Double getBMI() {
        return BMI;
    }

    public String getGender() {
        return Gender;
    }

    public String getProfile() {
        return Profile;
    }

    public Integer getDietcount() {
        return Dietcount;
    }
}
