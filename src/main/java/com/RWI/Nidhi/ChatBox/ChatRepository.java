package com.RWI.Nidhi.ChatBox;

import com.RWI.Nidhi.entity.ChatMessage;
import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
}
