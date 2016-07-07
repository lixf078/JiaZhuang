package com.aiyiqi.decoration.app.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.aiyiqi.decoration.lib.share.SocialShareUtil;
import com.aiyiqi.decoration.lib.style.DrawableCenterTextView;
import com.aiyiqi.lib.utils.NetUtil;
import com.aiyiqi.lib.utils.ToastUtil;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by hubing on 16/3/21.
 */
public class BaseActivity extends Activity {
    public View mHeadLayout;
    public TextView mHeadLeftView;
    public TextView mHeadMiddelView;
    public TextView mHeadRightView;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lib_base_activity_base);
        noMoreMsg = noMsg = getResources().getString(R.string.lib_style_no_more);
        mSocialShareUtil = SocialShareUtil.getInstance(this);
        initBaseView();
    }

    private void initBaseView(){

        mHeadLayout = this.findViewById(R.id.head_layout);
        mHeadLeftView = (TextView) this.findViewById(R.id.head_left_view);
        mHeadLeftView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mHeadMiddelView = (TextView) this.findViewById(R.id.head_middel_view);
        mHeadRightView = (TextView) this.findViewById(R.id.head_right_view);
        mContentView = (ViewGroup) this.findViewById(R.id.content_layout);

        mLoadingStub = (ViewStub) this.findViewById(R.id.stub_loading);
        mNoDataStub = (ViewStub) this.findViewById(R.id.stub_no_data);
        mNoNetStub = (ViewStub) this.findViewById(R.id.stub_no_net);

        mPtrFrame = (PtrClassicFrameLayout) this.findViewById(R.id.rotate_header_frg_order);
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
                if(NetUtil.isNetworkAvailable(BaseActivity.this)){
                   if(mPullRefreshListener!=null){
                       mPullRefreshListener.onPullRefresh();
                   }
                }else{
                    ToastUtil.showToast(BaseActivity.this,"暂无网络，请稍后重试");
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
                if (NetUtil.isNetworkAvailable(BaseActivity.this)) {
                    if(listener!=null){
                        listener.onNoNetRefresh();;
                    }
                } else {
                    ToastUtil.showToast(BaseActivity.this, "暂无网络，请稍后重试");
                }
            }
        });
    }

    protected void hiddenNoNetView(){
        if(mNoNetView!=null && mNoNetView.getVisibility()==View.VISIBLE){
            mNoNetView.setVisibility(View.GONE);
        }
    }

    protected void resolveError(int errorCode,String errorMsg){
        if(errorCode == 3){
            Intent intent = new Intent();
            intent.setClassName(this,"com.aiyiqi.decoration.my.LoginActivity");
            startActivity(intent);
            finish();
        }else{
            ToastUtil.showToast(this, errorMsg);
        }

    }


    protected  void socialShareTocircle(SocialShareUtil.Share share){
        if(share!=null){
            if(!TextUtils.isEmpty(share.webUrl)){
                mSocialShareUtil.shareImageAndContentToCircle(this, share);
            }
        }
    }

    protected void socialShareImgOnly(SocialShareUtil.Share share){
        mSocialShareUtil.shareOnlyPictureTo(this, share);
    }


    public interface OnPullRefreshListener{
        public void onPullRefresh();
    }

    public interface OnNoNetRefreshListener{
        public void onNoNetRefresh();
    }

}
