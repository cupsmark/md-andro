package com.meetdesk.fragment;

import android.app.Activity;
import android.os.AsyncTask;
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
import com.meetdesk.controller.ControllerProduct;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.EndlessRecyclerViewScrollListener;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIText;
import com.meetdesk.view.UIToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/5/16.
 */
public class FragmentHotlist extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_HOTLIST = "tag:fragment-hotlist";

    ImageButton imagebuttonBack;
    RecyclerView recycler;

    ArrayList<String> dataID, dataImage, dataTitle, dataDesc, dataRate;
    HotlistAdapter adapter;
    LazyImageLoader imageLoader;
    int l = 5, o = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_hotlist, container, false);
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
            fillData(true);
        }
    }

    private void init()
    {
        dataID = new ArrayList<String>();
        dataTitle = new ArrayList<String>();
        dataImage = new ArrayList<String>();
        dataDesc = new ArrayList<String>();
        dataRate = new ArrayList<String>();
        adapter = new HotlistAdapter();
        imageLoader = new LazyImageLoader(activity);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.hotlist_imagebutton_back);

        recycler = (RecyclerView) activity.findViewById(R.id.hotlist_recycler);

        final LinearLayoutManager recycleLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recycleLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new HelperGeneral.RecyclerTouchListener(activity, recycler, new HelperGeneral.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("dataID", dataID.get(position));
                FragmentDetail detail = new FragmentDetail();
                iFragment.onNavigate(detail, param);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(recycleLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fillData(false);
            }
        });
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    private void fillData(final boolean firstInit)
    {
        new AsyncTask<Void, Integer, String>()
        {

            boolean success = false;
            String msg;
            ArrayList<String> tempID, tempTitle, tempImage, tempDesc, tempRate, tempDate;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(firstInit)
                {
                    o = 0;
                }
                tempID = new ArrayList<String>();
                tempTitle = new ArrayList<String>();
                tempImage = new ArrayList<String>();
                tempRate = new ArrayList<String>();
                tempDesc = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                ControllerProduct product = new ControllerProduct(activity);
                product.setToken(auth.getKeyUserToken());
                product.setL(l);
                product.setO(o);
                product.executeHotList();
                if(product.getSuccess())
                {
                    success = true;
                    tempID.addAll(product.getDataID());
                    tempTitle.addAll(product.getDataTitle());
                    tempImage.addAll(product.getDataImage());
                    tempRate.addAll(product.getDataRate());
                    tempDesc.addAll(product.getDataDesc());
                    o = product.getOffset();
                }
                else
                {
                    success = false;
                    msg = product.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    if(tempID.size() > 0)
                    {
                        for(int i = 0;i < tempID.size();i++)
                        {
                            dataID.add(tempID.get(i));
                            dataTitle.add(tempTitle.get(i));
                            dataImage.add(tempImage.get(i));
                            dataDesc.add(tempDesc.get(i));
                            dataRate.add(tempRate.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    new UIToast(activity, msg);
                }
            }
        }.execute();
    }




    public class HotlistAdapter extends RecyclerView.Adapter<HotlistAdapter.HotlistViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;


        public  HotlistAdapter()
        {

        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public HotlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_fragment_hotlist, parent, false);

            return new HotlistViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HotlistViewHolder holder, int position) {
            String desc = HelperGeneral.getLimitedWords(dataDesc.get(position), 10);
            holder.textTitle.setText(dataTitle.get(position));
            holder.textDesc.setText(desc);
            holder.textRate.setText(dataRate.get(position));
            imageLoader.showImage(HelperNative.getURL(11171) + dataImage.get(position), holder.viewIcon);
        }


        @Override
        public int getItemCount() {
            return dataID.size();
        }

        public class HotlistViewHolder extends RecyclerView.ViewHolder{
            ImageView viewIcon;
            UIText textTitle, textDesc, textRate;

            public HotlistViewHolder(View itemView) {
                super(itemView);
                viewIcon = (ImageView) itemView.findViewById(R.id.item_hotlist_image);
                textTitle = (UIText) itemView.findViewById(R.id.item_hotlist_title);
                textDesc = (UIText) itemView.findViewById(R.id.item_hotlist_desc);
                textRate = (UIText) itemView.findViewById(R.id.item_hotlist_rate);
            }
        }
        public boolean isHeader(int position) {
            return position == 0;
        }



    }

}
