package com.example.producer.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.producer.model.YoutubeVideo;
import com.example.producer.repository.YoutubeVideoRepository;
import com.example.producer.service.KafkaProducerService;
import com.example.producer.service.VideoCacheService;
import com.example.producer.service.YouTubeService;

@RestController
@RequestMapping("/api/youtube")
@CrossOrigin(origins = "*")
public class YoutubeController {

    private static final Logger logger = LoggerFactory.getLogger(YoutubeController.class);
    private final YouTubeService youtubeService;
    private final YoutubeVideoRepository videoRepository;
    private final KafkaProducerService kafkaProducerService;
    private final VideoCacheService videoCacheService;

    public YoutubeController(YouTubeService youtubeService, YoutubeVideoRepository videoRepository,
                             KafkaProducerService kafkaProducerService, VideoCacheService videoCacheService) {
        this.youtubeService = youtubeService;
        this.videoRepository = videoRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.videoCacheService = videoCacheService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchVideo(@RequestParam String query) {
        try {
            YoutubeVideo video;
            if (query.contains("youtube.com/") || query.contains("youtu.be/")) {
                // If the query is a full URL, use it directly
                video = youtubeService.fetchVideoData(query);
                String commentText = video.getCommentText();
                // if (commentText == null || commentText.trim().isEmpty()) {
                //     return ResponseEntity.badRequest()
                //             .body("Video will be skip due to the comment length not even or odd.");
                // }
            } else {
                // If the query is just a video ID, construct the URL
                String videoUrl = "https://www.youtube.com/watch?v=" + query;
                video = youtubeService.fetchVideoData(videoUrl);
                String commentText = video.getCommentText();
                // if (commentText == null || commentText.trim().isEmpty()) {
                //     return ResponseEntity.badRequest()
                //             .body("Video will be skip due to the comment length not even or odd.");
                // }
            }
            return ResponseEntity.ok(video);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid YouTube URL or video ID: {}", query, e);
            return ResponseEntity.badRequest().body("Invalid YouTube URL or video ID: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing video request: {}", query, e);
            return ResponseEntity.internalServerError().body("Error processing video request: " + e.getMessage());
        }
    }

    @GetMapping("/videos")
    public ResponseEntity<List<YoutubeVideo>> getAllVideos() {
        return ResponseEntity.ok(videoRepository.findAll());
    }

    @DeleteMapping("/videos/{videoId}")
    public ResponseEntity<?> deleteVideo(@PathVariable String videoId) {
        try {
            if (!videoRepository.existsById(videoId)) {
                return ResponseEntity.notFound().build();
            }
            videoRepository.deleteById(videoId);
            videoCacheService.clearVideoCache(videoId);
            kafkaProducerService.sendDeleteNotification(videoId);
            logger.info("Successfully deleted video with ID: {} and sent delete notification", videoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting video with ID: {}", videoId, e);
            return ResponseEntity.internalServerError().body("Error deleting video: " + e.getMessage());
        }
    }
}