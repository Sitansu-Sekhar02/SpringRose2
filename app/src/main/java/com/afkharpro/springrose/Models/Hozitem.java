package com.afkharpro.springrose.Models;

/**
 * Created by murdi on 09-May-17.
 */

public class Hozitem {


    public Hozitem (String Image,String Coderef,String Price,int id, String originalprice, String quantity){
this.coderef= Coderef;
        this.ID = id;
        this.image = Image;
        this.price = Price;
        this.originalprice = originalprice;
        this.quantity = quantity;
    }

    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoderef() {
        return coderef;
    }

    public void setCoderef(String coderef) {
        this.coderef = coderef;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String coderef;
    String price;

    public String getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(String originalprice) {
        this.originalprice = originalprice;
    }

    String originalprice;


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    String quantity;



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    int ID;
}
