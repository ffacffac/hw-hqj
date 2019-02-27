package cn.pda.zafingerprint;

import android.app.Activity;

public class MainActivity extends Activity {

//	/*** UI控件 ***/
//	private Button buttonOpenDevice;
//	private Button buttonCloseDevice;
//	private Button buttonGetImage;
//	private Button buttonRecordFP;
//	private Button buttonSearchFP;
//	private Button buttonCompareFP;
//	private Button buttonHelp ;
//	private Button buttonClear ;
//	private Button buttonDel ;
//	//	private EditText editSerialport;
//	private EditText editTips;
//	//	private Spinner spinnerBaudRate;
//	private ImageView imageViewFinger;
//
//	private Context context;
//
//	private FingerPrintCommandManager fpManager ; //指纹操作句柄
//
//	private boolean[] freeFringerAddress = new boolean[1000];
//
//	// 消息处理器
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.GroundMessage msg) {
//			String data = msg.getData().getString("msg");
//			if(data != null){
//				editTips.append(data);
//			}
//		};
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fp_activity_main);
//
//
//		/* 初始UI */
//		initUI();
//		//		//监听
//		listener();
//		Log.e("Tag", "OnCreate");
//	}
//
//
//	@Override
//	protected void onResume() {
//		//获取指纹实例
//		fpManager = new FingerPrintCommandManager(mHandler);
//		openFlag  = fpManager.open();
//		if(openFlag){
//			setButtonClickable(buttonRecordFP, true);
//			setButtonClickable(buttonCloseDevice, true);
//			setButtonClickable(buttonCompareFP, true);
//			setButtonClickable(buttonGetImage, true);
//			setButtonClickable(buttonSearchFP, true);
//			setButtonClickable(buttonDel, true);
//			setButtonClickable(buttonOpenDevice, false);
//			Arrays.fill(freeFringerAddress, false);
//			//读取指纹模板索引,这条指令可执行也可以不执行
//			//true为已存储模板，false为可存储空间
//			freeFringerAddress = fpManager.getFreeAddress();
//		}
//		super.onResume();
//	}
//
//	@Override
//	protected void onPause() {
//		if(fpManager != null){
//			fpManager.close();
//		}
//		super.onPause();
//	}
//
//	//初始化UI
//	private void initUI() {
//		context = this;
//		buttonOpenDevice = (Button) findViewById(R.id.button_open_device);
//		buttonCloseDevice = (Button) findViewById(R.id.button_close_device);
//		buttonGetImage = (Button) findViewById(R.id.button_getImg);
//		buttonRecordFP = (Button) findViewById(R.id.button_recordTemplet);
//		buttonSearchFP = (Button) findViewById(R.id.button_search);
//		buttonCompareFP = (Button) findViewById(R.id.button_db_compare);
//		buttonHelp = (Button) findViewById(R.id.button_help);
//		buttonClear = (Button) findViewById(R.id.button_clear);
//		buttonDel = (Button) findViewById(R.id.button_delete_all);
//		//		editSerialport = (EditText) findViewById(R.id.editText_serialport);
//		editTips = (EditText) findViewById(R.id.editText_tips);
//		//		spinnerBaudRate = (Spinner) findViewById(R.id.spinner_serialport);
//		imageViewFinger = (ImageView) findViewById(R.id.imageView_fp);
//		//		baudrates = getResources().getStringArray(R.array.baud_rate);
//		//		for (String baudrateStr : baudrates) {
//		//			baudRateList.add(baudrateStr);
//		//		}
//		//		spinnerBaudRate.setAdapter(new ArrayAdapter<String>(context,
//		//				android.R.layout.simple_spinner_dropdown_item, baudRateList));
//
//
//	}
//
//	private void listener() {
//		//打开设备
//		buttonOpenDevice.setOnClickListener(new OpenDevice());
//		//关闭设备
//		buttonCloseDevice.setOnClickListener(new CloseDevice());
//		//获取指纹图像
//		buttonGetImage.setOnClickListener(new GetImage());
//		//登记模板
//		buttonRecordFP.setOnClickListener(new RecordTemplet());
//		//搜索指纹
//		buttonSearchFP.setOnClickListener(new SearchFingerPrint());
//		//比对指纹
//		buttonCompareFP.setOnClickListener(new CompareFingerPrint());
//		//删除所有模板数据
//		buttonDel.setOnClickListener(new DelAllChara());
//
//		setButtonClickable(buttonCloseDevice, false);
//		setButtonClickable(buttonGetImage, false);
//		setButtonClickable(buttonRecordFP, false);
//		setButtonClickable(buttonSearchFP, false);
//		setButtonClickable(buttonCompareFP, false);
//		setButtonClickable(buttonDel, false);
//		//帮助，查看错误码
//		buttonHelp.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(MainActivity.this, HelpActivity.class);
//				startActivity(intent);
//
//			}
//		});
//		//清空信息
//		buttonClear.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				editTips.setText("");
//
//			}
//		});
//
//
//
//	}
//
//	//设置按钮是否可用
//	private void setButtonClickable(Button button, boolean flag){
//		button.setClickable(flag);
//		if(flag){
//			button.setTextColor(getResources().getColor(R.color.black));
//		}else{
//			button.setTextColor(getResources().getColor(R.color.gray));
//		}
//	}
//
//	boolean openFlag = false ;
//	//打开设备
//	private class OpenDevice implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			openFlag  = fpManager.open();
//			if(openFlag){
//				setButtonClickable(buttonRecordFP, true);
//				setButtonClickable(buttonCloseDevice, true);
//				setButtonClickable(buttonCompareFP, true);
//				setButtonClickable(buttonGetImage, true);
//				setButtonClickable(buttonSearchFP, true);
//				setButtonClickable(buttonDel, true);
//				setButtonClickable(buttonOpenDevice, false);
//				Arrays.fill(freeFringerAddress, false);
//				//读取指纹模板索引,这条指令可执行也可以不执行
//				//true为已存储模板，false为可存储空间
//				freeFringerAddress = fpManager.getFreeAddress();
//			}
//
//		}
//
//	}
//	//关闭设备
//	private class CloseDevice implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			if(fpManager != null){
//				fpManager.close();
//			}
//			setButtonClickable(buttonOpenDevice, true);
//			setButtonClickable(buttonCloseDevice, true);
//			setButtonClickable(buttonGetImage, false);
//			setButtonClickable(buttonRecordFP, false);
//			setButtonClickable(buttonSearchFP, false);
//			setButtonClickable(buttonCompareFP, false);
//			setButtonClickable(buttonDel, true);
//		}
//
//	}
//	//获取指纹图像
//	private class GetImage implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			Bitmap bp = fpManager.getFPImage();
//			if(bp != null){
//				imageViewFinger.setImageBitmap(bp);
//			}
//		}
//
//	}
//	//登记模板
//	private class RecordTemplet implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//
//			//调用函数时可以不将操作写在线程中
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					for(int i = 0; i < freeFringerAddress.length; i++){
//						//搜索未使用的存储空间
//						if(!freeFringerAddress[i]){
//							//登记模板，将指纹数据存入模块
//							if(fpManager.enroll(i)){
//								freeFringerAddress[i] = true;
//
//							}
//							break;
//						}
//					}
//
//				}
//			}).start();
//
//		}
//
//	}
//	//搜索指纹
//	private class SearchFingerPrint implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			int bufferID = FingerPrintCommandManager.BUFFER_A;
//			//获取指纹特征
//			boolean genFlag = fpManager.genChara(bufferID);
//			if(genFlag){
//				fpManager.searchChara(bufferID);
//			}
//
//		}
//
//	}
//	//比对指纹
//	private class CompareFingerPrint implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			Intent intent = new Intent(MainActivity.this, DatabaseOptionActivity.class);
//			startActivity(intent);
//		}
//
//	}
//
//	//删除模板
//	private class DelAllChara  implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			fpManager.emptyChara();
//		}
//
//	}
//
//
//
//
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		fpManager.close();
//	}



}
