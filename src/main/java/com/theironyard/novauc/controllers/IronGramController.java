package com.theironyard.novauc.controllers;

import com.theironyard.novauc.entities.Photo;
import com.theironyard.novauc.entities.User;
import com.theironyard.novauc.services.PhotoRepository;
import com.theironyard.novauc.services.UserRepository;

import com.theironyard.novauc.utilities.PasswordStorage;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

/**
 * Created by jerieshasmith on 3/21/17.
 */
@RestController
public class IronGramController {


    @Autowired
    UserRepository users;

    @Autowired
    PhotoRepository photos;


    Server dbui = null;

    @PostConstruct
    public void init() throws SQLException {
        dbui = Server.createWebServer().start();
    }

    @PreDestroy
    public void destroy() {
        dbui.stop();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(String username, String password, HttpSession session, HttpServletResponse response) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        } else if (!PasswordStorage.verifyPassword(password, user.getPassword())) {
            throw new Exception("Wrong password");
        }
        session.setAttribute("username", username);
        response.sendRedirect("/");
        return user;
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("/");
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public User getUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return users.findFirstByName(username);
    }


    @RequestMapping("/upload")
    public Photo upload(
            HttpSession session,
            HttpServletResponse response,
            String receiver,
            MultipartFile photo,
            Boolean isPublic,
            int time
    ) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in.");
        }
        User senderUser = users.findFirstByName(username);
        User receiverUser = users.findFirstByName(receiver);

        if (receiver == null) {
            throw new Exception("Receiver name doesn't exist.");
        }

        if (!photo.getContentType().startsWith("image")) {
            throw new Exception("Only images are allowed.");
        }

        File photoFile = File.createTempFile("photo", photo.getOriginalFilename(), new File("public"));
        FileOutputStream fos = new FileOutputStream(photoFile);
        fos.write(photo.getBytes());

        Photo p = new Photo();
        p.setSender(senderUser);
        p.setRecipient(receiverUser);
        p.setFilename(photoFile.getName());
        p.setSeconds(time);
        p.setPublic(isPublic);
        photos.save(p);

        response.sendRedirect("/");

        return p;
    }

    @RequestMapping("/photos")
    public List<Photo> showPhotos(HttpSession session) throws Exception {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new Exception("Not logged in.");
        }

        User user = users.findFirstByName(username);
        return photos.findByRecipient(user);
    }

   public synchronized void deletePhoto(Photo photo){


    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        public void run() {

        }


    };
        timer.schedule(task, photo.getSeconds() * 10000);


}

public void delete(Photo photo){
        photos.delete(photo.getId());
        File photofile = new File("/public" + photo.getFilename());
        photos.delete(photo);
    }

    @RequestMapping(path = "/public-photos", method = RequestMethod.GET)
        public Iterable<Photo> json(HttpSession session) {
            String username = (String) session.getAttribute("userName");
            User sender = users.findFirstByName(username);
            return photos.findByIsPublic(sender);
        }


    }






