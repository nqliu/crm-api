package com.crm.service;

/**
 * @author alani
 */
public interface EmailService {
    /**
     * 发送简单邮件
     */
    void sendSimpleMail(String to, String subject, String content);
}