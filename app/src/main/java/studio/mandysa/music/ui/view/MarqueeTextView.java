package studio.mandysa.music.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * 跑马灯效果的TextView, 使用方式：
 * xml文件中记得设置：android:focusable="true", android:singleLine="true"
 * <p>
 * Created by dasu on 2017/3/21.
 * http://www.jianshu.com/u/bb52a2918096
 */
public final class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine(true);
        setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}

