package com.smart.dao;

import com.smart.entities.Contact;
import com.smart.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    //method for get all contacts of by user id
    //@Query("from Contact c where c.user.id = :userId")
    //public List<Contact> findContactsByUserId(@Param("userId") int id);

    //method for getting data as page from database
    //Pageable has two parameter 1) datalist per page, 2) current page number
    @Query("from Contact c where c.user.id = :userId")
    public Page<Contact> findContactsByUserId(@Param("userId") int id, Pageable pageable);

    //method for search result
    public List<Contact> findByNameContainingAndUser(String name, User user);
}
