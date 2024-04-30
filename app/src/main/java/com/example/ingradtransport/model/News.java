package com.example.ingradtransport.model;

public class News {
    private int id;
    private String header;
    private String body;
    private String section;
    private String image;

    public News(int id, String header, String body, String section, String image) {
        this.setId(id);
        this.setHeader(header);
        this.setBody(body);
        this.setSection(section);
        this.setImage(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
