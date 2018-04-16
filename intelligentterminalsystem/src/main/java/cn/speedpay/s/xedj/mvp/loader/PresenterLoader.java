package cn.speedpay.s.xedj.mvp.loader;

import android.content.Context;
import android.support.v4.content.Loader;

import cn.speedpay.s.xedj.mvp.presenter.MvpPresenter;

/**
 * 说明：PresenterLoader
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/5/30 14:10
 * <p/>
 * 版本：verson 1.0*/


public class PresenterLoader<T extends MvpPresenter> extends Loader {

    private final PresenterFactory<T> factory;
    private T presenter;

/**
 * Stores away the application context associated with context.
 * Since Loaders can be used across multiple activities it's dangerous to
 * store the context directly; always use {@link #getContext()} to retrieve
 * the Loader's Context, don't use the constructor argument directly.
 * The Context returned by {@link #getContext} is safe to use across
 * Activity instances.
 *
 * @param context used to retrieve the application context.
 * */


    public PresenterLoader(Context context,PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }


    @Override
    protected void onStartLoading() {
        if (presenter != null){
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.detachView();
        presenter = null;
    }
}
