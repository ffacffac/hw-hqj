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
import szu.wifichat.android.groundbeen.Device;
import szu.wifichat.android.interfaces.IOnItemClickListener;

/**
 * Created by huangqj on 2017-06-13.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.StepHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Device> mDevices;
    private IOnItemClickListener mIOnItemClickListener;

    public void setIOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.mIOnItemClickListener = iOnItemClickListener;
    }

    public DeviceAdapter(Context context, ArrayList<Device> devices) {
        this.mContext = context;
        this.mDevices = devices;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_device_ground, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {
//        StringBuffer sb = new StringBuffer();
//        sb.append(mDevices.get(position).getName()).append("(").append(mDevices.get(position)
//                .getKM()).append(")");
        holder.tvDeviceName.setText(mDevices.get(position).getName());
        holder.tvKM.setText("(" + mDevices.get(position).getKM() + ")");
        holder.tvGroundPosition.setText("接挂位置：" + mDevices.get(position).getGroundPosition());
        int deviceState = mDevices.get(position).getState();
        if (deviceState == WorkEnum.DeviceState.Normal.getState()) {
            holder.ivDeviceState.setImageResource(0);
        } else if (deviceState == WorkEnum.DeviceState.Finish.getState()) {
            holder.ivDeviceState.setImageResource(R.drawable.icon_finish);
        } else if (deviceState == WorkEnum.DeviceState.WnderWay.getState()) {
            holder.ivDeviceState.setImageResource(R.drawable.icon_proceed);
        } else if (deviceState == WorkEnum.DeviceState.Error.getState()) {
            holder.ivDeviceState.setImageResource(R.drawable.icon_warning);
        }
        holder.itemView.setOnClickListener(new RVOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mDevices != null ? mDevices.size() : 0;
    }

    public static class StepHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_device_name_state)
        LinearLayout llDeviceNameState;
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.iv_device_state)
        ImageView ivDeviceState;
        @BindView(R.id.tv_device_km)
        TextView tvKM;
        @BindView(R.id.tv_device_ground_position)
        TextView tvGroundPosition;

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
                mIOnItemClickListener.onItemClick(v, positon, mDevices.get(positon));
            }
        }
    }
}
