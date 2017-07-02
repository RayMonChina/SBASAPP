package com.ideal.zsyy.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;


import com.ideal.zsyy.Config;
import com.fang.sbas.R;
import com.ideal.zsyy.adapter.IndicationCentertList2;
import com.ideal.zsyy.adapter.MyViewPagerAdapter;
import com.ideal.zsyy.entity.PhJkxjInfo;
import com.ideal.zsyy.entity.PhJkxjPicInfo;
import com.ideal.zsyy.utils.BitmapUtil;
import com.ideal.zsyy.video.SoundView;
import com.ideal.zsyy.video.VideoView;

public class HealthInfoDetailActivity extends Activity {

	private String video_url = "";

	// private String video_url = "http://10.4.251.232:8435/UPM/upload/11.flv";

	private final static String TAG = "VideoPlayerActivity";
	private boolean isOnline = false;
	private boolean isChangedVideo = false;

	public static LinkedList<MovieInfo> playList = new LinkedList<MovieInfo>();

	public class MovieInfo {
		String displayName;
		String path;
	}

	private Uri videoListUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	private static int position;
	// private static boolean backFromAD = false;
	private int playedTime;

	// private AdView adView;

	private VideoView vv = null;
	private SeekBar seekBar = null;
	private TextView durationTextView = null;
	private TextView playedTextView = null;
	private GestureDetector mGestureDetector = null;
	private AudioManager mAudioManager = null;

	private int maxVolume = 0;
	private int currentVolume = 0;

	private ImageButton bn1 = null;
	private ImageButton bn2 = null;
	private ImageView bn3 = null;
	private ImageButton bn4 = null;
	private ImageButton bn5 = null;

	private View controlView = null;
	private PopupWindow controler = null;

	private SoundView mSoundView = null;
	private PopupWindow mSoundWindow = null;

	// private View extralView = null;
	// private PopupWindow extralWindow = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlHeight = 0;

	private final static int TIME = 6868;

	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = false;
	private boolean isSilent = false;
	private boolean isSoundShow = false;

	private PhJkxjInfo phJkxjInfo;

	private ViewPager main_viewpager_picture;
	private MyViewPagerAdapter myPicViewPagerAdapter;
	private IndicationCentertList2 buttonList;
	private LinearLayout.LayoutParams layoutParams;
	private List<View> picViews;

	private Bitmap bitmap;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Object[] object;
			ImageView iv_icon;

			switch (msg.what) {

			case 0:
				object = (Object[]) msg.obj;
				bitmap = (Bitmap) object[0];
				iv_icon = (ImageView) object[1];
				iv_icon.setImageBitmap(bitmap);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthinfo_detail);

		phJkxjInfo = HealthInformationActivity.phJkxjInfo;

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		initView();
//		initVideo();

	}

	private void initView() {

		vv = (VideoView) findViewById(R.id.vv);

		final LinearLayout ll_video_play = (LinearLayout) findViewById(R.id.ll_video_play);
		ImageView video_play = (ImageView) findViewById(R.id.video_play);
		/*video_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ll_video_play.setVisibility(View.GONE);
				if (video_url != null) {
					vv.setVideoPath(video_url);
					isOnline = true;
					isChangedVideo = true;
				}

			}
		});*/

		String title = phJkxjInfo.getTitle();
		TextView ind_title = (TextView) findViewById(R.id.ind_title);
		ind_title.setText(title);

		String time = "";
		TextView ind_time = (TextView) findViewById(R.id.ind_time);
		ind_time.setText(time);

		int image_id = getIntent().getIntExtra("image_id", 0);
		ImageView ind_img = (ImageView) findViewById(R.id.ind_img);
		ind_img.setImageResource(image_id);

		String information_detail_introduction = phJkxjInfo.getMemo();
		TextView ind_text = (TextView) findViewById(R.id.ind_detail_introduction);
		ind_text.setText("        " + information_detail_introduction);
		// ind_text.setText("        "
		// + Html.fromHtml(information_detail_introduction));

		// String type = getIntent().getStringExtra("type").trim();
		//
		// if (type.equals("1")) {
		//
		// } else if (type.equals("2")) {
		//
		// }

		main_viewpager_picture = (ViewPager) findViewById(R.id.main_viewpager_picture);
		main_viewpager_picture
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub

						buttonList.setIndex(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

		picViews = new ArrayList<View>();

		List<PhJkxjPicInfo> jkxjpicinfo = phJkxjInfo.getJkxjpicinfo();

		for (int i = 0; i < jkxjpicinfo.size(); i++) {

			PhJkxjPicInfo phJkxjPicInfo = jkxjpicinfo.get(i);

			if (phJkxjPicInfo.getType().equals("1")) {

				ImageView imageView = new ImageView(getApplicationContext());
				imageView.setImageResource(R.drawable.hi_list_pic_dauflt);

				BitmapUtil.downloadBitmap(imageView, Config.down_url
						+ phJkxjPicInfo.getName(), handler, 0);

				imageView.setLayoutParams(layoutParams);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				picViews.add(imageView);

			} else if (phJkxjPicInfo.getType().equals("2")) {

				LinearLayout linearLayout = new LinearLayout(
						getApplicationContext());
				linearLayout.setLayoutParams(layoutParams);
				linearLayout.setGravity(Gravity.CENTER);
				// linearLayout.setBackgroundResource(R.drawable.video_bg);
				linearLayout.setBackgroundColor(Color.BLACK);

				ImageView imageView = new ImageView(getApplicationContext());
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				imageView.setLayoutParams(layoutParams);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setImageResource(R.drawable.video_play);
				imageView.setId(i);
				imageView.setTag(Config.down_url + phJkxjPicInfo.getName());
				imageView.setOnClickListener(null);

				linearLayout.addView(imageView);
				picViews.add(linearLayout);

			}
		}

		buttonList = (IndicationCentertList2) findViewById(R.id.index_indication);

		if (picViews.size() > 1) {

			buttonList.setCount(picViews.size());
			buttonList.setIndex(0);
		}

		myPicViewPagerAdapter = new MyViewPagerAdapter(picViews);
		main_viewpager_picture.setAdapter(myPicViewPagerAdapter);

		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
/*
	private OnClickListener play_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// ImageView imageView = (ImageView) v.findViewById(v.getId());
			// ToastUtil.show(v.getContext(), "" + v.getId());

			Intent intent = new Intent(v.getContext(),
					VideoPlayerActivity.class);
//			 intent.putExtra(
//			 "url",
//			 "http://f.youku.com/player/getFlvPath/sid/138207848314118_01/st/flv/fileid/0300010100525FD3E81FE911E22691C17E757D-40D5-E4FE-4918-8D2A8F3DD659?K=f69f1f1d15436810261d4a41&hd=2&ts=85");
			intent.putExtra("url", (String) v.getTag());
			// ToastUtil.show(v.getContext(), (String)v.getTag());
			startActivity(intent);
		}
	};

	private void initVideo() {

		// Log.d("OnCreate", getIntent().toString());

		position = -1;

		Looper.myQueue().addIdleHandler(new IdleHandler() {

			@Override
			public boolean queueIdle() {

				// TODO Auto-generated method stub
				if (controler != null && vv.isShown()) {

					controler.showAtLocation(vv, Gravity.TOP, 0, 0);

					// controler.update(0, 0, screenWidth, controlHeight);
				}

				// myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				return false;
			}
		});

		controlView = getLayoutInflater().inflate(R.layout.controler, null);
		controler = new PopupWindow(controlView);

		durationTextView = (TextView) controlView.findViewById(R.id.duration);
		playedTextView = (TextView) controlView.findViewById(R.id.has_played);

		mSoundView = new SoundView(this);
		mSoundView.setOnVolumeChangeListener(new OnVolumeChangedListener() {

			@Override
			public void setYourVolume(int index) {

				cancelDelayHide();
				updateVolume(index);
				hideControllerDelay();
			}
		});

		mSoundWindow = new PopupWindow(mSoundView);

		bn1 = (ImageButton) controlView.findViewById(R.id.button1);
		bn2 = (ImageButton) controlView.findViewById(R.id.button2);
		bn3 = (ImageView) controlView.findViewById(R.id.button3);
		bn4 = (ImageButton) controlView.findViewById(R.id.button4);
		bn5 = (ImageButton) controlView.findViewById(R.id.button5);

		vv.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {

				vv.stopPlayback();
				isOnline = false;

				new AlertDialog.Builder(HealthInfoDetailActivity.this)
						.setTitle("对不起")
						.setMessage("您所播的视频格式不正确，播放已停止。")
						.setPositiveButton("知道了",
								new AlertDialog.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										vv.stopPlayback();

									}

								}).setCancelable(false).show();

				return false;
			}

		});

		Uri uri = getIntent().getData();
		if (uri != null) {

			vv.stopPlayback();
			vv.setVideoURI(uri);
			isOnline = true;

			bn3.setImageResource(R.drawable.video_pause);
		} else {
			bn3.setImageResource(R.drawable.video_play);
		}

		getVideoFile(playList, new File("/sdcard/"));

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {

			Cursor cursor = getContentResolver()
					.query(videoListUri,
							new String[] { "_display_name", "_data" }, null,
							null, null);
			int n = cursor.getCount();
			cursor.moveToFirst();
			LinkedList<MovieInfo> playList2 = new LinkedList<MovieInfo>();
			for (int i = 0; i != n; ++i) {
				MovieInfo mInfo = new MovieInfo();
				mInfo.displayName = cursor.getString(cursor
						.getColumnIndex("_display_name"));
				mInfo.path = cursor.getString(cursor.getColumnIndex("_data"));
				playList2.add(mInfo);
				cursor.moveToNext();
			}

			if (playList2.size() > playList.size()) {
				playList = playList2;
			}
		}

		vv.setMySizeChangeLinstener(new MySizeChangeLinstener() {

			@Override
			public void doMyThings() {
				// TODO Auto-generated method stub
				setVideoScale(SCREEN_DEFAULT);
			}

		});

		bn1.setAlpha(0xBB);
		bn2.setAlpha(0xBB);
		// bn3.setAlpha(0xBB);
		bn4.setAlpha(0xBB);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		bn5.setAlpha(findAlphaFromSound());

		bn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// ////////////////////////////

				// Intent intent = new Intent();
				// intent.setClass(HealthInfoDetailActivity.this,
				// VideoChooseActivity.class);
				// HealthInfoDetailActivity.this.startActivityForResult(intent,
				// 0);
				// cancelDelayHide();
			}

		});

		bn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int n = playList.size();
				isOnline = false;
				if (++position < n) {
					vv.setVideoPath(playList.get(position).path);
					cancelDelayHide();
					hideControllerDelay();
				} else {
					HealthInfoDetailActivity.this.finish();
				}
			}

		});

		bn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelDelayHide();
				if (isPaused) {
					vv.start();
					bn3.setImageResource(R.drawable.video_pause);
					hideControllerDelay();
				} else {
					vv.pause();
					bn3.setImageResource(R.drawable.video_play);
				}
				isPaused = !isPaused;

			}

		});

		bn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isOnline = false;
				if (--position >= 0) {
					vv.setVideoPath(playList.get(position).path);
					cancelDelayHide();
					hideControllerDelay();
				} else {
					HealthInfoDetailActivity.this.finish();
				}
			}

		});

		bn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelDelayHide();
				if (isSoundShow) {
					mSoundWindow.dismiss();
				} else {
					if (mSoundWindow.isShowing()) {
						mSoundWindow.update(15, 0, SoundView.MY_WIDTH,
								SoundView.MY_HEIGHT);
					} else {
						mSoundWindow.showAtLocation(vv, Gravity.RIGHT
								| Gravity.CENTER_VERTICAL, 15, 0);
						mSoundWindow.update(15, 0, SoundView.MY_WIDTH,
								SoundView.MY_HEIGHT);
					}
				}
				isSoundShow = !isSoundShow;
				hideControllerDelay();
			}
		});

		bn5.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				if (isSilent) {
					bn5.setImageResource(R.drawable.soundenable);
				} else {
					bn5.setImageResource(R.drawable.sounddisable);
				}
				isSilent = !isSilent;
				updateVolume(currentVolume);
				cancelDelayHide();
				hideControllerDelay();
				return true;
			}

		});

		seekBar = (SeekBar) controlView.findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				if (fromUser) {

					if (!isOnline) {
						vv.seekTo(progress);
					}

				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				myHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
		});

		getScreenSize();

		mGestureDetector = new GestureDetector(getApplicationContext(),
				new SimpleOnGestureListener() {

					@Override
					public boolean onDoubleTap(MotionEvent e) {
						// TODO Auto-generated method stub
						if (isFullScreen) {
							setVideoScale(SCREEN_DEFAULT);
						} else {
							setVideoScale(SCREEN_FULL);
						}
						isFullScreen = !isFullScreen;
						Log.d(TAG, "onDoubleTap");

						if (isControllerShow) {
							showController();
						}
						// return super.onDoubleTap(e);
						return true;
					}

					@Override
					public boolean onSingleTapConfirmed(MotionEvent e) {
						// TODO Auto-generated method stub
						if (!isControllerShow) {
							showController();
							hideControllerDelay();
						} else {
							cancelDelayHide();
							hideController();
						}
						// return super.onSingleTapConfirmed(e);
						return true;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						// TODO Auto-generated method stub
						if (isPaused) {
							vv.start();
							bn3.setImageResource(R.drawable.video_pause);
							cancelDelayHide();
							hideControllerDelay();
						} else {
							vv.pause();
							bn3.setImageResource(R.drawable.video_play);
							cancelDelayHide();
							showController();
						}
						isPaused = !isPaused;
						// super.onLongPress(e);
					}
				});

		// vv.setVideoPath("http://202.108.16.171/cctv/video/A7/E8/69/27/A7E86927D2BF4D2FA63471D1C5F97D36/gphone/480_320/200/0.mp4");

		vv.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub

				setVideoScale(SCREEN_DEFAULT);
				isFullScreen = false;
				if (isControllerShow) {
					showController();
				}

				int i = vv.getDuration();
				Log.d("onCompletion", "" + i);
				seekBar.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				durationTextView.setText(String.format("%02d:%02d:%02d", hour,
						minute, second));

				
				 * controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
				 * controler.update(screenWidth, controlHeight);
				 * myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				 

				vv.start();
				bn3.setImageResource(R.drawable.video_pause);
				hideControllerDelay();
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			}
		});

		vv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				int n = playList.size();
				isOnline = false;
				if (++position < n) {
					vv.setVideoPath(playList.get(position).path);
				} else {
					vv.stopPlayback();
					finish();
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

			vv.stopPlayback();

			int result = data.getIntExtra("CHOOSE", -1);
			Log.d("RESULT", "" + result);
			if (result != -1) {
				isOnline = false;
				isChangedVideo = true;
				vv.setVideoPath(playList.get(result).path);
				position = result;
			} else {
				String url = data.getStringExtra("CHOOSE_URL");
				if (url != null) {
					vv.setVideoPath(url);
					isOnline = true;
					isChangedVideo = true;
				}
			}

			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private final static int PROGRESS_CHANGED = 0;
	private final static int HIDE_CONTROLER = 1;

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

			case PROGRESS_CHANGED:

				int i = vv.getCurrentPosition();
				seekBar.setProgress(i);

				if (isOnline) {
					int j = vv.getBufferPercentage();
					seekBar.setSecondaryProgress(j * seekBar.getMax() / 100);
				} else {
					seekBar.setSecondaryProgress(0);
				}

				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				playedTextView.setText(String.format("%02d:%02d:%02d", hour,
						minute, second));

				sendEmptyMessageDelayed(PROGRESS_CHANGED, 100);
				break;

			case HIDE_CONTROLER:
				hideController();
				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

				
				 * if(!isControllerShow){ showController();
				 * hideControllerDelay(); }else { cancelDelayHide();
				 * hideController(); }
				 
			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

		getScreenSize();
		if (isControllerShow) {

			cancelDelayHide();
			hideController();
			showController();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
//		playedTime = vv.getCurrentPosition();
//		vv.pause();
//		bn3.setImageResource(R.drawable.video_play);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		if (!isChangedVideo) {
//			vv.seekTo(playedTime);
//			vv.start();
//		} else {
//			isChangedVideo = false;
//		}
//
//		// if(vv.getVideoHeight()!=0){
//		if (vv.isPlaying()) {
//			bn3.setImageResource(R.drawable.video_pause);
//			hideControllerDelay();
//		}

		// Log.d("REQUEST", "NEW AD !");
		// adView.requestFreshAd();

		// if (getRequestedOrientation() !=
		// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// }

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		// TODO Auto-generated method stub
		
//		if (controler.isShowing()) {
//			controler.dismiss();
//			// extralWindow.dismiss();
//		}
//		
//		if (mSoundWindow.isShowing()) {
//			mSoundWindow.dismiss();
//		}
//
//		myHandler.removeMessages(PROGRESS_CHANGED);
//		myHandler.removeMessages(HIDE_CONTROLER);
//
//		if (vv.isPlaying()) {
//			vv.stopPlayback();
//		}
//
//		playList.clear();

		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}

		super.onDestroy();
	}

	private void getScreenSize() {

		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlHeight = screenHeight / 4;

		// adView = (AdView) extralView.findViewById(R.id.ad);
		// LayoutParams lp = adView.getLayoutParams();
		// lp.width = screenWidth * 3 / 5;
	}

	private void hideController() {

		if (controler.isShowing()) {

			controler.update(0, mHeight, 0, 0);
			// extralWindow.update(0, 0, screenWidth, 0);
			isControllerShow = false;
		}
		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
			isSoundShow = false;
		}
	}

	private void hideControllerDelay() {
		myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	private void showController() {

		controler.update(0, mHeight, screenWidth, controlHeight);
		// if (isFullScreen) {
		// extralWindow.update(0, 0, screenWidth, 60);
		// } else {
		// extralWindow.update(0, 25, screenWidth, 60);
		// }
		isControllerShow = true;
	}

	private void cancelDelayHide() {
		myHandler.removeMessages(HIDE_CONTROLER);
	}

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	private int mHeight;

	private void setVideoScale(int flag) {

		LayoutParams lp = vv.getLayoutParams();

		switch (flag) {
		case SCREEN_FULL:

			Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: "
					+ screenHeight);
			vv.setVideoScale(screenWidth, screenHeight);

			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			// if (getRequestedOrientation() !=
			// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			// }

			break;

		case SCREEN_DEFAULT:

			int videoWidth = vv.getVideoWidth();
			int videoHeight = vv.getVideoHeight();
			int mWidth = screenWidth;
			mHeight = screenHeight - 25;

			if (videoWidth > 0 && videoHeight > 0) {
				if (videoWidth * mHeight > mWidth * videoHeight) {
					// Log.i("@@@", "image too tall, correcting");
					mHeight = mWidth * videoHeight / videoWidth;
				} else if (videoWidth * mHeight < mWidth * videoHeight) {
					// Log.i("@@@", "image too wide, correcting");
					mWidth = mHeight * videoWidth / videoHeight;
				} else {

				}
			}

			vv.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			// if (getRequestedOrientation() !=
			// ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			// }

			break;
		}
	}

	private int findAlphaFromSound() {
		if (mAudioManager != null) {
			// int currentVolume =
			// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			int alpha = currentVolume * (0xCC - 0x55) / maxVolume + 0x55;
			return alpha;
		} else {
			return 0xCC;
		}
	}

	private void updateVolume(int index) {
		if (mAudioManager != null) {
			if (isSilent) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			} else {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
						0);
			}
			currentVolume = index;
			bn5.setAlpha(findAlphaFromSound());
		}
	}

	private void getVideoFile(final LinkedList<MovieInfo> list, File file) {

		file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				String name = file.getName();
				int i = name.indexOf('.');
				if (i != -1) {
					name = name.substring(i);
					if (name.equalsIgnoreCase(".mp4")
							|| name.equalsIgnoreCase(".3gp")) {

						MovieInfo mi = new MovieInfo();
						mi.displayName = file.getName();
						mi.path = file.getAbsolutePath();
						list.add(mi);
						return true;
					}
				} else if (file.isDirectory()) {
					getVideoFile(list, file);
				}
				return false;
			}
		});
	}
*/
}
