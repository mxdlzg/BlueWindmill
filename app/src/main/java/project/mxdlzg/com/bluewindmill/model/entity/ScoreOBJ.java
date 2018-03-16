package project.mxdlzg.com.bluewindmill.model.entity;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class ScoreOBJ implements MultiItemEntity{
    public static final int EVALUATED = 1;
    public static final int NOT_EVALUATED = 2;

    //课程代码，名称，绩点，平时，期中，期末，总评，二考，二考总评
    private String lesionCode,name,finl,credit,total;
    private float regular,mid,se,seTotal;

    public ScoreOBJ(String lesionCode, String name, String credit, float regular, float mid, String finl, String total, float se, float seTotal) {
        this.lesionCode = lesionCode;
        this.name = name;
        this.finl = finl;
        this.credit = credit;
        this.total = total;
        this.regular = regular;
        this.mid = mid;
        this.se = se;
        this.seTotal = seTotal;
    }

    public ScoreOBJ(String lesionCode, String name, String credit, String regular, String mid, String finl, String total, String se, String seTotal) {
        this.lesionCode = lesionCode;
        this.name = name;
        this.finl = finl;
        this.credit = credit;
        this.total = total;
        this.regular = checkNull(regular);
        this.mid = checkNull(mid);
        this.se = checkNull(se);
        this.seTotal = checkNull(seTotal);
    }

    private float checkNull(String score) {
        return TextUtils.isEmpty(score)?-1:Float.valueOf(score);
    }

    public String getLesionCode() {
        return lesionCode;
    }

    public void setLesionCode(String lesionCode) {
        this.lesionCode = lesionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFinl() {
        return finl;
    }

    public void setFinl(String finl) {
        this.finl = finl;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public float getRegular() {
        return regular;
    }

    public void setRegular(float regular) {
        this.regular = regular;
    }

    public float getMid() {
        return mid;
    }

    public void setMid(float mid) {
        this.mid = mid;
    }

    public float getSe() {
        return se;
    }

    public void setSe(float se) {
        this.se = se;
    }

    public float getSeTotal() {
        return seTotal;
    }

    public void setSeTotal(float seTotal) {
        this.seTotal = seTotal;
    }

    @Override
    public int getItemType() {
        return total.equals("未评教")?NOT_EVALUATED:EVALUATED;
    }

    public float getFloatFinl() {
        if (finl.equals("未评教"))
        return -1;
        else
            return Float.valueOf(finl);
    }

    public float getFloatTotal() {
        return finl.equals("未评教")?-1:Float.valueOf(total);
    }
}
