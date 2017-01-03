package com.ericyl.example.ui.fragment.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Filter;

import com.ericyl.example.R;
import com.ericyl.example.model.ui.SearchInfo;
import com.ericyl.example.ui.adapter.RVSearchSuggestionAdapter;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.DatabaseUtils;
import com.ericyl.example.util.SearchSuggestionTableUtils;
import com.ericyl.utils.ui.widget.CustomSearchView;
import com.ericyl.utils.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class SearchDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener, View.OnClickListener, CustomSearchView.ISearchViewController, SearchView.OnQueryTextListener, RVSearchSuggestionAdapter.IClickListener {

    private List<SearchInfo> suggestions = new ArrayList<>();
    private List<SearchInfo> allSuggestions = new ArrayList<>();
    private RVSearchSuggestionAdapter adapter;
    private View searchContent;
    private View layoutSearchView;
    private CustomSearchView searchView;
    private RecyclerView rvSuggestion;
    private Animator animatorIn;
    private Animator animatorOut;
    private Animation animationIn;

    public static SearchDialogFragment newInstance() {
        return new SearchDialogFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_dialog, container, false);
        init(view);
        return view;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    showAnimatorIn();
                else
                    showAnimationIn();
            }
        });
    }

    private void init(View view) {
        view.findViewById(R.id.shadow).setOnClickListener(this);
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        searchContent = view.findViewById(R.id.search_content);
        layoutSearchView = view.findViewById(R.id.layout_search_view);

        searchView = (CustomSearchView) view.findViewById(R.id.search_bar);
        searchView.setSearchController(this);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSuggestionsAdapter(null);
        searchView.setOnQueryTextListener(this);

        rvSuggestion = (RecyclerView) view.findViewById(R.id.rv_suggestion);
        rvSuggestion.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSuggestion.hasFixedSize();
        adapter = new RVSearchSuggestionAdapter(suggestions, new Filter() {
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
                suggestions.clear();
                suggestions.addAll((List<SearchInfo>) results.values);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setClickListener(this);
        rvSuggestion.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfos(null);
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
                        cursor = AppProperties.getContext().getContentResolver().query(uri, new String[]{SearchSuggestionTableUtils.NAME},
                                null, null, null);
                    else
                        cursor = AppProperties.getContext().getContentResolver().query(uri, new String[]{SearchSuggestionTableUtils.NAME},
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
            public void call(List<SearchInfo> list) {
                allSuggestions = list;
                suggestions.clear();
                suggestions.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });


    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new SearchDialog(getActivity(), R.style.AppTheme);
        Window localWindow = dialog.getWindow();
        if (localWindow != null) {
            localWindow.setBackgroundDrawableResource(android.R.color.transparent);
            localWindow.setDimAmount(0.0F);
        }
        dialog.setOnKeyListener(this);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
            case R.id.shadow:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animatorIn != null && animatorIn.isRunning())
                        return;
                    if (animatorOut != null && animatorOut.isRunning())
                        return;
                } else {
                    if (animationIn != null && !animationIn.hasEnded())
                        return;
                }
                dismiss();
                break;
        }
    }


    @Override
    public void doClose() {
        this.dismiss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.ACTION_DOWN:
            case KeyEvent.KEYCODE_BACK:
                SearchDialogFragment.this.dismiss();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        dismiss();
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        if (adapter != null)
            adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onItemClickListener(int position) {
//        searchView.setQuery(suggestions.get(position).getTitle(), true);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEARCH);
        intent.setPackage(getContext().getPackageName());
        intent.putExtra(SearchManager.QUERY, suggestions.get(position).getTitle());
        startActivity(intent);
        this.dismiss();
    }

    @Override
    public void dismiss() {
        searchView.setText(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showAnimatorOut();
        } else {
            super.dismiss();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showAnimatorIn() {
        animatorIn = ViewAnimationUtils.createCircularReveal(layoutSearchView, layoutSearchView.getWidth() - (int) DisplayUtils.dp2px(SearchDialogFragment.this.getContext(), 32, -1), layoutSearchView.getHeight() / 2, 0, (float) Math.hypot(layoutSearchView.getWidth() - 16, layoutSearchView.getHeight() / 2));

        animatorIn.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                searchContent.setVisibility(View.VISIBLE);
                rvSuggestion.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rvSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                rvSuggestion.setVisibility(View.GONE);
            }
        });
        animatorIn.start();
    }

    private void showAnimationIn() {
        rvSuggestion.setVisibility(View.GONE);
        animationIn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0F, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F);
        animationIn.setInterpolator(new LinearInterpolator());
        animationIn.setDuration(300);
        animationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rvSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rvSuggestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }


        });
        layoutSearchView.startAnimation(animationIn);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showAnimatorOut() {
        animatorOut =
                ViewAnimationUtils.createCircularReveal(layoutSearchView, layoutSearchView.getWidth() - (int) DisplayUtils.dp2px(this.getContext(), 32, -1), layoutSearchView.getHeight() / 2, (float) Math.hypot(layoutSearchView.getWidth() - 16, layoutSearchView.getHeight() / 2), 0);

        animatorOut.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                rvSuggestion.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                SearchDialogFragment.super.dismiss();
            }


        });
        animatorOut.start();
    }

    class SearchDialog extends Dialog {
        public SearchDialog(Context context) {
            super(context);
        }

        public SearchDialog(Context context, int theme) {
            super(context, theme);
        }

        protected SearchDialog(Context context, boolean flag, OnCancelListener onCancelListener) {
            super(context, flag, onCancelListener);
        }

        @Override
        public void onBackPressed() {
            SearchDialogFragment.this.dismiss();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
