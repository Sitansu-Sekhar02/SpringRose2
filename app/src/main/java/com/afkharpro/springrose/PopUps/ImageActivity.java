package com.afkharpro.springrose.PopUps;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

public class ImageActivity extends Activity {
ImageView imageView;
    String imagelink;
    String descript;
    TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image);
        imageView = (ImageView)findViewById(R.id.img);
        description = (TextView)findViewById(R.id.desc) ;
        Intent intent = getIntent();
        imagelink = intent.getStringExtra("imagelink");
        descript = intent.getStringExtra("desc");

        description.setText(Html.fromHtml(descript));
        Picasso.get()
                .load(imagelink)
                .placeholder(R.drawable.loading_icon)
                .error(R.drawable.loading_icon)
                .into(imageView);

       // Picasso.with(getApplicationContext()).load(R.drawable.springrose).into(imageView);
    }
}
