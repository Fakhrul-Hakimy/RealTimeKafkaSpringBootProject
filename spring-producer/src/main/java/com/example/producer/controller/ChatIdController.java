package com.example.producer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.producer.model.ChatId;
import com.example.producer.repository.ChatIdRepository;

@RestController
@RequestMapping("/api/chatid")
@CrossOrigin(origins = "*")
public class ChatIdController {
    private final ChatIdRepository chatIdRepository;

    public ChatIdController(ChatIdRepository chatIdRepository) {
        this.chatIdRepository = chatIdRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveChatId(@RequestBody String chatId) {
        String chat = chatId.trim();
        if(!chat.matches("[0-9]+") || chat.length() < 4) {
            return ResponseEntity.badRequest().body("Invalid chat ID format or currently ID usually more than 4");
        }
        if (!chatIdRepository.existsByChatId(chatId)) {
            chatIdRepository.save(new ChatId(chatId.trim()));
            return ResponseEntity.ok("Saved");
        } else {
            return ResponseEntity.badRequest().body("Chat ID already exists");
        }
    }


    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeChatId(@RequestBody String chatId) {
        String chat = chatId.trim();
        ChatId entity = chatIdRepository.findByChatId(chat).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().body("Chat ID not found");
        }
        entity.setSubscribed(true);
        chatIdRepository.save(entity);
        return ResponseEntity.ok("Subscribed");
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopChatId(@RequestBody String chatId) {
        String chat = chatId.trim();
        ChatId entity = chatIdRepository.findByChatId(chat).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().body("Chat ID not found");
        }
        entity.setSubscribed(false);
        chatIdRepository.save(entity);
        return ResponseEntity.ok("Stopped");
    }

    @GetMapping("/subscribed")
    public ResponseEntity<?> getSubscribedChatIds() {
        List<ChatId> chatIdEntities = chatIdRepository.findBySubscribedTrue();
        List<String> ids = new ArrayList<>();
        if (chatIdEntities != null && !chatIdEntities.isEmpty()) {
            for (ChatId c : chatIdEntities) {
                ids.add(c.getChatId());
            }
        }
        return ResponseEntity.ok(ids);
    }
}
