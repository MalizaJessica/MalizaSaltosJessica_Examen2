package com.fisei.athanasiaapp.models;

import java.util.List;

public class SaleRequest_MSJM {
    public int UserClientID;
    public List<SaleDetails_MSJM> SaleDetails_MSJM;

    public SaleRequest_MSJM(int id, List<SaleDetails_MSJM> details){
        this.UserClientID = id;
        this.SaleDetails_MSJM = details;
    }
    public SaleRequest_MSJM(){}
}