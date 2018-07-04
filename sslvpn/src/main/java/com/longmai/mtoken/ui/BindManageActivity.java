package com.longmai.mtoken.ui;

import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.longmai.mtoken.ui.base.BaseActivity;
import com.longmai.mtoken.widget.DeviceManagerDialog;
import com.longmai.mtoken.widget.DeviceManagerDialog.OnClickListener;
import com.longmai.mtoken.widget.MResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindManageActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private DeviceManagerDialog dialog;
    private List<Map<String, String>> list = new ArrayList();
    private SimpleAdapter mAdapter;
    private ListView mListView;

    protected void initWidget() {
        setContentView(MResource.getIdByName(getApplication(), "layout", "activity_bind_manage"));
        setTitle((CharSequence) "已绑定设备");
        this.mAdapter = new SimpleAdapter(this, this.list, MResource.getIdByName(getApplication(), "layout", "device_manage_item"), new String[]{"name"}, new int[]{MResource.getIdByName(getApplication(), "id", "text_name")});
        this.mListView = (ListView) findViewById(MResource.getIdByName(getApplication(), "id", "device_manage_list"));
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
        updata();
    }

    private void updata() {
        this.list.clear();
        String name = getSharedPreferences("setting", 0).getString("bind", "");
        if (!TextUtils.isEmpty(name)) {
            Map<String, String> map = new HashMap();
            map.put("name", name);
            this.list.add(map);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onClick(View view, int position) {
        if (view.getId() == MResource.getIdByName(getApplication(), "id", "dialog_action_unbind")) {
            Editor editor = getSharedPreferences("setting", 0).edit();
            editor.remove("bind");
            editor.commit();
            updata();
        }
        this.dialog.dismiss();
        Toast.makeText(this, "设置成功", 0).show();
    }

    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
        this.dialog = new DeviceManagerDialog(this, position, false);
        this.dialog.setOnClickListener(this);
        this.dialog.setTitle((CharSequence) ((Map) this.list.get(position)).get("name"));
        this.dialog.setMessage((CharSequence) "解绑后需要重新绑定后才能够扫描到");
        this.dialog.show();
    }
}
