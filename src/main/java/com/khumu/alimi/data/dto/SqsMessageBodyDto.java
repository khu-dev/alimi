package com.khumu.alimi.data.dto;

import com.amazonaws.services.sqs.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SqsMessageBodyDto{
    String type;
    String messageId;
    String topicArn;
    String message;
}
