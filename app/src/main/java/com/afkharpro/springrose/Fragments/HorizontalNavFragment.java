package com.afkharpro.springrose.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afkharpro.springrose.Adapters.ArtHozitemAdapter;
import com.afkharpro.springrose.Adapters.HozitemAdapter;
import com.afkharpro.springrose.Adapters.MyRecyclerAdapter;
import com.afkharpro.springrose.Adapters.SearchAdapter;
import com.afkharpro.springrose.Adapters.SeeAllListAdapter;
import com.afkharpro.springrose.Adapters.WishlistAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.BuildConfig;
import com.afkharpro.springrose.MainActivity;
import com.afkharpro.springrose.MenuActivities.WishListActivity;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.Models.Featurecat;
import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.Models.ViewModel;
import com.afkharpro.springrose.OrderFlow.SetAddressesActivity;
import com.afkharpro.springrose.ProductActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.SeeAllActivity;
import com.afkharpro.springrose.ShopActivity;
import com.afkharpro.springrose.WebActivity;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.relex.circleindicator.CircleIndicator;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by murdi on 03-May-17.
 */

public class HorizontalNavFragment extends Fragment implements LocationListener {

    SearchView simpleSearchView;
    GoogleApiClient mGoogleApiClient;
    static final Integer GPS_SETTINGS = 0x7;
    static final Integer LOCATION = 0x1;
    private int identifier;
    JsonObjectRequest jsObjRequest;
    Context context;
    App app = App.getInstance();
    String url = "https://springrose.com.sa/index.php?route=feed/rest_api/products&category=";
    List<ViewModel> vmlist = new ArrayList<>();
    List<ViewModel> vmlisttest = new ArrayList<>();
    View rowView;
    Handler mHandler;
    SweetAlertDialog loading;
    ArrayList<Featurecat> catlist;
    ArrayList<Featurecat> catlistsorted;
    TextView results;
    RecyclerView recyclerView;
    SeeAllListAdapter adapter;
    ArrayList<JSONObject> productsarr;
    ViewPager viewPager;
    GridView gridView;
    ListView list;

    ProgressBar spinner;
    MapView mMapView;
    private GoogleMap googleMap;
    FastScroller fastScroller;
    double longitude;
    double latitude;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    boolean loveb, rateb, prob, bornb, artb, longlifeb, arragementsb, hand_bouquetb, motherdayb, graduationb, eidb, corporateb, birthdayb, bestsellersb, specialb, mixb;
    ScrollView sv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        context = getContext();
        identifier = args.getInt("identifier");
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        Typeface font = Typeface.createFromAsset(context.getAssets(), "tajawalregular.ttf");
        mHandler = new Handler();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {


        if (identifier == 0) {
            rowView = inflater.inflate(R.layout.deliveryagain, container, false);
            sv = (ScrollView) rowView.findViewById(R.id.scroll);
            ImageView wedd = (ImageView) rowView.findViewById(R.id.wedd);
            ImageView ballon = (ImageView) rowView.findViewById(R.id.balloon);
            LinearLayout number = (LinearLayout) rowView.findViewById(R.id.vp_horizontal_ntb2) ;

            number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:920012712"));

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.CALL_PHONE},
                                1002);
                        return;
                    }
                    context.startActivity(intent);
                }
            });


            PagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public int getCount() {
                    return app.getSlidersarr().size();
                }

                @Override
                public Fragment getItem(int position) {

                    Fragment fragment = new SilderFragment();


                    Bundle args = new Bundle();

                    args.putInt("identifier", position);
                    fragment.setArguments(args);


                    return fragment;
                }
            };


            viewPager = (ViewPager) rowView.findViewById(R.id.vp_horizontal_ntb);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(app.getSlidersarr().size() - 1);
            viewPager.setClipToPadding(false);
            viewPager.buildDrawingCache(true);
            viewPager.setDrawingCacheEnabled(true);

            catlist = new ArrayList<>();
            catlistsorted = new ArrayList<>();

            wedd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, WebActivity.class);
                    i.putExtra("title",context.getString(R.string.WeddingEvents));
                    i.putExtra("link","https://www.springrosewedding.com/");
                    startActivity(i);
                }
            });

            ballon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, WebActivity.class);
                    i.putExtra("title",context.getString(R.string.KidsPartiesBalloons));
                    i.putExtra("link","https://www.instagram.com/springrose_balloons");
                    startActivity(i);
                }
            });


            CircleIndicator indicator = (CircleIndicator) rowView.findViewById(R.id.indicator);

            indicator.setViewPager(viewPager);


            Runnable mUpdateTimeTask = new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() != (app.getSlidersarr().size() - 1)) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

                    } else {

                        viewPager.setCurrentItem(0, true);
                    }


                    mHandler.postDelayed(this, 4000);
                }
            };


            mUpdateTimeTask.run();

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
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.activateFetched();
                            } else {

                            }
                            checkremote();

                        }
                    });
            recyclerView = (RecyclerView) rowView.findViewById(R.id.rlist);

            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setHasFixedSize(true);







//


//            rowView.findViewById(R.id.seelove).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(67, context.getString(R.string.LoveandRomance));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seeborn).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(62, context.getString(R.string.NewBorn));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seeart).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(72, context.getString(R.string.Artifical));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seelong).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(70, context.getString(R.string.LongLifeFlowers));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seearrange).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(59, context.getString(R.string.Arrangements));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seehand).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(60, context.getString(R.string.HandBouquet));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seemother).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(61, context.getString(R.string.MothersDay));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seegrad).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(63, context.getString(R.string.Graduation));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seebirth).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(64, context.getString(R.string.Birthday));
//                }
//            });
////
//
//
//            rowView.findViewById(R.id.seeeid).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(65, context.getString(R.string.Eid));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seecorp).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(66, context.getString(R.string.Corporate));
//                }
//            });
//
//            rowView.findViewById(R.id.seespecial).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(74, context.getString(R.string.Specialities));
//                }
//            });
//
//            rowView.findViewById(R.id.seemix).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(75, context.getString(R.string.MixOccasions));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seenew).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(1, context.getString(R.string.NewProducts));
//                }
//            });

//
//            rowView.findViewById(R.id.seerate).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(2, context.getString(R.string.MostRated));
//                }
//            });
//
//
//            rowView.findViewById(R.id.seebest).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    seeall(3, context.getString(R.string.BestSellers));
//                }
//            });


            return rowView;
        } else if (identifier == 1) {
            rowView = inflater.inflate(R.layout.cat, container, false);


//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {


            if (app.getArrangments().size() == 0) {
                synchronized (this){
                    cathozilist(59, app.getArrangments());
                }

            } else {
                loadoffline(59, app.getArrangments());
            }

            if (app.getHand().size() == 0) {
                synchronized (this){
                    cathozilist(60, app.getHand());
                }

            } else {
                loadoffline(60, app.getHand());
            }

            if (app.getLonglife().size() == 0) {
                synchronized (this){
                    cathozilist(70, app.getLonglife());
                }

            } else {
                loadoffline(70, app.getLonglife());
            }

            if (app.getArtificial().size() == 0) {
                synchronized (this){
                    cathozilist(72, app.getArtificial());
                }

            } else {
                loadoffline(72, app.getArtificial());
            }


            if (app.getSpecialities().size() == 0) {
                synchronized (this){
                    cathozilist(74, app.getSpecialities());
                }

            } else {
                loadoffline(74, app.getSpecialities());
            }


            if (app.getMixOccasions().size() == 0) {
                synchronized (this){
                    cathozilist(75, app.getMixOccasions());
                }

            } else {
                loadoffline(75, app.getMixOccasions());
            }


//
//                }
//            }, 000);

            rowView.findViewById(R.id.seearrange).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(59, context.getString(R.string.Arrangements));
                }
            });


            rowView.findViewById(R.id.seehand).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(60, context.getString(R.string.HandBouquet));
                }
            });


            rowView.findViewById(R.id.seelong).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(70, context.getString(R.string.LongLifeFlowers));
                }
            });


            rowView.findViewById(R.id.seeart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(72, context.getString(R.string.Artifical));
                }
            });


            rowView.findViewById(R.id.seespecial).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(74, context.getString(R.string.Specialities));
                }
            });

            rowView.findViewById(R.id.seemix).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(75, context.getString(R.string.MixOccasions));
                }
            });

            return rowView;
        } else if (identifier == 2) {
            rowView = inflater.inflate(R.layout.occ, container, false);


//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {


            if (app.getLove().size() == 0) {
                synchronized (this){
                    cathozilist(67, app.getLove());
                }

            } else {
                loadoffline(67, app.getLove());
            }

            if (app.getNewborn().size() == 0) {
                synchronized (this){
                    cathozilist(62, app.getNewborn());
                }

            } else {
                loadoffline(62, app.getNewborn());
            }


            if (app.getMother().size() == 0) {
                synchronized (this){
                    cathozilist(61, app.getMother());
                }

            } else {
                loadoffline(61, app.getMother());
            }

            if (app.getGraduation().size() == 0) {
                synchronized (this){
                    cathozilist(63, app.getGraduation());
                }

            } else {
                loadoffline(63, app.getGraduation());
            }

            if (app.getBirthday().size() == 0) {
                synchronized (this){
                    cathozilist(64, app.getBirthday());
                }

            } else {
                loadoffline(64, app.getBirthday());
            }

            if (app.getEid().size() == 0) {
                synchronized (this){
                    cathozilist(65, app.getEid());
                }

            } else {
                loadoffline(65, app.getEid());
            }

            if (app.getCoporate().size() == 0) {
                synchronized (this){
                    cathozilist(66, app.getCoporate());
                }

            } else {
                loadoffline(66, app.getCoporate());
            }


//                }
//            }, 000);


            rowView.findViewById(R.id.seelove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(67, context.getString(R.string.LoveandRomance));
                }
            });


            rowView.findViewById(R.id.seeborn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(62, context.getString(R.string.NewBorn));
                }
            });


            rowView.findViewById(R.id.seemother).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(61, context.getString(R.string.MothersDay));
                }
            });


            rowView.findViewById(R.id.seegrad).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(63, context.getString(R.string.Graduation));
                }
            });


            rowView.findViewById(R.id.seebirth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(64, context.getString(R.string.Birthday));
                }
            });


            rowView.findViewById(R.id.seeeid).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(65, context.getString(R.string.Eid));
                }
            });


            rowView.findViewById(R.id.seecorp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeall(66, context.getString(R.string.Corporate));
                }
            });

            return rowView;
        } else if (identifier == 3) {
            rowView = inflater.inflate(R.layout.account, container, false);


            mMapView = (MapView) rowView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume(); // needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
                        return;
                    }


                    googleMap.setMyLocationEnabled(true);
                    googleMap.setBuildingsEnabled(true);
                    getbranches();

                    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    try {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();


                        LatLng sydney = new LatLng(latitude, longitude);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    } catch (Exception e) {
                        LatLng sydney = new LatLng(24.71647834777832, 46.65942001342);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            Intent intent = new Intent(Intent.ACTION_CALL);

                            intent.setData(Uri.parse("tel:" + marker.getSnippet()));

                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        1002);
                                return;
                            }
                            context.startActivity(intent);

                        }
                    });


                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            View v = getLayoutInflater(savedInstanceState).inflate(R.layout.marker, null);
                            TextView title = (TextView) v.findViewById(R.id.title);
                            final TextView info = (TextView) v.findViewById(R.id.info);
                            TextView comment = (TextView) v.findViewById(R.id.comment);
                            title.setText(marker.getTitle());
                            info.setText(marker.getSnippet());
                            comment.setText(marker.getTag().toString());


                            return v;
                        }
                    });

                }
            });


            return rowView;
        } else if (identifier == 4) {
            rowView = inflater.inflate(R.layout.search, container, false);

            gridView = (GridView) rowView.findViewById(R.id.listView2);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    try {
                        intent.putExtra("id", Integer.parseInt(productsarr.get(i).getString("id")));
                        if (!productsarr.get(i).getString("quantity").equals("0"))
                            startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
            spinner = (ProgressBar) rowView.findViewById(R.id.progressBar);
            simpleSearchView = (SearchView) rowView.findViewById(R.id.searchview);
            results = (TextView) rowView.findViewById(R.id.t1);
            simpleSearchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    simpleSearchView.onActionViewExpanded();
                }
            });


            simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    onlinesearch(query);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
                }
            });
            return rowView;

        }


        return null;
    }




    public synchronized void search(String query) {
        productsarr = new ArrayList<>();
        for (int i = 0; i < app.getSearcharr().size(); i++) {
            try {
                JSONArray catarr = app.getSearcharr().get(i).getJSONArray("category");
                String productcode = app.getSearcharr().get(i).getString("model");
                String descript = app.getSearcharr().get(i).getString("description");
                for (int j = 0; j < catarr.length(); j++) {
                    String catname = catarr.getJSONObject(j).getString("name");
                    if (catname.toLowerCase().contains(query) ||
                            productcode.contains(query)) {
                        productsarr.add(app.getSearcharr().get(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (productsarr.size() != 0) {
            adapter = new SeeAllListAdapter(context, 0, productsarr);
            list.setAdapter(adapter);
            results.setVisibility(View.VISIBLE);
        } else {
            onlinesearch(query);
        }

        spinner.setVisibility(View.GONE);

    }

    public synchronized void onlinesearch(String query) {
        productsarr = new ArrayList<>();
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=feed/rest_api/products&search=" + query, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                JSONArray productslist = response.getJSONArray("data");
                                for (int i = 0; i < productslist.length(); i++) {

                                    productsarr.add(productslist.getJSONObject(i));
                                }
                                if (productsarr.size() != 0) {
                                    adapter = new SeeAllListAdapter(context, 0, productsarr);
                                    gridView.setAdapter(adapter);
                                    results.setVisibility(View.VISIBLE);
                                } else {
                                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText(context.getString(R.string.NoResults))
                                            .show();
                                    results.setVisibility(View.GONE);
                                }

                            } else {
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(context.getString(R.string.NoResults))
                                        .show();
                                results.setVisibility(View.GONE);
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


    public synchronized void checkremote() {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=feed/rest_api/HomeProducts", null, new Response.Listener<JSONArray>() {



                    @Override
                    public void onResponse(JSONArray arr) {
                        try {

                            for ( int i = 0;i<arr.length();i++){
                                String heading = arr.getJSONObject(i).getString("heading").toLowerCase();
                            ViewModel  vm = new ViewModel();
                            vm.setID(i);
                            vm.setTitle(heading);
                                ArrayList<Hozitem> items = new ArrayList<>();

                                JSONArray products = arr.getJSONObject(i).getJSONArray("products");
                                for ( int j = 0;j<products.length();j++) {
                                    String name = products.getJSONObject(j).getString("name");
                                    String price = "";
                                    String originalprice = products.getJSONObject(j).getString("price");
                                    if (!products.getJSONObject(j).getString("special").equals("false")){
                                        price = products.getJSONObject(j).getString("special");
                                    }else{
                                        price = products.getJSONObject(j).getString("price");
                                    }

                                    String image = products.getJSONObject(j).getString("thumb");
                                    int id = products.getJSONObject(j).getInt("product_id");
                                    String quantity = "1";
                                    items.add(new Hozitem(image, name, price, id,originalprice,quantity));
                                }

                           vm.setItems(items);


                            vmlist.add(vm);


                                setVMlist();
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

        requestQueue.add(jsonArrayRequest);

        // jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);


























    /*   String feature_order = mFirebaseRemoteConfig.getString("features_order");
        List<String> elephantList = Arrays.asList(feature_order.split(","));

        bestsellersb = mFirebaseRemoteConfig.getBoolean("best_sellers");

        if (bestsellersb) {
            catlist.add(new Featurecat(3, context.getString(R.string.BestSellers), "best_sellers"));

        }


        specialb = mFirebaseRemoteConfig.getBoolean("specialities");

        if (specialb) {
            catlist.add(new Featurecat(74, context.getString(R.string.Specialities), "specialities"));

        }

        mixb = mFirebaseRemoteConfig.getBoolean("mix_occasions");

        if (mixb) {
            catlist.add(new Featurecat(75, context.getString(R.string.MixOccasions), "mix_occasions"));
        }


        artb = mFirebaseRemoteConfig.getBoolean("artificial_flowers");

        if (artb) {

            catlist.add(new Featurecat(72, context.getString(R.string.Artifical), "artificial_flowers"));


        }

        longlifeb = mFirebaseRemoteConfig.getBoolean("long_life_flowers");

        if (longlifeb) {

            catlist.add(new Featurecat(70, context.getString(R.string.LongLifeFlowers), "long_life_flowers"));


        }

        loveb = mFirebaseRemoteConfig.getBoolean("love_and_romance");

        if (loveb) {

            catlist.add(new Featurecat(67, context.getString(R.string.LoveandRomance), "love_and_romance"));

        }


        rateb = mFirebaseRemoteConfig.getBoolean("most_rated");

        if (rateb) {

            catlist.add(new Featurecat(2, context.getString(R.string.MostRated), "most_rated"));


        }

        prob = mFirebaseRemoteConfig.getBoolean("new_products");

        if (prob) {

            catlist.add(new Featurecat(1, context.getString(R.string.NewProducts), "new_products"));


        }

        bornb = mFirebaseRemoteConfig.getBoolean("new_born");

        if (bornb) {

            catlist.add(new Featurecat(62, context.getString(R.string.NewBorn), "new_born"));


        }


        arragementsb = mFirebaseRemoteConfig.getBoolean("arrangments");

        if (arragementsb) {

            catlist.add(new Featurecat(59, context.getString(R.string.Arrangements), "arrangments"));


        }


        hand_bouquetb = mFirebaseRemoteConfig.getBoolean("hand_bouquet");

        if (hand_bouquetb) {

            catlist.add(new Featurecat(60, context.getString(R.string.HandBouquet), "hand_bouquet"));


        }


        motherdayb = mFirebaseRemoteConfig.getBoolean("motherday");

        if (motherdayb) {

            catlist.add(new Featurecat(61, context.getString(R.string.MothersDay), "motherday"));

        }


        graduationb = mFirebaseRemoteConfig.getBoolean("graduation");

        if (graduationb) {

            catlist.add(new Featurecat(63, context.getString(R.string.Graduation), "graduation"));


        }


        birthdayb = mFirebaseRemoteConfig.getBoolean("birthday");

        if (birthdayb) {

            catlist.add(new Featurecat(64, context.getString(R.string.Birthday), "birthday"));

        }


        eidb = mFirebaseRemoteConfig.getBoolean("eid");

        if (eidb) {

            catlist.add(new Featurecat(65, context.getString(R.string.Eid), "eid"));
            // cathozilist(65, app.getEid());


        }


        corporateb = mFirebaseRemoteConfig.getBoolean("corporate");

        if (corporateb) {

            catlist.add(new Featurecat(66, context.getString(R.string.Corporate), "corporate"));
        }

        for (int i = 0; i < elephantList.size(); i++) {
            for (int y = 0; y < catlist.size(); y++) {
                if (elephantList.get(i).equals(catlist.get(y).getNick())) {
                    catlistsorted.add(catlist.get(y));
                }
            }
        }

        int catsortedsize = catlistsorted.size();

//        for (int y = 0; y < catlist.size(); y++) {
//            for (int i = 0; i < catsortedsize; i++) {
//                if (catlist.get(y).getNick().equals(catlistsorted.get(i).getNick())) {
//                    catlist.remove(y);
//                }
//            }
//        }
//
//        for (int y = 0; y < catlist.size(); y++) {
//            catlistsorted.add(catlist.get(y));
//        }


        for (int i = 0; i < catlistsorted.size(); i++) {
            if (catlistsorted.get(i).getID() != 1 && catlistsorted.get(i).getID() != 2 && catlistsorted.get(i).getID() != 3) {
                createMockList(catlistsorted.get(i));
            }
            if (catlistsorted.get(i).getID() == 1 || catlistsorted.get(i).getID() == 2) {
                ArrayList<Hozitem> hozi = new ArrayList<>();
                try {
                    cathozilistsearch(hozi, catlistsorted.get(i).getID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (catlistsorted.get(i).getID() == 3) {
                ArrayList<Hozitem> hozi = new ArrayList<>();
                bslist(hozi);
            }


        }


*/



    }

    public void setVMlist(){
        if (vmlist.size()==catlistsorted.size()) {
            for (int i = 0; i < catlistsorted.size(); i++) {
                for (int y = 0; y < vmlist.size(); y++) {
                    if (catlistsorted.get(i).getID() == (vmlist.get(y).getID())) {
                        vmlisttest.add(vmlist.get(y));
                    }
                }
            }

        }
        recyclerView.setAdapter(new MyRecyclerAdapter(getContext(), vmlist, R.layout.item));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public synchronized void loadoffline(int catid, ArrayList<Hozitem> items) {
        if (catid == 59) {


            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest = (TwoWayView) rowView.findViewById(R.id.lvItemsarrange);
            lvTest.setAdapter(adapter);
            lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getArrangments().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getArrangments().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 60) {


            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest2 = (TwoWayView) rowView.findViewById(R.id.lvItemshand);
            lvTest2.setAdapter(adapter);
            lvTest2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getHand().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getHand().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 70) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemslong);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getLonglife().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getLonglife().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 72) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsart);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getArtificial().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getArtificial().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 67) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemslove);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getLove().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getLove().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 62) {


            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsborn);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getNewborn().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getNewborn().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 61) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsmother);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getMother().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getMother().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 63) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsgrad);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getGraduation().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getGraduation().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 64) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsbirth);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getBirthday().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getBirthday().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 65) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemseid);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getEid().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getEid().get(i).getID());
                        startActivity(intent);
                    }
                }
            });


        } else if (catid == 66) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemscorp);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getCoporate().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getCoporate().get(i).getID());
                        startActivity(intent);
                    }
                }
            });

        } else if (catid == 74) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsspecial);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getSpecialities().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getSpecialities().get(i).getID());
                        startActivity(intent);
                    }
                }
            });

        } else if (catid == 75) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsmix);
            lvTest3.setAdapter(adapter);

            lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    if (!app.getMixOccasions().get(i).getQuantity().equals("0")){
                        intent.putExtra("id", app.getMixOccasions().get(i).getID());
                        startActivity(intent);
                    }
                }
            });

        }
    }

    public synchronized void loadofflinesearch(final ArrayList<Hozitem> items, final int choice) {
        if (choice == 1) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest = (TwoWayView) rowView.findViewById(R.id.lvItemsnew);
            lvTest.setAdapter(adapter);
            lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("id", app.getNewproducts().get(i).getID());
                    startActivity(intent);
                }
            });


        } else if (choice == 2) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest = (TwoWayView) rowView.findViewById(R.id.lvItemsrate);
            lvTest.setAdapter(adapter);
            lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("id", app.getViewed().get(i).getID());
                    startActivity(intent);
                }
            });

        } else if (choice == 3) {

            HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
            TwoWayView lvTest = (TwoWayView) rowView.findViewById(R.id.lvItemsbest);
            lvTest.setAdapter(adapter);
            lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("id", app.getBestsellers().get(i).getID());
                    startActivity(intent);
                }
            });

        }
    }

    public synchronized void cathozilist(final int catid, final ArrayList<Hozitem> items) {


        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url + catid + "&limit=6", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray arr = response.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                String name = arr.getJSONObject(i).getString("name");
                                String price = arr.getJSONObject(i).getString("price_formated");
                                String originalprice = arr.getJSONObject(i).getString("price_formated");
                                if (!arr.getJSONObject(i).getString("special_formated").equals("false")){
                                    price = arr.getJSONObject(i).getString("special_formated");
                                }else{
                                    price = arr.getJSONObject(i).getString("price_formated");
                                }

                                String image = arr.getJSONObject(i).getString("image");
                                int id = arr.getJSONObject(i).getInt("id");
                                String quantity = arr.getJSONObject(i).getString("quantity");
                                items.add(new Hozitem(image, name, price, id, originalprice,quantity));
                                app.getSearcharr().add(arr.getJSONObject(i));
                            }

                            Log.v("###########catid ", String.valueOf(catid));
                            if (catid == 59) {

                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest = (TwoWayView) rowView.findViewById(R.id.lvItemsarrange);
                                lvTest.setAdapter(adapter);
                                lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getArrangments().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getArrangments().get(i).getID());
                                            startActivity(intent);
                                        }

                                    }
                                });

                            } else if (catid == 60) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest2 = (TwoWayView) rowView.findViewById(R.id.lvItemshand);
                                lvTest2.setAdapter(adapter);
                                lvTest2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getHand().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getHand().get(i).getID());
                                            startActivity(intent);
                                        }

                                    }
                                });

                            } else if (catid == 70) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemslong);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getLonglife().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getLonglife().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else if (catid == 72) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsart);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getArtificial().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getArtificial().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });


                            } else if (catid == 67) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemslove);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getLove().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getLove().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 62) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsborn);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getNewborn().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getNewborn().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 61) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsmother);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getMother().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getMother().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 63) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsgrad);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getGraduation().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getGraduation().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 64) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsbirth);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getBirthday().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getBirthday().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 65) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemseid);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getEid().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getEid().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 66) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemscorp);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getCoporate().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getCoporate().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 74) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsspecial);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getSpecialities().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getSpecialities().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else if (catid == 75) {
                                HozitemAdapter adapter = new HozitemAdapter(context, 0, items);
                                TwoWayView lvTest3 = (TwoWayView) rowView.findViewById(R.id.lvItemsmix);
                                lvTest3.setAdapter(adapter);

                                lvTest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(context, ProductActivity.class);
                                        if (!app.getMixOccasions().get(i).getQuantity().equals("0")){
                                            intent.putExtra("id", app.getMixOccasions().get(i).getID());
                                            startActivity(intent);
                                        }
                                    }
                                });

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.v("###########URL ", url + catid + "&limit=6");
                        String s = url;
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

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }


    public synchronized void createMockList(final Featurecat cat) {


        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url + cat.getID() + "&limit=6", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            ViewModel vm;
                            ArrayList<Hozitem> items = new ArrayList<>();
                            JSONArray arr = response.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                String name = arr.getJSONObject(i).getString("name");
                                String price = arr.getJSONObject(i).getString("price_formated");
                                String originalprice = arr.getJSONObject(i).getString("price_formated");
                                if (!arr.getJSONObject(i).getString("special_formated").equals("false")){
                                    price = arr.getJSONObject(i).getString("special_formated");
                                }else{
                                    price = arr.getJSONObject(i).getString("price_formated");
                                }

                                String image = arr.getJSONObject(i).getString("image");
                                int id = arr.getJSONObject(i).getInt("id");
                                String quantity = arr.getJSONObject(i).getString("quantity");
                                items.add(new Hozitem(image, name, price, id,originalprice,quantity));
                            }
                            vm = new ViewModel();
                            vm.setID(cat.getID());
                            vm.setTitle(cat.getTitle());
                            vm.setItems(items);


                            vmlist.add(vm);


                            setVMlist();


                        } catch (JSONException e) {
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

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);



    }


    public synchronized void bslist(final ArrayList<Hozitem> items) {


        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=feed/rest_api/bestsellers", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ViewModel vm;
                            JSONArray arr = response.getJSONObject("data").getJSONArray("products");
                            for (int i = 0; i < arr.length(); i++) {
                                String name = arr.getJSONObject(i).getString("name");
                                String price = arr.getJSONObject(i).getString("price_formated");
                                String originalprice = arr.getJSONObject(i).getString("price_formated");
                                if (!arr.getJSONObject(i).getString("special_formated").equals("false")){
                                    price = arr.getJSONObject(i).getString("special_formated");
                                }else{
                                    price = arr.getJSONObject(i).getString("price_formated");
                                }
                                String image = arr.getJSONObject(i).getString("thumb");
                                int id = Integer.parseInt(arr.getJSONObject(i).getString("product_id"));
                                String quantity = arr.getJSONObject(i).getString("quantity");
                                items.add(new Hozitem(image, name, price, id,originalprice,quantity));
                                app.getSearcharr().add(arr.getJSONObject(i));
                                app.getBestsellers().add(new Hozitem(image, name, price, id,originalprice,quantity));
                            }

                            vm = new ViewModel();
                            vm.setID(3);
                            vm.setTitle(context.getString(R.string.BestSellers));
                            vm.setItems(items);
                            vmlist.add(vm);

                           setVMlist();
                        } catch (JSONException e) {
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


    public synchronized void cathozilistsearch(final ArrayList<Hozitem> items, final int choice) throws JSONException {


        JSONObject ser = new JSONObject();
        if (choice == 1) {
            ser.put("sort", "date_added");
            ser.put("order", "desc");
        } else if (choice == 2) {
            ser.put("sort", "Rating");
            ser.put("order", "desc");
        }


        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=feed/rest_api/search&limit=6", ser, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ViewModel vm;
                            JSONArray arr = response.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                String name = arr.getJSONObject(i).getString("name");
                                String price = arr.getJSONObject(i).getString("price_formated");
                                String originalprice = arr.getJSONObject(i).getString("price_formated");

                                if (!arr.getJSONObject(i).getString("special_formated").equals("false")){
                                    price = arr.getJSONObject(i).getString("special_formated");
                                }else{
                                    price = arr.getJSONObject(i).getString("price_formated");
                                }
                                String image = arr.getJSONObject(i).getString("image");
                                int id = arr.getJSONObject(i).getInt("id");
                                String quantity = arr.getJSONObject(i).getString("quantity");
                                items.add(new Hozitem(image, name, price, id,originalprice,quantity));
                                app.getSearcharr().add(arr.getJSONObject(i));
                                if (choice == 1) {
                                    app.getNewproducts().add(new Hozitem(image, name, price, id,originalprice,quantity));

                                } else if (choice == 2) {
                                    app.getViewed().add(new Hozitem(image, name, price, id,originalprice,quantity));

                                }

                            }


                            if (choice == 1) {
                                vm = new ViewModel();
                                vm.setID(choice);
                                vm.setTitle(context.getString(R.string.NewProducts));
                                vm.setItems(items);
                                vmlist.add(vm);

                            } else if (choice == 2) {
                                vm = new ViewModel();
                                vm.setID(choice);
                                vm.setTitle(context.getString(R.string.MostRated));
                                vm.setItems(items);
                                vmlist.add(vm);

                            }
                            setVMlist();

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


    public synchronized void seeall(int id, String title) {
        Intent i = new Intent(context, SeeAllActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        startActivity(i);
    }

    public synchronized void getbranches() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=feed/rest_api/stores", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                JSONArray productslist = new JSONArray();
                                productslist = response.getJSONArray("data");
                                LatLng sydney;
                                for (int i = 0; i < productslist.length(); i++) {
                                    app.getBranchesarr().add(productslist.getJSONObject(i));
                                    String geocode = productslist.getJSONObject(i).getString("geocode");
                                    String splitted[] = geocode.split(",", 2);
//                                    String latitude = geocode.substring( 0, geocode.indexOf(","));
//                                    String longitude = geocode.substring( 1, geocode.indexOf(","));
                                    String latitude = splitted[0];
                                    String longitude = splitted[1];
                                    double lat = Double.parseDouble(latitude);
                                    double lon = Double.parseDouble(longitude);
                                    sydney = new LatLng(lat, lon);
                                    googleMap.addMarker(new MarkerOptions().position(sydney)
                                            .title(productslist.getJSONObject(i).getString("name"))
                                            .snippet(productslist.getJSONObject(i).getString("telephone"))

                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapsrlogo)))
                                            .setTag(productslist.getJSONObject(i).getString("comment"));


                                }


                            } else {


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

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            }
        } else {
            //  Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } catch (Exception ex) {

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public synchronized void message(final Context context, String msg, final JsonObjectRequest jor) {

        SweetAlertDialog pDialog = new SweetAlertDialog((Activity) context, SweetAlertDialog.WARNING_TYPE);

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


}
