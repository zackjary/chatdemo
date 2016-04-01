package com.coco.chat.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.coco.chat.R;
import com.coco.chat.base.BaseActivity;
import com.coco.chat.fragment.FindFragment;
import com.coco.chat.fragment.InteractFragment;
import com.coco.chat.fragment.MessageFragment;
import com.coco.chat.fragment.MineFragment;
import com.coco.chat.widget.ChangeColorTab;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private List<Fragment> mTabFrags;
    private List<ChangeColorTab> mTabIndicators;
    private FragmentPagerAdapter mAdapter;
    private String[] mTitles = new String[4];

    private ChangeColorTab mCurrTab;
    private PopupMenu mPopupMenu;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTabIndicators = new ArrayList<>(4);
        mTabIndicators.add((ChangeColorTab) findViewById(R.id.tab_message));
        mTabIndicators.add((ChangeColorTab) findViewById(R.id.tab_find));
        mTabIndicators.add((ChangeColorTab) findViewById(R.id.tab_interact));
        mTabIndicators.add((ChangeColorTab) findViewById(R.id.tab_mine));
    }

    @Override
    protected void initEvent() {
        for (ChangeColorTab tab : mTabIndicators) {
            tab.setOnClickListener(this);
        }
        setmCurrTab(0);
        mCurrTab.setAlpha(1.0f);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                System.out.println("onPageSelected....");
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ChangeColorTab left = mTabIndicators.get(position);
                    ChangeColorTab right = mTabIndicators.get(position + 1);
                    left.setAlpha(1 - positionOffset);
                    right.setAlpha(positionOffset);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        setmCurrTab(mViewPager.getCurrentItem());
                        System.out.println("onPageScrollStateChanged   SCROLL_STATE_IDLE");
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mTabFrags = new ArrayList<>(4);
        mTabFrags.add(new MessageFragment());
        mTabFrags.add(new FindFragment());
        mTabFrags.add(new InteractFragment());
        mTabFrags.add(new MineFragment());

        mTitles[0] = getString(R.string.text_tab_message);
        mTitles[1] = getString(R.string.text_tab_find);
        mTitles[2] = getString(R.string.text_tab_interact);
        mTitles[3] = getString(R.string.text_tab_mine);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mTabFrags.get(position);
            }

            @Override
            public int getCount() {
                return mTabFrags.size();
            }
        };
    }

    @Override
    public void onClick(View v) {
        mCurrTab.setAlpha(0);
        switch (v.getId()) {
            case R.id.tab_message:
                mViewPager.setCurrentItem(0, false);
                setmCurrTab(0);
                mCurrTab.setAlpha(1.0f);
                break;
            case R.id.tab_find:
                mViewPager.setCurrentItem(1, false);
                setmCurrTab(1);
                mCurrTab.setAlpha(1.0f);
                break;
            case R.id.tab_interact:
                mViewPager.setCurrentItem(2, false);
                setmCurrTab(2);
                mCurrTab.setAlpha(1.0f);
                break;
            case R.id.tab_mine:
                mViewPager.setCurrentItem(3, false);
                setmCurrTab(3);
                mCurrTab.setAlpha(1.0f);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                mPopupMenu = new PopupMenu(this, mToolbar);
                mPopupMenu.getMenuInflater().inflate(R.menu.menu_home_more, mPopupMenu.getMenu());
                //使用反射，强制显示菜单图标
                try {
                    Field field = mPopupMenu.getClass().getDeclaredField("mPopup");
                    field.setAccessible(true);
                    MenuPopupHelper mHelper = (MenuPopupHelper) field.get(mPopupMenu);
                    mHelper.setForceShowIcon(true);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //TODO
                        return false;
                    }
                });
                mPopupMenu.setGravity(Gravity.END);
                mPopupMenu.show();
                break;
            case R.id.action_search:
                //TODO
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setmCurrTab(int index) {
        if (mCurrTab != null) {
            mCurrTab.setClickable(true);
        }
        mCurrTab = mTabIndicators.get(index);
        mCurrTab.setClickable(false);
//        mToolbar.setTitle(mTitles[index]);
//        invalidateOptionsMenu();
    }
}
