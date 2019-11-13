package com.example.networkdemo;

import java.io.Serializable;

public class Ip implements Serializable {
    private int code;
    private IPData data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public IPData getData() {
        return data;
    }

    public void setData(IPData data) {
        this.data = data;
    }




}
