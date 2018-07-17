package com.whmaster.tl.whmaster.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.presenter.LibraryPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.utils.ScreenUtils;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;


public class LibraryPopup extends PopupWindow{
    private View mView;
    private Context mContext;
    private LinearLayout mContentLayout;
    private String mPositionCode = "",mPositionPointCode="";
    private LibraryPresenter libraryPresenter;
    private ArrayList<ArrayMap<String,Object>> mList;
    public LibraryPopup(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.library_popup, null);
        mContentLayout = mView.findViewById(R.id.content_id);

        LayoutParams params = mContentLayout.getLayoutParams();
        params.height = ScreenUtils.getScreenHeight(mContext)/3;
        params.width = (int) (ScreenUtils.getScreenWidth(mContext)*0.8);
        mContentLayout.setLayoutParams(params);
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);

        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw2 = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw2);
		ColorDrawable dw = new ColorDrawable(0x40000000);
//		//设置SelectPicPopupWindow弹出窗体的背景
        mView.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = mView.findViewById(R.id.content_id).getTop();
                int bottom = mView.findViewById(R.id.content_id).getBottom();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height || y>bottom){
						dismiss();
                    }
                }
                return true;
            }
        });
    }
    public void setData(String positionCode,String positionPointCode){
       mPositionCode = positionCode;
        mPositionPointCode = positionPointCode;
        libraryPresenter.getMovePosition("474",mPositionPointCode);
    }
}
