package com.magicsoft.whalepaydemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lhqpay.ewallet.utils.BaseUtil;
import com.magicsoft.whale.entity.AppPayChannel;
import com.magicsoft.whale.entity.PayChannel;
import com.magicsoft.whale.entity.PayResp;
import com.magicsoft.whale.util.WhalePay;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    /**
     * 获取的支付方式存在于此数组中才可以显示，微信支付宝双乾的pano为0，其他支付方式值为收款帐号编号
     */
    private List<PayChannel> receiptArray = new ArrayList<PayChannel>();//app提供的收款通道
    /**
     * 获取的支付方式存在于此数组中才可以显示，微信支付宝双乾的pano为0，其他支付方式值为收款帐号编号
     */
    private List<AppPayChannel> payTypeArray = new ArrayList<AppPayChannel>();//商户支持的支付通道

    private List<String> arrayData;
    //  private List<Map<String, ?>> simpleData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPayTypeArray();
        arrayData = getData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,arrayData);
        setListAdapter(adapter);
    }
    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("彩之云邻花钱");
        data.add("Whale支付（微信支付宝邻花钱）");
   
        return data;
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        getReceiptArray(position);
        akpay();
    }


    /**
     *
     */
    private void getReceiptArray(int which) {

        PayChannel payChannel2 = new PayChannel();
        payChannel2.setAtid(2);
        payChannel2.setAno("9f22bdb6934141ecb7e5a4506958a51b");
        payChannel2.setInfo("优惠");
        payChannel2.setIsDefault(0);
        payChannel2.setCtype(4);
        payChannel2.setType("fanpiao");
        payChannel2.setName("彩之云全国饭票");
        payChannel2.setMoney(1);

        PayChannel payChannel3 = new PayChannel();
        payChannel3.setAtid(26);
        payChannel3.setAno("1026f3ea6372f073484c88d3de8104d8");
        payChannel3.setInfo("优惠");
        payChannel3.setIsDefault(0);
        payChannel3.setCtype(1);
        payChannel3.setType("alipay");
        payChannel3.setName("彩之云支付宝");
        payChannel3.setMoney(0.01);

        PayChannel payChannel4 = new PayChannel();
        payChannel4.setAtid(25);
        payChannel4.setAno("2c5bd3201b9f46c19d6823322e9cb5fb");
        payChannel4.setInfo("优惠");
        payChannel4.setIsDefault(0);
        payChannel4.setCtype(1);
        payChannel4.setType("weixin");
        payChannel4.setName("彩之云微信");
        payChannel4.setMoney(0.01);

        PayChannel payChannel5 = new PayChannel();
        payChannel5.setAtid(31);
        payChannel5.setAno("1031c5a4efa552b24491bda9d212abbb");
        payChannel5.setInfo("优惠");
        payChannel5.setIsDefault(0);
        payChannel5.setCtype(1);
        payChannel5.setType("lhq");
        payChannel5.setName("彩之云邻花钱");
        payChannel5.setMoney(0.1);

        receiptArray.clear();

        switch (which) {
            case 0:
                receiptArray.add(payChannel5);
                break;
            case 1:
                payChannel2.setIsDefault(1);              
                receiptArray.add(payChannel2);
                receiptArray.add(payChannel3);
                receiptArray.add(payChannel4);
                receiptArray.add(payChannel5);
                break;
       
        }

    }


    /**
     *
     */
    private void getPayTypeArray() {

        AppPayChannel payChannel2 = new AppPayChannel();
        payChannel2.setAtid(2);
        payChannel2.setAno("qg_platform_1111");
        payChannel2.setName("彩之云全国饭票");

        AppPayChannel payChannel3 = new AppPayChannel();
        payChannel3.setAtid(26);
        payChannel3.setAno("57000501ac16410cab602096dc66baa1");
        payChannel3.setName("彩之云支付宝");

        AppPayChannel payChannel4 = new AppPayChannel();
        payChannel4.setAtid(25);
        payChannel4.setAno("2c5bd3201b9f46c19d6823322e9cb5fb");
        payChannel4.setName("彩之云微信");

        AppPayChannel payChannel5 = new AppPayChannel();
        payChannel5.setAtid(31);
        payChannel5.setAno("1031c5a4efa552b24491bda9d212abbb");
        payChannel5.setName("彩之云邻花钱");


       
        payTypeArray.add(payChannel2);
        payTypeArray.add(payChannel3);
        payTypeArray.add(payChannel4);
        payTypeArray.add(payChannel5);
    }

    private void akpay() {
        WhalePay akPay = WhalePay.getInstance(this);
        PayResp pay = new PayResp();
        //接入平台账号 必填
        pay.setPno("5e6d164b24be4d748fde32d814b17171");
        //用户账号，保留字段 必填
        pay.setCno("cf43a400aabf49dba4e2c62a5fe29f07");
           /*
     当发生跨币种汇兑交易时，金融平台会自动根据汇率计算交易金额。转账时一般需要固定支付金额，避免多扣款；消费时一般不固定，确保收款正确。
     */
        //支付金额固定，0不固定，1固定
        pay.setFixedorgmoney("0");
        //
        pay.setUserId("10086");
        //用户姓名 选填
        pay.setUserName("测试用户");
        //订单类型 选填
        pay.setOrdertype("0");
        //业务系统交易编号 必填
        pay.setOrderno("20161008143504" + BaseUtil.createRandomNumber(1000, 9000));
        //交易说明（显示给用户的内容） 必填
        pay.setContent("商品");
        //交易描述 选填
        pay.setDetail("商品描述");
        //手机号 选填
        pay.setMobile("12345678901");
        // 交易生效时间
        pay.setStarttime("");
        // 交易失效时间
        pay.setStoptime("");
        //业务系统回调地址
        pay.setCallback("");
        //调用方ip地址
        pay.setRemoteip("192.168.0.200");
        pay.setOrgsubaccount("");// 保留字段
        pay.setDestsubaccount("");// 保留字段
   /* 小区信息 均为option*/
        //省
        pay.setProvince("");
        //城市
        pay.setCity("");
        //区
        pay.setBlock("");
        //小区名称
        pay.setGbName("");
        pay.setReceiptArray(receiptArray);
        pay.setPayTypeArray(payTypeArray);

        akPay.pay(pay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (10010 == resultCode) {
            // WhalePay返回
            if (data != null) {
                int code = data.getIntExtra("code", -1);

                if (code == 10088) {
                    //支付成功
                    Toast.makeText(this, data.getStringExtra("message"), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this, data.getStringExtra("message"),
                            Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}