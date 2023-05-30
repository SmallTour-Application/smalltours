package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.FavoriteTour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FavoriteTourRepository extends JpaRepository<FavoriteTour, FavoriteTour.FavoriteTourID> {

    //특정 TourId에 대해 연결된 모든 member_id의 수를 셈
    //좋아요(상품에대해)
    @Query("SELECT COUNT(DISTINCT ft.memberId) FROM FavoriteTour ft WHERE ft.tourId = :tourId")
    long countByTourId(@Param("tourId") int tourId);

    @Query("SELECT ft FROM FavoriteTour ft, Member m WHERE ft.memberId = m.id AND m.id = :memberId AND m.role = 0")
    Page<FavoriteTour> findByMemberId(@Param("memberId") Integer memberId, Pageable pageable);
}
