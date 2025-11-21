package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM

        this.id = id;

        // since input delivery time is in string so make this
        String dTime[] = deliveryTime.split(":");  // split in HH:MM format  .. in String array

        int hour = Integer.parseInt(dTime[0]);
        int min = Integer.parseInt(dTime[1]);

        //deliveryTime  = HH*60 + MM
        this.deliveryTime = hour*60+min;    // in minutes
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
