package tweet;

import java.util.Date;

/**
 * Created by jakubinyi on 2017.04.05..
 */
public class Tweet {

    private String poster;
    private String content;
    private Date timestamp;
    private int id;

    public Tweet(String poster, String content) {
        this.poster = poster;
        this.content = content;
        this.timestamp = new Date();
    }

    public String getPoster() {
        return poster;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
