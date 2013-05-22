package com.vito.xmutems.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
/**
 * 对话框工具
 * @author Administrator
 *
 */
public class DialogUtil {
	public static void showMsg(Context c, String title, String content) {
		Builder builder = new AlertDialog.Builder(c);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public static void showMsg(Context c, String content) {
		showMsg(c, "提示", content);
	}

	public static void showMsg(Context c, String content,
			OnClickListener listener) {
		Builder builder = new AlertDialog.Builder(c);
		builder.setTitle("提示");
		builder.setMessage(content);
		builder.setNeutralButton("确定", listener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void showConfirm(Context c, String content,
			OnClickListener okListener, OnClickListener cancelListener) {
		Builder builder = new AlertDialog.Builder(c);
		builder.setTitle("提示");
		builder.setMessage(content);
		builder.setPositiveButton("确定", okListener);
		builder.setNegativeButton("取消", cancelListener);

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	static int idx = 0;

	public final static Dialog radioDialog(Context context, String title,
			final String[] items,
			final DialogInterface.OnClickListener onOKClickListener) {
		return DialogUtil.radioDialog(context, title, items, 0,
				onOKClickListener);
	}

	// 单选框调用该方法
	public final static Dialog radioDialog(Context context, String title,
			final String[] items, int selectedIndex,
			final DialogInterface.OnClickListener onOKClickListener) {
		idx = selectedIndex;
		Dialog dialog;
		Builder builder = new android.app.AlertDialog.Builder(context);
		// 设置对话框的图标
		// builder.setIcon(R.drawable.ic_launcher);
		// 设置对话框的标题
		builder.setTitle(title);
		// 0: 默认第一个单选按钮被选中
		builder.setSingleChoiceItems(items, idx,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						idx = which;
					}
				});

		// 添加一个确定按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				onOKClickListener.onClick(dialog, idx);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// 创建一个单选按钮对话框
		dialog = builder.create();
		return dialog;
	}

	/**
	 * 创建一个列表对话框
	 * 
	 * @param context
	 * @param title
	 * @param items
	 * @param itemClickListener
	 * @return
	 */
	public final static Dialog listDialog(Context context, String title,
			String[] items, OnClickListener itemClickListener) {
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setItems(items, itemClickListener);

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}

	/**
	 * 消除软键盘
	 * @param ctx
	 * @param editText
	 */
	public final static void dismissKeyboard(Context ctx,EditText editText) {
		InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

}
