package com.agriserve.controller;

import com.agriserve.dto.feedback.FeedbackRequest;
import com.agriserve.dto.feedback.FeedbackResponse;
import com.agriserve.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        return new ResponseEntity<>(feedbackService.submitFeedback(request), HttpStatus.CREATED);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(feedbackService.getFeedbackBySession(sessionId));
    }
}