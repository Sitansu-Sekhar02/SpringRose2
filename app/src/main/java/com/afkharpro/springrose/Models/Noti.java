package com.afkharpro.springrose.Models;

import com.orm.SugarRecord;

/**
 * Created by murdi on 10-May-17.
 */

public class Noti extends SugarRecord<Noti> {
    public boolean turnedon;


    public Noti(){
    }
    public Noti(boolean turnedon){
        this.turnedon = turnedon;

    }

}
