package com.lattels.smalltour.service.admin;


import com.lattels.smalltour.dto.admin.education.EducationDTO;
import com.lattels.smalltour.dto.admin.education.EducationDetailDTO;
import com.lattels.smalltour.dto.admin.education.EducationGuideDTO;
import com.lattels.smalltour.dto.admin.education.EducationVideoDTO;
import com.lattels.smalltour.model.Education;
import com.lattels.smalltour.model.Member;
import com.lattels.smalltour.persistence.EducationLogRepository;
import com.lattels.smalltour.persistence.EducationRepository;
import com.lattels.smalltour.persistence.MemberRepository;
import com.lattels.smalltour.util.FileUploadProgressListener;
import com.lattels.smalltour.util.MultipartUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminVideoService {
    private static final Logger logger = LoggerFactory.getLogger(AdminVideoService.class);
    private final EducationRepository educationRepository;
    private final MemberRepository memberRepository;
    private final EducationLogRepository educationLogRepository;

    @Autowired
    private FileUploadProgressListener progressListener;

    @Autowired
    private HttpSession httpSession;

    @Value("${ffprobe.location}")
    private String ffprobePath;



    @Value("${file.path.education}")
    private String educationFilePath;

    public File getEducationDirectoryPath() {
        File file = new File(educationFilePath);
        file.mkdirs();

        return file;
    }

    /**
     * 관리자 인지 체크
     */
    private void checkAdmin(final int adminId){
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자를 찾을수없습니다."));
        if(admin.getRole() != 3){
            throw new RuntimeException("관리자만 접근 가능합니다.");
        }
    }

    // 진행률 리스너를 이용한 동영상 업로드
    public void uploadVideoWithProgress(int memberId, EducationVideoDTO.UploadRequestDTO uploadRequestDTO,
                                        List<MultipartFile> videoFile) throws IOException {

        try {
            // 진행률 리스너를 세션에 추가
            httpSession.setAttribute("uploadProgressListener", progressListener);
            checkAdmin(memberId);

            // 해당 섹션에 존재하는 비디오 개수 구하기
            //long videoCount = educationRepository.countByVideoId();

            Education education = Education.builder()
                    .startDay(uploadRequestDTO.getStartDay())
                    .endDay(uploadRequestDTO.getEndDay())
                    .title(uploadRequestDTO.getTitle())
                    .uploadDay(LocalDate.now())
                    .build();
            educationRepository.save(education);

            // 비디오 파일인지 구분
            boolean isEducationVideoFile;

            // 동영상이 있을 경우
            if (videoFile != null && !videoFile.isEmpty()) {
                MultipartFile multipartFile = videoFile.get(0); // 동영상 한개만 저장
                if (multipartFile.getSize() <= 0 || multipartFile.getOriginalFilename() == null) {
                    logger.error("잘못된 비디오 파일 제공");
                    return;
                }
                // 비디오 파일이라고 체크
                isEducationVideoFile = true;

                // 동영상 저장
                File newFile = saveFile(education.getVideoPath(), education.getId(), getEducationDirectoryPath(), multipartFile, isEducationVideoFile);

                // 교육 비디오 파일명 DB에 저장
                education.setVideoPath(newFile.getName());
                education.setState(1);  // 일단 수강가능으로 1:수강가능 0:불가능

                // 동영상 재생시간 받아와서 저장
                education.setPlayTime(getVideoPlayTime(newFile));
            } else {
                logger.error("동영상 업로드 : 동영상이 들어오지 않음");
            }
            // 비디오 파일이 아니라고 체크
            isEducationVideoFile = false;
            educationRepository.save(education);
        } catch (Exception e) {
            logger.error("Error uploading video", e);  // 로그 추가
        } finally {
            // 업로드가 완료되면 진행률 리스너를 세션에서 제거
            httpSession.removeAttribute("uploadProgressListener");
        }
    }

    // 동영상 수정
    public void updateVideo(int adminId,EducationVideoDTO.UpdateRequestDTO updateRequestDTO, List<MultipartFile> videoFile) throws IOException {

        checkAdmin(adminId);

        // 섹션 정보 가져오기
        Education oldEducationVideo = educationRepository.findById(updateRequestDTO.getId());

        Education education= Education.builder()
                .id(oldEducationVideo.getId())
                .startDay(updateRequestDTO.getStartDay() != null ? updateRequestDTO.getStartDay() : oldEducationVideo.getStartDay())
                .endDay(updateRequestDTO.getEndDay() != null ? updateRequestDTO.getEndDay() : oldEducationVideo.getEndDay())
                .title(updateRequestDTO.getTitle() != null ? updateRequestDTO.getTitle() : oldEducationVideo.getTitle())
                .uploadDay(LocalDate.now())
                .state(1) // 일단 수강가능으로 1:수강가능 0:불가능
                .build();


        // 비디오 파일인지 구분
        boolean isEducationVideoFile;

        // 동영상이 있을 경우
        if (videoFile != null && !videoFile.isEmpty()) {
            isEducationVideoFile = true;
            // 새로운 비디오 파일이 있을 경우
            MultipartFile multipartFile = videoFile.get(0);
            File newFile = noUpdateFile(education.getVideoPath(), education.getId(), getEducationDirectoryPath(), multipartFile, isEducationVideoFile);

            // 새로운 파일이 성공적으로 저장된 경우에만 정보 업데이트
            if (newFile != null) {
                education.setVideoPath(newFile.getName());
                education.setPlayTime(getVideoPlayTime(newFile));
            }

        }
        else {
            // 새로운 동영상 파일이 없다면 기존 정보 입력
            education.setVideoPath(oldEducationVideo.getVideoPath());
            education.setPlayTime(oldEducationVideo.getPlayTime());
        }

        educationRepository.save(education);

    }
    private File noUpdateFile(String fileName, int videoId, File directoryPath, MultipartFile multipartFile, boolean isEducationVideoFile) {
        try {
            File newFile;

            // 새로운 파일이 제공된 경우에만 기존 파일 삭제 및 새로운 파일 저장
            if (!multipartFile.isEmpty()) {
                // 기존 파일이 있다면 삭제
                if (fileName != null) {
                    deleteVideoFile(fileName, directoryPath);
                }

                if (isEducationVideoFile) {
                    // 동영상 저장 (파일명 : "동영상 ID.확장자")
                    newFile = MultipartUtils.saveVideo(multipartFile, directoryPath, String.valueOf(videoId));
                } else {
                    // 이미지 저장 (파일명 : "동영상 ID.확장자")
                    newFile = MultipartUtils.saveImage(multipartFile, directoryPath, String.valueOf(videoId));
                }

                return newFile;
            } else {
                // 새로운 파일이 제공되지 않았으면 null 반환
                return directoryPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 파일 저장
    private File saveFile(String fileName, int videoId, File directoryPath, MultipartFile multipartFile, boolean isEducationVideoFile) {
        try{
            // 기존 파일이 있다면 삭제
            if (fileName != null) {
                deleteVideoFile(fileName, directoryPath);
            }

            File newFile;

            if (isEducationVideoFile){
                // 동영상 저장 (파일명 : "동영상 ID.확장자")
                newFile = MultipartUtils.saveVideo(multipartFile, directoryPath, String.valueOf(videoId));
            }
            else {
                // 파일 저장 (파일명 : "동영상 ID.확장자")
                newFile = MultipartUtils.saveImage(multipartFile, directoryPath, String.valueOf(videoId));
            }

            return newFile;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // 파일 삭제
    private void deleteVideoFile(String fileName, File directoryPath) {
        // 파일 삭제
        String imagePath = fileName;
        if(imagePath != null) {
            File oldImageFile = new File(directoryPath, imagePath);
            oldImageFile.delete();
        }
    }

    // 동영상 재생시간 구하기
    private LocalTime getVideoPlayTime(File file) throws IOException {

        // 동영상 파일의 메타데이터 읽기
        FFprobe fFprobe = new FFprobe(ffprobePath);
        FFmpegFormat format = fFprobe.probe(file.getAbsolutePath()).format;

        // 동영상 파일의 재생시간 가져오기
        double durationInSeconds = format.duration;
        long durationMillis = (long) (durationInSeconds * 1000);
        // LocalTime으로 변환
        return LocalTime.ofNanoOfDay(durationMillis * 1_000_000);

    }

    //교육 영상 게시판 목록
    public EducationDTO commonEducationLog(int adminId, int page, int countPerPage, int state,Integer educationId){
        checkAdmin(adminId);
        //강좌갯수
        int educationLogCount = educationRepository.countState(state,educationId);

        // 페이지
        Pageable pageable = PageRequest.of(page, countPerPage);

        List<Object[]> results = educationRepository.findByGuideEducationContent(pageable,state,educationId);
        List<EducationDTO.EducationListDTO> educationLogVideoDTO = new ArrayList<>();
        for (Object[] result : results) {
            //교육영상 당 통계
            int completedVideoMember = educationRepository.findCompletedEducation(Integer.parseInt(String.valueOf(result[3])));

            EducationDTO.EducationListDTO educationLogListDTO = EducationDTO.EducationListDTO.builder()
                    .id(Integer.parseInt(String.valueOf(result[3])))
                    .educationTitle(((String)result[0]))
                    .startDay(((java.sql.Date)result[1]).toLocalDate())
                    .endDay(((java.sql.Date)result[2]).toLocalDate())
                    .progress(completedVideoMember)
                    .state(state)
                    .build();
            educationLogVideoDTO.add(educationLogListDTO);
        }

        return EducationDTO.builder()
                .count(educationLogCount)
                .educationLogList(educationLogVideoDTO)
                .build();
    }

    //state에 따라 교육관리 게시판 상태가 0:수강종료(불가능) 1:수강중 2:일시중지
    public EducationDTO getEducationList(int adminId, int page, int countPerPage,int state,Integer educationId){
        return commonEducationLog(adminId,page,countPerPage,state,educationId);
    }

    //강좌 수강 상태 변경(0:수강 종료 1: 수강가능 2: 일시중지)
    public void updatestatus(int adminId,int educationId,int state){
        checkAdmin(adminId);
        //education가져오기
       Education education = educationRepository.findById(educationId);
       education.setState(state);
       educationRepository.save(education);
    }

    //교육기록 삭제
    public void deleteEducationInfo(int adminId, int educationId){
        checkAdmin(adminId);
        Education education = educationRepository.findById(educationId);
        educationRepository.delete(education);
    }


    public EducationDetailDTO getEducationDetail(int adminId, int educationId){
        checkAdmin(adminId);
        List<Object[]> educations = educationRepository.findEducationDetail(educationId);

        if (educations.isEmpty()) {
            throw new RuntimeException("해당 강좌를 수강한 가이드들 기록이 없습니다.");
        }

        List<EducationDetailDTO.EducationDTO> educationDTOS = new ArrayList<>();

        for(Object[] educationDetail : educations){

            EducationDetailDTO.EducationDTO educationDTO = EducationDetailDTO.EducationDTO.builder()
                    .id((Integer) educationDetail[0])
                    .videoPath((String) educationDetail[1])
                    .startDay(educationDetail[2] != null ? ((java.sql.Date) educationDetail[2]).toLocalDate() : null)
                    .endDay(educationDetail[3] != null ? ((java.sql.Date) educationDetail[3]).toLocalDate() : null)
                    .playTime(educationDetail[4] != null ? ((java.sql.Time) educationDetail[4]).toLocalTime() : null) // LocalDateTime으로 변환
                    .title((String) educationDetail[5])
                    .uploadDay(educationDetail[6] != null ? ((java.sql.Date) educationDetail[6]).toLocalDate() : null)
                    .educationState(((Integer) educationDetail[7]))
                    .educationLogId((Integer) educationDetail[8])
                    .guideId((Integer) educationDetail[9])
                    .guideName((String) educationDetail[10])
                    .lastView(educationDetail[11] != null ? ((java.sql.Time) educationDetail[11]).toLocalTime() : null) // LocalDateTime으로 변환
                    .educationLogState(educationDetail[12] != null ? ((Number) educationDetail[12]).intValue() : null)
                    .completedDate(educationDetail[13] != null ? ((java.sql.Date) educationDetail[13]).toLocalDate() : null)
                    .build();

            educationDTOS.add(educationDTO);
        }

        return EducationDetailDTO.builder()
                .educationDTO(educationDTOS)
                .build();
    }
}