package project.mxdlzg.com.bluewindmill.view.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.intrusoft.squint.DiagonalView;

import java.util.List;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;

/**
 * Created by mxdlzg on 18-3-16.
 */

public class ScoreRcyAdapter extends BaseMultiItemQuickAdapter<ScoreOBJ,BaseViewHolder> {
    public ScoreRcyAdapter(@Nullable List<ScoreOBJ> data) {
        super(data);
        addItemType(ScoreOBJ.EVALUATED,R.layout.score_rcy_item);
        addItemType(ScoreOBJ.NOT_EVALUATED,R.layout.score_rcy_empty_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreOBJ item) {
        switch (helper.getItemViewType()){
            case ScoreOBJ.EVALUATED:
                helper.setText(R.id.score_tv_itemName,item.getName())
                        .setText(R.id.score_tv_item_total_score,item.getTotal())
                        .setText(R.id.score_tv_item_peaceScore,setColor("平时：",item.getRegular()))
                        .setText(R.id.score_tv_item_middleScore,setColor("期中：",item.getMid()))
                        .setText(R.id.score_tv_item_finalScore,setColor("期末：",item.getFloatFinl()))
                        .setText(R.id.score_tv_item_secondFinalScore,setColor("二考：",item.getSe()))
                        .setText(R.id.score_tv_item_secondTotalScore,setColor("二考总评：",item.getSeTotal()));
                if (item.getFloatTotal() < 60){
                    DiagonalView view = helper.<DiagonalView>getView(R.id.score_item_diagonal_view);
                    view.setSolidColor(Color.parseColor("#ff5252"));
                }
                break;
                case ScoreOBJ.NOT_EVALUATED:
                    helper.setText(R.id.score_tv_itemName,item.getName())
                            .setText(R.id.score_tv_item_total_score,item.getTotal())
                            .setText(R.id.score_tv_item_peaceScore,setColor("平时：",item.getRegular()))
                            .setText(R.id.score_tv_item_middleScore,setColor("期中：",item.getMid()))
                            .setText(R.id.score_tv_item_finalScore,setColor("期末：",item.getFloatFinl()))
                            .setText(R.id.score_tv_item_secondFinalScore,setColor("二考：",item.getSe()))
                            .setText(R.id.score_tv_item_secondTotalScore,setColor("二考总评：",item.getSeTotal()));
                    break;
                    default:break;
        }
    }

    /**
     * set text color
     * @param title title
     * @param score score
     * @return colored text
     */
    private Spanned setColor(String title, float score){
        if (score == -1){
            return Html.fromHtml(title);
        }
        StringBuilder builder = new StringBuilder();
        if (score<60){
            builder.append("<font color='#e53935'>");
            builder.append(title);
            builder.append(score);
            builder.append("</font>");
        }else {
            builder.append(title);
            builder.append(score);
        }
        return Html.fromHtml(builder.toString());
    }
}
