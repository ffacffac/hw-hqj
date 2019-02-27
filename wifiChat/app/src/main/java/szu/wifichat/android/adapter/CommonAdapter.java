package szu.wifichat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
    public List<T> list;
    public LayoutInflater inflater;
    public Context context;

    public CommonAdapter(Context context, List<T> list)
    {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return mGetView(position, convertView, parent);
    }

    public abstract View mGetView(int position, View convertView, ViewGroup parent);
}
