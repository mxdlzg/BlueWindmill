package project.mxdlzg.com.bluewindmill.model.entity;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class SCInfo {
    private int totalItems;
    private int totalPages;
    private int itemCountPerPage;
    private int remainder;

    private float[] scScore,scPresentation;
    private Map<String, String> urlKV;


    public SCInfo(int totalItems, int totalPages) {
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        itemCountPerPage = totalItems/totalPages;
        remainder = totalItems%totalPages;
    }

    public SCInfo(int count, int totalPages, float[] scScore, float[] scPresentation, Map<String, String> urlKV) {
        this.totalItems = count;
        this.totalPages = totalPages;
        this.scPresentation = scPresentation;
        this.scScore = scScore;
        this.urlKV = urlKV;
    }

    public SCInfo(int count, int totalPages, float[] scScore, float[] scPresentation) {
        this.totalItems = count;
        this.totalPages = totalPages;
        this.scPresentation = scPresentation;
        this.scScore = scScore;
    }

    public float getScScore(int index){
        if (scScore != null){
            return scScore[index];
        }
        return 0;
    }

    public String getUrl(String key){
        return urlKV.get(key);
    }

    public float getScPresentation(int index){
        if (scPresentation != null){
            return scPresentation[index];
        }
        return 0;
    }

    public int getRemainder() {
        return remainder;
    }

    public int getItemCountPerPage() {
        return itemCountPerPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getscPresentationText() {
        return String.format("%.2f(讲座报告)+%.2f(社会实践)+%.2f(创新创业创意)+%.2f(安全教育网络教学)+%.2f(其他)",
                scPresentation[0],
                scPresentation[1],
                scPresentation[2],
                scPresentation[3],
                scPresentation[4]);
    }
}
