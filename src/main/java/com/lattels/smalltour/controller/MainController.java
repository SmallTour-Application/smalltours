package com.lattels.smalltour.controller;


import com.lattels.smalltour.dto.main.PopularTourDTO;
import com.lattels.smalltour.dto.mainBannerDTO;
import com.lattels.smalltour.service.MainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/unauth/main")
@Api(tags = "UnauthMain", description = "메인화면 API 컨트롤러")
public class MainController {

    private final MainService mainService;


    // 평점이 높은 가이드 3명
    @GetMapping("/top-ratings")
    @ApiOperation(value = "평점이 높은 가이드 3명 표시하기")
    public ResponseEntity<?> getTopRatedGuides(){
        return ResponseEntity.ok(mainService.getTopRatedGuides());
    }

    //평점이 높은 Tours 탑3
    @GetMapping("/popularTours")
    @ApiOperation(value = "평점이 높은 Tour샆움 3개 표시하기")
    public ResponseEntity<?> getPopularTours() {
        return ResponseEntity.ok(mainService.getPopularTours());
    }

/*    //mainBanner 가져오기
    @GetMapping("/banner")
    @ApiOperation(value = "Get banner contents", notes = "Fetch the banner contents")
    public ResponseEntity<mainBannerDTO> getBannerContent() {
        mainBannerDTO bannerContent = mainService.getBannerContent();
        return new ResponseEntity<>(bannerContent, HttpStatus.OK);
    }*/

}