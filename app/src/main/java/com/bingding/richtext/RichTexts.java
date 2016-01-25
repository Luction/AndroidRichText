package com.bingding.richtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class RichTexts {

    private static final String TAG = "RichText";

    public static void setImageSpan(Spannable s, TaggedInfo info, Context context, int resourceId) {
        removeSpans(s, info.start, info.end, ImageSpan.class);
        safelySetSpan(s, new ImageSpan(context, resourceId, DynamicDrawableSpan.ALIGN_BASELINE), info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setImageSpan(Spannable s, TaggedInfo info, DynamicDrawableSpan span) {
        removeSpans(s, info, ImageSpan.class);
        safelySetSpan(s, span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setImageSpan(Spannable s, int start, int end, Context context, int resourceId) {
        removeSpans(s, start, end, ImageSpan.class);
        safelySetSpan(s, new ImageSpan(context, resourceId, DynamicDrawableSpan.ALIGN_BASELINE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setRichTextClickSpan(Spannable s, TaggedInfo info, RichTextClickSpan span) {
        removeSpans(s, info, RichTextClickSpan.class);
        safelySetSpan(s, span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void safelySetSpan(Spannable s, Object what, int start, int end, int flags) {
        if ((start >= 0) && (end >= start) && (end <= s.length())) {
            s.setSpan(what, start, end, flags);
        } else {
            Log.e(TAG, "fail set spain,start:" + start + ",end:" + end + ",but lng is:" + s.length());
        }
    }

    public static <T> void removeSpans(Spannable s, TaggedInfo info, Class<T> clz) {
        removeSpans(s, info.start, info.end, clz);
    }

    public static <T> void removeSpans(Spannable s, int start, int end, Class<T> clz) {
        T[] spans = s.getSpans(start, end, clz);
        if (spans != null) {
            for (T span : spans) {
                try {
                    s.removeSpan(span);
                } catch (NullPointerException e) {
                    Log.e(TAG, "remove spans error", e);
                }
            }
        }
    }

    public static class StickerSpan extends DynamicDrawableSpan {
        private Drawable mDrawable;

        public StickerSpan(Context context, Bitmap bitmap,int maxWidth,int maxHeight) {
            super(DynamicDrawableSpan.ALIGN_BASELINE);
            mDrawable = new BitmapDrawable(context.getResources(), bitmap);
            int width = mDrawable.getIntrinsicWidth();
            int height = mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(0, 0, width > maxWidth ? maxWidth : width, height > maxHeight ? maxHeight : height);
        }

        public StickerSpan(Context context, int drawable,int maxWidth,int maxHeight) {
            super(DynamicDrawableSpan.ALIGN_BASELINE);
            mDrawable = context.getResources().getDrawable(drawable);
            int width = mDrawable.getIntrinsicWidth();
            int height = mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(0, 0, width>maxWidth?maxWidth:width,height>maxHeight?maxHeight:height);
        }

        @Override
        public Drawable getDrawable() {
            return mDrawable;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = mDrawable;
            canvas.save();

            canvas.translate(x, 0);
            b.draw(canvas);
            canvas.restore();
        }
    }


    public static class RichTextClickSpan extends ClickableSpan {
        private RichTextClickListener listener;
        private String content;


        public RichTextClickSpan(RichTextClickListener listener,String content) {
            this.listener = listener;
            this.content = content;
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onRichTextClick((TextView) widget,content);
            }
        }
    }

/**
 *    callback to be invoked when rich text is clicked
 */
    public interface RichTextClickListener {
        void onRichTextClick(TextView v, String content);
    }

    public static class TaggedInfo {
        public int start;
        public int end;
        public String content;

        public TaggedInfo(int b, int e, String text) {
            start = b;
            end = e;
            content = text;
        }
    }

}
