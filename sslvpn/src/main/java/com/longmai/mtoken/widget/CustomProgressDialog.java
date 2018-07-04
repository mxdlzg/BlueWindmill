package com.longmai.mtoken.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {
    private static final String TAG = CustomProgressDialog.class.getName();
    private Context context;
    private boolean flag = true;

    public CustomProgressDialog(Context context, String message) {
        super(context, MResource.getIdByName(context, "style", "CustomProgressDialogStyle"));
        Log.d(TAG, "CustomProgressDialog() - message: " + message);
        this.context = context;
        setContentView(MResource.getIdByName(context, "layout", "customprogressdialog"));
        TextView tv_alert = (TextView) findViewById(MResource.getIdByName(context, "id", "tv_alert"));
        if (tv_alert != null) {
            tv_alert.setText(message);
        }
    }

    public CustomProgressDialog(Context context, int resid) {
        super(context, MResource.getIdByName(context, "style", "CustomProgressDialogStyle"));
        Log.d(TAG, "CustomProgressDialog() - resid: " + resid);
        this.context = context;
        setContentView(MResource.getIdByName(context, "layout", "customprogressdialog"));
        TextView tv_alert = (TextView) findViewById(MResource.getIdByName(context, "id", "tv_alert"));
        if (tv_alert != null) {
            tv_alert.setText(resid);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged() - hasFocus: " + hasFocus);
        if (!hasFocus) {
            dismiss();
        }
    }

    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        this.flag = flag;
    }

    public void show() {
        Log.d(TAG, "show()");
        super.show();
    }

    public void cancel() {
        Log.d(TAG, "cancel()");
        super.cancel();
    }

    public void dismiss() {
        Log.d(TAG, "dismiss()");
        super.dismiss();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != 0 || !isOutOfBounds(event) || !this.flag) {
            return false;
        }
        cancel();
        return true;
    }

    private boolean isOutOfBounds(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int slop = ViewConfiguration.get(this.context).getScaledWindowTouchSlop();
        View decorView = getWindow().getDecorView();
        return x < (-slop) || y < (-slop) || x > decorView.getWidth() + slop || y > decorView.getHeight() + slop;
    }
}
