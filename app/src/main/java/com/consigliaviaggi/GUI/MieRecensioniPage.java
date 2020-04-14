package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.consigliaviaggi.Controller.MieRecensioniController;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.R;

import java.util.ArrayList;
import java.util.List;

public class MieRecensioniPage extends AppCompatActivity {

    private ListView listViewMieRecensioni;
    private SearchView searchView;
    private  MieRecensioniController mieRecensioniController;
    private ArrayList<Recensione> listaMieRecensioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mie_recensioni_page);

        listViewMieRecensioni = findViewById(R.id.listView);
        searchView=findViewById(R.id.searchView);
        mieRecensioniController = new MieRecensioniController(this,this);
        ArrayList<String> listaSuggeriemnti = new ArrayList<>();
        listaMieRecensioni = mieRecensioniController.getMieRecensioni();
        listaSuggeriemnti = mieRecensioniController.inizializzaSuggerimenti(listaMieRecensioni);
        CustomAdapterMieRecensioniPage customAdapterMieRecensioniPage = new CustomAdapterMieRecensioniPage(this,listaMieRecensioni);
        listViewMieRecensioni.setAdapter(customAdapterMieRecensioniPage);
        addSuggestion(listaSuggeriemnti,searchView);



        listViewMieRecensioni.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mieRecensioniController.openGestioneRecensionePage(listaMieRecensioni.get(position));
            }
        });
        closeSearchView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        listViewMieRecensioni = findViewById(R.id.listView);

        final MieRecensioniController mieRecensioniController = new MieRecensioniController(this,this);

        final ArrayList<Recensione> listaMieRecensioni = mieRecensioniController.getMieRecensioni();

        CustomAdapterMieRecensioniPage customAdapterMieRecensioniPage = new CustomAdapterMieRecensioniPage(this,listaMieRecensioni);
        listViewMieRecensioni.setAdapter(customAdapterMieRecensioniPage);

    }

    public void addSuggestion(final List<String> suggestions, final SearchView searchView){

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);

        searchView.setSuggestionsAdapter(suggestionAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
            public boolean onSuggestionClick(int position) {
                Cursor cursor= searchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                String suggestion =cursor.getString(2);//2 is the index of col containing suggestion name.
                searchView.setQuery(suggestion,true);//setting suggestion
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                effettuaRicerca(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Hit the network and take all the suggestions and store them in List 'suggestions'

                String[] columns = { BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                };
                MatrixCursor cursor = new MatrixCursor(columns);
                for (int i = 0; i < suggestions.size(); i++) {
                    if(suggestions.get(i).toLowerCase().contains(searchView.getQuery().toString().toLowerCase())) {
                        String[] tmp = {Integer.toString(i), suggestions.get(i), suggestions.get(i)};
                        cursor.addRow(tmp);
                    }
                }
                suggestionAdapter.swapCursor(cursor);
                return true;
            }
        });
    }
    private void effettuaRicerca(String query){
        if(!query.isEmpty()) {
            ArrayList<Recensione> listaRicerca = mieRecensioniController.ricercaRecensione(query, listaMieRecensioni);
            CustomAdapterMieRecensioniPage customAdapterMieRecensioniPage = new CustomAdapterMieRecensioniPage(this, listaRicerca);
            listViewMieRecensioni.setAdapter(customAdapterMieRecensioniPage);
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void closeSearchView(){
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAdapterMieRecensioniPage customAdapterMieRecensioniPage = new CustomAdapterMieRecensioniPage(MieRecensioniPage.this,listaMieRecensioni);
                listViewMieRecensioni.setAdapter(customAdapterMieRecensioniPage);
                searchView.setQuery("",false);
                closeKeyboard();
            }
        });
    }

}
