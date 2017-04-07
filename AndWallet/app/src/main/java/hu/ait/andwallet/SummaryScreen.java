package hu.ait.andwallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SummaryScreen extends AppCompatActivity {

    private int income;
    private int expense;

    private TextView incTv;
    private TextView expTv;
    private TextView totalTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_screen);

        Intent intent = getIntent();
        income = intent.getIntExtra("INCOME", -1);
        expense = intent.getIntExtra("EXPENSE", -1);

        getMoneyViews();
        fillMoneyFields();
    }

    private void fillMoneyFields() {
        incTv.setText("" + income);
        expTv.setText("" + expense);
        totalTv.setText("" + (income - expense));
    }

    private void getMoneyViews() {
        incTv = (TextView) findViewById(R.id.summaryInc);
        expTv = (TextView) findViewById(R.id.summaryExp);
        totalTv = (TextView) findViewById(R.id.summaryTotal);
    }
}
