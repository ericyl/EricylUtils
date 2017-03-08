package com.ericyl.example.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ericyl.example.R;
import com.ericyl.example.event.ChangeThemeEvent;
import com.ericyl.example.event.JumpNavPageEvent;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.example.ui.fragment.AboutFragment;
import com.ericyl.example.ui.fragment.FeedbackFragment;
import com.ericyl.example.ui.fragment.HelpFragment;
import com.ericyl.example.ui.fragment.barcode.BarcodeFragment;
import com.ericyl.example.ui.fragment.home.HomeFragment;
import com.ericyl.example.ui.fragment.network.NetworkFragment;
import com.ericyl.example.ui.fragment.utils.UtilsFragment;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.util.ActivityUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.layout_drawer_menu)
    NavigationView layoutDrawerMenu;
    @BindView(R.id.layout_drawer)
    DrawerLayout layoutDrawer;

    private FragmentManager fragmentManager;

    private static final String TAG = "TAG_FRAGMENTS";
//    private ArrayList<String> tags = new ArrayList<>();

    private String tag;
    private static Class[] classes;
    //    private static Integer[] tabIds;
    private static ArrayList<Integer> tabIdList;

    private ActionBarDrawerToggle drawerToggle;


    private boolean doubleBackToExitPressedOnce = false;

    private int navId;

    static {
        classes = new Class[]{HomeFragment.class, UtilsFragment.class, NetworkFragment.class, BarcodeFragment.class, FeedbackFragment.class, HelpFragment.class};
//        tabIds = new Integer[]{R.id.nav_main, R.id.nav_utils, R.id.nav_network, R.id.nav_barcode, R.id.nav_feedback, R.id.nav_help};
//        tabIdList = new ArrayList<>(Arrays.asList(tabIds));
        tabIdList = new ArrayList<Integer>() {
            {
                add(R.id.nav_main);
                add(R.id.nav_utils);
                add(R.id.nav_network);
                add(R.id.nav_barcode);
                add(R.id.nav_feedback);
                add(R.id.nav_help);
            }
        };
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null)
            tag = savedInstanceState.getString(TAG);
        initDrawerLayout();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setFragment();
    }

    private void setFragment() {
        String fragmentName = TextUtils.isEmpty(tag) ? HomeFragment.class.getSimpleName() : tag;
        setFragment(fragmentName, 0);
    }

    private void setFragment(String fragmentName, @IdRes int tabIdRes) {
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        for (Class cls : classes) {
            if (cls.getSimpleName().equals(fragmentName)) {
                try {
                    tag = fragmentName;
                    BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(tag);
                    if (fragment == null)
                        fragment = (BaseFragment) cls.newInstance();
                    fragment.setTabIdRes(tabIdRes);
                    fragmentManager.beginTransaction().replace(R.id.content, fragment, tag).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG, tag);
    }

    private void initDrawerLayout() {
        drawerToggle = new ActionBarDrawerToggle(this, layoutDrawer, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
                navigateWithItemId(navId, 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
                navId = 0;
            }

        };
        drawerToggle.syncState();
        layoutDrawer.addDrawerListener(drawerToggle);
        layoutDrawerMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                layoutDrawer.closeDrawer(GravityCompat.START);
                navId = menuItem.getItemId();
                return true;
            }


        });
    }


    private void navigateWithItemId(@IdRes int itemId, @IdRes int tabIdRes) {
        switch (itemId) {
            case R.id.nav_main:
            case R.id.nav_utils:
            case R.id.nav_network:
            case R.id.nav_barcode:
            case R.id.nav_help:
                String simpleName = classes[tabIdList.indexOf(itemId)].getSimpleName();
                if (!simpleName.equals(tag))
                    setFragment(simpleName, tabIdRes);
                break;
            case R.id.nav_about:
                AboutFragment aboutFragment = AboutFragment.newInstance();
                aboutFragment.show(fragmentManager, "AboutFragment");
                break;
            case R.id.nav_picture:
                ActivityUtils.jumpActivity(this, TakePhotoActivity.class);
                break;
            case R.id.nav_settings:
                ActivityUtils.jumpActivity(this, SettingActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        boolean flag = super.onOptionsItemSelected(item);
        boolean flag = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                flag = drawerToggle.onOptionsItemSelected(item);
                break;
        }
        return flag;
    }

    @Override
    public void onBackPressed() {
        if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
            layoutDrawer.closeDrawer(GravityCompat.START);
        } else {
            doBack();
        }
    }

    private void doBack() {
        if (!tag.equals(HomeFragment.class.getSimpleName())) {
            menuItemSetChecked(new JumpNavPageEvent(R.id.nav_main));
        } else {
            /**
             * double click the back button to exit
             */
            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
                /**
                 * like Wechat
                 */
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.click_back_again_to_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Subscribe
    public void menuItemSetChecked(JumpNavPageEvent event) {
        MenuItem menuItem = layoutDrawerMenu.getMenu().findItem(event.getIdRes());
        if (menuItem != null)
            menuItem.setChecked(event.isChecked());
        navigateWithItemId(event.getIdRes(), event.getTabIdRes());
    }

    @Subscribe
    public void changeTheme(ChangeThemeEvent event) {
        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
