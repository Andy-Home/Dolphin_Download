package com.andy.dolphin_download;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.Task;

import java.util.List;

/**
 * 下载列表适配器
 * <p>
 * Created by Administrator on 2017/8/1.
 */

public class DownloadAdapter extends BaseAdapter {
    private final String TAG = "DownloadAdapter";
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
            viewHolder.percent = (TextView) convertView.findViewById(R.id.percent);
            viewHolder.remove = (ImageView) convertView.findViewById(R.id.remove);
            viewHolder.fileName = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.oper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task task = mTasks.get(position);
                    if (task.getStatus() == Task.PAUSE || task.getStatus() == Task.ERROR) {
                        Log.d(TAG, "重新开始下载任务：" + task.getKey());
                        DownloadManager.restart(task);
                    } else {
                        DownloadManager.stop(task);
                        Log.d(TAG, "暂停下载任务：" + task.getKey());
                    }
                }
            });
            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadManager.remove(mTasks.get(position));
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //避免复用时第一行的 oper 会 View.GONE
            viewHolder.oper.setVisibility(View.VISIBLE);
        }

        Task task = mTasks.get(position);
        if (task.getStatus() == Task.PAUSE) {
            viewHolder.oper.setBackgroundResource(R.drawable.start);
        } else if (task.getStatus() == Task.ERROR) {
            viewHolder.oper.setBackgroundResource(R.drawable.error);
        } else if (task.getStatus() == Task.FINISH) {
            viewHolder.oper.setVisibility(View.GONE);
        } else {
            viewHolder.oper.setBackgroundResource(R.drawable.pause);
        }

        int percent = (int) (task.getPercent() * 100);
        viewHolder.mProgress.setProgress(percent);
        viewHolder.percent.setText(percent + "%");
        viewHolder.fileName.setText(task.getFileName());
        return convertView;
    }

    private class ViewHolder {
        ImageView oper, remove;
        ProgressBar mProgress;
        TextView percent, fileName;
    }
}
