package com.example.networkdemo;

import java.io.Serializable;

public class Ip implements Serializable {
    private int code;

    public Ip(int code, IPData data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Ip{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

    public IPData getData() {
        return data;
    }

    public void setData(IPData data) {
        this.data = data;
    }

    private IPData data;


}
