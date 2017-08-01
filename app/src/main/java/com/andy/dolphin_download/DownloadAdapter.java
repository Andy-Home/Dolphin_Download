package com.andy.dolphin_download;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.Task;

import java.util.List;

/**
 * 下载列表适配器
 * <p>
 * Created by Administrator on 2017/8/1.
 */

public class DownloadAdapter extends BaseAdapter {

    private List<Task> mTasks;

    public DownloadAdapter(List<Task> data) {
        mTasks = data;
    }

    @Override
    public int getCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    @Override
    public Object getItem(int position) {
        if (mTasks == null) {
            return null;
        }
        if ((mTasks.size() - 1) < position) {
            return null;
        }
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list, parent, false);
            viewHolder.oper = (ImageView) convertView.findViewById(R.id.oper);
            viewHolder.mProgress = (ProgressBar) convertView.findViewById(R.id.progress);
            viewHolder.oper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task task = mTasks.get(position);
                    if (task.getStatus() != Task.PAUSE) {
                        DownloadManager.stop(task);
                    } else {
                        DownloadManager.restart(task);
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Task task = mTasks.get(position);
        if (task.getStatus() == Task.PAUSE) {
            viewHolder.oper.setBackgroundResource(R.drawable.pause);
        } else {
            viewHolder.oper.setBackgroundResource(R.drawable.start);
        }
        viewHolder.mProgress.setProgress((int) (task.getPercent() * 100));
        return convertView;
    }

    class ViewHolder {
        ImageView oper;
        ProgressBar mProgress;
    }
}
