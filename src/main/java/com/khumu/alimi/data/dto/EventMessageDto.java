package com.khumu.alimi.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.lang.reflect.Type;

/**
 * Redis, SQS, SNS 등의 메시지 브로커에게 전달받는
 * JSON 형태의 Event 내용을 담는 객체
 * @param <R>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventMessageDto<R> {
    @SerializedName("resource_kind")
    private String resourceKind;
    @SerializedName("event_kind")
    private String eventKind;
    @SerializedName("resource")
    private R resource; // Notification이 이용할 data object

//    public <T> T getResource(Class<T> c){
//        return (T) this.resource;
//    }
}
