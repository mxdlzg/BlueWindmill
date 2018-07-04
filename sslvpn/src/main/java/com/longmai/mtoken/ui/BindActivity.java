package com.longmai.mtoken.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.longmai.mtoken.ui.base.BaseActivity;
import com.longmai.mtoken.widget.CustomProgressDialog;
import com.longmai.mtoken.widget.DeviceManagerDialog;
import com.longmai.mtoken.widget.DeviceManagerDialog.OnClickListener;
import com.longmai.mtoken.widget.MResource;
import com.longmai.security.plugin.SOF_DeviceLib;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindActivity extends BaseActivity implements OnClickListener, View.OnClickListener, OnItemClickListener {
    private DeviceManagerDialog dialog;
    private List<Map<String, Object>> list = new ArrayList();
    private SimpleAdapter mAdapter;
    private ListView mListView;

    class ScanDeviceAsyncTask extends AsyncTask<String, Integer, Integer> {
        Context context;
        List<String> devices = new ArrayList();
        Dialog progressDialog;

        public ScanDeviceAsyncTask(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            this.progressDialog = new CustomProgressDialog(this.context, "Please wait ... ");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            BindActivity.this.list.clear();
            BindActivity.this.mAdapter.notifyDataSetChanged();
        }

        protected Integer doInBackground(String... params) {
            SOF_DeviceLib.SOF_LoadLibrary(this.context, "0", "mToken K5", "com.longmai.security.plugin.driver.ble.BLEDriver", "BLE 蓝牙  Key", new String[0]);
            return Integer.valueOf(SOF_DeviceLib.SOF_EnumDevices(this.context, this.devices));
        }

        protected void onPostExecute(Integer result) {
            this.progressDialog.dismiss();
            if (result.intValue() == 0) {
                for (int i = 0; i < this.devices.size(); i++) {
                    Map<String, Object> map = new HashMap();
                    map.put("name", this.devices.get(i));
                    map.put("img", Integer.valueOf(MResource.getIdByName(BindActivity.this.getApplication(), "drawable", "icon_new")));
                    BindActivity.this.list.add(map);
                }
                BindActivity.this.mAdapter.notifyDataSetChanged();
                return;
            }
            Toast.makeText(this.context, "扫描失败，错误码:" + SOF_DeviceLib.SOF_GetLastError(), 1).show();
        }

        protected void onCancelled() {
            this.progressDialog.dismiss();
        }
    }

    protected void initWidget() {
        setContentView(MResource.getIdByName(getApplication(), "layout", "activity_bind_device"));
        setTitle((CharSequence) "设备绑定");
        setRightTitleImage(MResource.getIdByName(getApplication(), "drawable", "ico_add_uin"), this);
        findViewById(MResource.getIdByName(getApplication(), "id", "bind_device_manage")).setOnClickListener(this);
        this.mAdapter = new SimpleAdapter(this, this.list, MResource.getIdByName(getApplication(), "layout", "device_manage_item"), new String[]{"name", "img"}, new int[]{MResource.getIdByName(getApplication(), "id", "text_name"), MResource.getIdByName(getApplication(), "id", "img_alert")});
        this.mListView = (ListView) findViewById(MResource.getIdByName(getApplication(), "id", "device_manage_list"));
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
    }

    public void onClick(View view, int position) {
        if (view.getId() == MResource.getIdByName(getApplication(), "id", "dialog_action_bind")) {
            Editor editor = getSharedPreferences("setting", 0).edit();
            editor.putString("bind", ((Map) this.list.get(position)).get("name").toString());
            editor.commit();
            Toast.makeText(this, "设置成功", 0).show();
        } else if (view.getId() == MResource.getIdByName(getApplication(), "id", "dialog_action_unbind")) {
            this.list.remove(position);
            this.mAdapter.notifyDataSetChanged();
        }
        this.dialog.dismiss();
    }

    public void onClick(View view) {
        if (view.getId() == MResource.getIdByName(getApplication(), "id", "bind_device_manage")) {
            startActivity(new Intent(this, BindManageActivity.class));
        } else if (view.getId() == MResource.getIdByName(getApplication(), "id", "bar_right_layout")) {
            new ScanDeviceAsyncTask(this).execute(new String[0]);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
        this.dialog = new DeviceManagerDialog(this, position);
        this.dialog.setOnClickListener(this);
        this.dialog.setTitle((String) ((Map) this.list.get(position)).get("name"));
        this.dialog.setMessage((CharSequence) "您需要绑定此设备吗?");
        this.dialog.show();
    }
}
