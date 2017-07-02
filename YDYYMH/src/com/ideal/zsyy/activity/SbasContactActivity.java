package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.List;

import com.fang.sbas.R;
import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.ideal.zsyy.adapter.SbasContactAdapter;
import com.ideal.zsyy.entity.SBASContactInfo;
import com.ideal.zsyy.request.SbasContactReq;
import com.ideal.zsyy.response.SbasContactRes;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SbasContactActivity extends Activity {

	private EditText et_search;
	private ListView lv_persons;
	private Button btn_back;
	private PreferencesService preferencesService;
	private SbasContactAdapter sbAdapter=null;
	private List<SBASContactInfo>contactInfos=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sbas_contact);
		initView();
		initData();
		initEvent();
	}
	
	private void initEvent(){
		et_search.addTextChangedListener(textWatcher);
		btn_back.setOnClickListener(clickListener);
	}
	
	private void initView(){
		et_search=(EditText)findViewById(R.id.et_query_contact);
		lv_persons=(ListView)findViewById(R.id.lv_contacts);
		btn_back=(Button)findViewById(R.id.btn_back);
	}
	
	private OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;

			default:
				break;
			}
		}
	};
	
	private TextWatcher textWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			sbAdapter.getFilter().filter(et_search.getText().toString());  
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void initData(){
		preferencesService=new PreferencesService(getApplicationContext());
		contactInfos=new ArrayList<SBASContactInfo>();
		sbAdapter=new SbasContactAdapter(contactInfos, SbasContactActivity.this, handler);
		lv_persons.setAdapter(sbAdapter);
		queryData();
	}
	
	private void queryData() {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		SbasContactReq req = new SbasContactReq();
		req.setOperType("2");
		req.setUser_ID(preferencesService.getLoginInfo().get("use_id").toString());

		GsonServlet<SbasContactReq, SbasContactRes> gServlet = new GsonServlet<SbasContactReq, SbasContactRes>(
				this);
		gServlet.request(req, SbasContactRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<SbasContactReq, SbasContactRes>() {

			@Override
			public void onResponseEnd(SbasContactReq commonReq, SbasContactRes commonRes,
					boolean result, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(SbasContactReq commonReq,
					SbasContactRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				contactInfos.clear();
				if (commonRes != null) {
					//Config.phUsers = commonRes.getPhUsers();
					if(commonRes.getContactList()!=null){
						contactInfos.addAll(commonRes.getContactList());
					}
					handler.sendEmptyMessage(1);
				}

			}

			@Override
			public void onResponseEndErr(SbasContactReq commonReq, SbasContactRes commonRes,
					String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				sbAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
		
}
