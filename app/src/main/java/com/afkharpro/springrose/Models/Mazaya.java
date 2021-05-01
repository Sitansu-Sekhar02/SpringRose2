package com.afkharpro.springrose.Models;

import com.orm.SugarRecord;

/**
 * Created by murdi on 12-Dec-17.
 */

public class Mazaya extends SugarRecord<Mazaya> {
    public String MID;


    public Mazaya() {
    }

    public Mazaya(String MID) {
        this.MID = MID;

    }
}
