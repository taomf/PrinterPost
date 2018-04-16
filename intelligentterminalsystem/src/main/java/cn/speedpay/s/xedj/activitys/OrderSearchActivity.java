package cn.speedpay.s.xedj.activitys;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.serialport.api.ShowToast;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.MBaseActivity;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.adapter.PullSelectAdapter;
import cn.speedpay.s.xedj.frame.ui.ToastUtil;
import cn.speedpay.s.xedj.presenter.OrderSearchPresenter;
import cn.speedpay.s.xedj.presenter.OrderSearchPresenterImp;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.RequestUtils;
import cn.speedpay.s.xedj.view.OrderSearchView;

/**
 * Created by taomf on 2016-8-16.
 * Description:
 */
public class OrderSearchActivity extends MBaseActivity implements View.OnClickListener,OrderSearchView {


    private PopupWindow pw;
    private ListView mListView;
    private List<String> mList;
    private PullSelectAdapter adapter;
    private TextView tv_goods_status,tv_send_order_status,tv_send_methd,tv_reject_status;
    private ImageView iv_ordernum_search,iv_orderaccount_serarch;
    private OrderSearchPresenter presenter;

    private EditText et_id_search,et_acc_search,et_order_account;
    private ListView searchListView;

    @Override
    public int getContentView() {
        return R.layout.activity_order_search;
    }

    @Override
    protected void initParmas() {
        presenter = new OrderSearchPresenterImp(this);

        findViewById(R.id.slide_detail_search).setOnClickListener(this);

        findViewById(R.id.iv_back).setOnClickListener(this);

        searchListView = (ListView) findViewById(R.id.order_search_lv);

        iv_ordernum_search = (ImageView)findViewById(R.id.at_order_search_iv);
        iv_orderaccount_serarch = (ImageView)findViewById(R.id.at_order_search_iv_acc);

        et_id_search = (EditText) findViewById(R.id.et_ordersearch_id);
        et_acc_search = (EditText) findViewById(R.id.et_ordersearch_acc);

        findViewById(R.id.tv_scan_print).setOnClickListener(this);  //扫一扫

        iv_ordernum_search.setOnClickListener(this); //订单编号查询
        iv_orderaccount_serarch.setOnClickListener(this); //订单账号查询

        /*et_id_search.setText("16082311492092010002");
        et_acc_search.setText("18170602313");*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.slide_detail_search: //显示侧边栏
                if(brandDialog != null){
                    if(!brandDialog.isShowing()){
                        showSlideDetailSearch();
                    }
                }else{
                    showSlideDetailSearch();
                }

                break;
            case R.id.rl_goods_status:  //备货状态
                List<String> stringList = new ArrayList<String>();
                stringList.add("初始化");
                stringList.add("备货中");
                stringList.add("备货成功");
                stringList.add("备货失败");
                stringList.add("备货异常");
                showPullDownSelect(tv_goods_status,stringList,v);
                break;
            case R.id.rl_send_order_status: //派单状态
                List<String> sendOrderList = new ArrayList<String>();
                sendOrderList.add("初始化");
                sendOrderList.add("派单中");
                sendOrderList.add("派单成功");
                sendOrderList.add("派单失败");
                showPullDownSelect(tv_send_order_status,sendOrderList,v);
                break;
            case R.id.rl_send_methd: //配送方式
                List<String> send_methd = new ArrayList<String>();
                send_methd.add("初始化");
                send_methd.add("自提");
                send_methd.add("小e配送");
                showPullDownSelect(tv_send_methd,send_methd,v);
                break;
            case R.id.rl_reject_status: //拒收状态
                List<String> reject_status = new ArrayList<String>();
                reject_status.add("初始化");
                reject_status.add("待拒收");
                reject_status.add("拒收成功");
                reject_status.add("拒收失败");
                showPullDownSelect(tv_reject_status, reject_status, v);
                break;

            case R.id.at_order_search_iv_acc: //订单账号查询
                presenter.orderAccount();
                break;
            case R.id.at_order_search_iv: //订单编号查询
                presenter.orderNumSearch();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_scan_print:  //扫一扫
                Intent scanIntent = new Intent(this,CaptureActivity.class);
                scanIntent.addFlags(Constant.MIntent.TOCAPTUREACTIVITY);
                startActivityForResult(scanIntent,Constant.MIntent.CAPTUREACTIVITYREQUESTCODE);

                break;

            case R.id.tv_restart :  //重置
                selectReset();

                break;
            case R.id.tv_confirm: //确认
                presenter.orderDetail(getAllSearchTip());
                brandDialog.cancel();

                break;
        }
    }

    /**
     * 重置
     */
    private void selectReset() {
        tv_goods_status.setText("点击选择");
        tv_send_order_status.setText("点击选择");
        tv_send_methd.setText("点击选择");
        tv_reject_status.setText("点击选择");

        et_order_account.setText("");
    }

    /**
     * 获取所选择的筛选条件
     * @return 条件集合
     */
    private Map<String,String> getAllSearchTip() {
        Map<String,String> map = new TreeMap<>();
        map.put("orderaccount", et_order_account.getText().toString().trim());

        map.put("readystatus", RequestUtils.getGoodStatus(tv_goods_status.getText().toString().trim()));
        map.put("assignstatus", RequestUtils.getSendStatus(tv_send_order_status.getText().toString().trim()));
        map.put("sendtype", RequestUtils.sendMethod(tv_send_methd.getText().toString().trim()));
        map.put("rejectstatus", RequestUtils.getRejectStatus(tv_reject_status.getText().toString().trim()));

        return  map;
    }

   AlertDialog brandDialog;
    /**
     * 显示侧边详情搜索栏
     */
    private void showSlideDetailSearch() {
        brandDialog = new AlertDialog.Builder(this).create();
        brandDialog.show();
        Window window = brandDialog.getWindow();
        //设置动画
        window.setWindowAnimations(R.style.mystyle);
        //window.setAttributes(params);
        // 设置窗口的内容页面,dialog_category_product_brand_layout.xml文件中定义view内容
        window.setContentView(R.layout.slide_detail_search);
        window.setGravity(Gravity.RIGHT);
        //品牌适配器加载
//        lv_brand = (ListView) window.findViewById(R.id.slide_detail_search);
        brandDialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        brandDialog.show();

        initPullDownSelect(window);
    }

    /**
     * 初始侧边栏化参数
     * @param window
     */
    private void initPullDownSelect(Window window) {

        window.findViewById(R.id.rl_goods_status).setOnClickListener(this);
        window.findViewById(R.id.rl_reject_status).setOnClickListener(this);
        window.findViewById(R.id.rl_send_methd).setOnClickListener(this);
        window.findViewById(R.id.rl_send_order_status).setOnClickListener(this);
        window.findViewById(R.id.tv_restart).setOnClickListener(this);
        window.findViewById(R.id.tv_confirm).setOnClickListener(this);
        et_order_account = (EditText) window.findViewById(R.id.et_order_account);

        tv_goods_status = (TextView) window.findViewById(R.id.tv_goods_status);
        tv_send_order_status = (TextView) window.findViewById(R.id.tv_send_order_status);
        tv_send_methd = (TextView) window.findViewById(R.id.tv_send_methd);
        tv_reject_status = (TextView) window.findViewById(R.id.tv_reject_status);
    }

    /**
     * 显示下拉选择框
     * @param v textview
     * @param list 集合
     * @param view 父控件
     */
    private void showPullDownSelect(TextView v,List list,View view){
        //初始化listView
        mListView = initListView(v,list);
        //弹出一个popupwindow窗体,把listView作为其内部类容显示
        pw = new PopupWindow(mListView,view.getWidth()+16, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置可以使用焦点
        pw.setFocusable(true);
        // 设置popupwindow点击外部可以被关闭
        pw.setOutsideTouchable(true);
        // 设置一个popupWindow的背景
        pw.setBackgroundDrawable(new BitmapDrawable());
        //popupwindow显示出来,显示在EditText下面
        pw.showAsDropDown(view, -8, -4);
    }

    /**
     * 侧边栏界面 初始化listview
     * @param textView
     * @param list
     * @return
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private ListView initListView(final TextView textView,List list) {
        mListView = new ListView(this);
        mListView.setBackground(getResources().getDrawable(R.drawable.rounded_range_pull_select));
        mListView.setDivider(getResources().getDrawable(R.drawable.horizontal_line));
        //去掉滚动条
        mListView.setVerticalScrollBarEnabled(false);
        mList = list;
        adapter = new PullSelectAdapter(list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String num = mList.get(position);
                textView.setText(num);
                pw.dismiss();
            }
        });
        return mListView;
    }

    /**
     * 返回搜索条件 订单号搜索
     * @return
     */
    @Override
    public String getOrderNumSearchString(){
        return et_id_search.getText().toString().trim();
    }

    /**
     * 返回搜索条件 账号搜索
     * @return
     */
    @Override
    public String getOrderAccountSearchString(){
        return et_acc_search.getText().toString().trim();
    }

    /**
     * 设置适配器数据
     * @param adapter
     */
    @Override
    public void setListView(BaseAdapter adapter){
        searchListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 返回listview
     * @return
     */
    @Override
    public ListView getListView(){
        return searchListView;
    }

    /**
     * 跳转详情页面
     * @param orderid  订单id
     * @param ordertype 订单类型
     */
    @Override
    public void startDeatilActivity(String orderid,String ordertype){
        Intent intent = new Intent();
        intent.putExtra("orderid",orderid);

        if(TextUtils.equals("商超订单",ordertype)){
            intent.setClass(this, MarketOrderDetailActivity.class);
        }else if(TextUtils.equals("闪购订单",ordertype)){
            intent.setClass(this,FlashOrderDetailActivity.class);
        }else if(TextUtils.equals("拒收订单",ordertype)){
            intent.setClass(this,RejectOrderDetailActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void showAlertMsg(String string) {
        ShowToast.showToast(this,string);
        ToastUtil.get().shortToast(string);
    }

    /**
     * 扫一扫返回的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String id = "";
        if(data != null && resultCode == Constant.MIntent.CAPTUREACTIVITYRESULTCODE){
             id = data.getStringExtra("code");
        }
        Log.i("taomf++++",id+"taomf");
        presenter.orderNumSearch(id);
    }
}
