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
    ArrayList<String> parentID, parentTitle, parentStatus, parentDesc, childID, childDate, childCode, childDesc, childPrice;
    ArrayList<Boolean> parentShowRemoveButton;
    TransactionHistoryAdapter adapter;
    LazyImageLoader imageLoader;
    HashMap<String, ArrayList<String>> childData;

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
        parentShowRemoveButton = new ArrayList<Boolean>();
        childID = new ArrayList<String>();
        childDate = new ArrayList<String>();
        childCode = new ArrayList<String>();
        childDesc = new ArrayList<String>();
        childPrice = new ArrayList<String>();
        childData = new HashMap<String, ArrayList<String>>();
        imageLoader = new LazyImageLoader(activity);
        adapter = new TransactionHistoryAdapter();
        listview = (ExpandableListView) activity.findViewById(R.id.transaction_history_listview);
        listview.setAdapter(adapter);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        param = getParameter();

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

        parentStatus.add("Pending");
        parentStatus.add("Success");
        parentStatus.add("Success");
        parentStatus.add("Cancelled");
        parentStatus.add("Pending");

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


    public class TransactionHistoryAdapter extends BaseExpandableListAdapter{

        public TransactionHistoryAdapter()
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
            String[] obj = new String[4];
            obj[0] = parentID.get(groupPosition);
            obj[1] = parentTitle.get(groupPosition);
            obj[2] = parentDesc.get(groupPosition);
            obj[3] = parentStatus.get(groupPosition);
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
