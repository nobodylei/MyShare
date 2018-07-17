package com.rjwl.reginet.myshare;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnTextWeChat, btnImgWeChat, btnTextFriend, btnImgFriend;

    private ShareParams shareParams;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String toastMsg = (String) msg.obj;
            Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
            Log.d("TAG1", "分享结果:" + toastMsg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    private void init() {

        PlatformConfig platformConfig = new PlatformConfig();
        platformConfig.setWechat("wx24ddb563862ef903", "3d2bc690f6769530b84272d5aa1b9acd");
        JShareInterface.init(getApplicationContext(), platformConfig);

        shareParams = new ShareParams();

        btnTextWeChat = findViewById(R.id.btn_text_wechat);
        btnImgWeChat = findViewById(R.id.btn_img_wechat);
        btnTextFriend = findViewById(R.id.btn_text_friend);
        btnImgFriend = findViewById(R.id.btn_img_friend);

        btnImgWeChat.setOnClickListener(this);
        btnTextWeChat.setOnClickListener(this);
        btnImgFriend.setOnClickListener(this);
        btnTextFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Friend 分享微信好友,Zone 分享微信朋友圈,Favorites 分享微信收藏
        String str = "";
        switch (view.getId()) {
            case R.id.btn_text_wechat:
                str = Wechat.Name;
                shareParams.setShareType(Platform.SHARE_TEXT);
                shareParams.setText("私人订制");//必须
                break;
            case R.id.btn_img_wechat:
                str = Wechat.Name;
                shareParams = new ShareParams();
                shareParams.setShareType(Platform.SHARE_IMAGE);
                break;
            case R.id.btn_img_friend:
                str = WechatMoments.Name;
                shareParams = new ShareParams();
                shareParams.setShareType(Platform.SHARE_IMAGE);
                break;
            case R.id.btn_text_friend:
                str = WechatMoments.Name;
                shareParams.setShareType(Platform.SHARE_TEXT);
                shareParams.setText("私人订制");//必须
                break;
        }

        JShareInterface.share(str, shareParams, mPlatActionListener);
    }

    private PlatActionListener mPlatActionListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享成功";
                Log.d("TAG1", "分享成功");
                handler.sendMessage(message);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享失败:" + (error != null ? error.getMessage() : "") + "---" + errorCode;
                Log.d("TAG1", "分享失败");
                handler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享取消";
                Log.d("TAG1", "分享取消");
                handler.sendMessage(message);
            }
        }
    };
}
