package com.redeyesncode.crmfinancegs.ui.loanadmin;


import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class PANEditText extends androidx.appcompat.widget.AppCompatEditText {
    public PANEditText(Context context) {
        super(context);
    }

    public PANEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PANEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
    }



    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        InputFilter[] filters = new InputFilter[] { new InputFilter.LengthFilter(10),new InputFilter.AllCaps() };

        setFilters(filters);
        if(text.toString()!=null){
            if(text.length()==0 || text.length() <5){
                setInputType(InputType.TYPE_CLASS_TEXT);
            }else if(text.toString().length()<=8){
                setInputType(InputType.TYPE_CLASS_NUMBER);
            }else if(text.toString().length()>=9){
                setInputType(InputType.TYPE_CLASS_TEXT);

            }
        }


    }

}
