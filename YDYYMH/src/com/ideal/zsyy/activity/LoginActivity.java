package com.ideal.zsyy.activity;

import com.ideal.wdm.tools.DataCache;
import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.request.UserReq;
import com.ideal.zsyy.response.UserRes;
import com.ideal.zsyy.service.PreferencesService;
import com.ideal.zsyy.utils.StringHelper;
import com.ideal2.base.gson.GsonServlet;
import com.ideal2.base.gson.GsonServlet.OnResponseEndListening;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText login_user_name;
	private EditText login_pwd;
	private PreferencesService preferencesService;
	private String logintype;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		preferencesService = new PreferencesService(getApplicationContext());

		intent = getIntent();
		logintype = intent.getStringExtra("logintype");
		initView();

	}

	private void initView() {

		login_user_name = (EditText) findViewById(R.id.login_user_name);
		login_pwd = (EditText) findViewById(R.id.login_pwd);

		login_user_name.setText(preferencesService.getLoginInfo()
				.get("loginName").toString());
		login_pwd.setText(preferencesService.getLoginInfo().get("pwd")
				.toString());

		Button pc_bt_submit = (Button) findViewById(R.id.pc_bt_submit);
		pc_bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String uname = login_user_name.getText().toString();
				String pwd = login_pwd.getText().toString();
				if (uname != null && !uname.equals("") && !pwd.equals("")
						&& pwd != null) {
					queryData(uname, pwd);
				} else if (uname == null || uname.equals("") || pwd.equals("")
						|| pwd == null) {
					Toast.makeText(LoginActivity.this, "用户名或密码不能为空", 1).show();
					login_pwd.setText(null);
				}
			}
		});

		TextView pc_bt_register = (TextView) findViewById(R.id.pc_bt_register);
		pc_bt_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						RegsiterProvisionActivity.class);
				startActivity(intent);
				finish();

			}
		});

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void queryData(final String user_name, final String pwd) {

		DataCache datacache = DataCache.getCache(this);
		datacache.setUrl(Config.url);

		UserReq req = new UserReq();
		req.setOperType("1");
		req.setUserAccount(user_name);
		req.setPassword(StringHelper.MD5EnCode(pwd));

		GsonServlet<UserReq, UserRes> gServlet = new GsonServlet<UserReq, UserRes>(
				this);
		gServlet.request(req, UserRes.class);
		gServlet.setOnResponseEndListening(new OnResponseEndListening<UserReq, UserRes>() {

			@Override
			public void onResponseEnd(UserReq commonReq, UserRes commonRes,
					boolean result, String errmsg, int responseCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseEndSuccess(UserReq commonReq,
					UserRes commonRes, String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				if (commonRes != null) {

					//Config.phUsers = commonRes.getPhUsers();

					preferencesService.saveLoginInfo(user_name, pwd, true,commonRes.getUserId(),commonRes.getUser_Code());
					if ("deptinfo".equals(logintype)) {
						String dept_id = intent.getStringExtra("dept_id");
						if (dept_id != null && !"".equals(dept_id)) {
							Intent intent = new Intent(LoginActivity.this,
									OrderDeptInfoDuty1Activity.class);
							intent.putExtra("dept_id", dept_id);
							startActivity(intent);
							finish();
						}
					} else if ("orderregister".equals(logintype)) {
						finish();
					} else if ("docinfo".equals(logintype)) {
						finish();
					} else if ("report".equals(logintype)) {
						finish();
					} else if ("yuyueTip".equals(logintype)) {
						finish();
					} else if ("operationTip".equals(logintype)) {
						finish();
					} else if ("reportDetail".equals(logintype)) {
						finish();
					} else if ("checkorder".equals(logintype)) {
						finish();
					} else if ("oprecure".equals(logintype)) {
						finish();

					} else if ("zhuyuanfee".equals(logintype)) {
						finish();

					}
					else if ("huifang".equals(logintype)) {
						finish();
					}
					else {
						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(intent);
						finish();
					}
				}

			}

			@Override
			public void onResponseEndErr(UserReq commonReq, UserRes commonRes,
					String errmsg, int responseCode) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), errmsg,
						Toast.LENGTH_SHORT).show();
			}

		});
	}

}
