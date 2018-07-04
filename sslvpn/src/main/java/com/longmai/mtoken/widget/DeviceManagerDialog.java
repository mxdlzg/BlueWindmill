package com.longmai.mtoken.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DeviceManagerDialog extends Dialog implements android.view.View.OnClickListener {
    private Button dialog_action_bind;
    private Button dialog_action_cancel;
    private Button dialog_action_unbind;
    private TextView dialog_text;
    private TextView dialog_title;
    private boolean flag = true;
    private OnClickListener listener;
    private int position;
    private CharSequence text;
    private CharSequence title;

    public interface OnClickListener {
        void onClick(View view, int i);
    }

    public DeviceManagerDialog(Context context, int position) {
        super(context, MResource.getIdByName(context, "style", "dialog_switch"));
        this.position = position;
    }

    public DeviceManagerDialog(Context context, int position, boolean flag) {
        super(context, MResource.getIdByName(context, "style", "dialog_switch"));
        this.position = position;
        this.flag = flag;
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(MResource.getIdByName(getContext(), "layout", "device_manager_dialog_layout"));
        this.dialog_action_bind = (Button) findViewById(MResource.getIdByName(getContext(), "id", "dialog_action_bind"));
        this.dialog_action_unbind = (Button) findViewById(MResource.getIdByName(getContext(), "id", "dialog_action_unbind"));
        this.dialog_action_cancel = (Button) findViewById(MResource.getIdByName(getContext(), "id", "dialog_action_cancel"));
        this.dialog_action_bind.setOnClickListener(this);
        if (this.flag) {
            this.dialog_action_bind.setVisibility(0);
        } else {
            this.dialog_action_bind.setVisibility(8);
        }
        this.dialog_action_unbind.setOnClickListener(this);
        this.dialog_action_cancel.setOnClickListener(this);
        this.dialog_title = (TextView) findViewById(MResource.getIdByName(getContext(), "id", "dialog_title"));
        this.dialog_title.setText(this.title);
        this.dialog_text = (TextView) findViewById(MResource.getIdByName(getContext(), "id", "dialog_text"));
        this.dialog_text.setText(this.text);
        setCanceledOnTouchOutside(true);
        Window localWindow = getWindow();
        localWindow.setBackgroundDrawableResource(MResource.getIdByName(getContext(), "drawable", "guide_bg"));
        localWindow.getAttributes().width = -1;
        localWindow.setGravity(80);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setTitle(int titleId) {
        this.title = getContext().getString(titleId);
    }

    public void setMessage(CharSequence text) {
        this.text = text;
    }

    public void setMessage(int resid) {
        this.text = getContext().getString(resid);
    }

    public void onClick(View view) {
        if (view.getId() == MResource.getIdByName(getContext(), "id", "dialog_action_cancel")) {
            dismiss();
        }
        if (this.listener != null) {
            this.listener.onClick(view, this.position);
        }
    }
}
