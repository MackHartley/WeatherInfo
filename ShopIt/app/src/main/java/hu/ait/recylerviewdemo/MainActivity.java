package hu.ait.recylerviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import hu.ait.recylerviewdemo.adapter.TodoRecyclerAdapter;
import hu.ait.recylerviewdemo.touch.TodoItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TODO_ID = "KEY_TODO_ID";
    public static final String NAME = "NAME";
    public static final String PRICE = "PRICE";
    public static final String CATEGORY = "CATEGORY";
    public static final String DESC = "DESC";

    public static final int REQUEST_CODE_EDIT = 101;
    private TodoRecyclerAdapter todoRecyclerAdapter;
    private RecyclerView recyclerTodo;

    private int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication) getApplication()).openRealm();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        setupUI();

        ifExtrasAddNewItem(extras);
    }

    private void ifExtrasAddNewItem(Bundle extras) {
        if (extras != null) {
            if (extras.containsKey(NAME)) {
                String name = extras.getString(NAME);
                int price = extras.getInt(PRICE);
                String category = extras.getString(CATEGORY);
                String desc = extras.getString(DESC);
                addRow(name, price, category, desc);
            }
        }
    }

    private void setupUI() {
        setUpToolBar();
        setUpAddTodoUI();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerTodo = (RecyclerView) findViewById(R.id.recyclerTodo);
        recyclerTodo.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTodo.setLayoutManager(layoutManager);

        todoRecyclerAdapter = new TodoRecyclerAdapter(this,
                ((MainApplication) getApplication()).getRealmTodo());
        recyclerTodo.setAdapter(todoRecyclerAdapter);

        // adding touch support
        ItemTouchHelper.Callback callback = new TodoItemTouchHelperCallback(todoRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerTodo);
    }

    private void setUpAddTodoUI() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddActivity();
            }
        });
    }

    private void goToAddActivity() {
        Intent intent = new Intent(MainActivity.this, AddItem.class);
        startActivity(intent);
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void addRow(String name, int price, String category, String desc) {
        todoRecyclerAdapter.addItem(name, price, category, desc);
        recyclerTodo.scrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_new_item:
                goToAddActivity();
                return true;
            case R.id.menu_delete_purchased:
                deleteFinished();
                return true;
            case R.id.menu_delete_all:
                deleteAll();
                return true;
        }
        return true;
    }

    public void deleteFinished() {
        todoRecyclerAdapter.deleteFinished();
    }

    public void deleteAll() {
        todoRecyclerAdapter.deleteAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication) getApplication()).closeRealm();
    }

    public void openEditActivity(int index, String todoID) {
        positionToEdit = index;

        Intent startEdit = new Intent(this, EditTodoActivity.class);

        startEdit.putExtra(KEY_TODO_ID, todoID);

        startActivityForResult(startEdit, REQUEST_CODE_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE_EDIT) {
                    String todoID = data.getStringExtra(
                            EditTodoActivity.KEY_TODO);

                    todoRecyclerAdapter.updateTodo(todoID, positionToEdit);
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, R.string.cancelled, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
