package com.bingding.richtext.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bingding.richtext.R;
import com.bingding.richtext.RichTextWrapper;
import com.bingding.richtext.RichTexts;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        RichTextWrapper richTextWrapper = new RichTextWrapper((TextView) findViewById(R.id.tv_main));
        richTextWrapper.addResolver(ImageResolver.class);
        richTextWrapper.setOnRichTextListener(ImageResolver.class, new RichTexts.RichTextClickListener() {
            @Override
            public void onRichTextClick(TextView v, String content) {
                Toast.makeText(v.getContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
        richTextWrapper.setText("hello [img]img1[/img]world [img]img2[/img]..");
    }

}
