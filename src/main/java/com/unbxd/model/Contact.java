package com.unbxd.model;

public class Contact {
    private int id;
    private String name;
    private String phone;
    private String address;

    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return phone;
    }

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }
}
