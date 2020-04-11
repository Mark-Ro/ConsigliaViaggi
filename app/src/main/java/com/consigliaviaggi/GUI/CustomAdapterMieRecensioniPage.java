package com.consigliaviaggi.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.R;


import java.util.ArrayList;

public class CustomAdapterMieRecensioniPage extends BaseAdapter {

    private Context context;
    private ArrayList<Recensione> arrayList;

    public CustomAdapterMieRecensioniPage(Context context, ArrayList<Recensione> arrayList) {
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
        CustomAdapterMieRecensioniPage.ViewHolder viewHolder=null;
        if (r==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            r=layoutInflater.inflate(R.layout.layout_mierecensioni_listview,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
            viewHolder = (CustomAdapterMieRecensioniPage.ViewHolder) r.getTag();

        viewHolder.textViewNomeStruttura.setText(arrayList.get(position).getNomeUtente());
        viewHolder.textViewTestoRecensione.setText(arrayList.get(position).getTesto());
        switch (arrayList.get(position).getVoto()) {
            case 1:
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_1stelle);
                break;
            case 2:
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_2stelle);
                break;
            case 3:
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_3stelle);
                break;
            case 4:
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_4stelle);
                break;
            case 5:
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_5stelle);
                break;
        }

        return r;
    }

    class ViewHolder {
        TextView textViewNomeStruttura,textViewTestoRecensione;
        ImageView imageViewStelle;

        public ViewHolder(View view) {
            textViewNomeStruttura = view.findViewById(R.id.textViewNomeStrutturaMieRecensioni);
            imageViewStelle = view.findViewById(R.id.imageViewStelle);
            textViewTestoRecensione = view.findViewById(R.id.textViewTestoMieRecensioni);
        }
    }
}
