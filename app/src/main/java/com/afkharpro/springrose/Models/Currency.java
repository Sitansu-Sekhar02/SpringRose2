package com.afkharpro.springrose.Models;

import com.orm.SugarRecord;

import org.json.JSONObject;

/**
 * Created by murdi on 10-May-17.
 */

public class Currency extends SugarRecord<Currency> {
    public String currency;


    public Currency(){
    }
    public Currency(String currency){
        this.currency = currency;

    }

}
