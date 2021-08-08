package com.khumu.alimi.data.dto;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 주의! SQS의 JSON 데이터를 POJO로 맵핑할 때는 snake case안 쓰고 camelCase or Pascal 쓰는 듯. (e.g. JSON에서 MessageAttributes => 자바의 messageAttributes 필드
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
/*
SNS에서 SQS로 전달된 메시지.
SNS의 메시지 필드 및 메시지 애트리뷰트 모두가 SQS의 메시지 필드로서 전달됨.
SQS의 Message 필드로 데이터를 전달할 때 예시
{
        "message":"{\"id\": 184, \"title\": \"수강신청 안내\", \"author_name\": \"컴퓨터공학과\" }",
        "MessageAttributes": {
            "resource_kind": {
                "type":"String",
                "value": "announcement"
            },
            "event_kind": {
                "type":"String",
                "value": "create"
            }
        }
}

SNS를 통해 메시지를 전달할 떄의 예시
{
    "id": 184,
    "title": "수강신청 안내",
    "author_name": "컴퓨터공학과"
}
MessageAttribute는 resource_kind: announcement, event_kind: create
*/
public class SqsMessageBodyDto{
    String type;
    String messageId;
    String topicArn;
    String message;
    MessageAttributes messageAttributes;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class MessageAttributes {
        // SQS 측에서 맵핑할 때에는 snake case를 안 쓰네.....
        // 직접 명시해줘야한다.
        @JsonProperty("resource_kind")
        MessageAttribute resourceKind;
        @JsonProperty("event_kind")
        MessageAttribute eventKind;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class MessageAttribute {
        String type;
        String value;
    }

}

