package project.mxdlzg.com.bluewindmill.model.entity;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by mxdlzg on 18-3-14.
 */

public class UserRecyclerItem {
    public String name,detail;
    public int icon;
    public Intent intent;

    public UserRecyclerItem(String name, String detail, int icon,Intent intent) {
        this.name = name;
        this.detail = detail;
        this.icon = icon;
        this.intent = intent;
    }
}
