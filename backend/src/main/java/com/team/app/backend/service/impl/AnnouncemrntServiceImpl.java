package com.team.app.backend.service.impl;

import com.team.app.backend.persistance.dao.AnnouncementDao;
import com.team.app.backend.persistance.dao.NotificationDao;
import com.team.app.backend.persistance.dao.UserActivityDao;
import com.team.app.backend.persistance.dao.UserDao;
import com.team.app.backend.persistance.model.Announcement;
import com.team.app.backend.persistance.model.Notification;
import com.team.app.backend.persistance.model.UserActivity;
import com.team.app.backend.service.AnnouncementService;
import com.team.app.backend.service.NotificationService;
import com.team.app.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;


@Service

public class AnnouncemrntServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;

    @Autowired
    private UserActivityDao userActivityDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    MessageSource messageSource;

    @Transactional
    public void createAnnouncement(Announcement announcement) {

        announcement.setCategoryId(1L);
        long millis=System.currentTimeMillis();
        java.sql.Timestamp date = new java.sql.Timestamp(millis);
        announcement.setDate(date);

        UserActivity userActivity=new UserActivity();
        userActivity.setCategoryId(4L);
        userActivity.setDate(new java.sql.Timestamp(millis));
        userActivity.setUserId(announcement.getUserId());
        userActivity.setElem_id(announcement.getId());
        //userActivity.setText(String.format("%s created announcement titled \"%s\"",userDao.get(announcement.getUserId()).getUsername(),announcement.getTitle()));

        userActivityDao.create(userActivity);

        announcementDao.create(announcement);
    }

    @Override
    public List<Announcement> getCreated() {
        return announcementDao.getCreated();
    }


    @Override
    public List<Announcement> getAll() {
        return announcementDao.getAll();
    }

    @Override
    public void approve(Announcement announcement) {
        Notification notification = new Notification();
        notification.setCategoryId(1L);
        notification.setUserId(announcement.getUserId());
        String[] params = new String[]{announcement.getTitle()};
        if(announcement.getStatusId() == 2) {
            announcementDao.approve(announcement.getId());
            notification.setText(messageSource.getMessage("announcement.approved", params, userService.getUserLanguage(announcement.getUserId())));
        } else {
            notification.setText(messageSource.getMessage("announcement.not.approved", params, userService.getUserLanguage(announcement.getUserId())));
            announcementDao.delete(announcement.getId());
        }
        notificationService.create(notification);
    }

    public Announcement  getAnnouncement(Long id) {
       return new Announcement();
    }
    @Transactional
    public void updateAnnouncement(Announcement announcement) {
        announcementDao.update(announcement);
    }
    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementDao.delete(id);
    }

}
