package com.example.knowyourgovernment.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knowyourgovernment.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class OfficialActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_official);

        TextView location = findViewById(R.id.location);
        TextView office = findViewById(R.id.office);
        TextView name = findViewById(R.id.name);
        TextView party = findViewById(R.id.party);
        TextView address = findViewById(R.id.address);
        TextView phones = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        TextView website = findViewById(R.id.web);
        TextView web2 = findViewById(R.id.web2);
        TextView phone2 = findViewById(R.id.phone2);

        ImageView imageView = findViewById(R.id.imageView);
        ImageButton youtube = findViewById(R.id.youtube);
        ImageButton google = findViewById(R.id.google);
        ImageButton twitter = findViewById(R.id.twitter);
        ImageButton facebook = findViewById(R.id.facebook);
        ScrollView layout = findViewById(R.id.scrollview);

        String location2 = getIntent().getStringExtra("location");
        String name2 = getIntent().getStringExtra("name");
        String address2 = getIntent().getStringExtra("address");
        ArrayList<String> phones2 = getIntent().getStringArrayListExtra("phones");
        String email2 = getIntent().getStringExtra("email");
        ArrayList<String> website2 = getIntent().getStringArrayListExtra("website");
        String office2 = getIntent().getStringExtra("office");
        String party2 = getIntent().getStringExtra("party");

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String FACEBOOK_URL = "https://www.facebook.com/" +getIntent().getStringExtra("channelsFacebook");
                    String urlToUse;
                    PackageManager packageManager = getPackageManager();
                    try {
                        int versionCode = packageManager.getPackageInfo("com.facebook.katana",0).versionCode;
                        if (versionCode >= 3002850) { //newer versions of fb app
                            urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                        } else { //older versions of fb app
                            urlToUse = "fb://page/" + getIntent().getStringExtra("channelsFacebook");
                        }
                    } catch
                    (PackageManager.NameNotFoundException e) {
                        urlToUse = FACEBOOK_URL;
                    }
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    facebookIntent.setData(Uri.parse(urlToUse));
                    startActivity(facebookIntent);
                }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                { Intent intent = null;
                    String name = getIntent().getStringExtra("channelsTwitter");
                    try {
                        getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
                    }
                    startActivity(intent);
                }
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getIntent().getStringExtra("channelsGoogle");
                Intent intent = null;
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivi ty"); intent.putExtra("customAppUri", name);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
                }
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getIntent().getStringExtra("channelsYoutube");
                Intent intent = null;
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse("https://www.youtube.com/" + name));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
                }
            }
        });

        location.setText(location2);
        office.setText(office2);
        name.setText(name2);
        address.setText(address2);
        phones.setText(phones2.get(0));
        if(phones2.size()==2) phone2.setText(phones2.get(1));
        email.setText(email2);
        website.setText(website2.get(0));
        if(website2.size()==2) web2.setText(website2.get(1));
        party.setText("("+party2+")");

        if(party2.equals("Democratic Party")) layout.setBackgroundColor(Color.parseColor("#0000FF"));
        if(party2.equals("Republican Party")) layout.setBackgroundColor(Color.parseColor("#FF0000"));

        if(getIntent().getStringExtra("channelsYoutube")!=null) youtube.setVisibility(View.VISIBLE);
        if(getIntent().getStringExtra("channelsGoogle")!=null) google.setVisibility(View.VISIBLE);
        if(getIntent().getStringExtra("channelsTwitter")!=null) twitter.setVisibility(View.VISIBLE);
        if(getIntent().getStringExtra("channelsFacebook")!=null) facebook.setVisibility(View.VISIBLE);

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfficialActivity.this,PhotoActivity.class);
                intent.putExtra("location",getIntent().getStringExtra("location"));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("office",getIntent().getStringExtra("office"));
                intent.putExtra("party",getIntent().getStringExtra("party"));
                intent.putExtra("photoUrl",photoUrl);
                startActivity(intent);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q="+address2);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phones2.get(0)));
                startActivity(intent);
            }
        });
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phones2.get(1)));
                startActivity(intent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto",email2, null));
                startActivity(intent);
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(website2.get(0)));
                startActivity(intent);
            }
        });
        web2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(website2.get(1)));
                startActivity(intent);
            }
        });

    }
}