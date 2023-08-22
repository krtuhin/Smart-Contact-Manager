package com.smart.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int myOrderId;
    private String orderId;
    private int amount;
    private String receipt;
    private String status;
    private String paymentId;

    @ManyToOne
    private User user;

    public int getMyOrderId() {
        return myOrderId;
    }

    public void setMyOrderId(int myOrderId) {
        this.myOrderId = myOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
