package com.consigliaviaggi.GUI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.consigliaviaggi.Entity.Gallery;
import com.consigliaviaggi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterGalleryPage extends BaseAdapter {

    private Context context;
    private ArrayList<Gallery> arrayList;

    public CustomAdapterGalleryPage(Context context, ArrayList<Gallery> listaGallery) {
        this.context = context;
        this.arrayList = listaGallery;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View r=convertView;
        CustomAdapterGalleryPage.ViewHolder viewHolder=null;
        if (r==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            r=layoutInflater.inflate(R.layout.gallery_listview_layout,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) r.getTag();

        Picasso.get().load(arrayList.get(position).getImmagine()).noFade().fit().centerCrop().into(viewHolder.imageViewGallery);
        return r;
    }

    class ViewHolder {
        ImageView imageViewGallery;
        TextView textViewNomeStruttura,textViewVoto,textViewDistanza;

        public ViewHolder(View view) {
            imageViewGallery = view.findViewById(R.id.imageViewListGallery);
        }
    }
}
