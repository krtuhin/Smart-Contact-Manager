package com.smart.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "c_contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotBlank(message = "Name cannot be empty..!!")
    private String name;

    @Size(min = 10, max = 10, message = "Phone number cannot be empty & must have 10 digit..!!")
    private String phone;

    @NotBlank(message = "Email cannot be empty..!!")
    @Email(message = "Invalid email..!!")
    private String email;
    private String work;
    private String nickName;

    @Column(length = 1000)
    @NotBlank(message = "Description cannot be empty..!!")
    private String description;
    private String picture;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Contact() {
    }

    public Contact(int id, String name, String phone, String email, String work, String nickName, String description, String picture, User user) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.work = work;
        this.nickName = nickName;
        this.description = description;
        this.picture = picture;
        this.user = user;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", work='" + work + '\'' +
                ", nickName='" + nickName + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", user=" + user +
                '}';
    }
}
