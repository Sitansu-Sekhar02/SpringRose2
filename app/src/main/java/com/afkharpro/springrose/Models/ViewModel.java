package com.afkharpro.springrose.Models;

import java.util.ArrayList;

/**
 * Created by murdi on 03-Dec-17.
 */

public class ViewModel {
    int ID;
    String Title;




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

    public ArrayList<Hozitem> getItems() {
        return items;
    }

    public void setItems(ArrayList<Hozitem> items) {
        this.items = items;
    }

    ArrayList<Hozitem> items = new ArrayList<>();
}
