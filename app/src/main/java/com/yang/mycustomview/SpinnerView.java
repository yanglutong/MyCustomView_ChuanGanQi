package com.yang.mycustomview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class SpinnerView extends RelativeLayout {

    private ImageView mImage;
    private EditText mEdit;
    private PopupWindow popupWindow;
    private ArrayList<String> datas;

    public SpinnerView(Context context) {
        super(context);
    }

    //最少两个参数构造
    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate
                (R.layout.spinner, null);
        addView(view);

        initView(view);
        initData();
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("列表" + i);
        }
    }

    private void initView(View view) {
        mImage = view.findViewById(R.id.image);
        mEdit = view.findViewById(R.id.et);

        mImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void pop() {
        if (popupWindow == null) {
            //点击图片弹出pop
            ListView listView = new ListView(getContext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, datas);
            listView.setAdapter(adapter);


            popupWindow = new PopupWindow(listView, mEdit.getWidth(), 500);
            //兼容低版本
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部消失
            popupWindow.setOutsideTouchable(true);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mEdit.setText(datas.get(position));
                    popupWindow.dismiss();
                    mEdit.setSelection(datas.get(position).length());
                }
            });
        }
        //在某个控件下方显示
        popupWindow.showAsDropDown(mEdit);
    }
}
