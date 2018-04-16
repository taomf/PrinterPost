package cn.speedpay.s.xedj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/8/22.
 *  自定义GridView控件，解决在ListView 或ScrollView中使用GridView导致GridView显示不全的问题
 *
 */
public class GridViewForScrollView extends GridView{
    public GridViewForScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GridViewForScrollView(Context context) {
        this(context,null);
    }

    public GridViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
