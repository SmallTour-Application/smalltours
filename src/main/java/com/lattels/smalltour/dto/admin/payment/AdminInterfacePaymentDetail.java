package com.lattels.smalltour.dto.admin.payment;


import java.time.LocalDate;
import java.time.LocalDateTime;


public interface AdminInterfacePaymentDetail {
    Integer getPaymentId();
    Integer getMemberId();
    String getMemberName();
    String getEmail();
    String getTel();
    Integer getGuideId();
    String getGuideName();
    LocalDate getPaymentDay();
    LocalDate getStartDay();
    LocalDate getEndDay();
    Integer getPeople();
    Integer getState();
    Integer getTourId();
    String getTitle();
    Integer getPrice();
    LocalDate getDepartureDay();
}
