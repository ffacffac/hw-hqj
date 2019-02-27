package szu.wifichat.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import szu.wifichat.android.R;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.groundbeen.Step;
import szu.wifichat.android.interfaces.IOnItemClickListener;

/**
 * Created by huangqj on 2017-06-13.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Step> mStep;
    private IOnItemClickListener mIOnItemClickListener;

    public void setIOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.mIOnItemClickListener = iOnItemClickListener;
    }

    public StepAdapter(Context context, ArrayList<Step> steps) {
        this.mContext = context;
        this.mStep = steps;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_step, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {
        holder.tvName.setText(mStep.get(position).getName());
        int state = mStep.get(position).getState();
        if (state == WorkEnum.StepState.NotExecutable.getState()) {
            holder.ivState.setImageResource(R.drawable.icon_no_select);
            holder.tvPar.setBackgroundColor(mContext.getResources().getColor(R.color
                    .step_no_select));
        } else if (state == WorkEnum.StepState.WnderWay.getState()) {
            holder.ivState.setImageResource(R.drawable.icon_select_step);
            holder.tvPar.setBackgroundColor(mContext.getResources().getColor(R.color
                    .step_select));

        } else if (state == WorkEnum.StepState.NotExecutable.getState()) {
            holder.ivState.setImageResource(R.drawable.icon_finish);
            holder.tvPar.setBackgroundColor(mContext.getResources().getColor(R.color
                    .step_select));
        }
        //最后一个的要去掉进度条背景
        if (position == mStep.size() - 1) {
            holder.tvPar.setVisibility(View.INVISIBLE);
        } else {
            holder.tvPar.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new RVOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mStep != null ? mStep.size() : 0;
    }

    public static class StepHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_step_state)
        ImageView ivState;
        @BindView(R.id.tv_step_par)
        TextView tvPar;
        @BindView(R.id.tv_step_name)
        TextView tvName;

        public StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class RVOnClickListener implements View.OnClickListener {

        int positon;

        public RVOnClickListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onClick(View v) {
            if (mIOnItemClickListener != null) {
                mIOnItemClickListener.onItemClick(v, positon, mStep.get(positon));
            }
        }
    }
}
