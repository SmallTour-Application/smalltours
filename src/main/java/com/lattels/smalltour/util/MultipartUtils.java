package com.lattels.smalltour.util;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Slf4j
@UtilityClass
public class MultipartUtils {

    // 사용 가능한 이미지 형식
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png"
//            "image/gif"
    );


    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4",
            "video/avi",
            "video/mkv"
    );


    /**
     * MultipartFile이 이미지 형식인지 확인합니다.
     */
    public static boolean isImage(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (contentType == null) {
            return false;
        }

        return ALLOWED_IMAGE_TYPES.contains(contentType);
    }

    /**
     * MultipartFile을 지정된 폴더에 이미지 파일로 저장합니다.
     * 저장한 후 저장한 File을 반환합니다.
     * @param directory 저장할 폴더
     * @param fileName 파일 이름 (확장자 제외)
     * @return 저장된 파일을 반환합니다.
     */
    @SneakyThrows
    public static File saveImage(MultipartFile multipartFile, File directory, String fileName) {
        log.info(multipartFile.getOriginalFilename() + " -> " + fileName + " 저장 시작");

        // 이미지 파일인지 체크
        Preconditions.checkArgument(isImage(multipartFile), "이미지 파일이 아닙니다. (파일명: %s)", multipartFile.getOriginalFilename());

        // 확장자 계산
        String contentType = multipartFile.getContentType();
        String fileExtension = contentType.substring(contentType.lastIndexOf("/") + 1);

        // 새로운 파일 경로 지정
        directory.mkdirs();
        File newFile = new File(directory, fileName + "." + fileExtension);

        // 파일 저장
        multipartFile.transferTo(newFile);

        log.info(multipartFile.getOriginalFilename() + " -> " + fileName + " 저장 완료 (" + newFile.getAbsolutePath() + ")");

        return newFile;
    }


    public static File saveVideo(MultipartFile multipartFile, File directory, String fileName) throws Exception {
        return saveFile(multipartFile, directory, fileName, ALLOWED_VIDEO_TYPES, "비디오 파일");
    }

    private static File saveFile(MultipartFile multipartFile, File directory, String fileName,
                                 List<String> allowedTypes, String fileTypeDescription) throws Exception {
        log.info(multipartFile.getOriginalFilename() + " -> " + fileName + " 저장 시작");

        String contentType = multipartFile.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException(fileTypeDescription + "이 아닙니다. (파일명: " + multipartFile.getOriginalFilename() + ")");
        }

        String fileExtension = contentType.substring(contentType.lastIndexOf("/") + 1);

        if (!directory.exists() && !directory.mkdirs()) {
            throw new Exception("디렉토리 생성 실패: " + directory.getAbsolutePath());
        }

        File newFile = new File(directory, fileName + "." + fileExtension);

        // 동일한 이름의 파일이 존재하는 경우 이름을 변경
        int count = 1;
        while (newFile.exists()) {
            newFile = new File(directory, fileName + "_" + count + "." + fileExtension);
            count++;
        }

        multipartFile.transferTo(newFile);

        log.info(multipartFile.getOriginalFilename() + " -> " + fileName + " 저장 완료 (" + newFile.getAbsolutePath() + ")");

        return newFile;
    }

}