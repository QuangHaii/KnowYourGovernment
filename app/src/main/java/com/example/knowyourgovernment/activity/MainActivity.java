package com.example.knowyourgovernment.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knowyourgovernment.Channels;
import com.example.knowyourgovernment.Official;
import com.example.knowyourgovernment.OfficialAdapter;
import com.example.knowyourgovernment.R;
import com.example.knowyourgovernment.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Official> officials = new ArrayList<>();
    private RecyclerView list;
    private OfficialAdapter listAdapter = new OfficialAdapter(this, officials);
    private Intent intent;
    private ProgressDialog progressDialog;
    private String locationID;
    private HashMap<Integer, String> indexMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list_item);
    }

    public void InputDialog() {
        if (!isConnected()) dialogInternetConnection();
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enter a City, State or a Zip Code:");
            builder.setCancelable(true);
            EditText input = new EditText(this);
            input.setGravity(Gravity.CENTER_HORIZONTAL);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (input.getText().toString().equals("")) dialog.cancel();
                    else {
                        String api = "https://civicinfo.googleapis.com/civicinfo/v2/representatives?address=" + input.getText().toString() + "&key=AIzaSyBZp6kRPZc7oEAfGN01qe6z5xFxw97FfF4";
                        MyAsyncTasks asyncTask = new MyAsyncTasks();
                        asyncTask.execute(api);
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading data...Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    BufferedReader bufferedReader = new BufferedReader(isw);
                    current = readAll(bufferedReader);
                    return current;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                TextView location = findViewById(R.id.location);
                JSONObject jsonObject = new JSONObject(s);
                JSONObject input = jsonObject.getJSONObject("normalizedInput");
                locationID = input.getString("city") + ", " + input.getString("state") + " " + input.getString("zip");
                location.setText(locationID);
                initIndex(indexMap, s);
                addOfficial(indexMap, s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initIndex(HashMap<Integer, String> map, String temp) {
        try {
            JSONObject jsonObject = new JSONObject(temp);
            JSONArray officeArray = jsonObject.getJSONArray("offices");
            for (int i = 0; i < officeArray.length(); i++) {
                JSONObject currentOffice = officeArray.getJSONObject(i);
                JSONArray officialArray = currentOffice.getJSONArray("officialIndices");
                for (int j = 0; j < officialArray.length(); j++) {
                    map.put(officialArray.getInt(j), currentOffice.getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addOfficial(HashMap<Integer, String> map, String temp) {
        if (!officials.isEmpty()) officials.clear();
        try {
            JSONObject jsonObject = new JSONObject(temp);
            JSONArray officialsArray = jsonObject.getJSONArray("officials");
            for (int i = 0; i < officialsArray.length(); i++) {
                Channels channelTemp = new Channels(null, null, null, null);
                JSONObject index = officialsArray.getJSONObject(i);
                officials.add(new Official(map.get(i),
                        index.getString("name"),
                        index.getString("party"),
                        addressToString(officialsArray, i),
                        phonesToString(officialsArray, i),
                        emailToString(officialsArray, i),
                        urlsToString(officialsArray, i),
                        channelString(officialsArray, i, channelTemp),
                        index.optString("photoUrl")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list.setAdapter(listAdapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addOnItemTouchListener(new RecyclerItemClickListener(this, list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(MainActivity.this, OfficialActivity.class);
                pushIntent(intent, position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public String addressToString(JSONArray officialsArray, int i) {
        try {
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("geocodingSummaries");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getString("queryString");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No Data Provided";
    }

    public String emailToString(JSONArray officialsArray, int i) {
        try {
            String temp = "";
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("emails");
            for (int j = 0; j < jsonArray.length(); j++) {
                temp += jsonArray.getString(j);
                if (j < jsonArray.length() - 1) temp += "\n";
            }
            return temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No Data Provided";
    }

    public ArrayList<String> phonesToString(JSONArray officialsArray, int i) {
        try {
            ArrayList<String> temp = new ArrayList<>();
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("phones");
            for (int j = 0; j < jsonArray.length(); j++) {
                temp.add(jsonArray.optString(j));
            }
            return temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> urlsToString(JSONArray officialsArray, int i) {
        try {
            ArrayList<String> temp = new ArrayList<>();
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("urls");
            for (int j = 0; j < jsonArray.length(); j++) {
                temp.add(jsonArray.optString(j));
            }
            return temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Channels channelString(JSONArray officialsArray, int i, Channels channel) {
        try {
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("channels");
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject index2 = jsonArray.getJSONObject(j);
                if (index2.getString("type").equals("Youtube"))
                    channel.setYoutube(index2.getString("id"));
                if (index2.getString("type").equals("Twitter"))
                    channel.setTwitter(index2.getString("id"));
                if (index2.getString("type").equals("Google+"))
                    channel.setGoogle(index2.getString("id"));
                if (index2.getString("type").equals("Facebook"))
                    channel.setFacebook(index2.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return channel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                InputDialog();
                return true;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        char[] buf = new char[256000];
        while ((cp = rd.read(buf)) != -1) {
            sb.append(buf, 0, cp);
            System.out.print(sb);
        }
        return sb.toString();
    }

    public void pushIntent(Intent intent, int position) {
        intent.putExtra("location", locationID);
        intent.putExtra("office", officials.get(position).getOffice());
        intent.putExtra("name", officials.get(position).getName());
        intent.putExtra("party", officials.get(position).getParty());
        intent.putExtra("address", officials.get(position).getAddress());
        intent.putExtra("phones", officials.get(position).getPhones());
        intent.putExtra("email", officials.get(position).getEmail());
        intent.putExtra("website", officials.get(position).getWebsite());
        intent.putExtra("channelsYoutube", officials.get(position).getChannels().getYoutube());
        intent.putExtra("channelsGoogle", officials.get(position).getChannels().getGoogle());
        intent.putExtra("channelsFacebook", officials.get(position).getChannels().getFacebook());
        intent.putExtra("channelsTwitter", officials.get(position).getChannels().getTwitter());
        intent.putExtra("photoUrl", officials.get(position).getPhotoUrl());
        startActivity(intent);
    }

    public void dialogInternetConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stocks Cannot Be Updated Without A Network Connection");
        builder.setCancelable(true);
        builder.show();
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected())
                return true;
            else
                return false;
        }
        return false;
    }

}

