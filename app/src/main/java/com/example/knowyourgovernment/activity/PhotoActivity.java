package com.example.knowyourgovernment.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.knowyourgovernment.R;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.official_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.switchh:
                if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                return true;
            case R.id.about:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        TextView location = findViewById(R.id.location);
        TextView office = findViewById(R.id.office);
        TextView name = findViewById(R.id.name);
        LinearLayout layout = findViewById(R.id.layout);
        ImageView imageView =findViewById(R.id.imageView);

        String party = getIntent().getStringExtra("party");
        if(party.equals("Democratic Party")) layout.setBackgroundColor(Color.parseColor("#0000FF"));
        if(party.equals("Republican Party")) layout.setBackgroundColor(Color.parseColor("#FF0000"));

        location.setText(getIntent().getStringExtra("location"));
        office.setText(getIntent().getStringExtra("office"));
        name.setText(getIntent().getStringExtra("name"));

        String photoUrl = getIntent().getStringExtra("photoUrl");
        if (!photoUrl.equals("") ) {
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);
                }
            });
            Picasso picasso = builder.build();
            picasso.load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        } else {
            Picasso.with(this).load((Uri) null)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missing)
                    .into(imageView);
        }
    }
}