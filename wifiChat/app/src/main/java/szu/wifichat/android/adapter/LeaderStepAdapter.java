package szu.wifichat.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import szu.wifichat.android.R;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.groundbeen.Step;

public class LeaderStepAdapter extends CommonAdapter<Step> {

    private int selected = 0;// 默认选中第一个步骤

    public LeaderStepAdapter(Context context, List<Step> list) {
        super(context, list);
    }

    @Override
    public View mGetView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_step_leader, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvStateName.setText(list.get(position).getName());
        int state = list.get(position).getState();
        if (state == WorkEnum.StepState.NotExecutable.getState()) {
            holder.tvStatePar.setBackgroundColor(context.getResources().getColor(R.color
                    .step_no_select));
            switch (position) {
                case 0:
                    holder.ivStepState.setImageResource(R.drawable.icon_step1_no_select);
                    break;
                case 1:
                    holder.ivStepState.setImageResource(R.drawable.icon_step2_no_select);
                    break;
                case 2:
                    holder.ivStepState.setImageResource(R.drawable.icon_step3_no_select);
                    break;
                case 3:
                    holder.ivStepState.setImageResource(R.drawable.icon_step4_no_select);
                    break;
                case 4:
                    holder.ivStepState.setImageResource(R.drawable.icon_step5_no_select);
                    break;
                default:
                    break;
            }

        } else if (state == WorkEnum.StepState.WnderWay.getState()) {
//            holder.ivStepState.setImageResource(R.drawable.icon_select_step);
            holder.tvStatePar.setBackgroundColor(context.getResources().getColor(R.color
                    .step_select));

            switch (position) {
                case 0:
                    holder.ivStepState.setImageResource(R.drawable.icon_step1_select);
                    break;
                case 1:
                    holder.ivStepState.setImageResource(R.drawable.icon_step2_select);
                    break;
                case 2:
                    holder.ivStepState.setImageResource(R.drawable.icon_step3_select);
                    break;
                case 3:
                    holder.ivStepState.setImageResource(R.drawable.icon_step4_select);
                    break;
                case 4:
                    holder.ivStepState.setImageResource(R.drawable.icon_step5_select);
                    break;
                default:
                    break;
            }

        } else if (state == WorkEnum.StepState.Finish.getState()) {
            holder.tvStatePar.setBackgroundColor(context.getResources().getColor(R.color
                    .step_select));
            holder.ivStepState.setImageResource(R.drawable.icon_finish);
        }
        //最后一个的要去掉进度条背景
        if (position == list.size() - 1) {
            holder.tvStatePar.setVisibility(View.INVISIBLE);
        } else {
            holder.tvStatePar.setVisibility(View.VISIBLE);
        }

        if (position == selected) {
            holder.tvStateName.setBackgroundResource(R.drawable.bg_leader_step_select);
        } else {
            if (state == WorkEnum.StepState.NotExecutable.getState()) {
                holder.tvStateName.setBackgroundResource(R.drawable.bg_leader_step_no_operation);
            } else {
                holder.tvStateName.setBackgroundResource(R.drawable.bg_leader_step_normal);
            }
        }

        return convertView;
    }

    private void setStepStateIcon(ViewHolder holder, int position, int iconSrc) {
        switch (position) {
            case 0:
                holder.ivStepState.setImageResource(R.drawable.icon_step1_no_select);
                break;
            case 1:
                holder.ivStepState.setImageResource(R.drawable.icon_step2_no_select);
                break;
            case 2:
                holder.ivStepState.setImageResource(R.drawable.icon_step3_no_select);
                break;
            case 3:
                holder.ivStepState.setImageResource(R.drawable.icon_step4_no_select);
                break;
            case 4:
                holder.ivStepState.setImageResource(R.drawable.icon_step5_no_select);
                break;
            default:
                break;
        }
    }

    /**
     * 单项刷新状态
     *
     * @param position
     */
//    public void updateItem(GridView listView, int position)
//    {
//        int visiblePosition = listView.getFirstVisiblePosition();
//        if (position - visiblePosition >= 0)
//        {
//            View view = listView.getChildAt(position - visiblePosition);
//            ViewHolder vh = (ViewHolder) view.getTag();
//            vh.ivState = (ImageView) view.findViewById(R.id.iv_bug_manager_state);
//            BugDispose bug = list.get(position);
//            // 销缺状态
//            if (bug.getBugState() == Eenum.BugDisposeState.Disposed.getValue())
//            {
//                vh.ivState.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                vh.ivState.setVisibility(View.GONE);
//            }
//        }
//    }
    public void setItemBacView(int position) {
        selected = position;
        notifyDataSetChanged();
//        int visiblePosition = listView.getFirstVisiblePosition();
//        if (position - visiblePosition >= 0) {
//            View view = listView.getChildAt(position - visiblePosition);
//            ViewHolder vh = (ViewHolder) view.getTag();
//            if (position == selected) {
//                view.setBackgroundColor(context.getResources().getColor(R.color
//                        .bg_module_rb_selected));
//            } else {
//                view.setBackgroundColor(context.getResources().getColor(R.color
//                        .bg_module_rb_normal));
//            }
//        }
    }

    private static class ViewHolder {

        ImageView ivStepState;
        TextView tvStatePar;
        TextView tvStateName;

        public ViewHolder(View convertView) {
            ivStepState = (ImageView) convertView.findViewById(R.id.iv_step_leader_state);
            tvStatePar = (TextView) convertView.findViewById(R.id.tv_step_leader_par);
            tvStateName = (TextView) convertView.findViewById(R.id.tv_step_leader_name);
        }
    }
}
