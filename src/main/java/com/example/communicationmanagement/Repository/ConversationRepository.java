package com.example.communicationmanagement.Repository;

import com.example.communicationmanagement.entities.Conversation;
import com.example.communicationmanagement.entities.ConversationType;
import com.example.communicationmanagement.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {
     Conversation findByParticipantsContainingAndType(List<Long> participants, ConversationType type);
     @Query("SELECT c FROM Conversation c WHERE :userId MEMBER OF c.participants")
     List<Conversation> findByUserIdInParticipants(@Param("userId") Long userId);
}
