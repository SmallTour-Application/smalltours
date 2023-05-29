package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.ResponseDTO;
import com.lattels.smalltour.dto.search.SearchGuideDTO;
import com.lattels.smalltour.dto.search.SearchPackageDTO;
import com.lattels.smalltour.service.MainService;
import com.lattels.smalltour.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/unauth/search")
@Api(tags = "unauthSearch", description = "검색창 컨트롤러")
public class SearchController {

    private final SearchService searchService;



    @ApiOperation(value = "패키지 검색", notes = "검색창에 입력한 값으로 패키지를 검색합니다.")
    @GetMapping("/package")
    public ResponseEntity<?> searchTours(@RequestParam int type,
                                         @RequestParam String location,
                                         @RequestParam int people,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                         @RequestParam int sort,
                                         @RequestParam(defaultValue = "0") int page) {
        return searchService.searchTours(type, location, people, start, end, sort, page);
    }

    @GetMapping("/guide")
    public ResponseEntity<SearchGuideDTO> searchGuides(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "sort", defaultValue = "0") int sort,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return searchService.searchGuide(keyword, sort, page);
    }

}