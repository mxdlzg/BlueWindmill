package android.mxdlzg.com.bluewindmill.model.entity;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class UnifiedScore {
    private String score,passed,kind,batch;

    public UnifiedScore(String score, String passed, String kind, String batch) {
        this.score = score;
        this.passed = passed;
        this.kind = kind;
        this.batch = batch;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPassed() {
        return passed;
    }

    public void setPassed(String passed) {
        this.passed = passed;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
