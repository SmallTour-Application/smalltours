package com.lattels.smalltour.dto.admin.payment;


import java.time.LocalDate;


public interface AdminInterfacePaymentTourList {
    Integer getPaymentId();
    Integer getTourId();
    String getTitle();
    Integer getMemberId();
    String getMemberName();
    Integer getGuideId();
    String getGuideName();
    Integer getPrice();
    Integer getState();
    LocalDate getDepartureDay();
    Integer getPeople();
    LocalDate getPaymentDay();
}
