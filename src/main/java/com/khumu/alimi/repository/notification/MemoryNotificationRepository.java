package com.khumu.alimi.repository.notification;

import com.khumu.alimi.data.Notification;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class MemoryNotificationRepository implements NotificationRepository{
    ConcurrentMap<Long, Notification> mem;

    public MemoryNotificationRepository() {
        System.out.println("New MemoryNotificationRepository");
        this.mem = new ConcurrentHashMap<>();
    }

    @Override
    public Notification create(Notification n) {
        if (n.getId() == null || n.getId() == 0){
            n.setId((long) (mem.size() + 1));
        }
        mem.put(n.getId(), n);

        for (Notification no : mem.values()) {
            System.out.println(no.getTitle() + ": " + no.getContent());
        }
        return mem.get((long) mem.size());
    }

    @Override
    public Notification get(Long id) {
        return mem.get(id);
    }

    @Override
    public Notification update(Notification n) {
        return null;
    }

    @Override
    public List<Notification> list() {
        List<Notification> l = new ArrayList<>();
        for (Notification n: this.mem.values() ) {
            l.add(n);
        }
        return l;
    }

    @Override
    public List<Notification> list(String username) {
        List<Notification> l = new ArrayList<>();
        for (Notification n: this.mem.values() ) {
            if(username.equals(n.getRecipientObj().getUsername())){
                l.add(n);
            }
        }
        return l;
    }

    public void clear(){
        this.mem.clear();
    }
}
