package szu.wifichat.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import szu.wifichat.android.R;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.groundbeen.Step;
import szu.wifichat.android.groundbeen.StepAndDevice;
import szu.wifichat.android.interfaces.IOnItemClickListener;

/**
 * Created by huangqj on 2017-06-13.
 */

public class StepAdapter1 extends RecyclerView.Adapter<StepAdapter1.StepHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<StepAndDevice> mStep;
    private IOnItemClickListener mIOnItemClickListener;

    public void setIOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.mIOnItemClickListener = iOnItemClickListener;
    }

    public StepAdapter1(Context context, ArrayList<StepAndDevice> steps) {
        this.mContext = context;
        this.mStep = steps;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_step_1, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {
        //没有设备的话就是步骤标题
        if (mStep.get(position).getDevice() == null) {
            holder.vLing.setVisibility(View.GONE);
            holder.llStepDevice.setVisibility(View.GONE);
            holder.llStep.setVisibility(View.VISIBLE);
            holder.tvStepName.setText(mStep.get(position).getStep().getName());
            int state = mStep.get(position).getStep().getState();
            if (state == WorkEnum.StepState.NotExecutable.getState()) {
                holder.ivStepState.setImageResource(R.drawable.icon_no_select);
            } else if (state == WorkEnum.StepState.WnderWay.getState()) {
                holder.ivStepState.setImageResource(R.drawable.icon_select_step);
            } else if (state == WorkEnum.StepState.NotExecutable.getState()) {
                holder.ivStepState.setImageResource(R.drawable.icon_finish);
            }
        }
        //这里是设备
        else {
            holder.vLing.setVisibility(View.VISIBLE);
            holder.llStepDevice.setVisibility(View.VISIBLE);
            holder.llStep.setVisibility(View.GONE);
            holder.tvDeviceName.setText(mStep.get(position).getDevice().getName());
            int deviceState = mStep.get(position).getDevice().getState();
            if (deviceState == WorkEnum.DeviceState.Normal.getState()) {
                holder.ivDeviceState.setImageResource(0);
            } else if (deviceState == WorkEnum.DeviceState.WnderWay.getState()) {
                holder.ivDeviceState.setImageResource(R.drawable.icon_proceed);
            } else if (deviceState == WorkEnum.DeviceState.Finish.getState()) {
                holder.ivDeviceState.setImageResource(R.drawable.icon_finish);
            } else if (deviceState == WorkEnum.DeviceState.Error.getState()) {
                holder.ivDeviceState.setImageResource(R.drawable.icon_warning);
            }
            holder.itemView.setOnClickListener(new RVOnClickListener(position));
        }
    }

    @Override
    public int getItemCount() {
        return mStep != null ? mStep.size() : 0;
    }

    public static class StepHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_step_state)
        ImageView ivStepState;
        //        @BindView(R.id.tv_step_par)
//        TextView tvPar;
        @BindView(R.id.tv_step_name)
        TextView tvStepName;
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.iv_device_state)
        ImageView ivDeviceState;
        @BindView(R.id.ll_step)
        LinearLayout llStep;
        @BindView(R.id.ll_step_device)
        LinearLayout llStepDevice;
        @BindView(R.id.v_step_ling)
        TextView vLing;

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
