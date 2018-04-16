package cn.speedpay.s.xedj.frame.http.callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 说明：返回Bean对象
 */
public abstract class ModelCallBack<T> extends BaseHttpCallBack<T>{

    public ModelCallBack(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        clazz = (Class) params[0];
    }

}
