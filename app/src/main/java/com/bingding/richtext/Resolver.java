package com.bingding.richtext;

import android.support.v4.util.ArrayMap;
import android.text.Spannable;
import android.util.SparseArray;
import android.widget.TextView;

/**
 * Interface definition for a resolver that resolve provided data to your rich text.
 *
 * @author Bingding.
 *
 */
public interface Resolver {

    /**
     * Resolve your rich text here.
     *
     * @param textView the textView display rich text;
     * @param sp the content of TextView;
     * @param extra extra data if exist；
     * @param listener Callback if need;
     */
    void resolve(TextView textView, Spannable sp, SparseArray<Object> extra, RichTexts.RichTextClickListener listener);
}
