package com.afkharpro.springrose.Base;

import android.content.Context;
import android.graphics.Color;

import com.afkharpro.springrose.Models.Country;
import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.Models.Zones;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by murdi on 09-May-17.
 */

public class App {
    private static final App ourInstance = new App();

    public static App getInstance() {
        return ourInstance;
    }

    private App() {
    }

    public ArrayList<Zones> getZonesArrayList() {
        return zonesArrayList;
    }

    public String getSurlink() {
        return surlink;
    }

    public void setSurlink(String surlink) {
        this.surlink = surlink;
    }

    String surlink;

    public ArrayList<Zones> zonesArrayList = new ArrayList<>();

    public ArrayList<Country> getCountryArrayList() {
        return countryArrayList;
    }

    public void setCountryArrayList(ArrayList<Country> countryArrayList) {
        this.countryArrayList = countryArrayList;
    }

    public ArrayList<Country> countryArrayList = new ArrayList<>();

    public boolean isItemInCart() {
        return itemInCart;
    }

    public void setItemInCart(boolean itemInCart) {
        this.itemInCart = itemInCart;
    }

    boolean itemInCart;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    String access_token;
    String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    String language;

    public ArrayList<JSONObject> getSearcharr() {
        return searcharr;
    }

    ArrayList<JSONObject> searcharr = new ArrayList<>();

    public ArrayList<JSONObject> getSlidersarr() {
        return slidersarr;
    }

    ArrayList<JSONObject> slidersarr = new ArrayList<>();

    public ArrayList<JSONObject> getBranchesarr() {
        return branchesarr;
    }

    ArrayList<JSONObject> branchesarr = new ArrayList<>();




    ArrayList<Hozitem> arrangments= new ArrayList<>();
    ArrayList<Hozitem> love= new ArrayList<>();
    ArrayList<Hozitem> mother= new ArrayList<>();
    ArrayList<Hozitem> eid= new ArrayList<>();
    ArrayList<Hozitem> coporate= new ArrayList<>();
    ArrayList<Hozitem> graduation= new ArrayList<>();
    ArrayList<Hozitem> birthday= new ArrayList<>();
    ArrayList<Hozitem> hand= new ArrayList<>();
    ArrayList<Hozitem> longlife= new ArrayList<>();
    ArrayList<Hozitem> artificial= new ArrayList<>();

    public ArrayList<Hozitem> getArrangments() {
        return arrangments;
    }

    public ArrayList<Hozitem> getLove() {
        return love;
    }

    public ArrayList<Hozitem> getMother() {
        return mother;
    }

    public ArrayList<Hozitem> getEid() {
        return eid;
    }

    public ArrayList<Hozitem> getCoporate() {
        return coporate;
    }

    public ArrayList<Hozitem> getGraduation() {
        return graduation;
    }

    public ArrayList<Hozitem> getBirthday() {
        return birthday;
    }

    public ArrayList<Hozitem> getHand() {
        return hand;
    }

    public ArrayList<Hozitem> getLonglife() {
        return longlife;
    }

    public ArrayList<Hozitem> getArtificial() {
        return artificial;
    }

    public ArrayList<Hozitem> getBestsellers() {
        return bestsellers;
    }

    public ArrayList<Hozitem> getViewed() {
        return viewed;
    }

    public ArrayList<Hozitem> getNewproducts() {
        return newproducts;
    }

    public ArrayList<Hozitem> getNewborn() {
        return newborn;
    }

    ArrayList<Hozitem> bestsellers= new ArrayList<Hozitem>();
    ArrayList<Hozitem> viewed= new ArrayList<Hozitem>();
    ArrayList<Hozitem> newproducts= new ArrayList<Hozitem>();
    ArrayList<Hozitem> newborn= new ArrayList<Hozitem>();

    public ArrayList<Hozitem> getSpecialities() {
        return specialities;
    }

    ArrayList<Hozitem> specialities= new ArrayList<Hozitem>();

    public ArrayList<Hozitem> getMixOccasions() {
        return mixOccasions;
    }

    ArrayList<Hozitem> mixOccasions= new ArrayList<Hozitem>();


    public void message(final Context context,String msg, final JsonObjectRequest jor) {

        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                MySingleton.getInstance(context).addToRequestQueue(jor);
            }
        })
                .show();

    }



}
