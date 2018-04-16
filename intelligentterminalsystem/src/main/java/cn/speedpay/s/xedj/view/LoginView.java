package cn.speedpay.s.xedj.view;

/**
 * Created by jesse on 15-6-24.
 */
public interface LoginView {
    public String getUsername();
    public String getPassword();
    public void showMsg(boolean msg, String desc);
}
