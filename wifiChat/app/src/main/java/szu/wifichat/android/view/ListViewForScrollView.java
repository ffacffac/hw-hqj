package szu.wifichat.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/3/3.
 * <p/>
 * listView嵌套在ScrollView里面出现的listview显示不全的问题的解决方案,用自定义的布局，重新计算
 */
public class ListViewForScrollView extends ListView
{

    public ListViewForScrollView(Context context)
    {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * listview重新计算
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);// 记得传过去
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_MOVE)
        {
            return true; // 禁止滑动
        }
        return super.dispatchTouchEvent(ev);
    }
}
