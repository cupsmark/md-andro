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
import com.meetdesk.view.UIText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/1/16.
 */
public class FragmentInbox extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_INBOX = "tag:fragment-inbox";

    ImageButton imagebuttonBack;
    RecyclerView recycler;

    ArrayList<String> inboxID, inboxTitle, inboxUser, inboxDate, inboxMessage;
    ArrayList<Boolean> inboxNotif, inboxShowGroupDate;
    InboxAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_inbox, container, false);
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
        inboxID = new ArrayList<String>();
        inboxTitle = new ArrayList<String>();
        inboxUser = new ArrayList<String>();
        inboxDate = new ArrayList<String>();
        inboxMessage = new ArrayList<String>();
        inboxNotif = new ArrayList<Boolean>();
        inboxShowGroupDate = new ArrayList<Boolean>();
        adapter = new InboxAdapter();
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.inbox_imagebutton_back);
        recycler = (RecyclerView) activity.findViewById(R.id.inbox_recycler);


        final LinearLayoutManager recycleLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recycleLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new HelperGeneral.RecyclerTouchListener(activity, recycler, new HelperGeneral.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                launchInboxNew(inboxUser.get(position), inboxTitle.get(position));
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

    private void launchInboxNew(String user, String title)
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("new", "0");
        param.put("user", user);
        param.put("title", title);
        FragmentInboxNew inboxNew = new FragmentInboxNew();
        iFragment.onNavigate(inboxNew, param);
    }


    private void getData()
    {
        inboxID.add("1");
        inboxID.add("2");
        inboxID.add("3");
        inboxID.add("4");
        inboxID.add("5");

        inboxUser.add("User 1");
        inboxUser.add("User 2");
        inboxUser.add("User 1");
        inboxUser.add("User 2");
        inboxUser.add("User 1");

        inboxTitle.add("Workspace Info 1");
        inboxTitle.add("Workspace Info 2");
        inboxTitle.add("Workspace Info 3");
        inboxTitle.add("Workspace Info 4");
        inboxTitle.add("Workspace Info 5");

        inboxDate.add("2016-10-01 11:05:00");
        inboxDate.add("2016-10-01 16:30:00");
        inboxDate.add("2016-09-20 08:00:00");
        inboxDate.add("2016-09-20 10:00:00");
        inboxDate.add("2016-09-20 12:00:00");

        inboxShowGroupDate.add(true);
        inboxShowGroupDate.add(false);
        inboxShowGroupDate.add(true);
        inboxShowGroupDate.add(false);
        inboxShowGroupDate.add(false);

        inboxNotif.add(true);
        inboxNotif.add(false);
        inboxNotif.add(false);
        inboxNotif.add(false);
        inboxNotif.add(true);

        inboxMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        inboxMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        inboxMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        inboxMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        inboxMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");

        adapter.notifyDataSetChanged();
    }

    private String getInboxDateFormat(String date)
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

    private String getInboxTimeFormat(String time)
    {
        String result = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("KK a");
            result = sdf2.format(newDate);
        } catch (ParseException e) {
            result = "";
        }
        return result;
    }


    public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;


        public  InboxAdapter()
        {

        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_fragment_inbox, parent, false);

            return new InboxViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final InboxViewHolder holder, int position) {

            String desc = HelperGeneral.getLimitedWords(inboxMessage.get(position), 10);
            String date = getInboxDateFormat(inboxDate.get(position));
            String time = getInboxTimeFormat(inboxDate.get(position));
            holder.textUser.setText(inboxUser.get(position));
            holder.textDesc.setText(desc);
            holder.textTime.setText(time);
            holder.textTitle.setText(inboxTitle.get(position));
            holder.textDate.setText(date);
            if(inboxNotif.get(position))
            {
                holder.iconNotif.setImageResource(R.drawable.icon_inbox_notif);
            }
            if(!inboxNotif.get(position))
            {
                holder.iconNotif.setImageResource(R.drawable.icon_inbox_notif_transparent);
            }
            if(inboxShowGroupDate.get(position))
            {
                holder.textDate.setVisibility(View.VISIBLE);
            }
            if(!inboxShowGroupDate.get(position))
            {
                holder.textDate.setVisibility(View.GONE);
            }
        }


        @Override
        public int getItemCount() {
            return inboxID.size();
        }

        public class InboxViewHolder extends RecyclerView.ViewHolder{
            ImageView iconNotif;
            UIText textUser, textTitle, textTime, textDesc, textDate;

            public InboxViewHolder(View itemView) {
                super(itemView);
                iconNotif = (ImageView) itemView.findViewById(R.id.item_inbox_notif);
                textUser = (UIText) itemView.findViewById(R.id.item_inbox_user);
                textTitle = (UIText) itemView.findViewById(R.id.item_inbox_title);
                textTime = (UIText) itemView.findViewById(R.id.item_inbox_time);
                textDesc = (UIText) itemView.findViewById(R.id.item_inbox_desc);
                textDate = (UIText) itemView.findViewById(R.id.item_inbox_date);
            }
        }
        public boolean isHeader(int position) {
            return position == 0;
        }



    }
}
