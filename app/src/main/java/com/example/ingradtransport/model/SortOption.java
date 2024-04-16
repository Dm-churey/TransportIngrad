package com.example.ingradtransport.model;

public class SortOption {

    private String title;
    private int sort_id;

    public SortOption(String title, int sort_id) {
        this.title = title;
        this.sort_id = sort_id;
    }

    public String getTitle() {
        return title;
    }
    public int getSort_id() {
        return sort_id;
    }

}
