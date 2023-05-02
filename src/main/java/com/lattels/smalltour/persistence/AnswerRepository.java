package com.lattels.smalltour.persistence;




import com.lattels.smalltour.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnswerRepository extends JpaRepository<Answer, Integer> {
  
 
}
