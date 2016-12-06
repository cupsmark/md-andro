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
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIDialogConfirm;
import com.meetdesk.view.UIDialogLoading;
import com.meetdesk.view.UIText;
import com.meetdesk.view.UIToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/5/16.
 */
public class FragmentWishList extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_WHISLIST = "tag:fragment-whislist";

    ImageButton imagebuttonBack;
    RecyclerView recycler;

    ArrayList<String> dataID, dataImage, dataTitle, dataDesc;
    ArrayList<Boolean> dataPrepareRemove;
    WishlistAdapter adapter;
    LazyImageLoader imageLoader;
    UIButton buttonEdit;
    boolean flagPrepareRemoving = true;
    int l = 10, o = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_wishlist, container, false);
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
            getData(false);
        }
    }

    private void init()
    {
        dataID = new ArrayList<String>();
        dataTitle = new ArrayList<String>();
        dataImage = new ArrayList<String>();
        dataDesc = new ArrayList<String>();
        dataPrepareRemove = new ArrayList<Boolean>();
        adapter = new WishlistAdapter();
        imageLoader = new LazyImageLoader(activity);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.wishlist_imagebutton_back);
        buttonEdit = (UIButton) activity.findViewById(R.id.wishlist_button_edit);
        recycler = (RecyclerView) activity.findViewById(R.id.wishlist_recycler);

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
                showDialogDelete(dataID.get(position));
            }
        }));
        recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(recycleLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getData(true);
            }
        });
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagPrepareRemoving) {
                    setAllPrepareRemove(true);
                    buttonEdit.setText("Done");
                    flagPrepareRemoving = false;
                } else {
                    setAllPrepareRemove(false);
                    buttonEdit.setText("Edit");
                    flagPrepareRemoving = true;
                }
            }
        });
    }

    private void getData(final boolean isLoadMore)
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            ArrayList<String> tempID, tempImage, tempTitle, tempDesc;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(!isLoadMore)
                {
                    o = 0;
                }
                tempID = new ArrayList<String>();
                tempImage = new ArrayList<String>();
                tempTitle = new ArrayList<String>();
                tempDesc = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                ControllerProduct product = new ControllerProduct(activity);
                product.setToken(auth.getKeyUserToken());
                product.setL(l);
                product.setO(o);
                product.executeWishList();
                if(product.getSuccess())
                {
                    success = true;
                    tempID.addAll(product.getDataID());
                    tempTitle.addAll(product.getDataTitle());
                    tempDesc.addAll(product.getDataDesc());
                    tempImage.addAll(product.getDataImage());
                    o = product.getOffset();
                }
                else {
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
                            dataDesc.add(tempDesc.get(i));
                            dataImage.add(tempImage.get(i));
                            dataPrepareRemove.add(false);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    new UIToast(activity, msg).show();
                }
            }
        }.execute();
    }

    private void deleteData(final String id)
    {
        new AsyncTask<Void, Integer, String>()
        {

            boolean success = false;
            String msg;
            UIDialogLoading dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new UIDialogLoading(activity);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "product"};
                String[] value = new String[]{auth.getKeyUserToken(), id};
                ControllerProduct product = new ControllerProduct(activity);
                product.setPostParameter(field, value);
                product.executeWishDelete();
                if(product.getSuccess())
                {
                    success = true;
                }
                msg = product.getMessage();
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if(success)
                {
                    for(int i = 0;i < dataID.size();i++)
                    {
                        if(dataID.get(i).equals(id))
                        {
                            dataID.remove(i);
                            dataTitle.remove(i);
                            dataImage.remove(i);
                            dataDesc.remove(i);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                new UIToast(activity, msg).show();
            }
        }.execute();
    }

    private void setAllPrepareRemove(boolean remove)
    {
        for(int i = 0;i < dataID.size();i++)
        {
            dataPrepareRemove.set(i, remove);
        }
        adapter.notifyDataSetChanged();
    }

    private void showDialogDelete(final String id)
    {
        final UIDialogConfirm dialogConfirm = new UIDialogConfirm(activity);
        dialogConfirm.setMessage("Are you sure delete this from wishlist?");
        dialogConfirm.setDialogTitle("Confirmation");
        dialogConfirm.show();
        dialogConfirm.getButtonYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();
                deleteData(id);
            }
        });
        dialogConfirm.getButtonNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();
            }
        });
    }

    public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;


        public  WishlistAdapter()
        {

        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public WishlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_fragment_wishlist, parent, false);

            return new WishlistViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final WishlistViewHolder holder, final int position) {
            String desc = HelperGeneral.getLimitedWords(dataDesc.get(position), 10);
            holder.textTitle.setText(dataTitle.get(position));
            holder.textDesc.setText(desc);
            imageLoader.showImage(HelperNative.getURL(11171) + dataImage.get(position), holder.viewIcon);

            if(dataPrepareRemove.get(position))
            {
                holder.buttonRemove.setVisibility(View.VISIBLE);
                holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogDelete(dataID.get(position));
                    }
                });
            }
            if(!dataPrepareRemove.get(position))
            {
                holder.buttonRemove.setVisibility(View.GONE);
            }
        }


        @Override
        public int getItemCount() {
            return dataID.size();
        }

        public class WishlistViewHolder extends RecyclerView.ViewHolder{
            ImageView viewIcon;
            UIText textTitle, textDesc;
            UIButton buttonRemove, buttonDetail;

            public WishlistViewHolder(View itemView) {
                super(itemView);
                viewIcon = (ImageView) itemView.findViewById(R.id.item_wishlist_image);
                textTitle = (UIText) itemView.findViewById(R.id.item_wishlist_title);
                textDesc = (UIText) itemView.findViewById(R.id.item_wishlist_desc);
                buttonRemove = (UIButton) itemView.findViewById(R.id.item_wishlist_remove);
                buttonDetail = (UIButton) itemView.findViewById(R.id.item_wishlist_detail);
            }
        }
        public boolean isHeader(int position) {
            return position == 0;
        }



    }
}
