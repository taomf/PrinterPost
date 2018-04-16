package cn.speedpay.s.xedj.activitys;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.MBaseActivity;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.frame.ui.ToastUtil;
import cn.speedpay.s.xedj.listener.EditTextWatcher;
import cn.speedpay.s.xedj.presenter.LoginPresenter;
import cn.speedpay.s.xedj.presenter.LoginPresenterIml;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.RequestUtils;
import cn.speedpay.s.xedj.view.LoginView;

/**
 * Created by taomf on 2016-8-16.
 * Description:
 */
public class LoginActivity extends MBaseActivity implements View.OnClickListener,LoginView {
    private EditText et_account, et_password;
    private RelativeLayout rl_login;
    private TextView tv_login;
    private LoginPresenter loginPresenter;


    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initParmas() {
        loginPresenter = new LoginPresenterIml(this);

        et_account = (EditText) findViewById(R.id.at_login_et_account);
        et_password = (EditText) findViewById(R.id.at_login_et_password);
        rl_login = (RelativeLayout) findViewById(R.id.at_login_rl_login);
        tv_login = (TextView) findViewById(R.id.at_login_tv_login);
        //点击事件
        rl_login.setOnClickListener(this);
        //输入框监听事件
        et_account.addTextChangedListener(new EditTextWatcher(rl_login,et_account,et_password,tv_login));
        et_password.addTextChangedListener(new EditTextWatcher(rl_login, et_account, et_password, tv_login));

      /*  et_account.setText("18170602313");
        et_password.setText("09104836");*/
        rl_login.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.at_login_rl_login: //登录
                loginPresenter.login();
                break;
        }
    }


    @Override
    public String getUsername() {
        return et_account.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return et_password.getText().toString().trim();
    }


    @Override
    public void showMsg(boolean status,String desc) {
        if(status){ //登录成功
            Intent intent = new Intent();
            intent.putExtra("loginSuccess",true);
            setResult(200,intent);
            finish();
        }
            ToastUtil.get().shortToast(desc);
    }
}
