package com.androidtutorialshub.loginregister.models;

public class ContactsData {
    String name;
    String phone;
    String emailid;

    public ContactsData(String name, String phone, String emailid) {
        this.name = name;
        this.phone = phone;
        this.emailid = emailid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }
}
