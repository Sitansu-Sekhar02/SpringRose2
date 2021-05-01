package com.afkharpro.springrose.Models;

import com.orm.SugarRecord;

/**
 * Created by murdi on 15-May-17.
 */

public class AccessToken extends SugarRecord<AccessToken> {
    public String token;


    public AccessToken(){
    }
    public AccessToken(String token){
        this.token = token;

    }
}
