package com.aiyiqi.decoration.lib.share;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;

import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.Logger;
import com.aiyiqi.lib.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import com.aiyiqi.decoration.lib.share.R;

/**
 * Created by hubing on 16/4/21.
 */
public class SocialShareUtil {

    private String TAG = "ShareUtil";

    private static Object clockObj = new Object();
    private static SocialShareUtil mInstance;

    protected UMSocialService mUMSocialService;

    private SocialShareUtil(Activity activity){
        mUMSocialService = UMServiceFactory.getUMSocialService("com.umeng.share");
        //微博
        mUMSocialService.getConfig().setSsoHandler(new SinaSsoHandler());
        addSharePlatform(activity);
    }

    public static SocialShareUtil getInstance(Activity activity){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new SocialShareUtil(activity);
            }
            return mInstance;
        }
    }
    private void addSharePlatform(Activity activity){
        addQQPlatform(activity);
        addQZonePlatform(activity);
        addWXPlatform(activity);
        addWXCirclePlatform(activity);
    }

    private void addQZonePlatform(Activity activity){
        //qq空间
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, AppKey.QQ_ID,AppKey.QQ_SCREATE);
        qZoneSsoHandler.addToSocialSDK();
    }

    private void addQQPlatform(Activity activity){
        //qq
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,AppKey.QQ_ID,AppKey.QQ_SCREATE);
        qqSsoHandler.addToSocialSDK();
    }

    private void addWXPlatform(Activity activity){
        //微信
        UMWXHandler wxHandler = new UMWXHandler(activity,AppKey.WEIXINID,AppKey.WEIXIIN_SCREATE);
        wxHandler.addToSocialSDK();
    }

    private void addWXCirclePlatform(Activity activity){
        //朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(activity,AppKey.WEIXIN_CIRCLE_ID,AppKey.WEIXIN_CIRCLE_SCREATE);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    public void authorizeCallBack(int requestCode, int resultCode, Intent data){
        UMSsoHandler ssoHandler = mUMSocialService.getConfig().getSsoHandler(requestCode) ;

        mUMSocialService.getConfig().getSsoHandlersMap();
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private boolean isEmptyForSsoHandler(){
        SocializeConfig socializeConfig = mUMSocialService.getConfig();
        SparseArray<UMSsoHandler> sparseArray = socializeConfig.getSsoHandlersMap();

        if(sparseArray!=null){
            return sparseArray.size() >  0 ? false :true;
        }
        return true;
    }

   public void  shareImageAndContentToCircle(final Activity activity, Share share ){
       if(share == null){
           return;
       }
       if(isEmptyForSsoHandler()){
           addSharePlatform(activity);
       }
       // 设置微信分享内容
       final String title = share.title + activity.getResources().getString(R.string.lib_share_from_common);
       final String titleUrl = String.format(share.webUrl, share.id);
       Logger.e(Constants.TAG, "titleUrl=====> " + titleUrl);
       UMImage umImage = null;
       if(!TextUtils.isEmpty(share.imgUrl)){
           umImage =new UMImage(activity,share.imgUrl);
       }else{
           umImage =new UMImage(activity,share.defaultRes);
       }

       if(TextUtils.isEmpty(share.shareDes)){
           share.shareDes = share.title;
       }
       // 设置朋友圈分享的内容
       CircleShareContent circleMedia = new CircleShareContent();
       circleMedia.setShareContent(share.shareDes);
       circleMedia.setTitle(title);
       circleMedia.setShareMedia(umImage);
       circleMedia.setTargetUrl(titleUrl);
       mUMSocialService.setShareMedia(circleMedia);
       mUMSocialService.postShare(activity,SHARE_MEDIA.WEIXIN_CIRCLE,new SocializeListeners.SnsPostListener() {
           @Override
           public void onStart() {
               ToastUtil.showToast(activity, "开始分享.");
           }

           @Override
           public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {

           }
       });

   }



    public void shareImageAndContent(Activity activity,Share share/*String newsTitlte,String newsWebUrl,String currentNewsId,String des,String imgURL*/){
        if(share == null){
            return;
        }
        if(isEmptyForSsoHandler()){
            addSharePlatform(activity);
        }
        // 设置微信分享内容
        final String title = share.title + activity.getResources().getString(R.string.lib_share_from_common);
        final String titleUrl = String.format(share.webUrl, share.id);
        UMImage umImage = null;
        if(!TextUtils.isEmpty(share.imgUrl)){
            umImage =new UMImage(activity,share.imgUrl);
        }else{
            umImage =new UMImage(activity,share.defaultRes);
        }

        if(TextUtils.isEmpty(share.shareDes)){
            share.shareDes = share.title;
        }
        Log.e(TAG, "title : " + title + " ; titleUrl : " + titleUrl + " ; imgURL : " + share.imgUrl + " ; content : " + share.shareDes);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(share.shareDes);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(titleUrl);
        weixinContent.setShareMedia(umImage);
        mUMSocialService.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(share.shareDes);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(umImage);
        circleMedia.setTargetUrl(titleUrl);
        mUMSocialService.setShareMedia(circleMedia);
        //设置qq空间分享
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(share.shareDes);
        qzone.setTargetUrl(titleUrl);
        qzone.setTitle(title);
        qzone.setShareMedia(umImage);
        mUMSocialService.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(share.shareDes);
        qqShareContent.setTitle(title);
        qqShareContent.setShareMedia(umImage);
        qqShareContent.setTargetUrl(titleUrl);
        mUMSocialService.setShareMedia(qqShareContent);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent( share.title +titleUrl + " @" + activity.getResources().getString(R.string.lib_share_sina_yiqi_name));
        sinaContent.setShareImage(umImage);
//        sinaContent.setTitle(newsTitlte);
        mUMSocialService.setShareMedia(sinaContent);

        CustomShareBoard shareBoard = new CustomShareBoard(activity,mUMSocialService);
        shareBoard.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public  void shareOnlyPictureTo(Activity activity,Share share/*String name,String imageUrl*/){
        if(share == null){
            return;
        }
        if(isEmptyForSsoHandler()){
            addSharePlatform(activity);
        }
        Log.e(TAG,"shareOnlyPictureTo >> name : "+share.title+" ; imageUrl : "+share.imgUrl+" ; share.localImgUrl : "+share.localImgUrl);
        // 设置微信分享内容
        String title;
        if (TextUtils.isEmpty(share.title)) {
            title = activity.getResources().getString(R.string.lib_share_pic);
        } else {
            title = share.title;
        }

        UMImage umImage =new UMImage(activity,share.localImgUrl);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle(title);
        weixinContent.setShareImage(umImage);
        mUMSocialService.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareMedia(umImage);
        circleMedia.setTitle(activity.getResources().getString(R.string.lib_share_pic));
        circleMedia.setTargetUrl(share.imgUrl);
        //  circleMedia.setShareImage(mImageUrl);
        circleMedia.setShareContent(title);
        circleMedia.setShareImage(umImage);
        mUMSocialService.setShareMedia(circleMedia);
        //设置qq空间分享
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareMedia(umImage);
        qzone.setTitle(activity.getResources().getString(R.string.lib_share_pic));
        qzone.setShareContent(title);
        qzone.setTargetUrl(share.imgUrl);
        // qzone.setShareMedia(uMusic);
        mUMSocialService.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareMedia(umImage);
        qqShareContent.setTitle(activity.getResources().getString(R.string.lib_share_pic));
        mUMSocialService.setShareMedia(qqShareContent);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareMedia(umImage);
        sinaContent.setTitle(activity.getResources().getString(R.string.lib_share_pic));
        sinaContent.setShareContent(title + share.imgUrl + " @" + activity.getResources().getString(R.string.lib_share_sina_yiqi_name));
        sinaContent.setTargetUrl(share.imgUrl);
        mUMSocialService.setShareMedia(sinaContent);
        CustomShareBoard shareBoard = new CustomShareBoard(activity,mUMSocialService);
        shareBoard.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

    }

    public void shareLink(Activity activity,ShareLink link,int imgRes){
        if(isEmptyForSsoHandler()){
            addSharePlatform(activity);
        }
        // 设置微信分享内容
        final String img_url = link.headerUrl;

        final String title = "分享了一个链接" + activity.getResources().getString(R.string.lib_share_from_common);
        final String titleUrl = link.headerContent;
        String des = link.headerDesc;
        if(TextUtils.isEmpty(des)){
            des = activity.getResources().getString(R.string.lib_share_from_common);
        }
        UMImage umImage = null;
        if(!TextUtils.isEmpty(img_url)){
            umImage =new UMImage(activity, img_url);
        }else{
            umImage =new UMImage(activity,imgRes);
        }

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(des);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(titleUrl);
        weixinContent.setShareMedia(umImage);
        mUMSocialService.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(des);
        circleMedia.setTitle(des);
        circleMedia.setShareMedia(umImage);
        circleMedia.setTargetUrl(titleUrl);
        mUMSocialService.setShareMedia(circleMedia);
        //设置qq空间分享
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(des);
        qzone.setTargetUrl(titleUrl);
        qzone.setTitle(title);
        qzone.setShareMedia(umImage);
        mUMSocialService.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(des);
        qqShareContent.setTitle(title);
        qqShareContent.setShareMedia(umImage);
        qqShareContent.setTargetUrl(titleUrl);
        mUMSocialService.setShareMedia(qqShareContent);

        SinaShareContent sinaContent = new SinaShareContent();
        String  text;
        if (des.length()>100)
        {
            text =des.substring(0,99);
            text=text+"...";
        }else
        {
            text=des;
        }
        sinaContent.setShareContent(text+titleUrl+" @"+activity.getResources().getString(R.string.lib_share_sina_yiqi_name));
        sinaContent.setShareImage(umImage);
        sinaContent.setTitle(title);
        mUMSocialService.setShareMedia(sinaContent);

        CustomShareBoard shareBoard = new CustomShareBoard(activity,mUMSocialService);
        shareBoard.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public static class Share{
        public String title;
        public String imgUrl;
        public String localImgUrl;
        public String webUrl;
        public String shareDes;
        public String id;

        public int defaultRes;
    }

    public static class ShareLink{
        public String   tid;
        public String headerUrl;// 图片的地址 对应 imagesrc
        public int    headerType;//链接类型 type
        public String headerContent;//链接类型的值 banner_url
        public String headerDesc;//链接类型的值 banner_url
    }




}
