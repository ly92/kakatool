package com.magicsoft.whalepaydemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.magicsoft.whale.util.Constant;
import com.magicsoft.whale.util.PayResultReceiver;
import com.magicsoft.whalepaydemo.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

//包名必须是android mainfest的包名（ cn.net.cyberway）+wxapi+WXPayEntryActivity
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;

    private final int DELAY_TIME = 0; // 延迟2秒

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weixing_pay_result);
        api = WXAPIFactory.createWXAPI(this, Constant.WXPAY_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 回調支付結果
                        Intent intent = new Intent(PayResultReceiver.PAY_RESULT_NOTIFICATION);
                        intent.putExtra("code", 10088);
                        intent.putExtra("message", "支付成功");
                        sendBroadcast(intent);
                        finish();

                    }
                }, DELAY_TIME);
            }

        } else {
            String msg = "";

            if (resp.errCode == -1) {
                msg = "已经取消支付";

            } else if (resp.errCode == -2) {
                msg = "支付失败";
            }
            Toast.makeText(WXPayEntryActivity.this, msg, Toast.LENGTH_SHORT);
            finish();
        }
    }
}