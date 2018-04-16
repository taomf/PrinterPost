package cn.speedpay.s.xedj.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.google.zxing.WriterException;
import com.umeng.update.UmengUpdateAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.speedpay.s.xedj.FastFrame;
import cn.speedpay.s.xedj.MBaseActivity;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.frame.ui.ToastUtil;
import cn.speedpay.s.xedj.presenter.MainPresenter;
import cn.speedpay.s.xedj.presenter.MainPresenterImp;
import cn.speedpay.s.xedj.utils.CodeUtil;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.SPUtils;
import cn.speedpay.s.xedj.view.MainView;

public class MainActivity extends MBaseActivity implements View.OnClickListener,MainView {

    private View view;
    @Override
    public int getContentView() {
        FastFrame.init(getApplication());


        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

    @Override
    protected void initParmas() {
        findViewById(R.id.at_main_iv_order_search).setOnClickListener(this);
        findViewById(R.id.at_main_iv_scan_print).setOnClickListener(this);

        view = findViewById(R.id.at_main_rl_logout);
        view.setOnClickListener(this);

        boolean status = SPUtils.getInstance("login").readBoolean("loginSuccess",false);
        if(status){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }


//        try {
//          Bitmap bitmap = CodeUtil.createQrBitmap("12222",1000,600);
//
//            File file = new File("/sdcard/myFolder");
//            if (!file.exists())
//                file.mkdir();
//
//            file = new File("/sdcard/temp.jpg".trim());
//            String fileName = file.getName();
//            String mName = fileName.substring(0, fileName.lastIndexOf("."));
//            String sName = fileName.substring(fileName.lastIndexOf("."));
//
//            // /sdcard/myFolder/temp_cropped.jpg
//            String newFilePath = "/sdcard/myFolder" + "/" + mName + "_cropped" + sName;
//            file = new File(newFilePath);
//            try {
//                file.createNewFile();
//                FileOutputStream fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.at_main_iv_scan_print:  //扫描打印
//                Intent print = new Intent(this, PrintDemo.class);
//                startActivity(print);
                Intent scanPrint = new Intent(this,CaptureActivity.class);
                scanPrint.addFlags(Constant.MIntent.MAINACTIVITYFLG);
                startActivity(scanPrint);
                break;
            case R.id.at_main_iv_order_search: //订单查询
                boolean status = SPUtils.getInstance("login").readBoolean("loginSuccess",false);
                if(status){ //登录成功
                    Intent orderSearchIntent = new Intent(this,OrderSearchActivity.class);
                    startActivity(orderSearchIntent);
                }else{
                    Intent loginIntent = new Intent(this,LoginActivity.class);
                    startActivityForResult(loginIntent, Constant.NetWork.CODE_LOGIN_ACTIVITY);
                }

                break;
            case R.id.at_main_rl_logout: //退出登录
                showtLogOutDialog();
                break;
        }
    }
    private Dialog mAlertDialog;
    private void showtLogOutDialog() {
        View inflate = View.inflate(this, R.layout.logout_dialog, null);

        final MainPresenter mainPresenter = new MainPresenterImp(this);

//		AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                        mainPresenter.loginOut();
                    }
                });
    }

    /**
     * 退出登录
     */
    @Override
    public void loginOut(String desc,boolean status) {
        if(status){
            view.setVisibility(View.GONE);
        }
        ToastUtil.get().shortToast(desc);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            boolean succ = data.getBooleanExtra("loginSuccess",false);
            if(succ){ //成功
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
            }
        }
    }
}
