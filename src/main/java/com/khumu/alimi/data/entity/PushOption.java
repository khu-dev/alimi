package com.khumu.alimi.data.entity;

import com.khumu.alimi.data.ResourceKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(indexes = {
        @Index(columnList = "pushOptionKind")
})
@Builder
@Entity
public class PushOption {
    @Id
    @Column
    // username
    String id;

    @Enumerated(value = EnumType.STRING)
    PushOptionKind pushOptionKind;

    @Builder.Default
    Boolean isActivated = true;

    @CreationTimestamp
    LocalDateTime createdAt;
}
