package com.lattels.smalltour.persistence;

import com.lattels.smalltour.dto.stats.TotalCntPerMonthDTO;
import com.lattels.smalltour.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {


    //findEmailByMemberId
    @Query(value = "SELECT email FROM member WHERE id = :id", nativeQuery = true)
    String findEmailByMemberId(@Param("id") int id);

    // 이메일로 찾기
    Member findByEmail(String email);

    // 이메일로 카운팅하기
    int countByEmail(String email);

    // 해당하는 이메일이 있는지 확인
    Boolean existsByEmail (String email);

    // 이메일과 비밀번호로 찾기
    Member findByEmailAndPassword(String email, String password);

    // 아이디로 찾기
    Optional<Member> findById(int id);

    // 가이드 아이디로 찾기
    @Query("SELECT m FROM Member m WHERE m.id = :guideId AND m.role = 2")
    Optional<Member> findByGuideId(@Param("guideId") int guideId);


    @Query(value = "SELECT * FROM member where id = :id", nativeQuery = true)
    Member findByMemberId(@Param("id") int id);





    // 아이디로 비밀번호 가져오기
    @Query(value = "SELECT password FROM member WHERE id = :id", nativeQuery = true)
    String findPasswordByMemberId(@Param("id") int id);

    // 아이디로 닉네임 가져오기
    @Query(value = "SELECT nickname FROM member WHERE id = :id", nativeQuery = true)
    String findNicknameByMemberId(@Param("id") int id);

    // 같은 전화번호가 있는지 확인
    @Query(value = "SELECT COUNT(id) FROM member WHERE tel = :tel", nativeQuery = true)
    int findByTel(@Param("tel") String tel);

    // 아이디로 전화번호 가져오기
    @Query(value = "SELECT tel FROM member WHERE id = :id", nativeQuery = true)
    String findTelByMemberId(@Param("id") int id);

    // 같은 주소 있는지 확인
    @Query(value = "SELECT COUNT(id) FROM member WHERE address = :address", nativeQuery = true)
    int findByAdd(@Param("address") String address);



    // 아이디로 주소 가져오기
    @Query(value = "SELECT address FROM member WHERE id = :id", nativeQuery = true)
    String findAddByMemberId(@Param("id") int id);

    // 같은 닉네임이 있는지 확인
    @Query(value = "SELECT COUNT(id) FROM member WHERE nickname = :nickname", nativeQuery = true)
    int findByNickname(@Param("nickname") String nickname);

    // 아이디로 이름 가져오기
    @Query(value = "SELECT name FROM member WHERE id = :id", nativeQuery = true)
    String findNameByMemberId(@Param("id") int id);

    // 아이디로 닉네임 가져오기
    @Query(value = "SELECT nickname FROM member WHERE id = :id", nativeQuery = true)
    String findNickNameByMemberId(@Param("id") int id);


    //멤버 권한 2번 가이드
    List<Member> findByRole(int role);

    //가이드 검색
    @Query("SELECT m FROM Member m WHERE m.role = 2 AND m.name LIKE %:keyword%")
    Page<Member> findGuidesByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<Member> findByIdAndRole(int id, int role);

    @Query(value = "SELECT * FROM member WHERE role IN (0,1,2)", nativeQuery = true)
    List<Member> findByMembersId();

    @Query(value = "SELECT * FROM member WHERE role IN (0,1,2)", nativeQuery = true)
    Member findByMemberId();

    @Query(value = "SELECT * FROM member m WHERE m.role IN (0,1,2) AND id = :memberId", nativeQuery = true)
    Member findByMemberInfoId(@Param("memberId") int memberId);

    @Query("SELECT m FROM Member m WHERE m.id = :adminId AND m.role = 3")
    Optional<Member> findByAdminId(@Param("adminId")int adminId);


    // 아이디로 블랙리스트 값 가져오기
    @Query(value = "SELECT black_list FROM member WHERE id = :id", nativeQuery = true)
    int findBlackListMemberId(@Param("id") int id);


    // 아이디로 우수가이드 값 가져오기
    @Query(value = "SELECT greate FROM member WHERE id = :id", nativeQuery = true)
    int findGreatMemberId(@Param("id") int id);

    //입력한 email값에 대응하는 memberId를가져오게함
    @Query(value = "SELECT * FROM member m WHERE m.id = :id AND m.role IN (0,2)", nativeQuery = true)
    Member findByReciverMemberId(@Param("id") int id);

    @Query(value = "SELECT * FROM member m WHERE m.role = 0", nativeQuery = true)
    List<Member> findByMemberAll();

    @Query(value = "SELECT * FROM member m WHERE m.role = 2", nativeQuery = true)
    List<Member> findByGuideAll();

    @Query(value = "SELECT * FROM member m WHERE m.name =:name OR m.email =:email OR m.tel =:tel OR m.birth_day =:birthDay AND role IN (0,1,2)", nativeQuery = true)
    List<Member> findByMemberSearchId(@Param("name") String name, @Param("email") String email, @Param("tel") String tel,@Param("birthDay")LocalDate birthDay,Pageable pageable);

    @Query(value = "SELECT * FROM member m WHERE m.name =:name", nativeQuery = true)
    Member findByMemberName(@Param("name") String name);

    /*
     * 권한에 맞는 회원 수 가져오기
     */
    @Query(value = "SELECT COUNT(m) FROM Member m WHERE m.role = :role AND m.state = 1")
    int countByMemberRole(@Param("role") int role);

    /*
    * 이름에 맞는 Member 리스트 가져오기
    */
    @Query(value = "SELECT m FROM Member m WHERE m.name = :name")
    List<Member> findAllByName(@Param("name") String name);

    /*
     * 1년간의 월별 가입자 수 가져오기
     */
    @Query(value = "SELECT new com.lattels.smalltour.dto.stats.TotalCntPerMonthDTO(m.joinDay, count(m)) " +
            "FROM Member m " +
            "WHERE m.joinDay >= :date " +
            "GROUP BY m.joinDay ")
    List<TotalCntPerMonthDTO> countMemberPerMonth(@Param("date") LocalDateTime date);

    @Query(value = "SELECT count(*) FROM member m WHERE m.role = 2", nativeQuery = true)
    Integer findByCountId();

    /**
     * @param role 권한
     * @return 유저 목록
     */
    Page<Member> findByRole(int role, Pageable pageable);

    /**
     * 검색어가 포함되는 이름을 가진 유저 목록
     * @param word 검색어
     * @param role 권한
     * @return 유저 목록
     */
    Page<Member> findByNameContainsAndRole(String word, int role, Pageable pageable);

    /**
     * 검색어가 포함되는 닉네임을 가진 유저 목록
     * @param word 검색어
     * @param role 권한
     * @return 유저 목록
     */
    Page<Member> findByNicknameContainsAndRole(String word, int role, Pageable pageable);

    /**
     * 검색어가 포함되는 이메일을 가진 유저 목록
     * @param word 검색어
     * @param role 권한
     * @return 유저 목록
     */
    Page<Member> findByEmailContainsAndRole(String word, int role, Pageable pageable);



    @Query(value = "SELECT * FROM member m JOIN best_guide bg ON m.id = bg.guide_id WHERE m.role = 2 AND m.id =:memberId", nativeQuery = true)
    Member findByMemberDetailInfoId(@Param("memberId") Integer memberId);
}



