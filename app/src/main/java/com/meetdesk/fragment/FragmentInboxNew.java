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
import com.meetdesk.controller.ControllerGeneral;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.EndlessRecyclerViewScrollListener;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UICircleImage;
import com.meetdesk.view.UICircleImageView;
import com.meetdesk.view.UIEditText;
import com.meetdesk.view.UIText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/5/16.
 */
public class FragmentInboxNew extends BaseFragment{

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_INBOX_NEW = "tag:fragment-inbox-new";

    UIText pagetitle, targetCaption, targetValue;
    ImageButton imagebuttonBack, imagebuttonSend;
    RecyclerView recycler;
    Map<String, String> param;
    String isNew = "1", users = "", titleMessage = "", targetID = "", targetField = "";
    ArrayList<String> dataID, dataDate, dataImage, dataMessage, dataIsSelf;
    LazyImageLoader imageLoader;
    InboxNewAdapter adapter;
    UIEditText editMessage;
    int l = 10, o = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_inbox_new, container, false);
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
            actionComponent();
        }
    }

    private void init()
    {
        param = getParameter();
        isNew = param.get("new");
        users = param.get("user");
        dataID = new ArrayList<String>();
        dataDate = new ArrayList<String>();
        dataImage = new ArrayList<String>();
        dataMessage = new ArrayList<String>();
        dataIsSelf = new ArrayList<String>();
        adapter = new InboxNewAdapter();
        imageLoader = new LazyImageLoader(activity);
        pagetitle = (UIText) activity.findViewById(R.id.inbox_new_pagetitle);
        targetCaption = (UIText) activity.findViewById(R.id.inbox_new_target_caption);
        targetValue = (UIText) activity.findViewById(R.id.inbox_new_target_value);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.inbox_new_imagebutton_back);
        recycler = (RecyclerView) activity.findViewById(R.id.inbox_new_recycler);
        editMessage = (UIEditText) activity.findViewById(R.id.inbox_new_message);
        imagebuttonSend = (ImageButton) activity.findViewById(R.id.inbox_new_imagebutton_send);
    }

    private void actionComponent()
    {
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        imagebuttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMessage();
            }
        });
        final LinearLayoutManager recycleLayoutManager = new LinearLayoutManager(activity);
        recycleLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(recycleLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(recycleLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getData(false);
            }
        });
        recycler.addOnItemTouchListener(new HelperGeneral.RecyclerTouchListener(activity, recycler, new HelperGeneral.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if(isNew.equals("1"))
        {
            targetField = "pid";
            targetID = param.get("target_id");
            pagetitle.setText(activity.getResources().getString(R.string.pagetitle_inbox_new));
            targetCaption.setVisibility(View.VISIBLE);
            targetCaption.setText("To : ");
            targetValue.setText(users);
        }
        else
        {
            targetField = "mid";
            targetID = param.get("target_id");
            titleMessage = param.get("title");
            pagetitle.setText(users);
            targetCaption.setVisibility(View.GONE);
            targetValue.setText(titleMessage);
        }

        getData(true);
    }

    private void getData(final boolean firstLoad)
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            ArrayList<String> tempID, tempDate, tempImage, tempMessage, tempIsSelf;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(firstLoad)
                {
                    o = 0;
                }
                tempID = new ArrayList<String>();
                tempDate = new ArrayList<String>();
                tempImage = new ArrayList<String>();
                tempMessage = new ArrayList<String>();
                tempIsSelf = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication authentication = new PrefAuthentication(activity);
                ControllerGeneral general = new ControllerGeneral(activity);
                general.setToken(authentication.getKeyUserToken());
                general.setL((l));
                general.setO(o);
                general.setTargetField(targetField);
                general.setTarget(targetID);
                general.executeListMessageDetail();
                if(general.getSuccess())
                {
                    success = true;
                    tempID.addAll(general.getMessageDetailID());
                    tempDate.addAll(general.getMessageDetailDate());
                    tempImage.addAll(general.getMessageDetailImage());
                    tempMessage.addAll(general.getMessageDetailContent());
                    tempIsSelf.addAll(general.getMessageDetailIsSelf());
                    o = general.getOffset();
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
                            dataDate.add(tempDate.get(i));
                            dataImage.add(tempImage.get(i));
                            dataMessage.add(tempMessage.get(i));
                            dataIsSelf.add(tempIsSelf.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }.execute();

    }

    private void addNewMessage()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg,tempID, tempDate, tempImage, tempIsSelf, tempMessage, valueMessage, valueAvatar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                valueMessage = editMessage.getText().toString();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication authentication = new PrefAuthentication(activity);
                valueAvatar = authentication.getKeyUserAvatar();
                String[] field = new String[]{"token", "message", "param_field", "param_value"};
                String[] value = new String[]{authentication.getKeyUserToken(), valueMessage, targetField, targetID};
                ControllerGeneral general = new ControllerGeneral(activity);
                general.setPostParameter(field, value);
                general.executeAddMessage();
                if(general.getSuccess())
                {
                    success = true;
                }
                msg = general.getMessage();
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    int newID = dataID.size() + 1;
                    Calendar c = Calendar.getInstance();
                    Date date = c.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDate = sdf.format(date);
                    dataID.add(Integer.toString(newID));
                    dataDate.add(strDate);
                    dataImage.add(valueAvatar);
                    dataIsSelf.add("1");
                    dataMessage.add(editMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    editMessage.getText().clear();
                }
            }
        }.execute();
    }



    public class InboxNewAdapter extends RecyclerView.Adapter<InboxNewAdapter.InboxNewViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;


        public  InboxNewAdapter()
        {

        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public InboxNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_fragment_inbox_new, parent, false);

            return new InboxNewViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final InboxNewViewHolder holder, int position) {
            if(dataIsSelf.get(position).equals("1"))
            {

            }
            else
            {

            }
            String desc = HelperGeneral.getLimitedWords(dataMessage.get(position), 10);
            String date = HelperGeneral.getDefaultDateFormat(dataDate.get(position));
            holder.textDesc.setText(desc);
            holder.textTime.setText(date);
            imageLoader.showImage(HelperNative.getURL(11171) + dataImage.get(position), holder.viewImage);
        }


        @Override
        public int getItemCount() {
            return dataID.size();
        }

        public class InboxNewViewHolder extends RecyclerView.ViewHolder{
            UICircleImageView viewImage;
            UIText textTime, textDesc;

            public InboxNewViewHolder(View itemView) {
                super(itemView);
                viewImage = (UICircleImageView) itemView.findViewById(R.id.item_inbox_new_avatar);
                textTime = (UIText) itemView.findViewById(R.id.item_inbox_new_date);
                textDesc = (UIText) itemView.findViewById(R.id.item_inbox_new_desc);
            }
        }
        public boolean isHeader(int position) {
            return position == 0;
        }



    }
}
