package com.whmaster.tl.whmaster.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.utils.ScreenUtils;


public class MsgLoadingDialog {
	private Context mContext;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
	private Dialog mDialog;
	private RelativeLayout mContentLayout;
	private TextView mMsgText;
	public MsgLoadingDialog(Context context) {
		this.mContext = context;
	}

	public MsgLoadingDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.msg_load_layout, null);
		mContentLayout = view.findViewById(R.id.log_load_content_layout);
		ViewGroup.LayoutParams params = mContentLayout.getLayoutParams();
		params.height = ScreenUtils.getScreenWidth(mContext)/3;
		params.width = ScreenUtils.getScreenWidth(mContext)/3;
		mContentLayout.setLayoutParams(params);
		mMsgText = view.findViewById(R.id.login_ing);
		mDialog = new Dialog(mContext, R.style.AlertDialogStyle);

		mDialog.setContentView(view);
		return this;
	}


	public MsgLoadingDialog setMsg(String msg) {
		showMsg = true;
		mMsgText.setText(msg);
		return this;
	}
	public void dismiss() {
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
	}
	public void show() {
		mDialog.show();
	}
}
