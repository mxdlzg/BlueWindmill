package project.mxdlzg.com.bluewindmill.view.adapter;

import android.content.Context;
import project.mxdlzg.com.bluewindmill.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 廷江 on 2017/5/1.
 */

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder>{
    private List<String> exams;
    private Context context;
    private int thisWeek = 1;
    private int thisDay = 0;

    /**
     * @param exams list
     * @param context context
     */
    public ExamAdapter(List<String> exams, Context context) {
        this.exams = exams;
        this.context = context;
    }

    public void setExams(List<String> exams) {
        this.exams = exams;
    }

    public void setThisDay(int thisDay) {
        this.thisDay = thisDay;
    }

    public void setThisWeek(int thisWeek) {
        this.thisWeek = thisWeek;
    }

    /**
     * @param parent 父容器
     * @param viewType type
     * @return viewHolder（自定义）
     */
    @Override
    public ExamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_rcy_item,parent,false);
        return new ExamAdapter.ViewHolder(view);
    }

    /**
     * @param holder viewHolder
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(ExamAdapter.ViewHolder holder, int position) {
        String exam = exams.get(position);
        String[] info = exam.split(";");
        String[] smash_time = info[1].split(" ");
        int week = Integer.parseInt(smash_time[0].replace("第","").replace("周",""));
        int day = Integer.parseInt(smash_time[1].replace("星期",""));
        int incrementDays = (week-thisWeek-2)*7+(7-thisDay)+day;
        holder.name.setText(info[0]);
        holder.time.setText(info[1]);
        holder.place.setText(info[2]);
        if (incrementDays<0 || (week-thisWeek-1)<0){
            holder.remain_time.setText("已完成");
            holder.remain_time.setTextColor(context.getResources().getColor(R.color.green500));
        }else {
            holder.remain_time.setText(incrementDays+"天");
            holder.remain_time.setTextColor(context.getResources().getColor(R.color.blue500));
        }
    }

    /**
     * @return list size
     */
    @Override
    public int getItemCount() {
        return exams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,time,place,remain_time;

        /**
         * @param itemView cardView
         */
        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.exam_rcy_name);
            time = (TextView) itemView.findViewById(R.id.exam_rcy_time);
            place = (TextView) itemView.findViewById(R.id.exam_rcy_place);
            remain_time = (TextView) itemView.findViewById(R.id.exam_rcy_remainTime);
        }
    }

}
