package com.smart.dao;

import com.smart.entities.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments, Integer> {

    //get payment details by order id
    public Payments findByOrderId(String orderId);
}
