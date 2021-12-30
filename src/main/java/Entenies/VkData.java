package Entenies;

import Entenies.Interface.VkDataInterface;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;

public class VkData implements VkDataInterface {
    private int userVkId;
    private String birthDay;
    private String city;
    private URL linkPhoto;
    private String gender;
    private int age = -1;

    public void setUserVkId(int userVkId) {
        this.userVkId = userVkId;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLinkPhoto(URL linkPhoto) {
        this.linkPhoto = linkPhoto;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String birthDay){
        LocalDate date = checkCorrectFormat(birthDay);
        if(date != null)
            age = calculatesAge(date);
        else
            age = -1;
    }

    public int getUserVkId() {
        return userVkId;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getCity() {
        return city;
    }

    public URL getLinkPhoto() {
        return linkPhoto;
    }

    public String getGender() {
        return gender;
    }

    public int getAge(){
        return age;
    }

    private int calculatesAge(LocalDate birthDate) {
        if (birthDate != null) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        } else {
            return 0;
        }
    }

    private LocalDate checkCorrectFormat(String bDate) {
        if (bDate == null)
            return null;

        var bDateArr = bDate.split("[.]");
        if (bDateArr.length != 3)
            return null;

        var day = Integer.valueOf(bDateArr[0]);
        var month = Integer.valueOf(bDateArr[1]);
        var year = Integer.valueOf(bDateArr[2]);

        return LocalDate.of(year, month, day);
    }

    @Override
    public String toString() {
        return "VkData{" +
                "userVkId=" + userVkId +
                ", birthDay='" + birthDay + '\'' +
                ", city='" + city + '\'' +
                ", linkPhoto=" + linkPhoto +
                ", gender='" + gender + '\'' +
                '}';
    }
}
