package Agents;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Email implements Serializable {

    private String mailFrom;
    private String[] mailTo;
    private String header;
    private String dateAndTime;
    private String body;
    private boolean recent;

    public void setMailFrom(String mailFrom){
        this.mailFrom = mailFrom;
    }
    public void setMailTo(String[] mailTo){
        this.mailTo = mailTo;
    }
    public void setHeader(String header){
        this.header = header;
    }
    public void setDateAndTime(){
        this.dateAndTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    }
    public void setBody(String body){
        this.body = body;
    }
    public void setRecentStatus(boolean v){
        this.recent = v;
    }

    public String getMailFrom(){
        return this.mailFrom;
    }
    public String[] getMailTo(){
        return this.mailTo;
    }
    public String getHeader(){
        return this.header;
    }
    public String getDateAndTime(){
        return this.dateAndTime;
    }
    public String getBody(){
        return this.body;
    }
    public boolean getRecentStatus(){
        return this.recent;
    }
}