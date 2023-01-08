package net.swordie.ms.client.character.chatemotion;

import jakarta.persistence.*;
import net.swordie.ms.util.container.Tuple;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emoticon_saved")
public class ChatEmoticonSaved {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int charid;
    private int emoticonid;
    private String chat;

    public ChatEmoticonSaved(int charid, int emoticonid, String chat) {
        this.charid = charid;
        this.emoticonid = emoticonid;
        this.chat = chat;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCharid() {
        return this.charid;
    }

    public void setCharid(int charid) {
        this.charid = charid;
    }

    public int getEmoticonid() {
        return this.emoticonid;
    }

    public void setEmoticonid(int emoticonid) {
        this.emoticonid = emoticonid;
    }

    public String getChat() {
        return this.chat;
    }

    public void setChat(String text) {
        this.chat = text;
    }

}
