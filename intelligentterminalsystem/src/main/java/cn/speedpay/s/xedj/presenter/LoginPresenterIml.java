package cn.speedpay.s.xedj.presenter;

import cn.speedpay.s.xedj.bean.UserBean;
import cn.speedpay.s.xedj.biz.LoginModelBiz;
import cn.speedpay.s.xedj.biz.LoginModelImpl;
import cn.speedpay.s.xedj.listener.OnLoginListener;
import cn.speedpay.s.xedj.view.LoginView;

/**
 * Created by taomf on 2016-8-17.
 * Description:
 */
public class LoginPresenterIml implements LoginPresenter,OnLoginListener {
    private LoginView loginView;
    private LoginModelBiz userBiz;


    public LoginPresenterIml(LoginView loginView){
        this.loginView = loginView;
        userBiz = new LoginModelImpl(this);
    }

    @Override
    public void login() {
        UserBean loginBean = new UserBean();
        loginBean.setUsername(loginView.getUsername());
        loginBean.setPassword(loginView.getPassword());
        userBiz.login(loginBean);
    }

    @Override
    public void loginStatus(boolean status,String desc) {  //返回登录的状态
        loginView.showMsg(status,desc);
    }
}
