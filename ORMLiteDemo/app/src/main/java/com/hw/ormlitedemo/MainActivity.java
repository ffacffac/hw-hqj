package com.hw.ormlitedemo;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hw.ormlitedemo.bean.User;
import com.hw.ormlitedemo.databinding.ActivityMainBinding;

import java.sql.SQLException;
import java.util.List;

/**
 * @author huangqj
 * @date 2019-07-26 09:53
 * 主页面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding binding;
    // private ViewModelFactory viewModelFactory;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // viewModelFactory = new ViewModelFactory();
        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance())
                                                                 .create(MainViewModel.class);
        binding.setModel(mainViewModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String name = binding.etName.getText().toString().trim();
                String pwd = binding.etPwd.getText().toString().trim();
                String address = binding.etAddress.getText().toString().trim();
                int age = Integer.parseInt(binding.etAge.getText().toString().trim());
                User user = new User(name, pwd, address, age);
                mainViewModel.insertUser(user);
                MyToast.showToast("保存成功");
                break;
            case R.id.btn_find:
                List<User> users = mainViewModel.findUserAll();
                binding.tvUserList.setText("");
                if (users != null) {
                    MyToast.showToast("查询成功");
                    for (User u : users) {
                        binding.tvUserList.append(u.toString());
                        binding.tvUserList.append("\n");
                    }
                }
                try {
                    List<User> userList = mainViewModel.userLinke();
                    Log.e(TAG, "onClick: 使用like查询" + userList.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_delete:

                break;
            case R.id.btn_update:
                try {
                    mainViewModel.update();
                } catch (SQLException e) {
                    Log.e(TAG, "onClick: ", e);
                }
                break;
            default:
                break;
        }
    }
}
