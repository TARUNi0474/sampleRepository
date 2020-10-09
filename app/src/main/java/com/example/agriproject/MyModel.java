package com.example.agriproject;

public class MyModel {
    String uuname,uemail,upassword,unumber,upincode,uloc;

    public String getUuname() {
        return uuname;
    }

    public String getUemail() {
        return uemail;
    }

    public String getUpassword() {
        return upassword;
    }

    public String getUnumber() {
        return unumber;
    }

    public String getUpincode() {
        return upincode;
    }

    public String getUloc() {
        return uloc;
    }

    public MyModel() {
    }

    public MyModel(String uuname, String uemail, String upassword, String unumber, String upincode, String uloc) {
        this.uuname = uuname;
        this.uemail = uemail;
        this.upassword = upassword;
        this.unumber = unumber;
        this.upincode = upincode;
        this.uloc = uloc;
    }
}
