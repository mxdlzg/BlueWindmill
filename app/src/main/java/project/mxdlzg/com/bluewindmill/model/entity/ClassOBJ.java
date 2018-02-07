package project.mxdlzg.com.bluewindmill.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 廷江 on 2017/3/12.
 */
public class ClassOBJ implements Parcelable {
    private int num;
    private String name;
    private String position;
    private String techer;
    private int start,end;
    private int day,index;
    private float score;
    private boolean multiplePos = false;
    private String[] weeks;
    private int color;

    public ClassOBJ(){
        this.num = -1;
    }

    public ClassOBJ(int num, String name,String techer,int day,int index,float score, boolean multiplePos) {
        this.num = num;
        this.name = name;
        this.techer = techer;
        this.day = day;
        this.index = index;
        this.score = score;
        this.multiplePos = multiplePos;
    }

    public ClassOBJ(int num, String name, String position, String techer, int start, int end, int day, int index, float score, boolean multiplePos, String[] weeks, int color) {
        this.num = num;
        this.name = name;
        this.position = position;
        this.techer = techer;
        this.start = start;
        this.end = end;
        this.day = day;
        this.index = index;
        this.score = score;
        this.multiplePos = multiplePos;
        this.weeks = weeks;
        this.color = color;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setTecher(String techer) {
        this.techer = techer;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setMultiplePos(boolean multiplePos) {
        this.multiplePos = multiplePos;
    }

    public String getWeek(int index){
        return weeks[index];
    }

    public void setWeek(int index,String value){
        this.weeks[index] = value;
    }

    public void setWeeks(String[] weeks) {
        this.weeks = weeks;
        this.position = "";
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String[] getWeeks() {
        return weeks;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getTecher() {
        return techer;
    }

    public boolean isMultiplePos() {
        return multiplePos;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getNum() {
        return num;
    }

    public float getScore() {
        return score;
    }

    public String getALL(){
        String result = "";
        result += (num+";");
        result += (name+";");
        result += (position+";");
        result += (techer+";");
        result += (start+";");
        result += (end+";");
        result += (day+";");
        result += (index+";");
        result += (score+";");
        result += (multiplePos+";");
        result += (color+"");
        return result;
    }

    public String getWeekString(){
        String result = "";
        for (int i = 0;i<weeks.length;i++){
            if (weeks[i] != null){
                result+=weeks[i];
                result+=",";
            }else {
                result+=",";
            }
        }
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.num);
        dest.writeString(this.name);
        dest.writeString(this.position);
        dest.writeString(this.techer);
        dest.writeInt(this.start);
        dest.writeInt(this.end);
        dest.writeInt(this.day);
        dest.writeInt(this.index);
        dest.writeInt(this.color);
        dest.writeFloat(this.score);
        dest.writeByte(this.multiplePos ? (byte) 1 : (byte) 0);
        dest.writeStringArray(this.weeks);
    }

    private ClassOBJ(Parcel in) {
        this.num = in.readInt();
        this.name = in.readString();
        this.position = in.readString();
        this.techer = in.readString();
        this.start = in.readInt();
        this.end = in.readInt();
        this.day = in.readInt();
        this.index = in.readInt();
        this.color = in.readInt();
        this.score = in.readFloat();
        this.multiplePos = in.readByte() != 0;
        this.weeks = in.createStringArray();
    }

    public static final Creator<ClassOBJ> CREATOR = new Creator<ClassOBJ>() {
        @Override
        public ClassOBJ createFromParcel(Parcel source) {
            return new ClassOBJ(source);
        }

        @Override
        public ClassOBJ[] newArray(int size) {
            return new ClassOBJ[size];
        }
    };
}
