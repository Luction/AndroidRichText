package com.bingding.richtext;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Wrap the TextView what need supported rich text.
 *
 * @author Bingding.
 */
public class RichTextWrapper {
    private SparseArray<Object> mExtra = new SparseArray<Object>();
    private HashMap<Class<? extends Resolver>, Resolver> mResolvers = new HashMap<Class<? extends Resolver>, Resolver>();
    private ArrayMap<String, RichTexts.RichTextClickListener> mListenerMap = new ArrayMap<String, RichTexts.RichTextClickListener>();
    private TextView mTextView;

    public RichTextWrapper(TextView textView) {
        if (textView == null) {
            return;
        }
        mTextView = textView;
        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                return true;
            }
        });
        textView.setMovementMethod(RTMovementMethod.getInstance());
        mTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resolveText();
            }
        });
    }


    public void putExtra(int key, Object value) {
        mExtra.put(key, value);
    }

    public void addResolver(Class<? extends Resolver>... clazzs) {
        for(Class<? extends Resolver> clazz:clazzs){
            if(!mResolvers.containsKey(clazz)) {
                mResolvers.put(clazz, null);
            }
        }
    }

    public void setOnRichTextListener(Class<? extends Resolver> clazz, RichTexts.RichTextClickListener listener) {
        if(!mResolvers.containsKey(clazz)) {
            mResolvers.put(clazz, null);
        }
        mListenerMap.put(clazz.getSimpleName(), listener);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public Context getContext() {
        return mTextView.getContext();
    }

    private void resolveText() {
        if (!(mTextView.getText() instanceof Spannable)) {
            mTextView.setText(new SpannableString(mTextView.getText()));
        }
        Spannable sp = (Spannable) mTextView.getText();
        for (Class<? extends Resolver> clazz : mResolvers.keySet()) {
            Resolver resolver = mResolvers.get(clazz);
            if (resolver == null) {
                try {
                    resolver = clazz.newInstance();
                    mResolvers.put(clazz, resolver);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            RichTexts.RichTextClickListener listener = mListenerMap.get(resolver.getClass().getSimpleName());
            resolver.resolve(mTextView, sp, mExtra, listener);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mTextView.setOnClickListener(onClickListener);
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        mTextView.setOnLongClickListener(onLongClickListener);
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
        resolveText();
    }
}
