package com.fisei.athanasiaapp.objects;

import java.util.Date;

public class Order_MSJM {
    public int ID;
    public String Date;
    public int UserClientID;
    public double Total;

    public Order_MSJM(int id, String date, int userID, double total){
        this.ID = id;
        this.Date = date;
        this.UserClientID = userID;
        this.Total = total;
    }
}
