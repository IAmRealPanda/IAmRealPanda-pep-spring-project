package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.*;
import com.example.exception.*;
import com.example.repository.*;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message creatMessage(Message message) throws InvalidInputException, UserDoesNotExistException {
        // valid messages
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 255) {
            throw new InvalidInputException("Message text must not be blank and must be under 255 characters");
        }

        // does user exist
        Optional<Account> existingAccount = accountRepository.findById(message.getPostedBy());
        if (existingAccount.isEmpty()) {
            throw new UserDoesNotExistException ("Posted by user does not exist");
        }

        // Save the message
        return messageRepository.save(message); 
    }

    // get all messages
    public List<Message> getAllMessages() throws ResourceNotFoundException{
        return messageRepository.findAll();
    }

    // get message by id
    public Optional<Message> getMessageByID(int msgID) throws ResourceNotFoundException {
        return messageRepository.findById(msgID);
    }

    // delete message by ID
    public void deleteMessageByID (int msgID) throws ResourceNotFoundException {
        try {
            messageRepository.deleteById(msgID);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error while deleting message");
        }
    }


    // // update message
    // @Transactional
    // public int updateMessage(Integer messageId, String newMessageText) {
    //     return messageRepository.updateMessageTextById(messageId, newMessageText);
    // }


    // // get all messages by account ID
    // public List<Message> getAllMessagesByAccountID(Integer id) throws ResourceNotFoundException {
    //     //return messageRepository.findMessagesByAccountId(id);
    //     // try {
           
    //     // } catch (Exception e) {
    //     //     System.out.println(e.getMessage());
    //     //     throw new ResourceNotFoundException("Error while deleting message");
    //     // }
    //     return messageRepository.findMessagesByAccountId(id);
    // }
}
