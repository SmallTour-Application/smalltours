package com.lattels.smalltour.persistence;


import com.lattels.smalltour.model.EmailMessage;
import com.lattels.smalltour.model.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface EmailMessageRepository extends JpaRepository<EmailMessage, Integer> {

}
