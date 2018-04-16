package cn.speedpay.s.xedj.activitys;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.speedpay.s.xedj.MBaseActivity;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.bean.OrderInfoDetailsBean;
import cn.speedpay.s.xedj.frame.ui.ToastUtil;
import cn.speedpay.s.xedj.presenter.FlashOrderDetailPresenter;
import cn.speedpay.s.xedj.presenter.FlashOrderDetailPresenterImp;
import cn.speedpay.s.xedj.printer.printer.PrinterClassSerialPort;
import cn.speedpay.s.xedj.utils.CodeUtil;
import cn.speedpay.s.xedj.view.FlashOrderDetailView;
import cn.speedpay.s.xedj.view.GridViewForScrollView;

/**
 * Created by Administrator on 2016/8/19.
 */
public class FlashOrderDetailActivity extends MBaseActivity implements FlashOrderDetailView {
    @InjectView(R.id.tv_order_num)
    TextView tvOrderNum;
    @InjectView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @InjectView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @InjectView(R.id.tv_receiver_phone)
    TextView tvReceiverPhone;
    @InjectView(R.id.tv_order_time)
    TextView tvOrderTime;
    @InjectView(R.id.tv_order_account)
    TextView tvOrderAccount;
    @InjectView(R.id.tv_pay_method)
    TextView tvPayMethod;
    @InjectView(R.id.tv_market_address)
    TextView tvMarketAddress;
    @InjectView(R.id.gv_goods_info)
    GridViewForScrollView gvGoodsInfo;
    @InjectView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @InjectView(R.id.tv_cut_down)
    TextView tvCutDown;
    @InjectView(R.id.tv_voucher)
    TextView tvVoucher;
    @InjectView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @InjectView(R.id.tv_order_remark)
    TextView tvOrderRemark;
    @InjectView(R.id.tv_invoice)
    TextView tvInvoice;
    @InjectView(R.id.tv_refund)
    TextView tvRefund;
    @InjectView(R.id.tv_print_ticket)
    TextView tvPrintTicket;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    //返回的数据
    private JSONObject obj;
    private FlashOrderDetailPresenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_flash_detail;
    }

    @Override
    protected void initParmas() {
        ButterKnife.inject(this);

        presenter = new FlashOrderDetailPresenterImp(this);
        String orderid = getIntent().getStringExtra("orderid");
        presenter.orderDetail(orderid);  //查询该订单详情
    }

    @Override
    public void setText(JSONObject object) {
        obj = object;
        try {
            tvOrderNum.setText(object.getString("orderid"));
            tvOrderStatus.setText(object.getString("orderstatus"));
            tvReceiverAddress.setText(object.getString("address"));
            tvReceiverPhone.setText(object.getString("orderphone"));
            tvOrderTime.setText(object.getString("ordertime"));
            tvOrderAccount.setText(object.getString("orderaccount"));
            tvPayMethod.setText(object.getString("orderpaychannel"));
            tvMarketAddress.setText(object.getString("ordersupermarket") );
            tvGoodsPrice.setText(object.getString("ordergoodsallprice"));
            tvCutDown.setText(object.getString("yxjmprice"));
            tvVoucher.setText(object.getString("yhprice"));
            tvPayMoney.setText(object.getString("orderpayprice"));
            tvOrderRemark.setText(object.getString("remark"));
            tvInvoice.setText(object.getString("isfp"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setListViewText(BaseAdapter adapter) {
        gvGoodsInfo.setAdapter(adapter);
    }

    /**
     * 闪购退款
     *
     * @param jsonObject 返回的信息
     */
    @Override
    public void tuiKuanMsg(JSONObject jsonObject) {
        try {
            ToastUtil.get().shortToast(jsonObject.getString("resultdesc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印小票 返回的数据
     *
     * @param jsonObject  数据
     */
    PrinterClassSerialPort printerClass = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (printerClass != null) {
            printerClass.close(this);
        }
    }

    @Override
    public void printTicket(JSONObject jsonObject) {
        try {
            //转成bean对象
            OrderInfoDetailsBean objBean = new Gson().fromJson(jsonObject.toString(), OrderInfoDetailsBean.class);


            String orderid = jsonObject.getString("orderid");

            String barcode = CodeUtil.orderIdTurnCodeID(orderid);
            Bitmap bitmap = CodeUtil.CreateBarCode(barcode);  //条码图片资源
            printerClass = new PrinterClassSerialPort(new Handler(), this);
            printerClass.open(this);

            printerClass.printTextFlash(objBean, FlashOrderDetailActivity.this, barcode);  //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMess(String str) {
        ToastUtil.get().shortToast(str);
    }

    /**
     * 闪购退款点击事件
     *
     * @param view
     */
    @OnClick(R.id.tv_refund)
    public void tuiKuan(View view) {
        showAlertDialog();
    }

    private Dialog mAlertDialog;

    private void showAlertDialog() {
        View inflate = View.inflate(this, R.layout.tuikuan_dialog, null);

        //        final MainPresenter mainPresenter = new MainPresenterImp(this);
        mAlertDialog = new Dialog(this, R.style.add_dialog);
        mAlertDialog.setContentView(inflate);
        mAlertDialog.show();

        inflate.findViewById(R.id.rl_cancle).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                    }
                });

        inflate.findViewById(R.id.rl_confim).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //确认退款
                        mAlertDialog.dismiss();
                        if (TextUtils.isEmpty(getCheckCount())) {
                            ToastUtil.get().shortToast("请勾选退款商品");
                        } else {
                            presenter.tuiKuan(getCheckCount(), tuiHuoStatus);
                        }
                    }
                });
    }

    /**
     * 返回
     *
     * @param view
     */
    @OnClick(R.id.iv_back)
    public void back(View view) {
        finish();
    }

    /**
     * 打印小票
     *
     * @param viwe
     */
    @OnClick(R.id.tv_print_ticket)
    public void printTicket(View viwe) {
        String orderid = tvOrderNum.getText().toString().trim();
        presenter.printTicket(orderid);
    }

    /**
     * 获取被选中的checkbox
     */
    String tuiHuoStatus = "1";  //退货状态

    public String getCheckCount() {
        String goodsid = "";
        try {
            JSONArray jsonArray = obj.getJSONArray("goodsinfo");
            StringBuffer ids = new StringBuffer();
            for (int i = 0; i < gvGoodsInfo.getChildCount(); i++) {
                LinearLayout ll = (LinearLayout) gvGoodsInfo.getChildAt(i);// 获得子级
                CheckBox chkone = (CheckBox) ll.findViewById(R.id.cb_select);// 从子级中获得控件
                if (chkone.isChecked()) {  //选中
                    ids.append(jsonArray.getJSONObject(i).getString("goodsid") + ",");
                } else { //若果有没被选中的
                    tuiHuoStatus = "0";
                }
            }
            goodsid = ids.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goodsid;
    }


}
