package com.example.employeesapp;

import java.io.Serializable;

public class Request implements Serializable {

    public String roomNum;
    public String service;
    public String department;
    public String time;
    public String id;

    public Request(String id,String roomNum, String service,String department,String time) {
        this.roomNum = roomNum;
        this.service = service;
        this.department = department;
        this.time = time;
        this.id = id;
    }
}
