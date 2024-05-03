package com.example.ingradtransport.preferences;

import android.content.Context;

import com.example.ingradtransport.model.User;

public class SharedPreferences {
    android.content.SharedPreferences myPrefs;
    android.content.SharedPreferences.Editor prefEditor;
    Context context;

    private static final String FILE_NAME = "CurrentUser";
    //private String image = "null";

    public SharedPreferences(Context context) {
        this.context = context;
        myPrefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    public void setUser(User user){
        prefEditor = myPrefs.edit();
        prefEditor.putString("id", user.getId() + "");
        prefEditor.putString("name", user.getName());
        prefEditor.putString("lastname", user.getLastname());
        prefEditor.putString("patronymic", user.getPatronymic());
        prefEditor.putString("age", user.getAge() + "");
        prefEditor.putString("phone", user.getPhone());
        prefEditor.putString("login", user.getLogin());
        prefEditor.putString("post", user.getPost());
        prefEditor.putString("token", user.getToken());
        prefEditor.putString("image", user.getImage());
        prefEditor.apply();
    }

    public User getUser(){
        User user = new User();
        user.setId(Integer.parseInt(myPrefs.getString("id", "0")));
        user.setName(myPrefs.getString("name", ""));
        user.setLastname(myPrefs.getString("lastname", ""));
        user.setPatronymic(myPrefs.getString("patronymic", ""));
        user.setAge(Integer.parseInt(myPrefs.getString("age", "0")));
        user.setPhone(myPrefs.getString("phone", ""));
        user.setLogin(myPrefs.getString("login", ""));
        user.setPost(myPrefs.getString("post", ""));
        user.setToken(myPrefs.getString("token", ""));
        user.setImage(myPrefs.getString("image", ""));
        return user;
    }

    public void updateImage(String newImage) {
        prefEditor = myPrefs.edit();
        prefEditor.putString("image", newImage);
        prefEditor.apply();
    }

//    public void setEntered(boolean bool) {
//        prefEditor = myPrefs.edit();
//        prefEditor.putBoolean("HAS_ENTERED", bool);
//        prefEditor.apply();
//
//    }
//
//    public boolean getEntered() {
//        return myPrefs.getBoolean("HAS_ENTERED", false);
//    }
}
