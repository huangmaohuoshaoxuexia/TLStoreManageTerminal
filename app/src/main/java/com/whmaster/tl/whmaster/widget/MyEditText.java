package com.whmaster.tl.whmaster.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by admin on 2018/6/7.
 */

public class MyEditText extends EditText{
    private ArrayList<TextWatcher> mListeners = null;

    public MyEditText(Context ctx)
    {
        super(ctx);
    }

    public MyEditText(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }

    public MyEditText(Context ctx, AttributeSet attrs, int defStyle)
    {
        super(ctx, attrs, defStyle);
    }

    @Override
    public Editable getText() {
        return super.getText();
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher)
    {
        if (mListeners == null)
        {
            mListeners = new ArrayList<TextWatcher>();
        }
        mListeners.add(watcher);

        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher)
    {
        if (mListeners != null)
        {
            int i = mListeners.indexOf(watcher);
            if (i >= 0)
            {
                mListeners.remove(i);
            }
        }

        super.removeTextChangedListener(watcher);
    }

    public void clearTextChangedListeners()
    {
        if(mListeners != null)
        {
            for(TextWatcher watcher : mListeners)
            {
                super.removeTextChangedListener(watcher);
            }

            mListeners.clear();
            mListeners = null;
        }
    }
}
