package hu.ait.recylerviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItem extends AppCompatActivity {

    private static final String NAME = "NAME";
    private static final String PRICE = "PRICE";
    private static final String CATEGORY = "CATEGORY";
    private static final String DESC = "DESC";

    private EditText nameFieldView;
    private EditText priceFieldView;
    private EditText catFieldView;
    private EditText descFieldView;
    private Button addBV;

    private String name;
    private int price;
    private String category;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        populateViews();
        setAddButtonListener();
    }

    private void setAddButtonListener() {
        addBV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasEmptyFields()) {
                    collectNewItemValues();
                    createMainActivityIntentWithData();
                }
            }
        });
    }

    private void collectNewItemValues() {
        name = nameFieldView.getText().toString();
        price = Integer.parseInt(priceFieldView.getText().toString());
        category = catFieldView.getText().toString();
        desc = descFieldView.getText().toString();
    }

    private void createMainActivityIntentWithData() {
        Intent intent = new Intent(AddItem.this, MainActivity.class);
        intent.putExtra(NAME, name);
        intent.putExtra(PRICE, price);
        intent.putExtra(CATEGORY, category);
        intent.putExtra(DESC, desc);
        startActivity(intent);
    }

    private boolean hasEmptyFields() {
        if (nameFieldView.getText().toString().equals("")) {
            nameFieldView.setError(getString(R.string.emptyField));
            return true;
        }
        if (priceFieldView.getText().toString().equals("")) {
            priceFieldView.setError(getString(R.string.emptyField));
            return true;
        }
        if (catFieldView.getText().toString().equals("")) {
            catFieldView.setError(getString(R.string.emptyField));
            return true;
        }
        if (descFieldView.getText().toString().equals("")) {
            descFieldView.setError(getString(R.string.emptyField));
            return true;
        }
        return false;
    }

    private void populateViews() {
        populateEditTextViews();
        populateButtonViews();
    }

    private void populateButtonViews() {
        addBV = (Button) findViewById(R.id.addButton);
    }

    private void populateEditTextViews() {
        nameFieldView = (EditText) findViewById(R.id.addNameField);
        priceFieldView = (EditText) findViewById(R.id.addPriceField);
        catFieldView = (EditText) findViewById(R.id.addCatField);
        descFieldView = (EditText) findViewById(R.id.addDescField);
    }
}
