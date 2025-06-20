package com.example.producer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.producer.model.ChatId;

@Repository
public interface ChatIdRepository extends JpaRepository<ChatId, Long> {
    boolean existsByChatId(String chatId);
    Optional<ChatId> findByChatId(String chatId);
    List<ChatId> findBySubscribedTrue();
}
