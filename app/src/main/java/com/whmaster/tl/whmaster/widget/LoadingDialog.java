package com.whmaster.tl.whmaster.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.whmaster.tl.whmaster.R;


public class LoadingDialog {
	private Context context;
	private Dialog dialog;
	private RelativeLayout lLayout_bg;
	private Display display;

	public LoadingDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public LoadingDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.loading_layout, null);

		lLayout_bg = view.findViewById(R.id.bg_layout);
		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.loadStyle);
		dialog.setContentView(view);
		return this;
	}

	public void show() {
		dialog.show();
	}
	public void dismiss() {
		if(dialog!=null){
			dialog.dismiss();
		}
	}
}
