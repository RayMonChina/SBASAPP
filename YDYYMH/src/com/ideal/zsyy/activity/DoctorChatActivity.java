package com.ideal.zsyy.activity;

import java.io.InputStreamReader;
import java.util.List;

import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.OnlineDoctorAdapter;
import com.ideal.zsyy.entity.OnlineDoctor;
import com.ideal.zsyy.entity.PhUser;
import com.ideal.zsyy.request.GetOnlineDocReq;
import com.ideal.zsyy.response.GetOnlineDocRes;
import com.ideal.zsyy.utils.DataCache;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DoctorChatActivity extends Activity{

	private ListView doctor_list;
	private Context mContext;
	private List<OnlineDoctor> doctors;
	private Button btnBack; // 返回按钮
	private PhUser phUsers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_chat);
		phUsers = Config.phUsers; 
		initView();
		//fillData();
	}
	public void initView()
	{
		mContext=DoctorChatActivity.this;
		doctor_list=(ListView) findViewById(R.id.doctor_list);
		btnBack = (Button) findViewById(R.id.btn_back);
		doctor_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OnlineDoctor doctor=doctors.get(position);
				Intent intent=new Intent(DoctorChatActivity.this,DoctorChatDetailActivity.class);
				String id_Card=phUsers.getId_Card();
				
				if (phUsers == null) {
					
					popLoginDialog();
				}else{
					
					if(phUsers.getId_Card()!=null && !phUsers.getId_Card().equals("")){
						
						intent.putExtra("patient_id", id_Card);
						intent.putExtra("doctor_id", doctor.getDoctorId());
						intent.putExtra("doctor_name", doctor.getDoctorName());
						startActivity(intent);
					}else{
						popDialog();
					}
				} 
				
				
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public void fillData()
	{
		GetOnlineDocReq req=new GetOnlineDocReq();
		GsonServlet<GetOnlineDocReq, GetOnlineDocRes> gServlet=new GsonServlet<GetOnlineDocReq, GetOnlineDocRes>(mContext);
	    gServlet.request(req, GetOnlineDocRes.class);
	    gServlet.setOnResponseEndListening(new OnResponseEndListening<GetOnlineDocReq, GetOnlineDocRes>() {

			@Override
			public void onResponseEnd(GetOnlineDocReq commonReq,
					GetOnlineDocRes commonRes, boolean result, String errmsg,
					int responseCode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onResponseEndSuccess(GetOnlineDocReq commonReq,
					GetOnlineDocRes commonRes, String errmsg, int responseCode) {
				if(commonRes!=null)
				{
					doctors=commonRes.getDoctorList();
					OnlineDoctorAdapter adapter=new OnlineDoctorAdapter(mContext, doctors);
					doctor_list.setAdapter(adapter);
				}
				
			}

			@Override
			public void onResponseEndErr(GetOnlineDocReq commonReq,
					GetOnlineDocRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				
			}
		
	    
	    });
	  
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		phUsers = Config.phUsers;
		if (phUsers == null) {
			
			popLoginDialog();
		}else{
			fillData();
			if(phUsers.getId_Card()!=null && !phUsers.getId_Card().equals("")){
				
				
			}else{
				popDialog();
			}
		} 
	}
	public void popLoginDialog()
	{
		/*AlertDialog.Builder builder = new AlertDialog.Builder(DoctorChatActivity.this);
		builder.setTitle("请先登录....");
		builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
*/				Intent intent = new Intent(DoctorChatActivity.this,LoginActivity.class);
				intent.putExtra("logintype", "report");
				startActivity(intent);
				this.finish();
		/*	}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.create().show();*/
	}
	public void popDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(DoctorChatActivity.this);
		builder.setTitle("个人身份证信息为空，请先完善个人信息再咨询!");
		builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DoctorChatActivity.this,EditPersonInfoActivity.class);
				intent.putExtra("editmsg", "report");
				startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.create().show();
	}
}
