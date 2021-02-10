package com.khumu.alimi.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private int id;
    private String kind;
    private String action;
    private Object obj; // Notification이 이용할 data object
    private int objId;
}
