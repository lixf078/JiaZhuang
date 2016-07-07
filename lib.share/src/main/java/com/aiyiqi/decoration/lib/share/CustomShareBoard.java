/**
 * 
 */

package com.aiyiqi.decoration.lib.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.aiyiqi.lib.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.exception.SocializeException;


/**
 * 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {
    private UMSocialService mController;
    private Activity mActivity;
    public CustomShareBoard(Activity activity, UMSocialService umSocialService) {
        super(activity);
        this.mActivity = activity;
        this.mController = umSocialService;
        initView(activity);
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.share_custom_board, null);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.qzone).setOnClickListener(this);
        rootView.findViewById(R.id.sinaweibo).setOnClickListener(this);
        rootView.findViewById(R.id.share_cannel).setOnClickListener(this);
        rootView.findViewById(R.id.dissmiss).setOnClickListener(this);
        setContentView(rootView);
        setAnimationStyle(R.style.umeng_socialize_shareboard_animation);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.wechat){
            performShare(SHARE_MEDIA.WEIXIN);
        }else if(id == R.id.wechat_circle){
            performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
        }else if(id == R.id.qq){
            performShare(SHARE_MEDIA.QQ);
        }else if(id == R.id.qzone){
            performShare(SHARE_MEDIA.QZONE);
        }else if(id == R.id.sinaweibo){
            performShare(SHARE_MEDIA.SINA);
        }else if(id == R.id.share_cannel){
            if (this.isShowing()){
                dismiss();
            }
        }else if(id == R.id.dissmiss){
            if (isShowing()){
                dismiss();
            }
        }
    }

    public  void doOauthVerify()
    {
        mController.doOauthVerify(mActivity, SHARE_MEDIA.SINA, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

                ToastUtil.showToast(mActivity, "授权开始");
            }

            @Override
            public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                ToastUtil.showToast(mActivity, "授权完成");
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {

                ToastUtil.showToast(mActivity, "授权错误");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtil.showToast(mActivity, "取消授权");
            }
        });
    }
    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(mActivity, platform, new SnsPostListener() {
            @Override
            public void onStart() {
                ToastUtil.showToast(mActivity, "开始分享.");
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                dismiss();
            }
        });
    }

    /*private void printhandler(String tag){
        SocializeConfig socializeConfig = mController.getConfig();
        SparseArray<UMSsoHandler> sparseArray = socializeConfig.getSsoHandlersMap();
        int key = 0;
        for(int i=0;i<sparseArray.size();i++){
            key = sparseArray.keyAt(i);
            UMSsoHandler handler = sparseArray.get(key);
        }
    }*/
}
