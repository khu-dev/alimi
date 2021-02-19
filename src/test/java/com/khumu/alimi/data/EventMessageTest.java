package com.khumu.alimi.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventMessageTest {
    @Test
    public void getResource_Resource_클래스_동적으로(){
        String title = "hello, Foo";
        String content = "I'm bar.";
        EventMessage<Comment> e = new EventMessage(
                "Notification", "create",
                new Comment(1L, title, "exists", content, "jinsu", null, 1, null, 0)
        );
        Comment c = e.getResource();
        assertEquals(c.getContent(), content);
    }
}
