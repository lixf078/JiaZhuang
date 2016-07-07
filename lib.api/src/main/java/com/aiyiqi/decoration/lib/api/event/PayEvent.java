package com.aiyiqi.decoration.lib.api.event;

import com.aiyiqi.decoration.lib.api.pay.PayState;
import com.aiyiqi.lib.base.BaseEvent;

/**
 * Created by hubing on 16/3/18.
 */
public class PayEvent extends BaseEvent {
    public static final int TYPE_PAY_QR = 0;
    public static final int TYPE_PAY_CHECKOUT_STATE = 1;
    public String qrString;
    public PayState payState;
}
