package com.meetdesk.fragment;

import android.app.Activity;
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
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/6/16.
 */
public class FragmentTransactionPending extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_TRANSACTION_PENDING = "tag:fragment-transaction-pending";

    ExpandableListView listview;
    Map<String, String> param;
    ArrayList<String> parentID, parentTitle, parentStatus, parentDesc, parentPrice, parentDate, parentShowDate, childID, childDate, childCode, childDesc, childPrice;
    ArrayList<Boolean> parentShowRemoveButton;
    TransactionPendingAdapter adapter;
    LazyImageLoader imageLoader;
    HashMap<String, ArrayList<String>> childData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_transaction_pending, container, false);
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
        param = getParameter();
        parentID = new ArrayList<String>();
        parentTitle = new ArrayList<String>();
        parentStatus = new ArrayList<String>();
        parentDesc = new ArrayList<String>();
        parentPrice = new ArrayList<String>();
        parentDate = new ArrayList<String>();
        parentShowDate = new ArrayList<String>();
        parentShowRemoveButton = new ArrayList<Boolean>();
        childID = new ArrayList<String>();
        childDate = new ArrayList<String>();
        childCode = new ArrayList<String>();
        childDesc = new ArrayList<String>();
        childPrice = new ArrayList<String>();
        childData = new HashMap<String, ArrayList<String>>();
        imageLoader = new LazyImageLoader(activity);
        adapter = new TransactionPendingAdapter();
        listview = (ExpandableListView) activity.findViewById(R.id.transaction_pending_listview);
        listview.setAdapter(adapter);
    }

    private void getData()
    {
        parentID.add("1");
        parentID.add("2");
        parentID.add("3");
        parentID.add("4");
        parentID.add("5");

        parentTitle.add("Co-Working Space 1");
        parentTitle.add("Co-Working Space 2");
        parentTitle.add("Service Office 3");
        parentTitle.add("Service Office 1");
        parentTitle.add("Co-working Space 5");

        parentPrice.add("2.000.000");
        parentPrice.add("1.600.000");
        parentPrice.add("3.400.000");
        parentPrice.add("1.100.000");
        parentPrice.add("1.750.000");

        parentStatus.add("Payment");
        parentStatus.add("Verification");
        parentStatus.add("Verification");
        parentStatus.add("Pending");
        parentStatus.add("Payment");

        parentDate.add("2016-10-01 11:05:00");
        parentDate.add("2016-10-01 16:30:00");
        parentDate.add("2016-09-20 08:00:00");
        parentDate.add("2016-09-20 10:00:00");
        parentDate.add("2016-09-20 12:00:00");

        parentShowDate.add("1");
        parentShowDate.add("0");
        parentShowDate.add("1");
        parentShowDate.add("0");
        parentShowDate.add("0");

        parentDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        parentDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        parentDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        parentDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        parentDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");

        childID.add("1");
        childID.add("2");
        childID.add("3");
        childID.add("4");
        childID.add("5");

        childDate.add("2016-10-01 11:05:00");
        childDate.add("2016-10-01 16:30:00");
        childDate.add("2016-09-20 08:00:00");
        childDate.add("2016-09-20 10:00:00");
        childDate.add("2016-09-20 12:00:00");

        childCode.add("MD-120889-9500");
        childCode.add("MD-160488-1710");
        childCode.add("MD-150799-4840");
        childCode.add("MD-167189-9535");
        childCode.add("MD-122088-5821");

        childDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        childDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        childDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        childDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");
        childDesc.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus");

        childPrice.add("2.000.000");
        childPrice.add("1.600.000");
        childPrice.add("3.400.000");
        childPrice.add("1.100.000");
        childPrice.add("1.750.000");

        ArrayList<String> subChild1 = new ArrayList<String>();
        subChild1.add(childID.get(0));
        ArrayList<String> subChild2 = new ArrayList<String>();
        subChild2.add(childID.get(1));
        ArrayList<String> subChild3 = new ArrayList<String>();
        subChild3.add(childID.get(2));
        ArrayList<String> subChild4 = new ArrayList<String>();
        subChild4.add(childID.get(4));
        ArrayList<String> subChild5 = new ArrayList<String>();
        subChild5.add(childID.get(4));
        childData.put(parentID.get(0), subChild1);
        childData.put(parentID.get(1), subChild2);
        childData.put(parentID.get(2), subChild3);
        childData.put(parentID.get(3), subChild4);
        childData.put(parentID.get(4), subChild5);

        adapter.notifyDataSetChanged();
    }


    private String getPendingDateFormat(String date)
    {
        String result = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = sdf.parse(date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            result = sdf2.format(newDate);
        } catch (ParseException e) {
            result = "";
        }
        return result;
    }

    private String getPendingTimeFormat(String time)
    {
        String result = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("K a");
            result = sdf2.format(newDate);
        } catch (ParseException e) {
            result = "";
        }
        return result;
    }

    public class TransactionPendingAdapter extends BaseExpandableListAdapter {

        public TransactionPendingAdapter()
        {

        }

        @Override
        public int getGroupCount() {
            return parentID.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childData.get(parentID.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            String[] obj = new String[7];
            obj[0] = parentID.get(groupPosition);
            obj[1] = parentTitle.get(groupPosition);
            obj[2] = parentDesc.get(groupPosition);
            obj[3] = parentPrice.get(groupPosition);
            obj[4] = parentStatus.get(groupPosition);
            obj[5] = parentDate.get(groupPosition);
            obj[6] = parentShowDate.get(groupPosition);
            return obj;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String valueChildData = childData.get(parentID.get(groupPosition)).get(childPosition);
            int indexChild = 0;
            for(int i = 0;i < childID.size();i++)
            {
                if(valueChildData.equals(childID.get(i)))
                {
                    indexChild = i;
                }
            }
            String obj[] = new String[5];
            obj[0] = childID.get(indexChild);
            obj[1] = childDate.get(indexChild);
            obj[2] = childCode.get(indexChild);
            obj[3] = childDesc.get(indexChild);
            obj[4] = childPrice.get(indexChild);
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
                convertView = LayoutInflater.from(activity).inflate(R.layout.item_fragment_transaction_pending_group, null);
            }
            holder.groupTitle = (UIText) convertView.findViewById(R.id.item_transaction_pending_title);
            holder.groupDesc = (UIText) convertView.findViewById(R.id.item_transaction_pending_desc);
            holder.groupPrice = (UIText) convertView.findViewById(R.id.item_transaction_pending_price);
            holder.groupStatus = (UIButton) convertView.findViewById(R.id.item_transaction_pending_status);
            holder.groupDate = (UIText) convertView.findViewById(R.id.item_transaction_pending_date);
            holder.groupTriangle = (RelativeLayout) convertView.findViewById(R.id.item_transaction_pending_triangle);
            String[] dataGroup = (String[]) getGroup(groupPosition);
            String desc = HelperGeneral.getLimitedWords(dataGroup[2], 10);
            String date = getPendingDateFormat(dataGroup[5]);
            String showDate = dataGroup[6];
            holder.groupTitle.setText(dataGroup[1]);
            holder.groupDesc.setText(desc);
            holder.groupPrice.setText(dataGroup[3]);
            holder.groupStatus.setText(dataGroup[4]);
            holder.groupDate.setText(date);
            if(isExpanded)
            {
                holder.groupTriangle.setVisibility(View.VISIBLE);
            }
            if(!isExpanded)
            {
                holder.groupTriangle.setVisibility(View.GONE);
            }
            if(showDate.equals("1"))
            {
                holder.groupDate.setVisibility(View.VISIBLE);
            }
            if(!showDate.equals("1"))
            {
                holder.groupDate.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildViewHolder holder = new ChildViewHolder();
            if(convertView == null)
            {
                convertView = LayoutInflater.from(activity).inflate(R.layout.item_fragment_transaction_pending_child, null);
            }
            holder.viewChildDate = (UIText) convertView.findViewById(R.id.item_transaction_pending_child_date);
            holder.viewChildCode = (UIText) convertView.findViewById(R.id.item_transaction_pending_child_code);
            holder.viewChildDesc = (UIText) convertView.findViewById(R.id.item_transaction_pending_child_desc);
            holder.viewChildPrice = (UIText) convertView.findViewById(R.id.item_transaction_pending_child_price);

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
            UIText groupTitle, groupDesc, groupPrice, groupDate;
            UIButton groupStatus;
            RelativeLayout groupTriangle;
        }

        class ChildViewHolder{
            UIText viewChildDate,viewChildCode, viewChildDesc, viewChildPrice;
        }
    }
}
