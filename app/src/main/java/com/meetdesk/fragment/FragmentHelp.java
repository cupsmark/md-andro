package com.meetdesk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.util.RecyclerViewItemDivider;
import com.meetdesk.view.UIText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/4/16.
 */
public class FragmentHelp extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_HELP = "tag:fragment-help";

    ImageButton imagebuttonBack;
    RecyclerView recycler;
    ArrayList<String> dataID, dataTitle;
    AdapterHelp adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_help, container, false);
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
            getData();
        }
    }

    private void init()
    {
        dataID = new ArrayList<String>();
        dataTitle = new ArrayList<String>();
        adapter = new AdapterHelp();
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.help_imagebutton_back);
        recycler = (RecyclerView) activity.findViewById(R.id.help_recycler);

        final LinearLayoutManager recycleLayoutManager = new LinearLayoutManager(activity);
        RecyclerViewItemDivider divider = new RecyclerViewItemDivider(activity, RecyclerViewItemDivider.VERTICAL_LIST);
        recycler.addItemDecoration(divider);
        recycler.setLayoutManager(recycleLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new HelperGeneral.RecyclerTouchListener(activity, recycler, new HelperGeneral.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(dataID.get(position).equals("1"))
                {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentHelpAbout about = new FragmentHelpAbout();
                    iFragment.onNavigate(about, param);
                }
                if(dataID.get(position).equals("2"))
                {
                    /*Map<String, String> param = new HashMap<String, String>();
                    FragmentHelpTerm term = new FragmentHelpTerm();
                    iFragment.onNavigate(term, param);*/
                    HelperGeneral.openAppURL(activity, "https://www.meetdesk.id/term");
                }
                if(dataID.get(position).equals("3"))
                {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentHelpContact contact = new FragmentHelpContact();
                    iFragment.onNavigate(contact, param);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    private void getData()
    {
        dataID.add("1");
        dataID.add("2");
        dataID.add("3");

        dataTitle.add("About");
        dataTitle.add("Terms and Privacy Policy");
        dataTitle.add("Contact Us");

        adapter.notifyDataSetChanged();
    }



    public class AdapterHelp extends RecyclerView.Adapter<AdapterHelp.HelpViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;


        public  AdapterHelp()
        {

        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public HelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_fragment_help, parent, false);

            return new HelpViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HelpViewHolder holder, int position) {

            holder.textUser.setText(dataTitle.get(position));
        }


        @Override
        public int getItemCount() {
            return dataID.size();
        }

        public class HelpViewHolder extends RecyclerView.ViewHolder{
            UIText textUser;

            public HelpViewHolder(View itemView) {
                super(itemView);
                textUser = (UIText) itemView.findViewById(R.id.item_help_title);
            }
        }
        public boolean isHeader(int position) {
            return position == 0;
        }



    }
}
