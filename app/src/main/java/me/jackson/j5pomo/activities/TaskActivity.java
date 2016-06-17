package me.jackson.j5pomo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import me.jackson.j5pomo.R;
import me.jackson.j5pomo.dao.TaskDao;
import me.jackson.j5pomo.model.Task;

public class TaskActivity extends AppCompatActivity {

    private EditText taskNameInput;
    private EditText taskDescriptionInput;
    private EditText taskPomodoriInput;

    private TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        taskDao = new TaskDao(this);

        taskNameInput = (EditText) findViewById(R.id.taskName);
        taskDescriptionInput = (EditText) findViewById(R.id.taskDescription);
        taskPomodoriInput = (EditText) findViewById(R.id.taskPomodori);
    }

    public void saveTask(View v) {
        Task task = new Task();

        task.setName(taskNameInput.getText().toString());
        task.setDescription(taskDescriptionInput.getText().toString());
        String minutesText = taskPomodoriInput.getText().toString();
        task.setPomodoro(Integer.parseInt(minutesText));
        task.setCompleted(false);

        taskDao.insert(task);

        setResult(RESULT_OK);
        finish();
    }
}
