package project.mxdlzg.com.bluewindmill.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.mxdlzg.com.bluewindmill.util.Util;

/**
 * Created by 廷江 on 2018/1/3.
 */

public class SCActivityDetail implements Parcelable{
    private String id,title,time;
    private String location,timeSpan,manager,managerPhone,host,organizer,content;
    private String startTime,cardStart,cardEnd;

    private StringBuilder label = new StringBuilder();
    private String weekTime;

    private static Pattern pattern = Pattern.compile("【.*?】");
    private static String[] colors = new String[]{"#18B781","2799E8","#8264BA","#797B7C"};

    public SCActivityDetail(String id, String title, String time) {
        this.id = id;
        this.title = title;
        this.time = time;

        int i = 0;
        Matcher matcher = pattern.matcher(title);
        while (matcher.find()){
            label.append("<font color='").append(colors[i]).append("'>").append(matcher.group()).append("</font>");
            this.title = this.title.replace(matcher.group(),"");
            if (i++ == colors.length){
                i = 0;
            }
        }
        weekTime = Util.getWeekTime(time);
    }

    public SCActivityDetail(String[] strings) {
        this.content = strings[0];
        this.location = strings[1];
        this.timeSpan = strings[2];
        this.manager = strings[3];
        this.managerPhone = strings[4];
        this.host = strings[5];
        this.organizer = strings[6];
        this.startTime = strings[7];
        this.cardStart = strings[8];
        this.cardEnd = strings[9];
    }

    public String getContent() {return content;}

    public String getLocation() {
        return location;
    }

    public String getTimeSpan() {
        return timeSpan;
    }

    public String getManager() {
        return manager;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public String getHost() {
        return host;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getCardStart() {
        return cardStart;
    }

    public String getCardEnd() {
        return cardEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {

        return label.toString();
    }

    public String getWeekTime() {
        return weekTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeString(this.location);
        dest.writeString(this.timeSpan);
        dest.writeString(this.manager);
        dest.writeString(this.managerPhone);
        dest.writeString(this.host);
        dest.writeString(this.organizer);
        dest.writeString(this.content);
        dest.writeString(this.startTime);
        dest.writeString(this.cardStart);
        dest.writeString(this.cardEnd);
        dest.writeSerializable(this.label);
        dest.writeString(this.weekTime);
    }

    protected SCActivityDetail(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.time = in.readString();
        this.location = in.readString();
        this.timeSpan = in.readString();
        this.manager = in.readString();
        this.managerPhone = in.readString();
        this.host = in.readString();
        this.organizer = in.readString();
        this.content = in.readString();
        this.startTime = in.readString();
        this.cardStart = in.readString();
        this.cardEnd = in.readString();
        this.label = (StringBuilder) in.readSerializable();
        this.weekTime = in.readString();
    }

    public static final Creator<SCActivityDetail> CREATOR = new Creator<SCActivityDetail>() {
        @Override
        public SCActivityDetail createFromParcel(Parcel source) {
            return new SCActivityDetail(source);
        }

        @Override
        public SCActivityDetail[] newArray(int size) {
            return new SCActivityDetail[size];
        }
    };
}
