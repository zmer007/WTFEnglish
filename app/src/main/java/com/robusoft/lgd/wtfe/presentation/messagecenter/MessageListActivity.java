package com.robusoft.lgd.wtfe.presentation.messagecenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.robusoft.lgd.wtfe.R;
import com.robusoft.lgd.wtfe.presentation.TitledActivity;

/**
 * User: lgd(1779964617@qq.com)
 * Date: 2017/4/18
 * Function:
 */
public class MessageListActivity extends TitledActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
    }
}
