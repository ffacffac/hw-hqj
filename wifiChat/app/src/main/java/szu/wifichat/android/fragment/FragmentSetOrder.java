package szu.wifichat.android.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import szu.wifichat.android.R;
import szu.wifichat.android.adapter.DeviceAdapter;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.groundbeen.Device;
import szu.wifichat.android.interfaces.IOnItemClickListener;
import szu.wifichat.android.view.DialogHelper;

public class FragmentSetOrder extends BaseFragment {

    @BindView(R.id.tv_device_group_title)
    TextView tvGroupTitle;
    @BindView(R.id.rv_ground_affirm)
    RecyclerView rvGroundAffirm;
    private ArrayList<Device> mDevices = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private int[] mStepID = {0, 1, 2, 3, 4};//步骤ID标识
    private String[] mDeviceName = {"L001#", "L002#", "L003#", "L004#", "L005#"};
    private String[] mDeviceLabel = {"E2004102651201810850C23D", "E20041026512014626300DAB",
            "E2003072020102661060AE73", "E20041026512003721803621"};

    @Override
    protected int getLayoutInt() {
        return R.layout.fragment_ground_affirm;
    }

    @Override
    protected void initView() {
        tvGroupTitle.setText("陈振兴/李俊龙---（0/4）");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvGroundAffirm.setLayoutManager(gridLayoutManager);
        deviceAdapter = new DeviceAdapter(getActivity(), mDevices);
        rvGroundAffirm.setAdapter(deviceAdapter);
        deviceAdapter.setIOnItemClickListener(new IOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                int deviceState = mDevices.get(position).getState();
                if (deviceState == WorkEnum.DeviceState.Normal.getState()) {
                    DialogHelper.getInstance(getActivity()).showToastShort("当前杆号不可操作");
                    return;
                }
//                showDeviceCheckDialog(position);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
