package com.afkharpro.springrose.MenuActivities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.R;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.ZopimChatFragment;

public class LiveChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZopimChat.init("boGPUzL6r8GgRF0eDZyQpgkgLZQ8AXGI");
        setContentView(R.layout.activity_live_chat);
        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getApplicationContext().getString(R.string.livechat));
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(face);
        tv.setTextSize((float)24);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);


        Users user = Users.findById(Users.class, (long) 1);
        if (user != null && !user.userid.equals("0")) {
            VisitorInfo visitorData = new VisitorInfo.Builder()
                    .name(user.first)
                    .email(user.email)
                    .phoneNumber(user.tele)
                    .build();

            ZopimChat.setVisitorInfo(visitorData);
        }


        ZopimChatFragment fragment = new ZopimChatFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.chat_fragment_container, fragment, ZopimChatFragment.class.getName());
        transaction.commit();
    }
}
