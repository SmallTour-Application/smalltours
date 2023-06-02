package com.lattels.smalltour.controller;

import com.lattels.smalltour.service.RoomService;
import com.lattels.smalltour.service.ToursService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/eumCodingImgs")
public class ImgController {

    private final ToursService toursService;

    private final RoomService roomService;

    @Value("${file.path}")
    private String memberFilePath;


    // 투어 썸네일 url
    @GetMapping(value = "/tours/{fileOriginName}")
    public ResponseEntity<Resource> getToursThumb(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = toursService.getToursDirectoryPath().getPath();
            FileSystemResource resource = new FileSystemResource(path + "\\" +fileName);
            if(!resource.exists()){
                throw new Exception();
            }
            HttpHeaders header = new HttpHeaders();
            Path filePath = null;
            filePath = Paths.get(path+fileName);
            header.add("Content-Type", Files.probeContentType(filePath)); // filePath의 마임타입 체크해서 header에 추가
            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        }catch(Exception e){
            throw new Exception();
        }
    }

    // 투어 이미지 리스트 url
    @GetMapping(value = "/tours/images/{fileOriginName}")
    public ResponseEntity<Resource> getToursImages(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = toursService.getToursImagesDirectoryPath().getPath();
            FileSystemResource resource = new FileSystemResource(path + "\\" +fileName);
            if(!resource.exists()){
                throw new Exception();
            }
            HttpHeaders header = new HttpHeaders();
            Path filePath = null;
            filePath = Paths.get(path+fileName);
            header.add("Content-Type", Files.probeContentType(filePath)); // filePath의 마임타입 체크해서 header에 추가
            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        }catch(Exception e){
            throw new Exception();
        }
    }

    // 호텔 방 이미지 url
    @GetMapping(value = "/tours/room/{fileOriginName}")
    public ResponseEntity<Resource> getRoomImage(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = roomService.getRoomDirectoryPath().getPath();
            FileSystemResource resource = new FileSystemResource(path + "\\" + fileName);
            if(!resource.exists()){
                throw new Exception();
            }
            HttpHeaders header = new HttpHeaders();
            Path filePath = null;
            filePath = Paths.get(path+fileName);
            header.add("Content-Type", Files.probeContentType(filePath)); // filePath의 마임타입 체크해서 header에 추가
            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        }catch(Exception e){
            throw new Exception();
        }
    }

    @GetMapping(value = "/member/{fileOriginName}")
    public ResponseEntity<Resource> getProfileImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String absolutePath = memberFilePath;
            String path = absolutePath; // 실제 이미지가 있는 위치
            FileSystemResource resource = new FileSystemResource(path + fileName);
            log.info("이미지 가져오기..." + path + fileName);
            if(!resource.exists()){
                throw new Exception();
            }
            HttpHeaders header = new HttpHeaders();
            Path filePath = null;
            filePath = Paths.get(path+fileName);
            header.add("Content-Type", Files.probeContentType(filePath)); // filePath의 마임타입 체크해서 header에 추가
            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        }catch(Exception e){
            throw new Exception();
        }
    }

}
