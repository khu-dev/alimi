package com.khumu.alimi.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private int id;
    private String title;
    private String content;
    private String recipient;
    private boolean isRead;
    private Time created_at;
}
