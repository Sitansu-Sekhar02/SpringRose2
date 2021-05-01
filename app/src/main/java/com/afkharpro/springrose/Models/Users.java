package com.afkharpro.springrose.Models;

import com.orm.SugarRecord;

import org.json.JSONObject;

/**
 * Created by murdi on 10-May-17.
 */

public class Users extends SugarRecord<Users> {
    public String userid;
    public String first;
    public String last;
    public String email;
    public String tele;
    public String password;


    public Users(){
    }
    public Users(String userid,String first,String last,String email,String tele,String password){
        this.userid = userid;
        this.first = first;
        this.last = last;
        this.email = email;
        this.tele = tele;
        this.password = password;



    }
}
