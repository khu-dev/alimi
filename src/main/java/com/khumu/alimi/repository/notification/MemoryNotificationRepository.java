package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class MemoryNotificationRepository implements NotificationRepository{
    ConcurrentMap<Integer, Notification> mem;

    public MemoryNotificationRepository() {

        System.out.println("New MemoryNotificationRepository");
        this.mem = new ConcurrentHashMap<>();
    }

    @Override
    public Notification create(Notification n) {
        if (n.getId() == 0){
            n.setId(mem.size() + 1);
        }
        mem.put(n.getId(), n);
        return mem.get(mem.size());
    }

    @Override
    public Notification get(int id) {
        return mem.get(id);
    }
}
