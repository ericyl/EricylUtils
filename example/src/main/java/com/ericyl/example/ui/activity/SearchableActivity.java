package com.ericyl.example.ui.activity;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgreenhalgh.android.simpleitemdecoration.linear.EndOffsetItemDecoration;
import com.ericyl.example.R;
import com.ericyl.example.model.ui_model.SearchInfo;
import com.ericyl.example.ui.adapter.RVSearchSuggestionAdapter;
import com.ericyl.example.util.DatabaseUtils;
import com.ericyl.example.util.SearchSuggestionTableUtils;
import com.ericyl.utils.ui.widget.CustomSearchView;
import com.ericyl.utils.ui.widget.support.recyclerview.BaseLinearLayoutManager;
import com.ericyl.utils.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ericyl.example.util.AppProperties.getContext;


public class SearchableActivity extends AppCompatActivity implements RVSearchSuggestionAdapter.IClickListener, SearchView.OnQueryTextListener, View.OnClickListener {

    private CustomSearchView searchView;
    private ImageView btnClose;
    private RecyclerView rvSuggestion;
    private TextView tvClearHistory;
    private BaseLinearLayoutManager layoutManager;
    private EndOffsetItemDecoration endOffsetItemDecoration;
    private RVSearchSuggestionAdapter suggestionAdapter;
    private List<SearchInfo> searchInfos = new ArrayList<>();
    private TextView tvSearchResult;
    private View shadow;


    private String old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        init();
        handleIntent(getIntent());

    }

    private void init() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        shadow = findViewById(R.id.shadow);
        searchView = (CustomSearchView) findViewById(R.id.search_bar);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setSuggestionsAdapter(null);
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(false);
        searchView.setFocusableInTouchMode(false);
        btnClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        btnClose.setAlpha(0F);
        shadow.setVisibility(View.GONE);
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btnClose.setAlpha(1F);
                    rvSuggestion.setVisibility(View.VISIBLE);
                    shadow.setVisibility(View.VISIBLE);
                } else {
                    btnClose.setAlpha(0F);
                    rvSuggestion.setVisibility(View.GONE);
                    shadow.setVisibility(View.GONE);
                }
            }
        });
        shadow.setOnClickListener(this);


        tvClearHistory = (TextView) findViewById(R.id.tv_clear_history);
        tvClearHistory.setOnClickListener(this);

        rvSuggestion = (RecyclerView) findViewById(R.id.rv_suggestion);
        rvSuggestion.setVisibility(View.GONE);
        rvSuggestion.setLayoutManager(layoutManager = new BaseLinearLayoutManager(getContext()));
        rvSuggestion.hasFixedSize();
        endOffsetItemDecoration = new EndOffsetItemDecoration((int) DisplayUtils.dp2px(getContext(), 48f, 0f));
        rvSuggestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (searchInfos.size() > 0 && layoutManager.isBottom(recyclerView)) {
                    tvClearHistory.setVisibility(View.VISIBLE);
                } else {
                    tvClearHistory.setVisibility(View.GONE);
                }
            }
        });
        suggestionAdapter = new RVSearchSuggestionAdapter(searchInfos, new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                getInfos(constraint.toString());
                FilterResults results = new FilterResults();
                results.values = searchInfos;
                results.count = searchInfos.size();
                return results;
            }

            @Override
            protected void publishResults(final CharSequence constraint, FilterResults results) {
                if (searchInfos.size() > 0)
                    rvSuggestion.addItemDecoration(endOffsetItemDecoration);
                else
                    rvSuggestion.removeItemDecoration(endOffsetItemDecoration);
                if (suggestionAdapter != null)
                    suggestionAdapter.notifyDataSetChanged();
            }
        });
        suggestionAdapter.setClickListener(this);
        rvSuggestion.setAdapter(suggestionAdapter);


        tvSearchResult = (TextView) findViewById(R.id.tv_search_result);

    }

    private void getInfos(String query) {
        Observable.just(query).observeOn(Schedulers.io()).map(new Func1<String, List<SearchInfo>>() {
            @Override
            public List<SearchInfo> call(String s) {
                List<SearchInfo> searchInfos = new ArrayList<>();
                Uri uri = Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION);
                Cursor cursor = null;
                try {
                    if (TextUtils.isEmpty(s))
                        cursor = getContext().getContentResolver().query(uri, new String[]{SearchSuggestionTableUtils.NAME},
                                null, null, null);
                    else
                        cursor = getContext().getContentResolver().query(uri, new String[]{SearchSuggestionTableUtils.NAME},
                                SearchSuggestionTableUtils.NAME + " LIKE ?", new String[]{"%" + s + "%"}, null);
                    if (cursor == null)
                        throw new RuntimeException("is null");
                    while (cursor.moveToNext()) {
                        searchInfos.add(new SearchInfo(R.drawable.ic_history_black_24dp,
                                cursor.getString(cursor.getColumnIndex(SearchSuggestionTableUtils.NAME))));
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
                return searchInfos;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<SearchInfo>>() {
            @Override
            public void call(List<SearchInfo> searchInfos1) {
                if (searchInfos == null)
                    searchInfos = new ArrayList<>();
                else
                    searchInfos.clear();
                searchInfos = searchInfos1;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            insertToDB(query);
            searchView.setQuery(query, false);
            searchMobileDocs(query);
            old = query;
            getInfos(query);
        }
    }

    private void insertToDB(String query) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION), new String[]{SearchSuggestionTableUtils.NAME},
                    SearchSuggestionTableUtils.NAME + " = ?", new String[]{query}, null);

            if (cursor == null || !cursor.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SearchSuggestionTableUtils.NAME, query);

                getContext().getContentResolver().insert(Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION), contentValues);
                searchInfos.add(new SearchInfo(R.drawable.ic_history_black_24dp, query));
                suggestionAdapter.notifyDataSetChanged();
            }
        } finally {


            if (cursor != null)
                cursor.close();
        }
    }

    private void searchMobileDocs(String keyWord) {
        tvSearchResult.setText(keyWord);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (shadow.getVisibility() == View.VISIBLE) {
                    searchView.setQuery(old, false);
                    searchView.clearFocus();
                } else finish();
                break;
            case R.id.shadow:
                searchView.setQuery(old, false);
                searchView.clearFocus();
                break;
            case R.id.tv_clear_history:
                getContext().getContentResolver().delete(Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION), null, null);
                searchInfos.clear();
                suggestionAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        suggestionAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onItemClickListener(int position) {
        String query = searchInfos.get(position).getTitle();
        old = query;
        searchView.clearFocus();
        searchView.setQuery(query, false);
        searchMobileDocs(query);
    }

}
