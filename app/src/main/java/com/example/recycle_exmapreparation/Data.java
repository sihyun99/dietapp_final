package com.example.recycle_exmapreparation;

public class Data {
    private String name;
    private String number;
    private String evaluate;
    private String time;
    private String quantity;
    private String date;
    private String area;

    //name , number, evaluate, time, quantity, date, area


    public Data(String name, String number,String evaluate,String time,String quantity,String date,String area) {
        this.name =name;
        this.number = number;
        this.evaluate = evaluate;
        this.time =time;
        this.quantity = quantity;
        this.date = date;
        this.area = area;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


}