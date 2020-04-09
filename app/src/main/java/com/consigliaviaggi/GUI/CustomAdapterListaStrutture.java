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
        if (arrayList.get(position).getDistanza()>=0)
            viewHolder.textViewDistanza.setText(String.valueOf(arrayList.get(position).getDistanza()) + "m");
        switch (arrotondaNumero(arrayList.get(position).getVoto())){

            case "1":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_1stelle);
                break;
            case "1.5":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_1_2stelle);
                break;
            case "2":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_2stelle);
                break;
            case "2.5":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_2_2stelle);
                break;
            case "3":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_3stelle);
                break;
            case "3.5":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_3_2stelle);
                break;
            case "4":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_4stelle);
                break;
            case "4.5":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_4_2stelle);
                break;
            case "5":
                viewHolder.imageViewStelle.setImageResource(R.drawable.ic_5stelle);
                break;

        }

        return r;
    }

    class ViewHolder {
        ImageView imageViewStruttura,imageViewStelle;
        TextView textViewNomeStruttura,textViewDistanza;

        public ViewHolder(View view) {
            imageViewStruttura = view.findViewById(R.id.imageViewStruttura);
            textViewNomeStruttura = view.findViewById(R.id.textViewNomeStruttura);
            imageViewStelle = view.findViewById(R.id.imageViewStella);
            textViewDistanza = view.findViewById(R.id.textViewDistanza);
        }
    }
    private String arrotondaNumero(float numero){
        int decimale= Integer.parseInt(String.valueOf(numero).substring(2,3));
        String risultato;
        if(decimale<5 || decimale>5){
            risultato=String.valueOf(Math.round(numero));
        }
        else{
            risultato=String.valueOf(numero).substring(0,3);
        }
        return risultato;
    }
}
