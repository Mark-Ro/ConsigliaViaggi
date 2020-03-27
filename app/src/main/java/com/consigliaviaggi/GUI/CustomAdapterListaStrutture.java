package com.consigliaviaggi.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterListaStrutture extends BaseAdapter {

    private Context context;
    private ArrayList<Struttura> arrayList;

    public CustomAdapterListaStrutture(Context context, ArrayList<Struttura> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        ViewHolder viewHolder=null;
        if (r==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            r=layoutInflater.inflate(R.layout.strutture_listview_layout,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) r.getTag();

        Picasso.get().load(arrayList.get(position).getFotoStruttura()).noFade().fit().centerCrop().into(viewHolder.imageViewStruttura);
        viewHolder.textViewNomeStruttura.setText(arrayList.get(position).getNomeStruttura());
        viewHolder.textViewVoto.setText(String.valueOf(arrayList.get(position).getVoto()));

        return r;
    }
    class ViewHolder {
        ImageView imageViewStruttura;
        TextView textViewNomeStruttura,textViewVoto;

        public ViewHolder(View view) {
            imageViewStruttura = view.findViewById(R.id.imageViewStruttura);
            textViewNomeStruttura = view.findViewById(R.id.textViewNomeStruttura);
            textViewVoto = view.findViewById(R.id.textViewVoto);
        }
    }
}
