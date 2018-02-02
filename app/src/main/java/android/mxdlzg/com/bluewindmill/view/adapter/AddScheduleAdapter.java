package android.mxdlzg.com.bluewindmill.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.model.config.Config;
import android.mxdlzg.com.bluewindmill.model.entity.TermOBJ;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 廷江 on 2017/3/26.
 */

public class AddScheduleAdapter extends RecyclerView.Adapter<AddScheduleAdapter.ViewHolder> {
    private List<TermOBJ> list;
    private Context context;

    /**
     * @param context context
     * @param list 课程表list
     */
    public AddScheduleAdapter(Context context, List<TermOBJ> list){
        this.context = context;
        this.list = list;
    }

    /**
     * @param parent parent
     * @param viewType type
     * @return 返回view
     */
    public AddScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_add_rcy_item,parent,false);
        return new ViewHolder(view);
    }

    /**
     * @param holder holder
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(final AddScheduleAdapter.ViewHolder holder, final int position) {
        TermOBJ termOBJ = list.get(position);
        String termName = termOBJ.getName();
        holder.name.setText(termName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("termOBJ",list.get(holder.getAdapterPosition()));
                ((AppCompatActivity)context).setResult(Config.ADD_SCHEDULE_OK,intent);
                ((AppCompatActivity)v.getContext()).finish();
            }
        });
    }

    /**
     * @return 返回list大小
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.schedule_add_rcy_card_name);
        }
    }

}
