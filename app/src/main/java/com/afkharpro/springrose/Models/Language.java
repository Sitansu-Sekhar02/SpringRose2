package com.afkharpro.springrose.Models;

import com.orm.SugarRecord;

/**
 * Created by murdi on 14-May-17.
 */

public class Language extends SugarRecord<Language> {
    public String lang;


    public Language() {
    }

    public Language(String lang) {
        this.lang = lang;

    }
}