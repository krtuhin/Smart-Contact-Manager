package com.smart.dao;

import com.smart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    //method to get user details from database using email
    @Query("from User where email = :email")
    public User getUserByUserName(@Param("email") String email);

}
