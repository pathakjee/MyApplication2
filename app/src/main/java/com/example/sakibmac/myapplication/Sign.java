package com.example.sakibmac.myapplication;

import io.realm.RealmObject;

/**
 * Created by Sakib Mac on 10/14/2017.
 */

public class Sign extends RealmObject {

    private String type;
    private String password;
    private String professsion;


    public String getProfesssion() {
        return professsion;
    }

    public String getMobile() {
        return mobile;
    }

    public int getParentId() {
        return parentId;
    }

    private String mobile;
    private int parentId;

    public Sign() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    private String email;
    private String name;
    private String image;
    private String id;
    private String contact;
    private String birthday;
    private String gender;
    private String religion;
    private String blood;
    private String address;
    private String fathername;
    private String mothername;
    private String clas;
    private String section;
    private String parentid;
    private String rollno;

    public void setType(String type) {
        this.type = type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProfesssion(String professsion) {
        this.professsion = professsion;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


}
