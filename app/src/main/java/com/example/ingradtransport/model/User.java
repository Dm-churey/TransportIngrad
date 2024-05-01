package com.example.ingradtransport.model;

public class User {
    private int id;
    private String name;
    private String lastname;
    private String patronymic;
    private int age;
    private String phone;
    private String login;
    private String password;
    private String post;
    private String token;
    private String image;
    private String message;

    public User(int id, String name, String lastname, String patronymic, int age, String phone, String login, String password, String post, String token, String image, String message) {
        this.setId(id);
        this.setName(name);
        this.setLastname(lastname);
        this.setPatronymic(patronymic);
        this.setAge(age);
        this.setPhone(phone);
        this.setLogin(login);
        this.setPassword(password);
        this.setPost(post);
        this.setToken(token);
        this.setImage(image);
        this.setMessage(message);
    }

    public User() {

    }

    public User(String name, String lastname, String patronymic, int age, String phone, String login, String password, String post) {
        this.name = name;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.age = age;
        this.phone = phone;
        this.login = login;
        this.password = password;
        this.post = post;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
