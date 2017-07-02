package com.ideal.zsyy.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fang.sbas.R;
import com.ideal.zsyy.activity.CheckOrderActivity;
import com.ideal.zsyy.activity.CheckRangeActivity;
import com.ideal.zsyy.activity.OperationRecureActivity;
import com.ideal.zsyy.activity.OperationTipActivity;
import com.ideal.zsyy.activity.TbReportDetailActivity;
import com.ideal.zsyy.activity.YuyueTipActivity;
import com.ideal.zsyy.activity.ZhuyuanFeeActivity;


import android.R.integer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

@SuppressWarnings("deprecation")
public class SmsReceiver extends BroadcastReceiver {

	private String TAG = "AutSMS";
	String strSendNumber="10657109000038_";//"12520";
	NotificationManager nManager;
	// 广播消息类型
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Log.i(TAG, "引发接收事件");
		// StringBuilder body=new StringBuilder("");//短信内容
		// StringBuilder sender=new StringBuilder("");//发件人
		// 先判断广播消息
		String action = intent.getAction();
		if (SMS_RECEIVED_ACTION.equals(action)) {
			// 获取intent参数
			Bundle bundle = intent.getExtras();
			// 判断bundle内容
			if (bundle != null) {
				// 取pdus内容,转换为Object[]
				Object[] pdus = (Object[]) bundle.get("pdus");
				// 解析短信
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < messages.length; i++) {
					byte[] pdu = (byte[]) pdus[i];
					messages[i] = SmsMessage.createFromPdu(pdu);
				}
				// 解析完内容后分析具体参数
				for (SmsMessage msg : messages) {
					// 获取短信内容
					String content = msg.getMessageBody();
					String sender = msg.getOriginatingAddress();
					Date date = new Date(msg.getTimestampMillis());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String sendTime = sdf.format(date);
					if(isShowNotification(context, sender, content))
					{
						abortBroadcast();
					}
				}

			}
		}// if 判断广播消息结束
	}

	private boolean isShowNotification(Context ctx,String sender,String content)
	{
		String []contentItems;
		String type;
		String id;
		String tickerText="";
		String strTitle=sender;
		String strcontent="";
		String strkey="id";
		int notifyId=1;
		Map<String,String>postVals=new HashMap<String, String>();
		Class<?>cls=null;
		boolean isShow=false;
		if(!sender.startsWith(strSendNumber))
		{
			return false;
		}
		if(content.indexOf("【")>-1)
		{
			content=content.substring(0,content.indexOf("【"));
		}
		contentItems=content.split(",");
		if(contentItems!=null&&contentItems.length>=2)
		{
			type=contentItems[0];
			id=contentItems[1];
		}
		else {
			return false;
		}
		if("1".equals(type))//预约挂号提醒
		{
			tickerText="预约挂号信息提示";
			strTitle="预约挂号信息";
			strcontent="您有预约挂号信息提示，请点击查看";
			postVals.put("id",id);
			cls=YuyueTipActivity.class;
			notifyId=1;
			isShow=true;
		}
		else if("2".equals(type)) {
			tickerText="医技报告信息提示";
			strTitle="医技报告信息";
			strcontent="您有医技报告信息提示，请点击查看";
			postVals.put("bgdh", id);
			cls=TbReportDetailActivity.class;
			notifyId=2;
			isShow=true;
		}
		else if("3".equals(type))
		{
			tickerText="手术预约信息提示";
			strTitle="手术预约信息";
			strcontent="您有手术预约信息提示，请点击查看";
			postVals.put("id", id);
			cls=OperationTipActivity.class;
			notifyId=3;
			isShow=true;
		}
		else if("4".equals(type)) {
			tickerText="住院费用提示";
			strTitle="住院费用提示信息";
			strcontent="您有住院费用信息提示，请点击查看";
			notifyId=4;
			if(contentItems.length==3)
			{
				postVals.put("pat_no", contentItems[1]);
				postVals.put("indate", contentItems[2]);
			}
			cls=ZhuyuanFeeActivity.class;
			isShow=true;
		}
		else if("5".equals(type)){
			tickerText="检查排队提示";
			strTitle="检查排队提示信息";
			strcontent="您有检查排队信息提示，请点击查看";
			notifyId=5;
			if(contentItems.length==3)
			{
				postVals.put("curr_no", contentItems[1]);
				postVals.put("deptname", contentItems[2]);
			}
			cls=CheckRangeActivity.class;
			isShow=true;
		}
		else if ("6".equals(type)) {
			tickerText="检查预约提示";
			strTitle="检查预约提示信息";
			strcontent="您有检查预约信息提示，请点击查看";
			notifyId=6;
			postVals.put("id",id);
			cls=CheckOrderActivity.class;
			isShow=true;
		}
		else if ("7".equals(type)) {
			tickerText="术后康复提示";
			strTitle="术后康复提示信息";
			strcontent="您有术后康复信息提示，请点击查看";
			notifyId=7;
			postVals.put("id",id);
			cls=OperationRecureActivity.class;
			isShow=true;
		}
		if(isShow)
		{
			showNotification(ctx,postVals, strTitle, tickerText, strcontent, cls,notifyId);
			return true;
		}
		return false;
	}

	private void showNotification(Context ctx,Map<String, String>postVal,String title,String tickerText,String content,Class<?>cls,int notId)
	{
		Intent intent=intent=new Intent(ctx,cls);
		if(postVal!=null)
		{
			for(Entry<String,String>item:postVal.entrySet())
			{
				intent.putExtra(item.getKey(),item.getValue());
			}
		}
		nManager=(NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
		long when=System.currentTimeMillis();
		Notification nfNotification=new Notification(R.drawable.applogo_2, tickerText, when);
		nfNotification.defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
		Uri ringUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		nfNotification.sound=ringUri;
		long[]vibrate=new long[]{1000,1000};
		nfNotification.vibrate=vibrate;
		nfNotification.ledARGB=Color.RED;
		nfNotification.ledOffMS=0;
		nfNotification.ledOnMS=1;
		nfNotification.flags=nfNotification.flags|Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_AUTO_CANCEL;
		PendingIntent pIntent=PendingIntent.getActivity(ctx,(int)Math.random()*10, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		nfNotification.setLatestEventInfo(ctx, title, content, pIntent);
		nManager.notify(notId,nfNotification);
	}
}
