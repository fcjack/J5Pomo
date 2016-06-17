package me.jackson.j5pomo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import me.jackson.j5pomo.dao.TaskDao;
import me.jackson.j5pomo.model.Task;

public class BoundService extends Service implements ServiceNotifier {

    public static final Integer TIME_DEFAULT_MINUTES = 25 * 60;

    private ListenValue obj;
    private IBinder binder;
    private boolean stop;
    private boolean isCountStarted;

    private TaskDao taskDao;

    public BoundService() {
        this.stop = false;
        this.isCountStarted = false;
        this.binder = new LocalBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startCounter(final Integer pomodoro, final Integer taskId) {
        if (!isCountStarted && pomodoro > 0) {
            isCountStarted = true;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Integer pomodoroCounter = pomodoro;
                    while (pomodoroCounter > 0 && !stop) {

                        Integer count = TIME_DEFAULT_MINUTES;
                        notifyValue(count);
                        while ((count > 0) && !stop) {
                            try {
                                count -= 1;
                                notifyValue(count);
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        if (count <= 0) {
                            updateTask(taskId);
                            pomodoroCounter--;
                        }
                    }

                    if (pomodoroCounter == 0) {
                        completeTask(taskId);
                    }
                    stop = false;
                    isCountStarted = false;
                }
            }).start();
        }
    }

    private void completeTask(Integer taskId) {
        Task task = taskDao.find(taskId);
        task.setCompleted(true);
        taskDao.update(task);

        obj.notifyCompleteTask(taskId);
    }

    private void updateTask(Integer taskId) {
        Task task = taskDao.find(taskId);
        Integer currentPomodoro = task.getPomodoro() - 1;
        task.setPomodoro(currentPomodoro);
        taskDao.update(task);

        obj.notifyEndPomodoro(taskId);
    }

    public void stopCounter() {
        this.stop = true;
        notifyValue(0);
    }

    public void closeService() {
        stopSelf();
    }

    @Override
    public void add(ListenValue obj) {
        this.obj = obj;
    }

    @Override
    public void notifyValue(Integer value) {
        Integer seconds = value % 60;
        Integer minutes = value / 60;
        obj.newValue(formatCounter(minutes, seconds));
    }

    private String formatCounter(Integer minutes, Integer seconds) {
        String minuteStr = minutes < 10 ? String.format("0%d:", minutes) : String.format("%d:", minutes);
        String secondsStr = seconds < 10 ? String.format("0%d", seconds) : String.format("%d", seconds);

        return minuteStr + secondsStr;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public class LocalBinder extends Binder {
        public BoundService getService() {
            return BoundService.this;
        }
    }
}
