package hu.ait.recylerviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import hu.ait.recylerviewdemo.data.Item;
import io.realm.Realm;

public class EditTodoActivity extends AppCompatActivity {
    public static final String KEY_TODO = "KEY_TODO";
    public static final String todoId = "todoID";
    private EditText name;
    private EditText estPriceView;
    private EditText categoryView;
    private EditText descView;
    private CheckBox status;
    private hu.ait.recylerviewdemo.data.Item itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        checkForIntentExtra();
        prepUIView();
    }

    private void checkForIntentExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.KEY_TODO_ID)) {
            String todoID = getIntent().getStringExtra(MainActivity.KEY_TODO_ID);
            itemToEdit = getRealm().where(Item.class)
                    .equalTo("todoId", todoID)
                    .findFirst();
        }
    }

    private void prepUIView() {
        populateTargetViews();
        setSaveButtonListerner();
        prepopulateEditTextFieldsWithPrevData();
    }

    private void prepopulateEditTextFieldsWithPrevData() {
        if (itemToEdit != null) {
            name.setText(itemToEdit.getItemName());
            estPriceView.setText(Integer.toString(itemToEdit.getPrice()));
            categoryView.setText(itemToEdit.getCategory());
            descView.setText(itemToEdit.getDesc());
            status.setChecked(itemToEdit.isDone());
        }
    }

    private void setSaveButtonListerner() {
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });
    }

    private void populateTargetViews() {
        name = (EditText) findViewById(R.id.etTodoText);
        status = (CheckBox) findViewById(R.id.cbTodoDone);
        estPriceView = (EditText) findViewById(R.id.etItemPrice);
        categoryView = (EditText) findViewById(R.id.etItemCat);
        descView = (EditText) findViewById(R.id.etItemDesc);
    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmTodo();
    }

    private void saveTodo() {
        if (!hasEmptyField()) {
            Intent intentResult = new Intent();

            getRealm().beginTransaction();
            updateNewTodoValuesFromFields();
            getRealm().commitTransaction();

            intentResult.putExtra(KEY_TODO, itemToEdit.getTodoID());
            setResult(RESULT_OK, intentResult);
            finish();
        }
    }

    private void updateNewTodoValuesFromFields() {
        itemToEdit.setItemTitle(name.getText().toString());
        itemToEdit.setDone(status.isChecked());
        itemToEdit.setPrice(Integer.parseInt(estPriceView.getText().toString()));
        itemToEdit.setCategory(categoryView.getText().toString());
        itemToEdit.setDesc(descView.getText().toString());
    }

    private boolean hasEmptyField() {
        if ("".equals(name.getText().toString())) {
            name.setError(getString(R.string.emptyField));
            return true;
        } else if ("".equals(estPriceView.getText().toString())) {
            estPriceView.setError(getString(R.string.emptyField));
            return true;
        } else if ("".equals(categoryView.getText().toString())) {
            categoryView.setError(getString(R.string.emptyField));
            return true;
        } else if ("".equals(descView.getText().toString())) {
            descView.setError(getString(R.string.emptyField));
            return true;
        }
        return false;
    }
}
