package com.example.krejcdav.myapplication;

public enum TransportType {
    TRAM    (0),
    BUS     (1);

    private final int value;
    TransportType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
