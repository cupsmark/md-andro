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
    String isNew = "1", users = "", titleMessage = "";
    ArrayList<String> dataID, dataDate, dataImage, dataMessage;
    ArrayList<Boolean> dataIsSelf;
    LazyImageLoader imageLoader;
    InboxNewAdapter adapter;
    UIEditText editMessage;

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
        dataIsSelf = new ArrayList<Boolean>();
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
            pagetitle.setText(activity.getResources().getString(R.string.pagetitle_inbox_new));
            targetCaption.setVisibility(View.VISIBLE);
            targetCaption.setText("To : ");
            targetValue.setText(users);
        }
        else
        {
            titleMessage = param.get("title");
            pagetitle.setText(users);
            targetCaption.setVisibility(View.GONE);
            targetValue.setText(titleMessage);
            getData();
        }

    }

    private void getData()
    {
        dataID.add("1");
        dataID.add("2");
        dataID.add("3");
        dataID.add("4");
        dataID.add("5");

        dataImage.add("tom.jpg");
        dataImage.add("sample_avatar.jpg");
        dataImage.add("tom.jpg");
        dataImage.add("sample_avatar.jpg");
        dataImage.add("tom.jpg");

        dataIsSelf.add(false);
        dataIsSelf.add(true);
        dataIsSelf.add(false);
        dataIsSelf.add(true);
        dataIsSelf.add(false);

        dataDate.add("2016-10-01 11:05:00");
        dataDate.add("2016-10-01 16:30:00");
        dataDate.add("2016-09-20 08:00:00");
        dataDate.add("2016-09-20 10:00:00");
        dataDate.add("2016-09-20 12:00:00");

        dataMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        dataMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        dataMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        dataMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");
        dataMessage.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque rhoncus, ultrices lectus at, dapibus neque. Suspendisse malesuada nibh a mauris luctus suscipit");

        adapter.notifyDataSetChanged();
    }

    private void addNewMessage()
    {
        int newID = dataID.size() + 1;
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        dataID.add(Integer.toString(newID));
        dataDate.add(strDate);
        dataImage.add("sample_avatar.jpg");
        dataIsSelf.add(true);
        dataMessage.add(editMessage.getText().toString());
        adapter.notifyDataSetChanged();
        editMessage.getText().clear();
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
            if(dataIsSelf.get(position))
            {

            }
            else
            {

            }
            String desc = HelperGeneral.getLimitedWords(dataMessage.get(position), 10);
            String date = HelperGeneral.getDefaultDateFormat(dataDate.get(position));
            holder.textDesc.setText(desc);
            holder.textTime.setText(date);
            imageLoader.showImage(HelperGeneral.getAssetsPath("images/"+dataImage.get(position)), holder.viewImage);
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
