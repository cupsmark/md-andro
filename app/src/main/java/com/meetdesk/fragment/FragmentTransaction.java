package com.meetdesk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.view.UIText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekobudiarto on 11/5/16.
 */
public class FragmentTransaction extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_TRANSACTION = "tag:fragment-transaction";

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton imagebuttonBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_transaction, container, false);
        return main_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null)
        {
            this.activity = (BaseActivity) activity;
            iFragment = (HelperGeneral.FragmentInterface) this.activity;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(activity != null)
        {
            init();
        }
    }

    private void init()
    {
        tabLayout = (TabLayout) activity.findViewById(R.id.transaction_tab);
        viewPager = (ViewPager) activity.findViewById(R.id.transaction_viewpager);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.transaction_imagebutton_back);

        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTab();
    }

    private void setupTab()
    {
        View tabHistory = LayoutInflater.from(activity).inflate(R.layout.tab_transaction, null, false);
        UIText textTabHistory = (UIText) tabHistory.findViewById(R.id.transaction_tab_item);
        textTabHistory.setText("HISTORY");
        tabLayout.getTabAt(0).setCustomView(tabHistory);

        View tabPending = LayoutInflater.from(activity).inflate(R.layout.tab_transaction, null, false);
        UIText textTabPending = (UIText) tabPending.findViewById(R.id.transaction_tab_item);
        textTabPending.setText("PENDING");
        tabLayout.getTabAt(1).setCustomView(tabPending);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentTransactionHistory(), "ONE");
        adapter.addFragment(new FragmentTransactionPending(), "TWO");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
