package com.example.telegrambot.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.telegrambot.model.YoutubeVideo;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final YouTubeTelegramBot telegramBot;
    private List<String> arrMessage = new ArrayList<>();
    private long startTime = System.currentTimeMillis();
    private String lastMessage = null;

    public KafkaConsumerService(YouTubeTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @KafkaListener(topics = "${kafka.topic.youtube-comments}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(YoutubeVideo video) {
        try {
            logger.info("Received video data from Kafka for video ID: {} ({})", video.getVideoId(), video.getTitle());
            if (video.getCommentText() != null && !video.getCommentText().trim().isEmpty()) {
                logger.info("Processing video with comment for Telegram notification: {}", video.getTitle());
                String message = String.format("""
                    🎥 New YouTube Video Update!
                    
                    Title: %s
                    Channel: %s
                    Comment: %s
                    
                    Stats:
                    👁️ Views: %d
                    👍 Likes: %d
                    💬 Comments: %d
                    
                    URL: %s
                    """,
                        video.getTitle(),
                        video.getChannelName(),
                        video.getCommentText(),
                        video.getViewCount(),
                        video.getLikeCount(),
                        video.getCommentCount(),
                        video.getVideoUrl()
                );
                boolean match = false;
                for (String theMessage : arrMessage) {
                    if(message.equals(theMessage)){
                        match = true;
                        break;
                    }
                }
                // Also block if the last sent message is the same (prevents double-send on window reset)
                if(!match && !message.equals(lastMessage)){
                    telegramBot.sendMessage(message);
                    arrMessage.add(message);
                    lastMessage = message;
                } else {
                    logger.warn("Simultaneous duplicate message blocked.");
                }
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 4 * 60 * 1000) {
                    arrMessage.clear();
                    startTime = System.currentTimeMillis();
                    logger.info("arrMessage has been clean");
                } else {
                    logger.info("arrMessage Did not clean yet");
                }
                logger.info("Message sent and arrMessage updated.");
                logger.info("Successfully processed Telegram notification for video: {}", video.getVideoId());
            } else {
                logger.warn("Received video without comment text in Telegram topic: {}", video.getVideoId());
            }
        } catch (Exception e) {
            logger.error("Error processing video data for Telegram: ", e);
            logger.debug("Full error details: ", e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.youtube-delete}")
    public void handleDeleteNotification(YoutubeVideo video) {
        logger.info("Received delete notification for video: {}", video.getVideoId());
        // If lastMessage contains the deleted video ID, clear it to prevent further notifications
        if (lastMessage != null && lastMessage.contains(video.getVideoId())) {
            lastMessage = null;
            logger.info("Cleared lastMessage for deleted video: {}", video.getVideoId());
        }
        // Optionally, you can notify users about the deletion if needed
    }
}