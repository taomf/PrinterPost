package cn.speedpay.s.xedj.activitys;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.serialport.api.ShowToast;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.speedpay.s.xedj.MBaseActivity;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.bean.OrderInfoDetailsBean;
import cn.speedpay.s.xedj.presenter.MarketOrderDetailPresenter;
import cn.speedpay.s.xedj.presenter.MarketOrderDetailPresenterImp;
import cn.speedpay.s.xedj.printer.printer.PrinterClassSerialPort;
import cn.speedpay.s.xedj.utils.CodeUtil;
import cn.speedpay.s.xedj.view.GridViewForScrollView;
import cn.speedpay.s.xedj.view.MarketOrderDetailView;

/**
 * Created by Administrator on 2016/8/20.
 * 商超订单详情页
 */
public class MarketOrderDetailActivity extends MBaseActivity implements MarketOrderDetailView {
    @InjectView(R.id.tv_receive_name)
    TextView tvReceiveName;
    @InjectView(R.id.tv_receive_phone)
    TextView tvReceivePhone;
    @InjectView(R.id.tv_receive_address)
    TextView tvReceiveAddress;
    @InjectView(R.id.gv_goods_info)
    GridViewForScrollView gvGoodsInfo;
    @InjectView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @InjectView(R.id.tv_freight)
    TextView tvFreight;
    @InjectView(R.id.tv_cut_down)
    TextView tvCutDown;
    @InjectView(R.id.tv_voucher)
    TextView tvVoucher;
    @InjectView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @InjectView(R.id.tv_conscience_money)
    TextView tvConscienceMoney;
    @InjectView(R.id.tv_order_remark)
    TextView tvOrderRemark;
    @InjectView(R.id.tv_invoice)
    TextView tvInvoice;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.at_print_rl_ticket)
    RelativeLayout atPrintRlTicket;
    @InjectView(R.id.tv_receive_timer)
    TextView tvReceiveTimer;

    private JSONObject jsObject;
    private MarketOrderDetailPresenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_market_detail;
    }

    @Override
    protected void initParmas() {
        ButterKnife.inject(this);

        presenter = new MarketOrderDetailPresenterImp(this);
        String orderid = getIntent().getStringExtra("orderid");
        presenter.orderDetail(orderid);  //查询该订单详情

    }

    @Override
    public void setText(JSONObject object) {
        jsObject = object;

        try {
            Log.i("aaaaaa", object.getString("name"));

            tvReceiveName.setText(object.getString("name"));
            tvReceivePhone.setText(object.getString("phone"));
            tvReceiveAddress.setText(object.getString("address"));
            tvGoodsPrice.setText(object.getString("ordergoodsallprice"));
            tvFreight.setText(object.getString("yfallprice"));
            tvCutDown.setText(object.getString("yxjmprice"));
            tvVoucher.setText(object.getString("couponpayprice"));
            tvConscienceMoney.setText(object.getString("bujiaoprice"));
            tvPayMoney.setText(object.getString("orderpayprice"));
            tvOrderRemark.setText(object.getString("remark"));
            tvInvoice.setText(object.getString("isfp"));

            tvReceiveTimer.setText(object.getString("ordertime"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setListViewText(BaseAdapter adapter) {

        gvGoodsInfo.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (printerClass != null) {
            printerClass.close(this);
        }
    }

    PrinterClassSerialPort printerClass = null;

    @Override
    public void printTicket(JSONObject jsonObject) {
        try {
            //转成bean对象
            OrderInfoDetailsBean objBean = new Gson().fromJson(jsonObject.toString(), OrderInfoDetailsBean.class);

            String orderid = null;

            orderid = jsonObject.getString("orderid");

            String barcode = CodeUtil.orderIdTurnCodeID(orderid);
            Bitmap bitmap = CodeUtil.CreateBarCode(barcode);  //条码图片资源

            printerClass = new PrinterClassSerialPort(new Handler(), this);
            printerClass.open(this);

            printerClass.printTextDemo(objBean, MarketOrderDetailActivity.this, barcode);  //
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showMsg(String string) {
        ShowToast.showToast(this, string);
    }

    @OnClick(R.id.iv_back)
    public void back(View view) {
        finish();
    }

    @OnClick(R.id.at_print_rl_ticket)
    public void printTicket(View view) {
        String orderid = null;
        try {
            orderid = jsObject.getString("orderid");
            presenter.printTicket(orderid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }
}
