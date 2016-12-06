package com.meetdesk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.meetdesk.activity.ActivityAuth;
import com.meetdesk.external.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.meetdesk.external.uil.core.assist.FailReason;
import com.meetdesk.external.uil.core.listener.ImageLoadingListener;
import com.meetdesk.fragment.FragmentHelp;
import com.meetdesk.fragment.FragmentHotlist;
import com.meetdesk.fragment.FragmentInbox;
import com.meetdesk.fragment.FragmentProfile;
import com.meetdesk.fragment.FragmentRequestMerchant;
import com.meetdesk.fragment.FragmentTransaction;
import com.meetdesk.fragment.FragmentWishList;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.util.RecyclerViewItemDecoration;
import com.meetdesk.view.UICircleImage;
import com.meetdesk.view.UIText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseActivity extends FragmentActivity implements HelperGeneral.FragmentInterface{

    SlidingMenu menu;
    boolean isShowMenu = true;
    ImageButton imagebuttonSlidemenuClose;
    UIText textFullname, textEmail;
    UICircleImage circleImageAvatar;
    RecyclerView recyclerViewMenu;
    ArrayList<String> dataID, dataTitle, dataIcon;
    SlidemenuAdapter adapter;
    LazyImageLoader imageLoader;
    UIText slidemenuFullname, slidemenuEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMenu();
        initGeneral();
        setupSlidemenu();
        filldata();
    }

    @Override
    public void onNavigate(BaseFragment fragmentSrc, Map<String, String> parameter) {

    }

    private void initGeneral()
    {
        dataID = new ArrayList<String>();
        dataTitle = new ArrayList<String>();
        dataIcon = new ArrayList<String>();
        adapter = new SlidemenuAdapter();
        imageLoader = new LazyImageLoader(BaseActivity.this);
        final PrefAuthentication authPref = new PrefAuthentication(BaseActivity.this);
        imagebuttonSlidemenuClose = (ImageButton) findViewById(R.id.slidemenu_close);
        textFullname = (UIText) findViewById(R.id.slidemenu_fullname);
        textEmail = (UIText) findViewById(R.id.slidemenu_email);
        circleImageAvatar = (UICircleImage) findViewById(R.id.slidemenu_avatar);
        recyclerViewMenu = (RecyclerView) findViewById(R.id.recyclerview_menu);
        slidemenuFullname = (UIText) findViewById(R.id.slidemenu_fullname);
        slidemenuEmail = (UIText) findViewById(R.id.slidemenu_email);

        imagebuttonSlidemenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });
        slidemenuFullname.setText(authPref.getKeyUserFullname());
        slidemenuEmail.setText(authPref.getKeyUserEmail());
        String filename = authPref.getKeyUserAvatar();
        if(!authPref.getKeyUserSource().equals("general"))
        {
            filename = authPref.getKeyUserAvatar().substring(authPref.getKeyUserAvatar().lastIndexOf("/")+1);
        }
        if(!HelperGeneral.checkInternalFile(filename, BaseActivity.this))
        {
            String imageURL = HelperNative.getURL(11171) + authPref.getKeyUserAvatar();
            if(!authPref.getKeyUserSource().equals("general"))
            {
                imageURL = authPref.getKeyUserAvatar();
            }
            final String finalFilename = filename;
            imageLoader.showImage(imageURL, circleImageAvatar, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    HelperGeneral.saveBitmapInternal(finalFilename, BaseActivity.this, loadedImage);
                    circleImageAvatar.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        if(HelperGeneral.checkInternalFile(filename, BaseActivity.this))
        {
            imageLoader.showImage(HelperGeneral.getPrivatePath(filename, BaseActivity.this), circleImageAvatar);
        }
    }

    private void initMenu()
    {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.SLIDING_WINDOW);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.slidemenu_shadow);
        menu.setShadowDrawable(R.drawable.slidemenu_shadow);
        menu.setBehindOffsetRes(R.dimen.slidemenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slidemenu_item);
        menu.setSlidingEnabled(isShowMenu);
        menu.bringToFront();
    }

    public void setShowMenu(boolean show)
    {
        this.isShowMenu = show;
    }

    public void toggleMenu()
    {
        menu.toggle(true);
    }
    private void setupSlidemenu()
    {
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(5);
        final LinearLayoutManager recycleLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMenu.setLayoutManager(recycleLayoutManager);
        recyclerViewMenu.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMenu.setAdapter(adapter);
        recyclerViewMenu.addOnItemTouchListener(new HelperGeneral.RecyclerTouchListener(getApplicationContext(), recyclerViewMenu, new HelperGeneral.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (dataID.get(position).equals("6")) {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentTransaction transaction = new FragmentTransaction();
                    onNavigate(transaction, param);
                }
                if (dataID.get(position).equals("7")) {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentProfile profile = new FragmentProfile();
                    onNavigate(profile, param);
                }
                if (dataID.get(position).equals("4")) {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentRequestMerchant reqMerchant = new FragmentRequestMerchant();
                    onNavigate(reqMerchant, param);
                }
                if (dataID.get(position).equals("2")) {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentInbox inbox = new FragmentInbox();
                    onNavigate(inbox, param);
                }
                if (dataID.get(position).equals("3")) {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentWishList wishList = new FragmentWishList();
                    onNavigate(wishList, param);
                }
                if (dataID.get(position).equals("5")) {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentHotlist hotlist = new FragmentHotlist();
                    onNavigate(hotlist, param);
                }
                if (dataID.get(position).equals("8")) {
                    Map<String, String> param = new HashMap<String, String>();
                    FragmentHelp help = new FragmentHelp();
                    onNavigate(help, param);
                }
                if (dataID.get(position).equals("9")) {
                    backToAuth();
                }
                toggleMenu();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void backToAuth()
    {
        PrefAuthentication authentication = new PrefAuthentication(BaseActivity.this);
        String source = authentication.getKeyUserSource();
        Intent i = new Intent(BaseActivity.this, ActivityAuth.class);
        i.putExtra("isLogout", true);
        i.putExtra("userSource", source);
        startActivity(i);
        finish();
    }

    private void filldata()
    {
        dataID.add("1");
        dataTitle.add("Home");
        dataIcon.add("icon_sidemenu_home.png");

        dataID.add("2");
        dataTitle.add("Inbox");
        dataIcon.add("icon_sidemenu_mail.png");

        dataID.add("3");
        dataTitle.add("Room Wish List");
        dataIcon.add("icon_sidemenu_whislist.png");

        dataID.add("4");
        dataTitle.add("Rent Out Your Space");
        dataIcon.add("icon_sidemenu_room.png");

        dataID.add("5");
        dataTitle.add("Hot Offer");
        dataIcon.add("icon_sidemenu_hotlist.png");

        dataID.add("6");
        dataTitle.add("Transaction");
        dataIcon.add("icon_sidemenu_transaction.png");

        dataID.add("7");
        dataTitle.add("Profile");
        dataIcon.add("icon_sidemenu_profile.png");

        dataID.add("8");
        dataTitle.add("Help");
        dataIcon.add("icon_sidemenu_help.png");

        dataID.add("9");
        dataTitle.add("Logout");
        dataIcon.add("icon_sidemenu_logout.png");

        adapter.notifyDataSetChanged();
    }

    public SlidingMenu getMenu()
    {
        return this.menu;
    }

    public class SlidemenuAdapter extends RecyclerView.Adapter<SlidemenuAdapter.GalleryViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;


        public  SlidemenuAdapter()
        {

        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_slidemenu, parent, false);

            return new GalleryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final GalleryViewHolder holder, int position) {

            holder.viewTitle.setText(dataTitle.get(position));
            imageLoader.showImage(HelperGeneral.getAssetsPath("images/" + dataIcon.get(position)), holder.viewIcon);
        }


        @Override
        public int getItemCount() {
            return dataID.size();
        }

        public class GalleryViewHolder extends RecyclerView.ViewHolder{
            ImageView viewIcon;
            UIText viewTitle;

            public GalleryViewHolder(View itemView) {
                super(itemView);
                viewIcon = (ImageView) itemView.findViewById(R.id.item_list_slidemenu_icon);
                viewTitle = (UIText) itemView.findViewById(R.id.item_list_slidemenu_title);
            }
        }
        public boolean isHeader(int position) {
            return position == 0;
        }
    }

    public void removeFragment(int countBeDelete)
    {
        List<Fragment> lists = getSupportFragmentManager().getFragments();
        for(int i = countBeDelete;i > 0;i--)
        {
            BaseFragment fragment = (BaseFragment) lists.get(i);
            if(fragment != null)
            {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }
}
