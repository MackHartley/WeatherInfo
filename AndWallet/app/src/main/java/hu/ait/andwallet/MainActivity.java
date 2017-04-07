package hu.ait.andwallet;

import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String INCOME_EXTRA = "INCOME";
    private static final String EXPENSE_EXTRA = "EXPENSE";

    private boolean isIncomeMode = true;
    private int expense = 0;
    private int income = 0;

    Button incomeBtn;
    Button saveBtn;
    EditText titleEt;
    EditText amtEt;
    LinearLayout layoutContent;
    TextView balanceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incomeBtn = (Button) findViewById(R.id.incomeBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        titleEt = (EditText) findViewById(R.id.titleEt);
        amtEt = (EditText) findViewById(R.id.amtEt);
        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);
        balanceTv = (TextView) findViewById(R.id.balanceTv);

        incomeBtn.setBackgroundColor(Color.parseColor("#00ccff"));

        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isIncomeMode) {
                    isIncomeMode = false;
                    incomeBtn.setBackgroundColor(Color.LTGRAY);
                } else {
                    isIncomeMode = true;
                    incomeBtn.setBackgroundColor(Color.parseColor("#00ccff"));
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEt.getText().toString();
                String amtText = amtEt.getText().toString();

                if(!title.equals("") && !amtText.equals("")){ //If forms filled out correctly
                    int amt = Integer.parseInt(amtEt.getText().toString());

                    View newRow = getLayoutInflater().inflate(R.layout.item_row, null);
                    TextView rowTitleTv = (TextView) newRow.findViewById(R.id.rowTitleTv);
                    rowTitleTv.setText(title);
                    TextView rowAmtTv = (TextView) newRow.findViewById(R.id.rowAmtTv);
                    rowAmtTv.setText("$" + amtText);

                    ImageView rowImg = (ImageView) newRow.findViewById(R.id.rowImg);
                    if(isIncomeMode){
                        rowImg.setImageResource(R.drawable.income);
                        income += amt;
                    } else {
                        rowImg.setImageResource(R.drawable.expense);
                        expense += amt;
                    }
                    layoutContent.addView(newRow, 0);
                } else {
                    checkForEmptyFields();
                }
                updateBal();
            }
        });
    }

    private void checkForEmptyFields() {
        if(titleEt.getText().toString().equals("")){
            titleEt.setError(getString(R.string.emptyField));
        }
        if(amtEt.getText().toString().equals("")){
            amtEt.setError(getString(R.string.emptyField));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delBtn:
                layoutContent.removeAllViews();
                restBal();
                return true;
            case R.id.sumBtn:
                goToSummary();
                // TODO: here
                return true;
        }
        return true;
    }

    public void updateBal() {
        balanceTv.setText("" + (income - expense));
    }

    public void restBal() {
        income = 0;
        expense = 0;
        updateBal();
    }

    public void goToSummary() {
        Intent intent = new Intent(this, SummaryScreen.class);
        intent.putExtra(INCOME_EXTRA, income);
        intent.putExtra(EXPENSE_EXTRA, expense);
        startActivity(intent);
    }
}
