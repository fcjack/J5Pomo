package me.jackson.j5pomo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import me.jackson.j5pomo.R;
import me.jackson.j5pomo.adapter.TaskAdapter;
import me.jackson.j5pomo.dao.TaskDao;
import me.jackson.j5pomo.enums.ActivitiesEnum;
import me.jackson.j5pomo.model.Task;
import me.jackson.j5pomo.services.BoundService;
import me.jackson.j5pomo.services.ListenValue;

public class MainActivity extends AppCompatActivity implements ListenValue {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TextView timerText;

    private TaskDao taskDao;
    private List<Task> tasks;

    private boolean mIsServiceBound;
    private BoundService mBoundService;
    private Intent mBoundServiceIntent;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = (TextView) findViewById(R.id.timerText);

        taskDao = new TaskDao(this);
        tasks = taskDao.findAll();
        taskAdapter = new TaskAdapter(this, tasks);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(taskAdapter);

        mBoundServiceIntent = new Intent(this, BoundService.class);
        startService(mBoundServiceIntent);
        mHandler = new Handler();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tasks = taskDao.findAll();
        taskAdapter.setTasks(tasks);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsServiceBound) {
            unbindService(mServiceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsServiceBound) {
            mBoundService.closeService();
        }
    }

    @Override
    public void newValue(final String value) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                timerText.setText(value);
            }
        });
    }

    @Override
    public void notifyEndPomodoro(Integer taskId) {
        Intent it = new Intent("END_POMODORO");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, -1, it, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tasks = taskDao.findAll();
                taskAdapter.setTasks(tasks);
                taskAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void notifyCompleteTask(Integer taskId) {
        Intent it = new Intent("COMPLETE_POMODORO");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, -1, it, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, -1, pendingIntent);
    }

    public void startTask(View v) {
        Integer taskId = (Integer) v.getTag();
        Task task = taskDao.find(taskId);
        startCounter(task.getPomodoro(), task.getId());
    }

    public void completeTask(View v) {
        Integer taskId = (Integer) v.getTag();
        Task task = taskDao.find(taskId);

        stopCounter();
        task.setCompleted(true);

        taskDao.update(task);
        tasks = taskDao.findAll();
        taskAdapter.setTasks(tasks);
        taskAdapter.notifyDataSetChanged();

        RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
        int color = Color.parseColor("#13ae1b");
        relativeLayout.setBackgroundColor(color);

        Integer count = relativeLayout.getChildCount();
        Button button = (Button) relativeLayout.getChildAt(count - 2);
        button.setEnabled(false);
    }

    public void newTask(View v) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivityForResult(intent, ActivitiesEnum.NEW_TASK.getId());
    }

    private void startCounter(Integer minutes, Integer id) {
        if (mIsServiceBound) {
            mBoundService.startCounter(minutes, id);
        }
    }

    private void stopCounter() {
        if (mIsServiceBound) {
            mBoundService.stopCounter();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            mBoundService = binder.getService();
            mBoundService.add(MainActivity.this);
            mBoundService.setTaskDao(taskDao);
            mIsServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsServiceBound = false;
        }
    };
}
