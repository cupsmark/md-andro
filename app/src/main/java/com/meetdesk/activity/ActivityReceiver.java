package com.meetdesk.activity;

import android.os.Bundle;

import com.meetdesk.BaseActivity;
import com.meetdesk.R;
import com.meetdesk.view.UIText;

public class ActivityReceiver extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMenu().setSlidingEnabled(false);
    }

    private void getMessage()
    {
        String message = getIntent().getStringExtra("message");
        UIText textMessage = (UIText) findViewById(R.id.act_receiver_message);
        textMessage.setText(message);
    }


}
