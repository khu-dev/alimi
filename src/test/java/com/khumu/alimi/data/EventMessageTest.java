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
        EventMessage e = new EventMessage(
                "Notification", "create",
                new Notification(1, title, content, "jinsu", false, null)
        );
        Notification n = e.getResource(Notification.class);
        assertEquals(n.getTitle(), title);
        assertEquals(n.getContent(), content);
    }
}
