package com.bingding.richtext.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.bingding.richtext.R;
import com.bingding.richtext.Resolver;
import com.bingding.richtext.RichTexts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuerufeng on 16/1/25.
 */
public class ImageResolver implements Resolver {

    public static final String IMG_MATCH_REGULAR = "\\[img](\\w+)\\[/img]";
    public static Pattern PATTERN = Pattern.compile(IMG_MATCH_REGULAR);


    @Override
    public void resolve(final TextView textView, final Spannable sp, SparseArray<Object> extra, final RichTexts.RichTextClickListener listener) {
        Matcher matcher = PATTERN.matcher(sp);
        final Context context = textView.getContext();


        while(matcher.find()){
            String content = matcher.group(1);
            final RichTexts.TaggedInfo info = new RichTexts.TaggedInfo(matcher.start(),matcher.end(),content);
            if(listener!=null) {
                RichTexts.RichTextClickSpan span = new RichTexts.RichTextClickSpan(listener,info.content);
                sp.setSpan(span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            RichTexts.StickerSpan testSpan = new RichTexts.StickerSpan(context, R.drawable.icon,100,100);
            RichTexts.setImageSpan(sp, info, testSpan);
            textView.postInvalidate();
        }
    }

}
