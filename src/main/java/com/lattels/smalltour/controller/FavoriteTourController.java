package com.lattels.smalltour.controller;

import com.lattels.smalltour.dto.StatsDTO;
import com.lattels.smalltour.dto.ToursDTO;
import com.lattels.smalltour.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite/tours")
@Api(tags = "Favorite Tour Controller", description = "투어 좋아요")
public class FavoriteTourController {

    private final FavoriteService favoriteService;


    /*
     * 투어 좋아요 추가
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "투어 좋아요 추가")
    public ResponseEntity<Object> add(@ApiIgnore Authentication authentication, @RequestBody ToursDTO.IdRequestDTO idRequestDTO) {

        favoriteService.add(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }

    /*
     * 투어 좋아요 삭제
     */
    @PostMapping(value = "/delete")
    @ApiOperation(value = "투어 좋아요 삭제")
    public ResponseEntity<Object> delete(@ApiIgnore Authentication authentication, @RequestBody ToursDTO.IdRequestDTO idRequestDTO) {

        favoriteService.delete(Integer.parseInt(authentication.getPrincipal().toString()), idRequestDTO);
        return ResponseEntity.ok().build();

    }
}
