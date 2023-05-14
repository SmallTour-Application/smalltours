package com.lattels.smalltour.persistence;

import com.lattels.smalltour.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    // 이메일로 찾기
    Member findByEmail(String email);

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


    //멤버 권한 1번 강사
    List<Member> findByRole(int role);

    //가이드 검색
    @Query("SELECT m FROM Member m WHERE m.role = 2 AND m.name LIKE %:keyword%")
    Page<Member> findGuidesByKeyword(@Param("keyword") String keyword, Pageable pageable);
}



