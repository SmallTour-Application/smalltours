package com.lattels.smalltour.controller;

import com.lattels.smalltour.service.*;
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
@RequestMapping("/img")
public class ImgController {

    private final MemberService memberService;

    private final ToursService toursService;

    private final RoomService roomService;

    private final ItemService itemService;

    private final EducationService educationService;

    private final SearchService searchService;

    private final ProfileService profileService;

    private final MemberGuideService memberGuideService;

    private final MainService mainService;





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

    // 아이템 이미지 url
    @GetMapping(value = "/item/{fileOriginName}")
    public ResponseEntity<Resource> getItemImage(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = itemService.getItemDirectoryPath().getPath();
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
            String path = memberService.getMemberDirectoryPath().getPath();
            FileSystemResource resource = new FileSystemResource(path + "\\" + fileName);
            if(!resource.exists()){
                throw new Exception("File not found: " + path + fileName);
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


    /*
     * 교육 동영상 가져오기
     */
    @GetMapping(value = "/education/{fileOriginName}")
    public ResponseEntity<Resource> getEducationFile(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = educationService.getEducationDirectoryPath().getPath();
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


    @GetMapping(value = "/search/member/{fileOriginName}")
    public ResponseEntity<Resource> getSearchProfileImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = searchService.getMemberDirectoryPath().getPath();
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

    @GetMapping(value = "/search/tour/{fileOriginName}")
    public ResponseEntity<Resource> getTourImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = searchService.getTourDirectoryPath().getPath();
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

    @GetMapping(value = "/profile/member/{fileOriginName}")
    public ResponseEntity<Resource> getProfileMemberImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = profileService.getMemberDirectoryPath().getPath();
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

    @GetMapping(value = "/profile/tour/{fileOriginName}")
    public ResponseEntity<Resource> getProfileTourImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = profileService.getTourDirectoryPath().getPath();
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


    @GetMapping(value = "/guide/member/{fileOriginName}")
    public ResponseEntity<Resource> getGuideProfileMemberImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = memberGuideService.getMemberDirectoryPath().getPath();
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

    @GetMapping(value = "/guide/portResume/{fileOriginName}")
    public ResponseEntity<Resource> getPortResumeImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = memberGuideService.getPortResumeDirectoryPath().getPath();
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



    @GetMapping(value = "/main/member/{fileOriginName}")
    public ResponseEntity<Resource> getMainMemberImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = mainService.getMemberDirectoryPath().getPath();
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

    @GetMapping(value = "/main/tour/{fileOriginName}")
    public ResponseEntity<Resource> getMainTourImg(@PathVariable("fileOriginName") String fileName) throws Exception{
        try{
            String path = mainService.getTourDirectoryPath().getPath();
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
