package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.*;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    
    private AccountService accountService;
    private MessageService messageService;
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        try {
            // service
            Account newAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(newAccount);  
        } catch (UsernameAlreadyExistsException  e) {
            // different errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 400
        }
    }

    // login 
    @PostMapping("/login")
    public ResponseEntity<?> userLogin (@RequestBody Account account) {
        try {
            // service
            Account existingAccount = accountService.accountLogin(account.getUsername(), account.getPassword());

            return ResponseEntity.ok(existingAccount);
        } catch(InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((e.getMessage())); // 401
        }
    }

    // new message
    @PostMapping("/messages")
    public ResponseEntity<?> newMessage (@RequestBody Message message) {
        try{
            // service
            Message newMessage = messageService.creatMessage(message);
            return ResponseEntity.ok(newMessage);

        } catch( InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 400
        } catch (UserDoesNotExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 400
        }
    }

    //get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        try {
            // service
            List<Message> allMessages = messageService.getAllMessages();
            return ResponseEntity.ok(allMessages);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList()); // server error
        }
    }

    // get message by messageID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageByID(@PathVariable Integer messageId) {
        try {
            Optional<Message> msg = messageService.getMessageByID(messageId);
            
            // null check
            if(msg.isPresent()) {
                return ResponseEntity.ok(msg.get()); // 200
            } else {
                return ResponseEntity.ok(null); // empty
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // servor error
        }

    }

    // delete message by ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByID (@PathVariable Integer messageId) {
        try {
            // returning number of rows affeceted by delete thus the integer return
            // see if messages exists
            Optional<Message> msg = messageService.getMessageByID(messageId);
            // null check
            if(msg.isPresent()) {
                messageService.deleteMessageByID(messageId);
                return ResponseEntity.ok(1);
            } else {
                return ResponseEntity.ok(null);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // servor error 500
        }
    }

    // update message
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessageText(@PathVariable Integer messageId, @RequestBody Map<String, String> updates) {
        try {
            // is message in message body
            if (!updates.containsKey("messageText")) {
                throw new InvalidInputException("Message text is required for updating.");
            }
            String newMessageText = updates.get("messageText");

            // service
            int rowsUpdated = messageService.updateMessageText(messageId, newMessageText);

            // rows updated - either 1 or 0
            return ResponseEntity.ok(rowsUpdated);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 400
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 400, but in reality should be 404
        }
    }

}
