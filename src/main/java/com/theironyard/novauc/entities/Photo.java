package com.theironyard.novauc.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by jerieshasmith on 3/21/17.
 */

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue
    int id;

    @ManyToOne
    User sender;

    @ManyToOne
    User recipient;

    @Column(nullable = false)
    String filename;

    @Column(nullable = false)
    String originalFilename;


    @Column(nullable= false)
    int seconds;



    @Column(nullable = false)
    Boolean isPublic;

    @Column(nullable = false)
    Long finishedTime;



    public Photo(User sender, User recipient, String filename, String originalFilename,  Boolean isPublic, int seconds) {
        this.sender = sender;
        this.recipient = recipient;
        this.originalFilename = originalFilename;
        this.filename = filename;
       this.seconds = seconds;

        this.isPublic = isPublic;

    }

    public Photo() {
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }



    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean Public) {
        isPublic = isPublic;
    }


    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
}
