package co.sdslabs.mdg.ndnmail;

public class Mail {

    private int id;

    private String sender;
    private String receiver;

    private String subject;
    private long date;

    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiver() {
        return sender;
    }

    public void setReceiver(String receiver) {
        this.sender = receiver;
    }

    public String getSender() {
        return receiver;
    }

    public void setSender(String to) {
        this.receiver = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
