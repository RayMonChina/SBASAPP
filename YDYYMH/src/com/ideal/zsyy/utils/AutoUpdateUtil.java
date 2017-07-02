package com.ideal.zsyy.utils;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.fang.sbas.R;
import com.ideal.zsyy.request.VersionValidateReq;
import com.ideal.zsyy.response.VersionValidateRes;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal2.base.gson.GsonServlet;

public class AutoUpdateUtil {
	private Context context;
	private String clientkind;
	private PreferencesService preferencesService;
	public static boolean isNewApk = false;
	public AutoUpdateUtil(Context context,String clientkind,PreferencesService preferencesService){
		this.context = context;
		this.clientkind = clientkind;
		this.preferencesService = preferencesService;
	}
	public void checkVersionSys() {
		VersionValidateReq vvreq = new VersionValidateReq();
		vvreq.setAuthenticationCode(DeviceHelper
				.ObtainDeviceID(context));
		vvreq.setClientType("ANDROID");
		vvreq.setVersionCode(DeviceHelper.getVersionCode(context)
				+ "");
//		vvreq.setClientKind(clientkind); 
		GsonServlet<VersionValidateReq, VersionValidateRes> gServlet = new GsonServlet<VersionValidateReq, VersionValidateRes>(
				context);
		gServlet.setShowDialog(false);
		gServlet.request(vvreq, VersionValidateRes.class);
		gServlet.setOnResponseEndListening(new GsonServlet.OnResponseEndListening<VersionValidateReq, VersionValidateRes>() {
			@Override
			public void onResponseEnd(VersionValidateReq commonReq,
					VersionValidateRes commonRes, boolean result,
					String errmsg, int responseCode) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponseEndSuccess(VersionValidateReq commonReq,
					VersionValidateRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {
					if (!TextUtils.isEmpty(commonRes.getFileUrl())) {
						isUpdate(commonRes.getFileUrl(),
								commonRes.getUpdateInfo());
						isNewApk = true; 
					}
				}
			}

			@Override
			public void onResponseEndErr(VersionValidateReq commonReq,
					VersionValidateRes commonRes, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void isUpdate(final String url, String updateInfo) {
		Builder dialog = new AlertDialog.Builder(context);
		dialog.setIcon(R.drawable.applogo);
		dialog.setTitle("版本更新");
		dialog.setMessage(updateInfo);
		dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Intent intent = new
				// Intent("com.ideal.zsyy.activity.AutoUpdateActivity");
				// intent.putExtra("url", url);
				// intent.putExtra("path", "/sdcard/iDownload");
				// MainActivity.this.startActivity(intent);
				downLoadApk(url);
			}
		});
		dialog.setNegativeButton(R.string.no_update,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						preferencesService.saveAutoUpdate(false); 
						dialog.dismiss();
						// login();
					}
				});
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// login();
			}
		});
		try {
			dialog.setCancelable(false);
			dialog.create().show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	String pro;
	String max;
	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk(final String url) {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File download = new File(DataUtils.download);
					if (!download.exists()) {
						download.mkdirs();
					}
					File file = DataUtils.getFileFromServer(url,download,pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
				}
			}
		}.start();
	}

	// 安装apk
	protected void installApk(File file) {
		if (file == null) {
			return;
		}
		if (file.exists()) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}
}
