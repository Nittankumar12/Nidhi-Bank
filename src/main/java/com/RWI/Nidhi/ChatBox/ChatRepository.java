package com.RWI.Nidhi.ChatBox;

import com.RWI.Nidhi.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage,Long> {
}
