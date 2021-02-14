package com.khumu.alimi.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage {
    @SerializedName("resource_kind")
    private String resourceKind;
    @SerializedName("event_kind")
    private String eventKind;
    @SerializedName("resource")
    private Object resource; // Notification이 이용할 data object

    public <T> T getResource(Class<T> c){
        return (T) this.resource;
    }
}
