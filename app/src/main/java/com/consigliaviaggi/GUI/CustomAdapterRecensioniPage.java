package com.consigliaviaggi.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        viewHolder.textViewVotoRecensioneAdapter.setText(String.valueOf(arrayList.get(position).getVoto()) + ".0");
        return r;
    }

    class ViewHolder {
        TextView textViewNomeUtenteRecensione,textViewVotoRecensioneAdapter,textViewTestoRecensione;

        public ViewHolder(View view) {
            textViewNomeUtenteRecensione = view.findViewById(R.id.textViewNomeUtenteRecensione);
            textViewVotoRecensioneAdapter = view.findViewById(R.id.textViewVotoRecensioneAdapter);
            textViewTestoRecensione = view.findViewById(R.id.textViewTestoRecensione);
        }
    }

}
