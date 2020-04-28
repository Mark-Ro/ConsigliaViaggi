package com.consigliaviaggi.GUI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.R;


import java.util.ArrayList;

public class CustomAdapterRecensioniPage extends BaseAdapter {

    private Context context;
    private ArrayList<Recensione> arrayList;

    public CustomAdapterRecensioniPage(Context context, ArrayList<Recensione> arrayList) {
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
        CustomAdapterRecensioniPage.ViewHolder viewHolder=null;
        if (r==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            r=layoutInflater.inflate(R.layout.recensioni_listview_layout,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) r.getTag();

        viewHolder.textViewNomeUtenteRecensione.setText(arrayList.get(position).getNomeUtente());
        viewHolder.textViewTestoRecensione.setText(arrayList.get(position).getTesto());
        if(arrayList.get(position)!=null)
            Log.i("nome pubblico",arrayList.get(position).getNomeUtente());
        RelativeLayout.LayoutParams layoutParams;
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
        TextView textViewNomeUtenteRecensione,textViewTestoRecensione;
        ImageView imageViewStelle;

        public ViewHolder(View view) {
            textViewNomeUtenteRecensione = view.findViewById(R.id.textViewNomeUtenteRecensione);
            imageViewStelle = view.findViewById(R.id.imageViewStelle);
            textViewTestoRecensione = view.findViewById(R.id.textViewTestoRecensione);
        }
    }

    private int convertDpToPx(float dp){
        return (int)(dp*context.getResources().getDisplayMetrics().density);

    }

}
