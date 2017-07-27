package com.aapkatrade.buyer.chat;

/**
 * Created by John on 8/29/2016.
 */
public class ChatDatas {
    String message, name, fcm_token, email;
    Long timestamp;
    boolean you;

    public ChatDatas(String message, String name, Long timestamp, boolean you) {


        this.message = message;
        this.you = you;
        this.name = name;
        this.timestamp = timestamp;

    }


}
