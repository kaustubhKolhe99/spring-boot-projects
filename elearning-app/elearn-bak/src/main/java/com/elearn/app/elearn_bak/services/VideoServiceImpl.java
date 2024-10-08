package com.elearn.app.elearn_bak.services;

import com.elearn.app.elearn_bak.dtos.CustomPageResponse;
import com.elearn.app.elearn_bak.dtos.VideoDto;
import com.elearn.app.elearn_bak.entities.Video;
import com.elearn.app.elearn_bak.exception.ResourceNotFoundException;
import com.elearn.app.elearn_bak.repository.VideoRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VideoServiceImpl implements VideoService {

    private VideoRepo videoRepo;

    private ModelMapper modelMapper;

    public VideoServiceImpl(VideoRepo videoRepo, ModelMapper modelMapper) {
        this.videoRepo = videoRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomPageResponse<VideoDto> getAll(int pageNumber, int pageSize, String sortBy, String sortSeq) {
        if (pageNumber <= 0) {
            // throw new InvalidRequestParameterException();
            return null;
        }
        Sort sort;

        if (sortSeq.equals("descending")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Video> videoPage = videoRepo.findAll(pageRequest);

        List<Video> videos = videoPage.getContent();

        List<VideoDto> videoDtos = videos
                .stream()
                .map(video -> entityToDto(video))
                .toList();
        CustomPageResponse<VideoDto> customPageResponse = new CustomPageResponse<>();
        customPageResponse.setPageNumber(pageNumber);
        customPageResponse.setPageSize(pageSize);
        customPageResponse.setTotalElements(videoPage.getTotalElements());
        customPageResponse.setTotalPages(videoPage.getTotalPages());
        customPageResponse.setContent(videoDtos);
        customPageResponse.setLast(videoPage.isLast());

        return customPageResponse;
    }

    @Override
    public VideoDto create(VideoDto videoDto) {
        videoDto.setId(UUID.randomUUID().toString());
        Video video = videoRepo.save(dtoToEntity(videoDto));
        return entityToDto(video);
    }

    @Override
    public VideoDto update(VideoDto videoDto, String videoId) {
        Video video = videoRepo
                .findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video with provided id not found!"));

        video.setTitle(videoDto.getTitle());
        video.setDesc(videoDto.getDesc());
        Video savedVideo = videoRepo.save(video);
        return entityToDto(savedVideo);
    }

    @Override
    public void delete(String videoId) {
        Video video = videoRepo
                .findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video with provided id not found"));

        videoRepo.delete(video);
    }

    @Override
    public VideoDto getById(String videoId) {
        Video video = videoRepo
                .findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video with provided Id not found!"));
        return entityToDto(video);
    }


    @Override
    public List<VideoDto> searchByTitle(String keyword) {
        List<Video> videos = videoRepo.findByTitle(keyword);
        return videos
                .stream()
                .map(video -> entityToDto(video))
                .toList();
    }

    public VideoDto entityToDto(Video video) {
        return modelMapper.map(video, VideoDto.class);
    }

    public Video dtoToEntity(VideoDto videoDto) {
        return modelMapper.map(videoDto, Video.class);
    }


}
