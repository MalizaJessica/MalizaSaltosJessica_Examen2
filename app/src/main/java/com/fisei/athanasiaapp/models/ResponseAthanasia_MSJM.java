package com.fisei.athanasiaapp.models;

public class ResponseAthanasia_MSJM {
    public boolean Success;
    public String Message;

    public ResponseAthanasia_MSJM(Boolean success, String message){
        this.Success = success;
        this.Message = message;
    }
}