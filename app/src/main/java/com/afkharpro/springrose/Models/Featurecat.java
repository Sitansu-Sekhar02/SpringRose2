package com.afkharpro.springrose.Models;

/**
 * Created by murdi on 03-Dec-17.
 */

public class Featurecat {
    int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    String Title;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    String nick;


    public Featurecat(int ID,String Title,String nick){
        this.ID=ID;
        this.Title=Title;
        this.nick=nick;
    }
}
