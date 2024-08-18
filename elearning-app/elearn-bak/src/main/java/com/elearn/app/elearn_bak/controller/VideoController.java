package com.elearn.app.elearn_bak.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearn.app.elearn_bak.config.AppConstants;
import com.elearn.app.elearn_bak.dtos.CustomMessage;
import com.elearn.app.elearn_bak.dtos.CustomPageResponse;
import com.elearn.app.elearn_bak.dtos.VideoDto;
import com.elearn.app.elearn_bak.services.VideoServiceImpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {
    
    private VideoServiceImpl videoService;

    @GetMapping
    public CustomPageResponse<VideoDto> getAll(
        @RequestParam(value="pageNumber", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER ) int pageNumber,
        @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE ) int pageSize,
        @RequestParam(value = "sortBy", required = false, defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
        @RequestParam(value = "sortSeq", required = true, defaultValue = AppConstants.DEFAULT_SORT_SEQUENCE) String sortSeq) {

        return videoService.getAll(pageNumber, pageSize, sortBy, sortSeq);
    }
    
    @GetMapping("/{videoId}")
    public VideoDto getVideo (@PathVariable String videoId ) {
        return videoService.getById(videoId);
    }

    @PostMapping
    public ResponseEntity<VideoDto> createVideo (@RequestBody VideoDto newVideo ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(videoService.create(newVideo));
    }

    @PutMapping("{videoId}")
    public VideoDto updateVideo (@PathVariable String videoId, VideoDto updatedVideo) {
        return videoService.update(updatedVideo, videoId);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<CustomMessage> deleteVideo(@PathVariable String videoId){
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("video deleted");
        customMessage.setSuccess(true);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(customMessage);
    }    
    
}
