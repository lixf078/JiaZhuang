package com.aiyiqi.decoration.lib.api;

import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by hubing on 15/12/3.
 */
public abstract class Listeners {
    public static abstract interface FetchListener<T extends Object>{
        public abstract void onStart();
        public abstract void onSuccess(T result);
        public abstract void onError(int errorCode, String errorMsg);
    }

    public abstract static class LoginOnSpanClickListener extends ClickableSpan {
        BaseLoginClickListener checkListener = new BaseLoginClickListener() {
            protected void doAfterLogin(View var1) {
                LoginOnSpanClickListener.this.doAfterLogin(var1);
            }
        };

        public LoginOnSpanClickListener() {
        }

        public final void onClick(View var1) {
            this.checkListener.onClick(var1);
        }

        protected abstract void doAfterLogin(View var1);
    }

    abstract static class BaseLoginClickListener {
        BaseLoginClickListener() {
        }

        public final void onClick(final View var1) {
        }

        protected abstract void doAfterLogin(View var1);
    }
}
