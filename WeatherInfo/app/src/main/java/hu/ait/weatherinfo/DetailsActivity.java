package hu.ait.weatherinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import hu.ait.weatherinfo.data.City;

public class DetailsActivity extends AppCompatActivity {

    String title;
    String desc;
    Double curT;
    Double minT;
    Double maxT;
    Double hum;
    String srise;
    String sset;

    TextView TitleView;
    TextView DescView;
    TextView CurTView;
    TextView MinTView;
    TextView MaxTView;
    TextView HumView;
    TextView SriseView;
    TextView SsetView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        if (getIntent().hasExtra(MainActivity.EXTI)) {
            getExtras();
            bindViews();
            populateViews();
        }
    }

    private void populateViews() {
        TitleView.setText(title);
        DescView.setText(desc);
        CurTView.setText(curT.toString());
        MinTView.setText(minT.toString());
        MaxTView.setText(maxT.toString());
        HumView.setText(hum.toString());
        SriseView.setText(srise);
        SsetView.setText(sset);
    }

    private void bindViews() {
        TitleView = (TextView) findViewById(R.id.dTitle);
        DescView = (TextView) findViewById(R.id.dDescription);
        CurTView = (TextView) findViewById(R.id.dCurT);
        MinTView = (TextView) findViewById(R.id.dMinT);
        MaxTView = (TextView) findViewById(R.id.dMaxT);
        HumView = (TextView) findViewById(R.id.dHum);
        SriseView = (TextView) findViewById(R.id.dSrise);
        SsetView = (TextView) findViewById(R.id.dSset);
    }

    private void getExtras() {
        title = getIntent().getStringExtra(MainActivity.EXTI);
        desc = getIntent().getStringExtra(MainActivity.EXDESC);
        curT = getIntent().getDoubleExtra(MainActivity.EXCURTEMP, -1);
        minT = getIntent().getDoubleExtra(MainActivity.EXMINTEMP, -1);
        maxT = getIntent().getDoubleExtra(MainActivity.EXMAXTEMP, -1);
        hum = getIntent().getDoubleExtra(MainActivity.EXHUM, -1);
        srise = getIntent().getStringExtra(MainActivity.EXSRI);
        sset = getIntent().getStringExtra(MainActivity.EXSSET);
    }
}
