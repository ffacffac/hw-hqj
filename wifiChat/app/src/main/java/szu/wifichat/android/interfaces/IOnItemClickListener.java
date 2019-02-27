package szu.wifichat.android.interfaces;

import android.view.View;

/**
 * Created by huangqj on 2016/5/12.
 */
public interface IOnItemClickListener<T> {
    void onItemClick(View view, int position, T data);
}
