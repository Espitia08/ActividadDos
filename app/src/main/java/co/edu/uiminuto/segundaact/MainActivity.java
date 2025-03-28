package co.edu.uiminuto.segundaact;

import static android.widget.Toast.makeText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etTask;
    private Button btnAdd;
    private ListView listTask;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initObject();

        this.btnAdd.setOnClickListener(this::addTask);

        this.listTask.setOnItemClickListener((parent, view, position, id) -> {
            String task = arrayList.get(position);
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("task", task);
            intent.putExtra("position", position);
            startActivityForResult(intent, 1);
        });
    }

    private void addTask(View view) {
        String task = this.etTask.getText().toString().trim();
        if (!task.isEmpty()) {
            this.arrayList.add(task);
            this.adapter.notifyDataSetChanged();
            this.etTask.setText("");
        } else {
            Toast.makeText(this, "Coloque una tarea", Toast.LENGTH_LONG).show();
        }
    }

    private void initObject() {
        this.etTask = findViewById(R.id.etTask);
        this.btnAdd = findViewById(R.id.btnAdd);
        this.listTask = findViewById(R.id.listTask);
        this.arrayList = new ArrayList<>();
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.arrayList);
        this.listTask.setAdapter(this.adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String updatedTask = data.getStringExtra("updatedTask");
            int position = data.getIntExtra("position", -1);
            boolean isDeleted = data.getBooleanExtra("isDeleted", false);

            if (isDeleted && position != -1) {
                arrayList.remove(position);
            } else if (updatedTask != null && position != -1) {
                arrayList.set(position, updatedTask);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
