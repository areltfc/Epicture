package eu.delattreepitech.arthur.dev_epicture_2018.TextViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MontserratTextView extends android.support.v7.widget.AppCompatTextView {
    public MontserratTextView(Context context) {
        super(context);
        work();
    }

    public MontserratTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        work();
    }
    public MontserratTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        work();
    }

    private void work() {
        setFont();
        setTextIsSelectable(true);
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Montserrat-Regular.ttf");
        setTypeface(font);
    }
}
