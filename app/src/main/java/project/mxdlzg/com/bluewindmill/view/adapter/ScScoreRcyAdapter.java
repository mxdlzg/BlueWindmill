package project.mxdlzg.com.bluewindmill.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.SCScoreDetail;

/**
 * Created by mxdlzg on 18-3-18.
 */

public class ScScoreRcyAdapter extends BaseQuickAdapter<SCScoreDetail,BaseViewHolder>{
    public ScScoreRcyAdapter(int layoutResId, @Nullable List<SCScoreDetail> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SCScoreDetail item) {
        helper.setText(R.id.sc_score_item_text,item.getActivity()+item.getRemark())
                .setText(R.id.sc_score_tv_item_total_score,item.getScore());
    }
}
