package com.ericyl.example.ui.activity;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.SearchInfo;
import com.ericyl.example.ui.BaseActivity;
import com.ericyl.example.ui.adapter.RVSearchSuggestionAdapter;
import com.ericyl.example.util.DatabaseUtils;
import com.ericyl.example.util.SearchSuggestionTableUtils;
import com.ericyl.utils.ui.widget.CustomSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ericyl.example.R.id.rv_suggestion;
import static com.ericyl.example.util.AppProperties.getContext;


public class SearchableActivity extends BaseActivity implements RVSearchSuggestionAdapter.IClickListener, CustomSearchView.ISearchViewController, SearchView.OnQueryTextListener, View.OnClickListener {

    @BindView(R.id.tv_search_result)
    TextView tvSearchResult;
    @BindView(R.id.shadow)
    View shadow;
    @BindView(R.id.search_bar)
    CustomSearchView searchView;
    @BindView(rv_suggestion)
    RecyclerView rvSuggestion;
    private ImageView btnClose;
    private RVSearchSuggestionAdapter suggestionAdapter;
    private List<SearchInfo> suggestions = new ArrayList<>();
    private List<SearchInfo> allSuggestions = new ArrayList<>();

    private String old;

    @Override
    public int getContentViewId() {
        return R.layout.activity_searchable;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    public void init(Bundle sa) {
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setSearchController(this);
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
        rvSuggestion.setVisibility(View.GONE);

        rvSuggestion.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSuggestion.hasFixedSize();
        suggestionAdapter = new RVSearchSuggestionAdapter(suggestions, new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (TextUtils.isEmpty(constraint)) {
                    results.values = allSuggestions;
                } else {
                    List<SearchInfo> searchInfos = new ArrayList<>();
                    for (SearchInfo searchInfo : allSuggestions) {
                        if (searchInfo.getTitle().contains(constraint)) {
                            searchInfos.add(searchInfo);
                        }
                    }
                    results.values = searchInfos;
                }
                return results;
            }

            @Override
            protected void publishResults(final CharSequence constraint, FilterResults results) {
                List<SearchInfo> list = (List<SearchInfo>) results.values;
                suggestions.clear();
                suggestions.addAll(list);
                suggestionAdapter.notifyDataSetChanged();

            }
        });
        suggestionAdapter.setClickListener(this);
        rvSuggestion.setAdapter(suggestionAdapter);
    }

    private Observable<Object[]> getInfos(String query) {
        return Observable.just(query).observeOn(Schedulers.io()).map(new Func1<String, Object[]>() {
            @Override
            public Object[] call(String s) {
                List<SearchInfo> searchInfos = new ArrayList<>();
                Uri uri = Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION);
                Cursor cursor = null;
                try {
//                    if (TextUtils.isEmpty(s))
                    cursor = getContext().getContentResolver().query(uri, new String[]{SearchSuggestionTableUtils.NAME},
                            null, null, null);
//                    else
//                        cursor = getContext().getContentResolver().query(uri, new String[]{SearchSuggestionTableUtils.NAME},
//                                SearchSuggestionTableUtils.NAME + " LIKE ?", new String[]{"%" + s + "%"}, null);
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
                return new Object[]{searchInfos, s};
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
            getInfos(query).flatMap(new Func1<Object[], Observable<Object[]>>() {
                @Override
                public Observable<Object[]> call(Object[] objects) {
                    return insertToDB(objects);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object[]>() {
                @Override
                public void call(Object[] objects) {
                    String query = (String) objects[1];
                    List<SearchInfo> list = (List<SearchInfo>) objects[0];
                    allSuggestions = list;
                    searchView.setText(query);
                    old = query;
                }
            });
            doSearch(query);
        }
    }

    private Observable<Object[]> insertToDB(Object[] objects) {
        return Observable.just(objects).observeOn(Schedulers.io()).map(new Func1<Object[], Object[]>() {
            @Override
            public Object[] call(Object[] objects) {
                String query = (String) objects[1];
                List<SearchInfo> searchInfos = (List<SearchInfo>) objects[0];
                Cursor cursor = null;
                try {
                    cursor = getContext().getContentResolver().query(Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION), new String[]{SearchSuggestionTableUtils.NAME},
                            SearchSuggestionTableUtils.NAME + " = ?", new String[]{query}, null);

                    if (cursor == null || !cursor.moveToFirst()) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(SearchSuggestionTableUtils.NAME, query);
                        getContext().getContentResolver().insert(Uri.parse(DatabaseUtils.URI_SEARCH_SUGGESTION), contentValues);
                        searchInfos.add(new SearchInfo(R.drawable.ic_history_black_24dp, query));
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
                return objects;
            }
        });

    }

    @OnClick({R.id.btn_back, R.id.shadow})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (shadow.getVisibility() == View.VISIBLE) {
                    searchView.setText(old);
                    searchView.clearFocus();
                } else finish();
                break;
            case R.id.shadow:
                searchView.setText(old);
                searchView.clearFocus();
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEARCH);
        intent.setPackage(getContext().getPackageName());
        intent.putExtra(SearchManager.QUERY, query);
        handleIntent(intent);
        searchView.clearFocus();
        suggestions.add(new SearchInfo(R.drawable.ic_history_black_24dp, query));
        suggestionAdapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        suggestionAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onItemClickListener(int position) {
        String query = suggestions.get(position).getTitle();
        old = query;
        searchView.setText(query);
        searchView.clearFocus();
        doSearch(query);
    }

    private void doSearch(String keyword) {
        tvSearchResult.setText(keyword);
    }

    @Override
    public void doClose() {
        searchView.setText(old);
        searchView.clearFocus();
    }
}
