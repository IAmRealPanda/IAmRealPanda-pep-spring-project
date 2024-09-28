package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    //@Query("SELECT * FROM message m WHERE m.postedBy = :accountId")
    //@Query(value = "SELECT m FROM message m WHERE m.postedBy = :accountId",  nativeQuery = true)
    //@Query("SELECT m FROM message m WHERE m.postedBy = :accountId")
   // @Query("SELECT m FROM Message m WHERE m.postedBy.accountId = :accountId")
    // @Query("SELECT m FROM message m WHERE m.postedBy = :accountId")
    // List<Message> findMessagesByAccountId(@Param("accountId") Integer accountId); 


    // @Modifying
    // @Transactional
    // @Query("UPDATE message m SET m.messageText = :newMessageText WHERE m.messageId = :messageId")
    // int updateMessageTextById(Integer messageId, String newMessageText);
}
