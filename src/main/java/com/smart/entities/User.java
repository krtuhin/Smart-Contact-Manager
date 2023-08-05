package com.smart.entities;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "c_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotBlank(message = "Name can't be empty..!")
    private String name;

    @NotBlank(message = "Email can't be empty..!")
    @Email(message = "Invalid email..!")
    @Column(unique = true)
    private String email;

    @Size(min = 8, max = 20, message = "Invalid password..! (password must be between 8 to 20 characters)")
    private String password;

    @Column(length = 500)
    private String about;
    private String profile;
    private String type;
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Contact> contacts = new ArrayList<>();

    public User() {
    }

    public User(int id, String name, String email, String password, String about, String profile, String type, boolean isActive, List<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.about = about;
        this.profile = profile;
        this.type = type;
        this.isActive = isActive;
        this.contacts = contacts;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", about='" + about + '\'' +
                ", profile='" + profile + '\'' +
                ", type='" + type + '\'' +
                ", isActive=" + isActive +
                ", contacts=" + contacts +
                '}';
    }
}