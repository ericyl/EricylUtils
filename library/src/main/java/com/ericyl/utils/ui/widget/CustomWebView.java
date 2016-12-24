package com.ericyl.utils.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Method;

@Deprecated
public class CustomWebView extends WebView {

	public CustomWebView(Context context) {
		super(context);
	}

	/**
	 * Disable the controls
	 */
	@SuppressLint({"NewApi", "SetJavaScriptEnabled"})
	@SuppressWarnings("deprecation")
	private void disableControls() {
		WebSettings settings = this.getSettings();
		// 基本的设置
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);// support zoom
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		if (Build.VERSION.SDK_INT >= 8) {
			settings.setPluginState(WebSettings.PluginState.ON);
		}
//		else {
//			 settings.setPluginsEnabled(true);
//		}
		this.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		// 去掉滚动条
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);

		// 去掉缩放按钮
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Use the API 11+ calls to disable the controls
			this.getSettings().setBuiltInZoomControls(true);
			this.getSettings().setDisplayZoomControls(false);
		} else {
			// Use the reflection magic to make it work on earlier APIs
			getController();
		}
	}

	/**
	 * This is where the magic happens :D
	 */
	private void getController() {
		try {
//			Class cls = Class.forName("android.webkit.WebView");
			Method method = WebView.class.getMethod("getZoomButtonsController");
			ZoomButtonsController zoomButtonsController = (ZoomButtonsController) method
					.invoke(this, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
