package szu.wifichat.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import szu.wifichat.android.R;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.groundbeen.Device;
import szu.wifichat.android.interfaces.IOnItemClickListener;

public class GroundAffirmAdapter extends CommonAdapter<Device> {

    private IOnItemClickListener iOnItemClickListener;

    public void setIOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
    }

    public GroundAffirmAdapter(Context context, List<Device> list) {
        super(context, list);
    }

    @Override
    public View mGetView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_step_1, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //没有设备的话就是步骤标题
        if (list.get(position).isTitle()) {
            if (list.get(position).getStepId() == 0 && list.get(position).isCanSendAffirm()) {
                holder.btnSendGroundAffirm.setVisibility(View.VISIBLE);
                holder.btnSendGroundAffirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iOnItemClickListener != null) {
                            iOnItemClickListener.onItemClick(v, position, null);
                        }
                    }
                });
            } else {
                holder.btnSendGroundAffirm.setVisibility(View.GONE);
            }
            holder.vLing.setVisibility(View.GONE);
            holder.llStepDevice.setVisibility(View.GONE);
            holder.llStep.setVisibility(View.VISIBLE);
            holder.tvStepName.setText(list.get(position).getName());
            int state = list.get(position).getState();
            if (state == WorkEnum.StepState.NotExecutable.getState()) {
//                holder.ivStepState.setImageResource(R.drawable.icon_no_select);
                switch (position) {
                    case 0:
                        holder.ivStepState.setImageResource(R.drawable.icon_step1_no_select);
                        break;
                    default:
                        break;
                }
            } else if (state == WorkEnum.StepState.WnderWay.getState()) {
//                holder.ivStepState.setImageResource(R.drawable.icon_select_step);
                switch (position) {
                    case 0:
                        holder.ivStepState.setImageResource(R.drawable.icon_step1_select);
                        break;
                    default:
                        break;
                }
            } else if (state == WorkEnum.StepState.Finish.getState()) {
                holder.ivStepState.setImageResource(R.drawable.icon_finish);
            }
        }
        //这里是设备
        else {
            holder.vLing.setVisibility(View.GONE);
            holder.llStepDevice.setVisibility(View.VISIBLE);
            holder.llStep.setVisibility(View.GONE);
            holder.tvDeviceName.setText(list.get(position).getName());
            holder.tvKM.setText("(" + list.get(position).getKM() + ")");
            holder.tvGroundPosition.setText("接挂位置：" + list.get(position).getGroundPosition());
            int deviceState = list.get(position).getState();
            if (deviceState == WorkEnum.DeviceState.Normal.getState()) {
                holder.ivDeviceState.setImageResource(0);
            } else if (deviceState == WorkEnum.DeviceState.WnderWay.getState()) {
                holder.ivDeviceState.setImageResource(R.drawable.icon_proceed);
            } else if (deviceState == WorkEnum.DeviceState.Finish.getState()) {
                holder.ivDeviceState.setImageResource(R.drawable.icon_finish);
            } else if (deviceState == WorkEnum.DeviceState.Error.getState()) {
                holder.ivDeviceState.setImageResource(R.drawable.icon_warning);
            }
//            holder.itemView.setOnClickListener(new StepAdapter1.RVOnClickListener(position));
        }
        return convertView;
    }

    private static class ViewHolder {
        //        @BindView(R.id.iv_step_state)
        ImageView ivStepState;
        //        @BindView(R.id.tv_step_name)
        TextView tvStepName;
        //        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        //        @BindView(R.id.iv_device_state)
        ImageView ivDeviceState;
        //        @BindView(R.id.ll_step)
        LinearLayout llStep;
        //        @BindView(R.id.ll_step_device)
        LinearLayout llStepDevice;
        //        @BindView(R.id.v_step_ling)
        TextView vLing;

        TextView tvKM;

        TextView tvGroundPosition;

        Button btnSendGroundAffirm;

        public ViewHolder(View convertView) {
//            ButterKnife.bind(this, convertView);
            ivStepState = (ImageView) convertView.findViewById(R.id.iv_step_state);
            tvStepName = (TextView) convertView.findViewById(R.id.tv_step_name);
            tvDeviceName = (TextView) convertView.findViewById(R.id.tv_device_name);
            ivDeviceState = (ImageView) convertView.findViewById(R.id.iv_device_state);
            llStep = (LinearLayout) convertView.findViewById(R.id.ll_step);
            llStepDevice = (LinearLayout) convertView.findViewById(R.id.ll_step_device);
            vLing = (TextView) convertView.findViewById(R.id.v_step_ling);
            btnSendGroundAffirm = (Button) convertView.findViewById(R.id.btn_send_ground_affirm);
            tvKM = (TextView) convertView.findViewById(R.id.tv_device_km);
            tvGroundPosition = (TextView) convertView.findViewById(R.id.tv_device_ground_position);
        }
    }

}
