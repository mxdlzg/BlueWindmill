package project.mxdlzg.com.bluewindmill.view.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.view.activity.ScDetailActivity;

/**
 * Created by mxdlzg on 18-2-8.
 */

public class ScRcyAdapter extends BaseQuickAdapter<SCActivityDetail,BaseViewHolder> {
    private Context context;

    public ScRcyAdapter(@Nullable List<SCActivityDetail> data, final Context context) {
        super(R.layout.second_child_rcy_item,data);
        this.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        this.isFirstOnly(true);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, SCActivityDetail item) {
        helper.setText(R.id.sc_child_rcy_label, Html.fromHtml(item.getLabel()))
                .setText(R.id.sc_child_rcy_WeekTime,item.getWeekTime())
                .setText(R.id.sc_child_rcy_Time,item.getTime())
                .setText(R.id.sc_child_rcy_TitleContent,item.getTitle());
        helper.setTag(R.id.sc_child_rcy_headerView,item);
        //helper.getView(R.id.sc_child_rcy_headerView).setBackgroundColor(mContext.getResources().getColor(R.color.pink400));
    }
}
