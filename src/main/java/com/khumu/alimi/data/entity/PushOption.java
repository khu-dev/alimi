package com.khumu.alimi.data.entity;

import com.khumu.alimi.data.ResourceKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
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
public class PushOption implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String username;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    PushOptionKind pushOptionKind;

    @Builder.Default
    Boolean isActivated = true;

    @CreationTimestamp
    LocalDateTime createdAt;
}
