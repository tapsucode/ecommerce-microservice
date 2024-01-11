package com.phamanh.notificationservice.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SimpleMailInfo {

    private final List<String> to = new ArrayList<>();

    private String subject;

    private String content;

    public SimpleMailInfo(@NonNull String to, @NonNull String subject, String content){
        this.addTo(to);
        this.setSubject(subject);
        if (content != null){
            this.setContent(content);
        }
    }

    public void addTo(String email) {
        to.add(email);
    }

    public String[] getTo(){
     return to.toArray(new String[0]);
    }

}
