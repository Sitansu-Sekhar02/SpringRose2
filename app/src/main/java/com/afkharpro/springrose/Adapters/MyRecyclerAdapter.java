package com.afkharpro.springrose.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.Models.ViewModel;
import com.afkharpro.springrose.ProductActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.SeeAllActivity;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 03-Dec-17.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<ViewModel> items;

    private int itemLayout;
    Context context;
    App app = App.getInstance();



    public MyRecyclerAdapter(Context context, List<ViewModel> items, int itemLayout) {

        this.items = items;
        this.context = context;
        this.itemLayout = itemLayout;

    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

        return new ViewHolder(v);

    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {

        final ViewModel item = items.get(position);

        holder.text.setText(item.getTitle());

        HozitemAdapter adapter = new HozitemAdapter(context, 0, item.getItems());
        holder.lvTest.setAdapter(adapter);


        holder.seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeall(item.getID(),item.getTitle());
            }
        });

        if (item.getID()==3){
            holder.seeall.setVisibility(View.GONE);
        }


        holder.lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, ProductActivity.class);
                ArrayList<Hozitem> items = item.getItems();
                intent.putExtra("id", items.get(i).getID() );
                ArrayList<Hozitem> selected = new ArrayList<>();
            /*    if (item.getID() == 59) {
                    selected = app.getArrangments();
                    intent.putExtra("id", app.getArrangments().get(i).getID());
                } else if (item.getID() == 60) {
                    selected = app.getHand();
                    intent.putExtra("id", app.getHand().get(i).getID());
                } else if (item.getID() == 70) {
                    selected = app.getLonglife();
                    intent.putExtra("id", app.getLonglife().get(i).getID());
                } else if (item.getID() == 72) {
                    selected = app.getArtificial();
                    intent.putExtra("id", app.getArtificial().get(i).getID());

                } else if (item.getID() == 67) {
                    selected = app.getLove();
                    intent.putExtra("id", app.getLove().get(i).getID());

                } else if (item.getID() == 62) {
                    selected = app.getNewborn();
                    intent.putExtra("id", app.getNewborn().get(i).getID());

                } else if (item.getID() == 61) {
                    selected = app.getMother();
                    intent.putExtra("id", app.getMother().get(i).getID());

                } else if (item.getID() == 63) {
                    selected = app.getGraduation();
                    intent.putExtra("id", app.getGraduation().get(i).getID());
                } else if (item.getID() == 64) {
                    selected = app.getBirthday();
                    intent.putExtra("id", app.getBirthday().get(i).getID());

                } else if (item.getID() == 65) {
                    selected = app.getEid();
                    intent.putExtra("id", app.getEid().get(i).getID());

                } else if (item.getID() == 66) {
                    selected = app.getCoporate();
                    intent.putExtra("id", app.getCoporate().get(i).getID());
                }else if (item.getID() == 74) {
                    intent.putExtra("id", app.getSpecialities().get(i).getID());
                    selected = app.getSpecialities();
                }else if (item.getID() == 75) {
                    selected = app.getMixOccasions();
                    intent.putExtra("id", app.getMixOccasions().get(i).getID());
                }else if (item.getID() == 1) {
                    selected = app.getNewproducts();
                    intent.putExtra("id", app.getNewproducts().get(i).getID());
                }else if (item.getID() == 2) {
                    selected = app.getViewed();
                    intent.putExtra("id", app.getViewed().get(i).getID());
                }else if (item.getID() == 3) {
                    selected = app.getBestsellers();
                    intent.putExtra("id", app.getBestsellers().get(i).getID());
                }*/

                    context.startActivity(intent);



            }
        });



        holder.itemView.setTag(item);

    }


    @Override public int getItemCount() {

        return items.size();

    }
    public synchronized void seeall(int id, String title) {
        Intent i = new Intent(context, SeeAllActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        context.startActivity(i);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public Button seeall;
        public TextView text;
        public TwoWayView lvTest;


        public ViewHolder(View itemView) {

            super(itemView);

            seeall= itemView.findViewById(R.id.seebtn);
            text = (TextView) itemView.findViewById(R.id.title);
            lvTest = (TwoWayView) itemView.findViewById(R.id.lvItem);
        }

    }

}
