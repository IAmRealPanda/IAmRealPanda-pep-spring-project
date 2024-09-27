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
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // servor error
        }

    }


}
