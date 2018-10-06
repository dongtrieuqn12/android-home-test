package dongtrieu.tikitest;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.HotKeywordOnclick {

    private RecyclerView recyclerView;
    private List<ViewModel> viewModelList;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();
        RecyclerViewInit();
        FletchData();
    }

    private void InitView() {
        recyclerView = findViewById(R.id.RecyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tiki Assignment");
    }

    private void RecyclerViewInit() {
        viewModelList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(this,viewModelList,this);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        gridLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    //set dp, using for config Recycler View item.
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @SuppressLint("StaticFieldLeak")
    private void FletchData() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder stringBuilder;
                try {
                    URL url = new URL("https://gist.githubusercontent.com/talenguyen/38b790795722e7d7b1b5db051c5786e5/raw/63380022f5f0c9a100f51a1e30887ca494c3326e/keywords.json");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line;
                    stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine())!= null){
                        stringBuilder.append(line).append("\n");
                    }

                } catch (IOException e) {
                    return "";
                }

                return stringBuilder.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for(int i = 0 ; i < jsonArray.length() ; i ++) {
                        ViewModel viewModel = new ViewModel();
                        viewModel.setName((String) jsonArray.get(i));
                        Random rnd = new Random();
                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        viewModel.setColor(color);
                        viewModelList.add(viewModel);
                    }
                    recyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    @Override
    public void ClickPosition(ViewModel viewModel) {
        Toast.makeText(MainActivity.this,viewModel.getName(),Toast.LENGTH_SHORT).show();
    }
}
