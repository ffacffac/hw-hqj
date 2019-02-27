package cn.pda.zafingerprint;

import android.app.Activity;

public class DatabaseOptionActivity extends Activity {
//	private Button buttonGetChara;
//	private Button buttonQuery;
//	private Button buttonInsert;
//	private EditText editInfo;
//	private EditText editName;
//	private Button buttonClear ;
//
//	private byte[] templet = null; // 指纹模板
//	private String name;
//	private List<FingerData> listFinger; // 数据库指纹数据
//	private DBServer dbServer;  //数据库操作
//	private FingerPrintCommandManager fp ;
//
//	private Handler handler = new Handler(){
//		public void handleMessage(android.os.GroundMessage msg) {
//			String data = msg.getData().getString("msg");
//			if(data != null){
//				editInfo.append(data);
//			}
//		};
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fp_db_option_activity);
//		// 初始UI
//		initView();
//		// 按钮监听
//		listener();
//		dbServer = new DBServer();
//		//先将数据库中的数据查询出来
//		listFinger = dbServer.queryAllData();
//	}
//
//	@Override
//	protected void onResume() {
//		//打开指纹设备
//		fp = new FingerPrintCommandManager(handler);
//		fp.open();
//		super.onResume();
//	}
//
//	@Override
//	protected void onPause() {
//		//关闭指纹设备
//		if(fp != null){
//			fp.close();
//		}
//		super.onPause();
//	}
//
//	private void initView() {
//		buttonGetChara = (Button) findViewById(R.id.button_option_getchara);
//		buttonQuery = (Button) findViewById(R.id.button_option_query);
//		buttonInsert = (Button) findViewById(R.id.button_option_insert);
//		editInfo = (EditText) findViewById(R.id.editText_option_info);
//		editName = (EditText) findViewById(R.id.editText_option_name);
//		buttonClear = (Button) findViewById(R.id.button_option_clear);
//
//	}
//
//	private void listener() {
//		// 获取特征模板
//		buttonGetChara.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				//将指纹模板生成在BUFFER_A
//				int buffid = FingerPrintCommandManager.BUFFER_A;
//				//生成模板
//				if(fp.genChara(buffid)){
//					templet = fp.getChara(buffid);
//				}
//
//			}
//		});
//		// 查询数据库
//		buttonQuery.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				FingerData data = compareDBdata();
//				if(data != null){
//					fp.sendMSG("已查找到，姓名：" + data.getName() + "\n");
//					//					fp.sendMSG("Search success，Name：" + data.getName() + "\n");
//					//					editInfo.append("已查找到，姓名：" + data.getName() + "\n");
//				}else{
//					//					fp.sendMSG("数据库无此指纹数据\n");
//					fp.sendMSG("Database without this FP\n");
//				}
//
//			}
//		});
//		//插入数据库
//		buttonInsert.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				String name = editName.getText().toString();
//				if(name == null){
//					editName.append("请输入name\n");
//					//					editName.append("please input name\n");
//					return ;
//				}
//				if(templet == null){
//					editInfo.append("指纹特征为null,请先获取指纹特征\n");
//					//					editInfo.append("FP chara is null,please get FP chara\n");
//					return ;
//				}
//				FingerData data = new FingerData();
//				data.setFinger(templet);
//				data.setName(name);
//				int flag = dbServer.insert(data);
//				if(flag > 0){
//					editInfo.append("插入数据库成功\n");
//					//					editInfo.append("Insert to DB success\n");
//					listFinger.add(data);
//				}else{
//					//					editInfo.append("插入数据库失败");
//					editInfo.append("Insert to DB fail\n");
//				}
//
//			}
//		});
//
//		buttonClear.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				editInfo.setText("");
//
//			}
//		});
//
//	}
//
//	private FingerData compareDBdata(){
//		int score = 0;  //比对的分值
//		if(listFinger == null || listFinger.isEmpty() || listFinger.size() == 0){
//			editInfo.setText("数据库数据为null，请先插入数据后，再查询\n");
//			//			editInfo.setText("Database is null，please insert data\n");
//			return null;
//		}
//		//将当前指纹特征存入buffer_a
//		if(fp.genChara(FingerPrintCommandManager.BUFFER_A)){
//			for(int i = 0; i < listFinger.size(); i++){
//				//将指纹数据放入缓存区B
//				boolean putFlag = fp.putChara(FingerPrintCommandManager.BUFFER_B,
//						listFinger.get(i).getFinger());
//				if(putFlag){
//					//比对A B缓存区的指纹模板，得出分值，当分值大于50可认为匹配成功
//					score = fp.matchChara();
//					if(score > 50){
//						return listFinger.get(i);
//					}
//				}
//			}
//		}
//		return null;
//	}
}
