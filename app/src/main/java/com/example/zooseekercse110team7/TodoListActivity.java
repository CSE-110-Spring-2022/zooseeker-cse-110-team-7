package com.example.zooseekercse110team7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zooseekercse110team7.todo_list.TodoListAdapter;
import com.example.zooseekercse110team7.todo_list.TodoListViewModel;

public class TodoListActivity extends AppCompatActivity {
    // Exposed for testing purposes later...
    public RecyclerView recyclerView;

    private TodoListViewModel viewModel;
    private EditText newTodoText;
    private Button addTodoButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        viewModel = new ViewModelProvider(this).get(TodoListViewModel.class);
        TodoListAdapter adapter = new TodoListAdapter();
        adapter.setOnCheckBoxClickedHandler(viewModel::toggleCompleted);
        adapter.setOnTextEditedHandler(viewModel::updateText);
        viewModel.getTodoListItems().observe(this, adapter::setTodoListItems);


        recyclerView = findViewById(R.id.todo_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        this.newTodoText = this.findViewById(R.id.new_todo_text);
        this.addTodoButton = this.findViewById(R.id.add_todo_btn);
        addTodoButton.setOnClickListener(this::onAddTodoClicked);

        adapter.setOnDeleteButtonClicked(viewModel::deleteTodo);
    }

    void onAddTodoClicked(View view){
        String text = newTodoText.getText().toString();
        newTodoText.setText("");
        viewModel.createTodo(text);
    }
}