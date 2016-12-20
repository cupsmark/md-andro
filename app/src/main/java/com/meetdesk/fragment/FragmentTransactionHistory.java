package com.meetdesk.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerBooking;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.EndlessListViewScrollListener;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIText;
import com.meetdesk.view.UIToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/6/16.
 */
public class FragmentTransactionHistory extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_TRANSACTION_HISTORY = "tag:fragment-transaction-history";

    ExpandableListView listview;
    Map<String, String> param;
    ArrayList<String> bookingDetailID, productTitle, parentStatus, productDesc, packageID, bookingDetailDate, bookingDetailCode, packageDesc, bookingDetailAmount;
    ArrayList<Boolean> parentShowRemoveButton;
    TransactionHistoryAdapter adapter;
    LazyImageLoader imageLoader;
    HashMap<String, ArrayList<String>> childData;
    int l = 10, o = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
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
            getData(true);
        }
    }

    private void init()
    {
        param = getParameter();
        bookingDetailID = new ArrayList<String>();
        productTitle = new ArrayList<String>();
        parentStatus = new ArrayList<String>();
        productDesc = new ArrayList<String>();
        parentShowRemoveButton = new ArrayList<Boolean>();
        packageID = new ArrayList<String>();
        bookingDetailDate = new ArrayList<String>();
        bookingDetailCode = new ArrayList<String>();
        packageDesc = new ArrayList<String>();
        bookingDetailAmount = new ArrayList<String>();
        childData = new HashMap<String, ArrayList<String>>();
        imageLoader = new LazyImageLoader(activity);
        adapter = new TransactionHistoryAdapter();
        listview = (ExpandableListView) activity.findViewById(R.id.transaction_history_listview);
        listview.setAdapter(adapter);
        listview.setOnScrollListener(new EndlessListViewScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getData(false);
            }
        });
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        param = getParameter();

    }

    private void getData(final boolean isFirstLoad)
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            ArrayList<String> tempBookingID, tempProductTitle, tempParentStatus, tempProductDesc, tempPackageID,
                    tempBookingDate, tempBookingCode, tempPackageDesc, tempBookingAmount;
            ArrayList<Boolean> tempShowRemove;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(isFirstLoad)
                {
                    o = 0;
                }
                tempBookingID = new ArrayList<String>();
                tempProductTitle = new ArrayList<String>();
                tempParentStatus = new ArrayList<String>();
                tempProductDesc = new ArrayList<String>();
                tempPackageID = new ArrayList<String>();
                tempBookingDate = new ArrayList<String>();
                tempBookingCode = new ArrayList<String>();
                tempPackageDesc = new ArrayList<String>();
                tempBookingAmount = new ArrayList<String>();
                tempShowRemove = new ArrayList<Boolean>();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                ControllerBooking booking = new ControllerBooking(activity);
                booking.setO(o);
                booking.setL(l);
                booking.setToken(auth.getKeyUserToken());
                booking.executeBookingList();
                if(booking.getSuccess())
                {
                    success = true;
                    tempBookingID.addAll(booking.getBookingDetailID());
                    tempProductTitle.addAll(booking.getProductTitle());
                    tempProductDesc.addAll(booking.getProductDesc());
                    tempPackageID.addAll(booking.getPackagePriceID());
                    tempPackageDesc.addAll(booking.getPackagePriceDesc());
                    tempBookingDate.addAll(booking.getBookingDetailDate());
                    tempBookingCode.addAll(booking.getBookingDetailCode());
                    tempBookingAmount.addAll(booking.getBookingDetailAmount());
                    o = booking.getOffset();
                }
                else
                {
                    success = false;
                    msg = booking.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    if(tempBookingID.size() > 0)
                    {
                        for(int i = 0;i < tempBookingID.size();i++)
                        {
                            bookingDetailID.add(tempBookingID.get(i));
                            productTitle.add(tempProductTitle.get(i));
                            parentStatus.add("Pending");
                            productDesc.add(tempProductDesc.get(i));
                            packageID.add(tempPackageID.get(i));
                            bookingDetailDate.add(tempBookingDate.get(i));
                            bookingDetailCode.add(tempBookingCode.get(i));
                            packageDesc.add(tempPackageDesc.get(i));
                            bookingDetailAmount.add(tempBookingAmount.get(i));
                            ArrayList<String> subChild1 = new ArrayList<String>();
                            subChild1.add(tempPackageID.get(i));
                            childData.put(tempBookingID.get(i), subChild1);
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



/*
        ArrayList<String> subChild2 = new ArrayList<String>();
        subChild2.add(packageID.get(1));
        ArrayList<String> subChild3 = new ArrayList<String>();
        subChild3.add(packageID.get(2));
        ArrayList<String> subChild4 = new ArrayList<String>();
        subChild4.add(packageID.get(4));
        ArrayList<String> subChild5 = new ArrayList<String>();
        subChild5.add(packageID.get(4));
        childData.put(bookingDetailID.get(1), subChild2);
        childData.put(bookingDetailID.get(2), subChild3);
        childData.put(bookingDetailID.get(3), subChild4);
        childData.put(bookingDetailID.get(4), subChild5);

        adapter.notifyDataSetChanged();*/
    }

    private void goConfirmation()
    {
        Map<String, String> param = new HashMap<String, String>();
        FragmentConfirmation confirmation = new FragmentConfirmation();
        iFragment.onNavigate(confirmation, param);
    }


    public class TransactionHistoryAdapter extends BaseExpandableListAdapter{

        public TransactionHistoryAdapter()
        {

        }

        @Override
        public int getGroupCount() {
            return bookingDetailID.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childData.get(bookingDetailID.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            String[] obj = new String[4];
            obj[0] = bookingDetailID.get(groupPosition);
            obj[1] = productTitle.get(groupPosition);
            obj[2] = productDesc.get(groupPosition);
            obj[3] = parentStatus.get(groupPosition);
            return obj;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String valueChildData = childData.get(bookingDetailID.get(groupPosition)).get(childPosition);
            int indexChild = 0;
            for(int i = 0;i < packageID.size();i++)
            {
                if(valueChildData.equals(packageID.get(i)))
                {
                    indexChild = i;
                }
            }
            String obj[] = new String[5];
            obj[0] = packageID.get(indexChild);
            obj[1] = bookingDetailDate.get(indexChild);
            obj[2] = bookingDetailCode.get(indexChild);
            obj[3] = packageDesc.get(indexChild);
            obj[4] = bookingDetailAmount.get(indexChild);
            return obj;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final GroupViewHolder holder = new GroupViewHolder();
            if(convertView == null)
            {
                convertView = LayoutInflater.from(activity).inflate(R.layout.item_fragment_transaction_history_group, null);
            }
            holder.groupTitle = (UIText) convertView.findViewById(R.id.item_transaction_history_title);
            holder.groupDesc = (UIText) convertView.findViewById(R.id.item_transaction_history_desc);
            holder.groupStatus = (UIText) convertView.findViewById(R.id.item_transaction_history_status);
            holder.groupProcess = (UIButton) convertView.findViewById(R.id.item_transaction_history_process);
            holder.groupTriangle = (RelativeLayout) convertView.findViewById(R.id.item_transaction_history_triangle);
            String[] dataGroup = (String[]) getGroup(groupPosition);
            String desc = HelperGeneral.getLimitedWords(dataGroup[2], 10);
            holder.groupTitle.setText(dataGroup[1]);
            holder.groupDesc.setText(desc);
            holder.groupStatus.setText(dataGroup[3]);
            holder.groupProcess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goConfirmation();
                }
            });
            if(isExpanded)
            {
                holder.groupTriangle.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.groupTriangle.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildViewHolder holder = new ChildViewHolder();
            if(convertView == null)
            {
                convertView = LayoutInflater.from(activity).inflate(R.layout.item_fragment_transaction_history_child, null);
            }
            holder.viewChildDate = (UIText) convertView.findViewById(R.id.item_transaction_history_child_date);
            holder.viewChildCode = (UIText) convertView.findViewById(R.id.item_transaction_history_child_code);
            holder.viewChildDesc = (UIText) convertView.findViewById(R.id.item_transaction_history_child_desc);
            holder.viewChildPrice = (UIText) convertView.findViewById(R.id.item_transaction_history_child_price);

            String[] subChild = (String[]) getChild(groupPosition, childPosition);
            String desc = HelperGeneral.getLimitedWords(subChild[3], 10);
            String date = HelperGeneral.getDefaultDateFormat(subChild[1]);
            holder.viewChildDate.setText(date);
            holder.viewChildDesc.setText(desc);
            holder.viewChildCode.setText(subChild[2]);
            holder.viewChildPrice.setText(subChild[4]);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupViewHolder{
            UIText groupTitle, groupDesc, groupStatus;
            UIButton groupProcess;
            RelativeLayout groupTriangle;
        }

        class ChildViewHolder{
            UIText viewChildDate,viewChildCode, viewChildDesc, viewChildPrice;
        }
    }

}
