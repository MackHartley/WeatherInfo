package hu.ait.weatherinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;

import java.util.Calendar;
import java.util.Date;

import hu.ait.weatherinfo.adapter.CityRecyclerAdaptor;
import hu.ait.weatherinfo.data.City;
import hu.ait.weatherinfo.data.WeatherInfo;
import hu.ait.weatherinfo.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//TODO: First, add realm persistence. Then create dialogue box which will take in user input city. API call to gather city info. Parse
//TODO: the city info to fill a city object. Add this new city object to the recycler view. Then
//TODO: change the FAB to allow additions and look better. Then clean up code.

//TODO: Also make scroll list move up when new city is added. Add about toast message.


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public CityRecyclerAdaptor cityRecyclerAdaptor;
    private RecyclerView recyclerView;

    private String units = "metric";
    private final String apikey = "36ea93f14331867f037e4da5ae1e8ac3";
    private WeatherAPI weatherAPI;

    public final static String EXTI = "EXTI";
    public final static String EXDESC = "EXDESC";
    public final static String EXCURTEMP = "EXCURTEMP";
    public final static String EXMINTEMP = "EXMINTEMP";
    public final static String EXMAXTEMP = "EXMAXTEMP";
    public final static String EXHUM = "EXHUM";
    public final static String EXSSET = "EXSSET";
    public final static String EXSRI = "EXSRI";
    public static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddCityDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        setUpRecyclerView();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);

    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerWeatherHolder);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        cityRecyclerAdaptor = new CityRecyclerAdaptor(this);
        recyclerView.setAdapter(cityRecyclerAdaptor);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            cityRecyclerAdaptor.clearAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_add_City) {
            createAddCityDialog();

        } else if (id == R.id.drawer_about) {
            Toast.makeText(this, "This app was created by Mack Hartley!", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createAddCityDialog() {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(MainActivity.this);
        View dView = getLayoutInflater().inflate(R.layout.city_dialog, null);
        dBuilder.setView(dView);
        final AlertDialog dialog = dBuilder.create();

        final EditText dCityET = (EditText) dView.findViewById(R.id.city_name_dialog);
        Button dButton = (Button) dView.findViewById(R.id.submit_dialog_button);
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dCityET.getText().toString().isEmpty()) {
                    String cityName = dCityET.getText().toString();
                    cityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
                    callApiWithCity(cityName);
                    dialog.dismiss();
                } else {
                    dCityET.setError(getString(R.string.enterCity));
                }
            }
        });

        dialog.show();
    }

    private void callApiWithCity(final String cityName) {
        Call<WeatherInfo> callInfo = weatherAPI.getWeatherForCity(cityName, units, apikey);

        callInfo.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if (response.body() != null) {
                    WeatherInfo wData = response.body();
                    City newCity = new City(cityName,
                            wData.getWeather().get(0).getDescription(),
                            wData.getMain().getTemp(),
                            wData.getMain().getTempMin(),
                            wData.getMain().getTempMax(),
                            wData.getMain().getHumidity(),
                            getSunTime(wData.getSys().getSunset().longValue()) + getString(R.string.am),
                            getSunTime(wData.getSys().getSunrise().longValue()) + getString(R.string.pm),
                            wData.getWeather().get(0).getIcon());
                    cityRecyclerAdaptor.addCity(newCity);
                    recyclerView.scrollToPosition(0);
                }
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                //Todo: add failure call here
                Log.w(getString(R.string.log_message), t);
                Toast.makeText(MainActivity.this, R.string.user_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getSunTime(long time) {
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;

        return (String.format("%02d:%02d:%02d", hour, minute, second));
    }

    public void launchDetailsActivity(String title, String desc, Double curT, Double minT, Double maxT, Double hum, String srise, String sset) {
        Intent moreDetials = new Intent(this, DetailsActivity.class);
        moreDetials.putExtra(EXTI, title);
        moreDetials.putExtra(EXDESC, desc);
        moreDetials.putExtra(EXCURTEMP, curT);
        moreDetials.putExtra(EXMINTEMP, minT);
        moreDetials.putExtra(EXMAXTEMP, maxT);
        moreDetials.putExtra(EXHUM, hum);
        moreDetials.putExtra(EXSRI, srise);
        moreDetials.putExtra(EXSSET, sset);
        startActivityForResult(moreDetials, REQUEST_CODE);
    }


}
