package com.larditrans.model;

import lombok.Data;

import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunny on 19.12.17
 */
@Data
public class Record {

    @Hide
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private int id;
    private String name;
    private int age;
    private boolean active;
    private Date date;
    @Hide
    private String dateString;
    private String login;
    private String address;
    private String email;

    public void setDate(Date date) {
        this.date = date;
        dateString = format.format(date);
    }

    public Object get(String s) {
        Object res = null;
        switch (s) {
            case "id": return id;
            case "name": return name;
            case "age": return age;
            case "active": return active;
            case "date": return date;
            case "login": return login;
            case "address": return address;
            case "email": return email;
        }
        return res;
    }
}
