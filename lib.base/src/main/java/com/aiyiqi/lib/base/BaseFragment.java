package com.aiyiqi.lib.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.aiyiqi.decoration.lib.share.SocialShareUtil;
import com.aiyiqi.decoration.lib.style.DrawableCenterTextView;
import com.aiyiqi.lib.utils.ImgUtil;
import com.aiyiqi.lib.utils.Logger;
import com.aiyiqi.lib.utils.NetUtil;
import com.aiyiqi.lib.utils.ToastUtil;

import java.io.File;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by hubing on 16/3/21.
 */
public class BaseFragment extends Fragment {
    public View mHeadLayout;
    public TextView mHeadLeftView;
    public TextView mHeadMiddelView;
    public TextView mHeadRightView;

    protected TextView mFootView;
    public ViewGroup mContentView;

    public ViewStub mLoadingStub;
    public ViewStub mNoDataStub;
    public ViewStub mNoNetStub;

    public View mLoadingView;
    public View mNoDataView;
    public View mNoNetView;

    private DrawableCenterTextView mRefreshView;
    private PtrClassicFrameLayout mPtrFrame;
    private View mCanSrollView;
    private OnPullRefreshListener mPullRefreshListener;

    protected String noMoreMsg,noMsg;

    private SocialShareUtil mSocialShareUtil;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSocialShareUtil = SocialShareUtil.getInstance(getActivity());

        View contentView = inflater.inflate(R.layout.lib_base_fragment_base,null,false);
        noMoreMsg = noMsg = getResources().getString(R.string.lib_style_no_more);
        initHeadView(contentView);

        return contentView;
    }

    protected void setCanSrollView(View scrollView,OnPullRefreshListener onPullRefreshListener){
        this.mCanSrollView = scrollView;
        this.mPullRefreshListener = onPullRefreshListener;
    }

    protected void setHeadLayoutVisiable(int visiable){
        if(mHeadLayout!=null){
            mHeadLayout.setVisibility(visiable);
        }
    }

    protected TextView getHeadLeftView() {
        return mHeadLeftView;
    }

    protected TextView getHeadMiddelView() {
        return mHeadMiddelView;
    }

    protected TextView getHeadRightView() {
        return mHeadRightView;
    }

    protected ViewGroup getContentView() {
        return mContentView;
    }

    protected TextView getFootView(){
        return mFootView;
    }

    protected void showLoadingVew(){
        if(mLoadingView == null){
            mLoadingView = mLoadingStub.inflate();
        }
        mLoadingView.setVisibility(View.VISIBLE);
    }

    protected void hiddenLoadingView(){
        if(mLoadingView!=null && mLoadingView.getVisibility()==View.VISIBLE){
            mLoadingView.setVisibility(View.GONE);
        }
    }

    protected void showNoDataVew(){
        if(mNoDataView == null){
            mNoDataView = mNoDataStub.inflate();
        }
        mNoDataView.setVisibility(View.VISIBLE);
    }

    protected void showNoDataView(boolean is){

    }

    protected void hiddenNoDataView(){
        if(mNoDataView!=null && mNoDataView.getVisibility()==View.VISIBLE){
            mNoDataView.setVisibility(View.GONE);
        }
    }

    protected void showNoNetVew(final OnNoNetRefreshListener listener){
        if(mNoNetView == null){
            mNoNetView = mNoNetStub.inflate();
        }
        mNoNetView.setVisibility(View.VISIBLE);
        mRefreshView = (DrawableCenterTextView) mNoNetView.findViewById(R.id.refresh);
        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isNetworkAvailable(getActivity())) {
                   if(listener!=null){
                       listener.onNoNetRefresh();;
                   }
                } else {
                    ToastUtil.showToast(getActivity(), "暂无网络，请稍后重试");
                }
            }
        });
    }

    protected void hiddenNoNetView(){
        if(mNoNetView!=null && mNoNetView.getVisibility()==View.VISIBLE){
            mNoNetView.setVisibility(View.GONE);
        }
    }

    public void initHeadView(View contentView){
        mHeadLayout = contentView.findViewById(R.id.head_layout);
        mHeadLeftView = (TextView) contentView.findViewById(R.id.head_left_view);
        mHeadMiddelView = (TextView) contentView.findViewById(R.id.head_middel_view);
        mHeadRightView = (TextView) contentView.findViewById(R.id.head_right_view);

        mFootView = (TextView) contentView.findViewById(R.id.foot_view);

        mContentView = (ViewGroup) contentView.findViewById(R.id.content_layout);

        mLoadingStub = (ViewStub) contentView.findViewById(R.id.stub_loading);
        mNoDataStub = (ViewStub) contentView.findViewById(R.id.stub_no_data);
        mNoNetStub = (ViewStub) contentView.findViewById(R.id.stub_no_net);

        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_frg_order);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if(mCanSrollView == null){
                    return false;
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mCanSrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if(NetUtil.isNetworkAvailable(getActivity())){
                   if(mPullRefreshListener!=null){
                       mPullRefreshListener.onPullRefresh();
                   }
                }else{
                    ToastUtil.showToast(getActivity(),"暂无网络，请稍后重试");
                }
                mPtrFrame.refreshComplete();
            }
        });
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
    }
    protected void resolveError(int errorCode,String errorMsg){
        if(errorCode == 3){
            Intent intent = new Intent();
            intent.setClassName(getActivity(),"com.aiyiqi.decoration.my.LoginActivity");
            startActivity(intent);
            getActivity().finish();
        }else{
            ToastUtil.showToast(getActivity(), errorMsg);
        }
    }
    public interface OnPullRefreshListener{
        public void onPullRefresh();
    }

    public interface OnNoNetRefreshListener{
        public void onNoNetRefresh();
    }

    protected void socialShare(SocialShareUtil.Share share){
        if(share!=null){
            if(!TextUtils.isEmpty(share.webUrl)){
                mSocialShareUtil.shareImageAndContent(getActivity(), share);
            }
        }
    }

     protected  void socialShareTocircle(SocialShareUtil.Share share){
         if(share!=null){
             if(!TextUtils.isEmpty(share.webUrl)){
                 mSocialShareUtil.shareImageAndContentToCircle(getActivity(), share);
             }
         }
     }
    protected void socialShareImgOnly(SocialShareUtil.Share share){
        mSocialShareUtil.shareOnlyPictureTo(getActivity(), share);
    }

//    protected void shareImageAndContent(String newsTitlte,String newsWebUrl,String currentNewsId,String des,String imgURL){
//
//        Logger.e(Constants.TAG, "shareImageAndContent >> newsTitlte : " + newsTitlte + " ; newsWebUrl ： " + newsWebUrl + " ;  currentNewsId : " + currentNewsId + " ; imgURL : " + imgURL + "; des : " + des);
//        SocialShareUtil.Share share = new SocialShareUtil.Share();
//        share.id = currentNewsId;
//        share.imgUrl = imgURL;
//        share.localImgUrl = "";
//        share.shareDes = des;
//        share.title = newsTitlte;
//        share.webUrl = newsWebUrl;
//        share.defaultRes = R.mipmap.ic_launcher;
//        socialShare(share);
//    }
//
//    protected  void shareOnlyPictureTo(String name,String imageUrl){
//        SocialShareUtil.Share share = new SocialShareUtil.Share();
//        share.imgUrl = imageUrl;
//        share.title = name;
//
//        if(ImgUtil.isImageDownloaded(Uri.parse(share.imgUrl))){
//            File file = ImgUtil.getCachedImageOnDisk(Uri.parse(share.imgUrl));
//            if(file!=null && file.exists()){
//                share.localImgUrl = file.getAbsolutePath();
//                mSocialShareUtil.shareOnlyPictureTo(this, share);
//            }
//
//        }
//    }
//
//    protected void shareLink(SocialShareUtil.ShareLink link){
//        if(link!=null){
//            mSocialShareUtil.shareLink(this, link,R.mipmap.ic_launcher);
//        }
//    }
}
