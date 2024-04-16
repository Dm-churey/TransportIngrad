package com.example.ingradtransport.model;

public class NewApplReq {
    private String purpose;
    private String address;
    private String date;
    private String start_time;
    private String finish_time;
    private String comment;

    public NewApplReq(String purpose, String address, String date, String start_time, String finish_time, String comment) {
        this.purpose = purpose;
        this.address = address;
        this.date = date;
        this.start_time = start_time;
        this.finish_time = finish_time;
        this.comment = comment;
    }
}
