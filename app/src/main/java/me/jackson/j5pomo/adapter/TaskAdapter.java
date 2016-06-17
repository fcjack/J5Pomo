package me.jackson.j5pomo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import me.jackson.j5pomo.R;
import me.jackson.j5pomo.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private LayoutInflater inflater;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_item, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskNameText.setText(task.getName());
        holder.taskDescText.setText(task.getDescription());
        holder.taskPomodoroText.setText(String.format("Pomodoros: %d", task.getPomodoro()));
        holder.taskStartBtn.setTag(task.getId());
        holder.taskCompleteBtn.setTag(task.getId());
        holder.relativeLayout.setTag("layout_" + task.getId());

        if (task.getCompleted()) {
            holder.changeBackground();
        }
    }

    @Override
    public int getItemCount() {
        if (tasks == null) return 0;
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView taskNameText;
        TextView taskDescText;
        TextView taskPomodoroText;
        Button taskStartBtn;
        Button taskCompleteBtn;

        public TaskViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.taskLayout);
            taskNameText = (TextView) itemView.findViewById(R.id.taskNameText);
            taskDescText = (TextView) itemView.findViewById(R.id.taskDescriptionText);
            taskPomodoroText = (TextView) itemView.findViewById(R.id.taskPomodoriText);

            taskStartBtn = (Button) itemView.findViewById(R.id.startTaskBtn);
            taskCompleteBtn = (Button) itemView.findViewById(R.id.completeTaskBtn);
        }

        public void changeBackground() {
            int color = Color.parseColor("#13ae1b");
            relativeLayout.setBackgroundColor(color);
            taskStartBtn.setEnabled(false);
        }
    }


}
