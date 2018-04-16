package cn.speedpay.s.xedj.activitys;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.speedpay.s.xedj.MBaseActivity;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.frame.ui.ToastUtil;
import cn.speedpay.s.xedj.presenter.RejectOrderDetailPresenter;
import cn.speedpay.s.xedj.presenter.RejectOrderDetailPresenterImp;
import cn.speedpay.s.xedj.view.GridViewForScrollView;
import cn.speedpay.s.xedj.view.RejectOrderDetailView;

/**
 * Created by Administrator on 2016/8/20.
 */
public class RejectOrderDetailActivity extends MBaseActivity implements RejectOrderDetailView {
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.tv_order_id)
    TextView tvOrderId;
    @InjectView(R.id.tv_zone_name)
    TextView tvZoneName;
    @InjectView(R.id.gv_rject_goods)
    GridViewForScrollView gvRjectGoods;
    @InjectView(R.id.tv_tuikuan_money)
    TextView tvTuikuanMoney;
    @InjectView(R.id.tv_creat_time)
    TextView tvCreatTime;
    @InjectView(R.id.tv_comfirm_reject)
    TextView tvComfirmReject;
    @InjectView(R.id.iv_back)
    ImageView ivBack;

    private JSONObject jsonObject;
    private RejectOrderDetailPresenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_reject_detail;
    }

    @Override
    protected void initParmas() {
        ButterKnife.inject(this);

        presenter  = new RejectOrderDetailPresenterImp(this);
        String orderid = getIntent().getStringExtra("orderid");
        presenter.orderDetail(orderid);  //查询该订单详情
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setText(JSONObject object) {
        jsonObject = object;
        try {
            tvOrderId.setText(object.getString("orderid"));
            tvZoneName.setText(object.getString("zonename"));
            tvCreatTime.setText(object.getString("rejectordercreatetime"));
            tvTuikuanMoney.setText(object.getString("refundamount"));

            if(TextUtils.equals(object.getString("orderstatuscode"), "42") || TextUtils.equals(object.getString("orderstatuscode"), "20")){
                tvComfirmReject.setText("已拒收");
                tvComfirmReject.setTextColor(Color.rgb(66,66,66));
                tvComfirmReject.setClickable(false);
                tvComfirmReject.setBackground(getResources().getDrawable(R.drawable.rounded_range_small_reset));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setListViewText(BaseAdapter adapter) {
        gvRjectGoods.setAdapter(adapter);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void rejectReceiver(String str) {
        tvComfirmReject.setText("已拒收");
        tvComfirmReject.setTextColor(Color.rgb(66,66,66));
        tvComfirmReject.setClickable(false);
        tvComfirmReject.setBackground(getResources().getDrawable(R.drawable.rounded_range_small_reset));
        ToastUtil.get().shortToast(str);
    }

    @Override
    public void rejectErrMsg(String str) {
        ToastUtil.get().shortToast(str);
    }

    @OnClick(R.id.iv_back)
    public void back(View view) {
        finish();
    }

    @OnClick(R.id.tv_comfirm_reject)  //拒收
    public void rejectReceiver(View view) {

        showAlerDialog();
    }

    private Dialog mAlertDialog;

    private void showAlerDialog() {
        View inflate = View.inflate(this, R.layout.reject_dialog, null);

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
                        presenter.rejectReceiver();
                        mAlertDialog.dismiss();
                    }
                });
    }
}
