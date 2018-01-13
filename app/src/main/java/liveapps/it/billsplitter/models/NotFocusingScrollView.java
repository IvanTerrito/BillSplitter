package liveapps.it.billsplitter.models;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by ivanterrito on 01/05/17.
 */

public class NotFocusingScrollView extends ScrollView {

    public NotFocusingScrollView(Context context) {
        super(context);
    }

    public NotFocusingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotFocusingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

}