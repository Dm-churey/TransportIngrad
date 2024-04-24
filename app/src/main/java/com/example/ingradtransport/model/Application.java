package com.example.ingradtransport.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;


public class Application {
    private int id;
    private int appl_items_id;
    private int driver_id;
    private String approve;
    private String status;
    private String purpose;
    private String address;
    private String date;
    private String start_time;
    private String finish_time;
    private int user_id;
    private String comment;
    private String created_at;
    private String user_name;
    private String user_lastname;
    private String user_patronymic;
    private String driver_name;
    private String driver_lastname;
    private String driver_patronymic;

    public Application(int id, int appl_items_id, int driver_id, String approve, String status, String purpose, String address, String date, String start_time, String finish_time, int user_id, String comment, String created_at, String user_name, String user_lastname, String user_patronymic, String driver_name, String driver_lastname, String driver_patronymic) {
        this.setId(id);
        this.setAppl_items_id(appl_items_id);
        this.setDriver_id(driver_id);
        this.setApprove(approve);
        this.setStatus(status);
        this.setPurpose(purpose);
        this.setAddress(address);
        this.setDate(date);
        this.setStart_time(start_time);
        this.setFinish_time(finish_time);
        this.setUser_id(user_id);
        this.setComment(comment);
        this.setCreated_at(created_at);
        this.setUser_name(user_name);
        this.setUser_lastname(user_lastname);
        this.setUser_patronymic(user_patronymic);
        this.setDriver_name(driver_name);
        this.setDriver_lastname(driver_lastname);
        this.setDriver_patronymic(driver_patronymic);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getUser_patronymic() {
        return user_patronymic;
    }

    public void setUser_patronymic(String user_patronymic) {
        this.user_patronymic = user_patronymic;
    }

    public int getAppl_items_id() {
        return appl_items_id;
    }

    public void setAppl_items_id(int appl_items_id) {
        this.appl_items_id = appl_items_id;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_lastname() {
        return driver_lastname;
    }

    public void setDriver_lastname(String driver_lastname) {
        this.driver_lastname = driver_lastname;
    }

    public String getDriver_patronymic() {
        return driver_patronymic;
    }

    public void setDriver_patronymic(String driver_patronymic) {
        this.driver_patronymic = driver_patronymic;
    }

    public static class ApplicationComparator implements Comparator<Application> {  // сортировка по дате
        @Override
        public int compare(Application application1, Application application2) {
            return application1.getDate().compareTo(application2.getDate());
        }
    }

}
