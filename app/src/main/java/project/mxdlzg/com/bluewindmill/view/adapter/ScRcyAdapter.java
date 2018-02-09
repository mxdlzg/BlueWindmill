package project.mxdlzg.com.bluewindmill.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;

/**
 * Created by mxdlzg on 18-2-8.
 */

public class ScRcyAdapter extends BaseQuickAdapter<SCActivityDetail,BaseViewHolder> {
    public ScRcyAdapter(@Nullable List<SCActivityDetail> data) {
        super(R.layout.second_child_rcy_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SCActivityDetail item) {
        helper.setText(R.id.sc_child_rcy_label,item.getLabel())
                .setText(R.id.sc_child_rcy_WeekTime,item.getWeekTime())
                .setText(R.id.sc_child_rcy_Time,item.getTime())
                .setText(R.id.sc_child_rcy_TitleContent,item.getTitle());
        helper.getView(R.id.sc_child_rcy_headerView).setBackgroundColor(mContext.getResources().getColor(R.color.pink400));
    }
}
