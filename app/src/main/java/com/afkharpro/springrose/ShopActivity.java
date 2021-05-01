package com.afkharpro.springrose;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.afkharpro.springrose.Base.NonSwipeableViewPager;
import com.afkharpro.springrose.MenuActivities.LiveChatActivity;
import com.afkharpro.springrose.MenuActivities.OrderStatusActivity;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.FlipAnimator;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Fragments.HorizontalNavFragment;
import com.afkharpro.springrose.MenuActivities.AccountActivity;
import com.afkharpro.springrose.MenuActivities.MyOrdersActivity;
import com.afkharpro.springrose.MenuActivities.WishListActivity;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.OrderFlow.CartActivity;
import com.afkharpro.springrose.OrderFlow.SetMethodsActivity;
import com.afkharpro.springrose.Settings.CurrencyActivity;
import com.afkharpro.springrose.Settings.LanguageActivity;
import com.afkharpro.springrose.Settings.NotiActivity;
import com.afkharpro.springrose.MenuActivities.ProfileActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import devlight.io.library.ntb.NavigationTabBar;
import mehdi.sakout.dynamicbox.DynamicBox;

public class ShopActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SearchView searchView;
    MenuItem myActionMenuItem;
    NavigationTabBar navigationTabBar;
    NonSwipeableViewPager viewPager;
    Menu topmenu;
    DynamicBox box;

    AnimationDrawable rocketAnimation;
    TextView nav_user;
    ImageView logo, logout;
    int width;
    int height;
    Context context;
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String share_message_en;
    String share_message_ar;
    SweetAlertDialog loading;
    Handler mHandler;
    public static boolean isback;
    ProgressBar bar;
    ShimmerFrameLayout container;
    View hView;
    LinearLayout loadinglin;
    PagerAdapter adapter;
    Fragment fragment;
    String link;
    boolean survey;
    int oid =0 ;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZopimChat.init("boGPUzL6r8GgRF0eDZyQpgkgLZQ8AXGI");
        //  overridePendingTransition(R.anim.translate_downtop, R.anim.hold);
        setContentView(R.layout.activity_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.springroselogo);

        context = getApplicationContext();
      //  sv = (ScrollView)findViewById(R.id.scroll);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        bar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        Drawable d = toolbar.getLogo();
        mHandler = new Handler();
        loadinglin = (LinearLayout) findViewById(R.id.loadinglin);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.vp_horizontal_ntb);
        getSupportActionBar();

        Intent i = getIntent();
        survey = i.getBooleanExtra("survey",false);
        oid = i.getIntExtra("oid",0);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users user = Users.findById(Users.class, (long) 1);
                if (user != null && !user.userid.equals("0")) {
                    dologin(user.email, user.password);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        hView = navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.username);
        logo = (ImageView) hView.findViewById(R.id.imageView);
        logout = (ImageView) hView.findViewById(R.id.imageViewlogout);
        container =
                (ShimmerFrameLayout) hView.findViewById(R.id.shimmer_view_container);
        // container.startShimmerAnimation();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(ShopActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(context.getString(R.string.Areyousure))
                        .showCancelButton(true)
                        .setConfirmText(context.getString(R.string.Yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                logout();
                            }
                        })
                        .show();
            }
        });


    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {

        if (survey){
            survey=false;
            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
            intent.putExtra("title","Survey");
            intent.putExtra("link",app.getSurlink()+oid);
            startActivity(intent);

        }

        initUI();

        if (topmenu != null) {
            if (app.isItemInCart()) {
                MenuItem searchItem = topmenu.findItem(R.id.action_search);
                searchItem.setIcon(R.drawable.iscart);
            } else {
                MenuItem searchItem = topmenu.findItem(R.id.action_search);
                searchItem.setIcon(R.drawable.cart);
            }
        }









        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadinglin.setVisibility(View.GONE);
                mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(false)
                        .build();
                mFirebaseRemoteConfig.setConfigSettings(configSettings);
                long cacheExpiration = 3600;
                if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                    cacheExpiration = 0;
                }
                mFirebaseRemoteConfig.fetch(cacheExpiration)
                        .addOnCompleteListener(ShopActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mFirebaseRemoteConfig.activateFetched();
                                } else {

                                }
                                share_message_en = mFirebaseRemoteConfig.getString("share_message_en");
                                share_message_ar = mFirebaseRemoteConfig.getString("share_message_ar");
                                link = mFirebaseRemoteConfig.getString("survey_link");
                                app.setSurlink(link);
                            }
                        });
            }
        }, 3000);

        super.onPostCreate(savedInstanceState);
    }




    private void startViewAnimations() {
        width = logo.getWidth() / 2;
        height = logo.getHeight() / 2;


        int period = 1000;

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new AccelerateInterpolator());

        FlipAnimator flipAnimator = new FlipAnimator(logo, logo, width, height);
        FlipAnimator flipAnimator_reverse = new FlipAnimator(logo, logo, width, height);

        flipAnimator.setStartOffset(2 * period);
        flipAnimator_reverse.setStartOffset(2 * period + 1000);
        animationSet.addAnimation(flipAnimator);
        animationSet.addAnimation(flipAnimator_reverse);

        animationSet.setStartOffset(period);
        animationSet.setAnimationListener(new CyclicAnimationListener());
        //logo.startAnimation(animationSet);
    }

    private class CyclicAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            startViewAnimations();
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
        }

        @Override
        public void onAnimationStart(Animation arg0) {
        }
    }

    @Override
    protected void onResume() {
        if (isback) {
            getcart();
            isback = false;
        }
        if (topmenu != null) {
            if (app.isItemInCart()) {
                MenuItem searchItem = topmenu.findItem(R.id.action_search);
                searchItem.setIcon(R.drawable.iscart);
            } else {
                MenuItem searchItem = topmenu.findItem(R.id.action_search);
                searchItem.setIcon(R.drawable.cart);
            }
        }


        Users user = Users.findById(Users.class, (long) 1);

        if (user != null && !user.userid.equals("0")) {
            dologin(user.email, user.password);
            nav_user.setText(context.getString(R.string.Welcome) + " " + user.first);
            logout.setVisibility(View.VISIBLE);
        } else {
            logout.setVisibility(View.GONE);
            nav_user.setText(context.getString(R.string.WelcomeGuest));
        }

        container.startShimmerAnimation();
        super.onResume();
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        navigationTabBar.setViewPager(viewPager, viewPager.getCurrentItem());


    }




    private void initUI() {


                 adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public int getCount() {
                        return 5;
                    }

                    @Override
                    public Fragment getItem(int position) {
                        fragment = new HorizontalNavFragment();
                        Bundle args = new Bundle();
                        args.putInt("identifier", position);
                        fragment.setArguments(args);
                        return fragment;
                    }
                };

                viewPager.setAdapter(adapter);

                viewPager.setOffscreenPageLimit(4);

                viewPager.setClipToPadding(false);
                viewPager.buildDrawingCache(true);
                viewPager.setDrawingCacheEnabled(true);




        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        Color.parseColor("#ffffff"))

                        .title(context.getString(R.string.Features))


                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor("#ffffff"))

                        .title(context.getString(R.string.Categories))

                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                       Color.parseColor("#ffffff"))

                        .title(context.getString(R.string.Occasion))

                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fifth),
                        Color.parseColor("#ffffff"))

                        .title(context.getString(R.string.Branches))

                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.search),
                        Color.parseColor("#ffffff"))
                        .title(context.getString(R.string.Search))
                        .build()
        );

        navigationTabBar.setModels(models);


        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





//


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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shop, menu);
        topmenu = menu;
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (app.isItemInCart()) {

            searchItem.setIcon(R.drawable.iscart);
        } else {

            searchItem.setIcon(R.drawable.cart);
        }

//        SearchManager searchManager = (SearchManager) ShopActivity.this.getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(ShopActivity.this.getComponentName()));
//        }

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Users user = Users.findById(Users.class, (long) 1);
                if (user != null && !user.userid.equals("0")) {
                    dologin(user.email, user.password);
                }
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);


                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Users user = Users.findById(Users.class, (long) 1);
            if (user != null && !user.userid.equals("0")) {
                dologin(user.email, user.password);
                nav_user.setText(user.first);
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_gallery) {
            navigationTabBar.setViewPager(viewPager, 0);
        } else if (id == R.id.nav_slideshow) {
            Users user = Users.findById(Users.class, (long) 1);
            if (user != null && !user.userid.equals("0")) {
                dologin(user.email, user.password);
            }
            Intent intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orderstatus) {
            Intent intent = new Intent(getApplicationContext(), OrderStatusActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_manage) {
            Users user = Users.findById(Users.class, (long) 1);
            if (user != null && !user.userid.equals("0")) {
                dologin(user.email, user.password);
            }

            Intent intent = new Intent(getApplicationContext(), WishListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(getApplicationContext(), LanguageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(getApplicationContext(), NotiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sends) {
            Intent intent = new Intent(getApplicationContext(), CurrencyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_shareapp) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            if (app.getLanguage().equals("ar")) {
                sendIntent.putExtra(Intent.EXTRA_TEXT, share_message_ar);
            } else {
                sendIntent.putExtra(Intent.EXTRA_TEXT, share_message_en);
            }
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_rateapp) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {

            }
        } else if (id == R.id.nav_terms) {
            Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_contact) {
            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
            intent.putExtra("title",context.getString(R.string.ContactUs));
            intent.putExtra("link","https://springrose.com.sa/contactus");
            startActivity(intent);
        }
        else if (id == R.id.chat) {



            startActivity(new Intent(context, LiveChatActivity.class));
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/logout/logout", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Users users = Users.findById(Users.class, (long) 1);
                            users.userid = "0";
                            users.save();

                            finish();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (volleyError instanceof NetworkError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ServerError) {
                            message(context, context.getString(R.string.Theservercouldnotbefound), jsObjRequest);
                        } else if (volleyError instanceof AuthFailureError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ParseError) {
                            message(context, context.getString(R.string.Parsingerror), jsObjRequest);
                        } else if (volleyError instanceof NoConnectionError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof TimeoutError) {
                            message(context, context.getString(R.string.ConnectionTimeOut), jsObjRequest);
                        }

                    }


                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void message(final Context context, String msg, final JsonObjectRequest jor) {

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                MySingleton.getInstance(context).addToRequestQueue(jor);
            }
        })
                .show();

    }



    public void dologin(String email, final String password) {

        JSONObject usercre = new JSONObject();
        try {
            usercre.put("email", email);
            usercre.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/login/login", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                String userid = response.getJSONObject("data").getString("customer_id");
                                String first = response.getJSONObject("data").getString("firstname");
                                String last = response.getJSONObject("data").getString("lastname");
                                String email = response.getJSONObject("data").getString("email");
                                String tele = response.getJSONObject("data").getString("telephone");
                                Users user = Users.findById(Users.class, (long) 1);
                                if (user == null) {
                                    user = new Users(userid, first, last, email, tele, password);
                                    user.save();
                                } else {
                                    user.userid = userid;
                                    user.first = first;
                                    user.last = last;
                                    user.email = email;
                                    user.tele = tele;
                                    user.password = password;
                                    user.save();
                                }


                            } else {


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (volleyError instanceof NetworkError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ServerError) {
                            message(context, context.getString(R.string.Theservercouldnotbefound), jsObjRequest);
                        } else if (volleyError instanceof AuthFailureError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ParseError) {
                            message(context, context.getString(R.string.Parsingerror), jsObjRequest);
                        } else if (volleyError instanceof NoConnectionError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof TimeoutError) {
                            message(context, context.getString(R.string.ConnectionTimeOut), jsObjRequest);
                        }

                    }


                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    public void getcart() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/cart/cart", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                JSONArray productslist = new JSONArray();
                                app.setItemInCart(true);
                            } else {
                                app.setItemInCart(false);

                            }

                            if (topmenu != null) {
                                if (app.isItemInCart()) {
                                    MenuItem searchItem = topmenu.findItem(R.id.action_search);
                                    searchItem.setIcon(R.drawable.iscart);
                                } else {
                                    MenuItem searchItem = topmenu.findItem(R.id.action_search);
                                    searchItem.setIcon(R.drawable.cart);
                                }
                            }

                            String x = "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError instanceof NetworkError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ServerError) {
                            message(context, context.getString(R.string.Theservercouldnotbefound), jsObjRequest);
                        } else if (volleyError instanceof AuthFailureError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ParseError) {
                            message(context, context.getString(R.string.Parsingerror), jsObjRequest);
                        } else if (volleyError instanceof NoConnectionError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof TimeoutError) {
                            message(context, context.getString(R.string.ConnectionTimeOut), jsObjRequest);
                        }

                    }


                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                // params.put("Authorization", "Bearer ee5375799c60356b872be97edaf836b8c91d3134" );
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
