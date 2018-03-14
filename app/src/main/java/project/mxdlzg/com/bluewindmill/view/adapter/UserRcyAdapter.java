package project.mxdlzg.com.bluewindmill.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.UserRecyclerItem;

/**
 * Created by mxdlzg on 18-3-14.
 */

public class UserRcyAdapter extends BaseQuickAdapter<UserRecyclerItem,BaseViewHolder> {
    public UserRcyAdapter(@Nullable List<UserRecyclerItem> data) {
        super(R.layout.user_rcy_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserRecyclerItem item) {
        helper.setText(R.id.user_rcy_item_name,item.name)
                .setText(R.id.user_rcy_item_detail,item.detail)
                .setImageResource(R.id.user_rcy_item_icon,item.icon)
                .setTag(R.id.user_rcy_item,item.intent);

    }
}
