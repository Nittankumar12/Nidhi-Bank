package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.NotificationFlag;
import com.RWI.Nidhi.enums.NotificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    private int notificationId;
    private String notificationTitle;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
    @Enumerated(EnumType.STRING)
    private NotificationFlag notificationFlag;

}
