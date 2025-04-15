package com.example.communicationmanagement.Repository;

import com.example.communicationmanagement.entities.Message;
import com.example.communicationmanagement.entities.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
//    List<Message> findByConversationIdAndIsDeletedFalse(Long conversationId);
//    List<Message> findBySenderIdAndIsDeletedFalse(Long senderId);
List<Message> findByIsPinned(Boolean isPinned);


}