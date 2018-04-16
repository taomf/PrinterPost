package cn.speedpay.s.xedj.listener;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.speedpay.s.xedj.R;

/**
 * Created by taomf on 2016-8-16.
 * Description: 输入框内容监听
 */
public class EditTextWatcher implements TextWatcher {
    private RelativeLayout layout;
    private EditText account;
    private EditText password;
    private TextView textView;

    public EditTextWatcher(RelativeLayout rl,EditText acc,EditText pass,TextView tv){
        layout = rl;
        account = acc;
        password = pass;
        textView = tv;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void afterTextChanged(Editable s) {
        String acco = account.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(!TextUtils.isEmpty(acco) && !TextUtils.isEmpty(pass)){ //可以登录
            layout.setBackground(layout.getResources().getDrawable(R.drawable.rounded_range_ac_main));
            textView.setTextColor(Color.rgb(255,255,255));
            layout.setClickable(true);

        }else{ //不能登录
            layout.setBackground(layout.getResources().getDrawable(R.drawable.rounded_range_ac_login));
            textView.setTextColor(Color.rgb(177, 177, 177));
            layout.setClickable(false);
        }

    }
}
