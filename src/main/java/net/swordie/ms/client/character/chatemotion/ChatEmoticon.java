package net.swordie.ms.client.character.chatemotion;

import jakarta.persistence.*;
import net.swordie.ms.util.container.Tuple;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emoticon")
public class ChatEmoticon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int charid;
    private int emoticonid;
    private long time;

    @Transient
    private List<Tuple<Integer, Short>> bookmarks = new ArrayList<>();

    public ChatEmoticon(int charid, int emoticonid, long time, String bookMarkss) {
        this.charid = charid;
        this.emoticonid = emoticonid;
        this.time = time;
        if (bookMarkss != null) {
            String[] bookMarks = bookMarkss.split(",");
            if (bookMarks.length > 2) {
                for (int i = 0; i < bookMarks.length; i += 2) {
                    this.bookmarks.add(new Tuple<>(Integer.valueOf(Integer.parseInt(bookMarks[i])), Short.valueOf(Short.parseShort(bookMarks[i + 1]))));
                }
            }
        }
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

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Tuple<Integer, Short>> getBookmarks() {
        return this.bookmarks;
    }

    public String getBookmark() {
        StringBuilder sb = new StringBuilder();
        for (Tuple<Integer, Short> a : this.bookmarks)
            sb.append(a.getLeft()).append(",").append(a.getRight()).append(",");
        if (sb.toString().contains(","))
            sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    public void setBookmarks(List<Tuple<Integer, Short>> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
