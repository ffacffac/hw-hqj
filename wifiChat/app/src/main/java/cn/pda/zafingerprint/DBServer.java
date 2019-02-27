package cn.pda.zafingerprint;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBServer {

	/**
	 * 将指纹数据存在SD卡上
	 * 数据库根目录/sdcard/fp_db/
	 *
	 */
	public static final String DB_DIR = Environment.getExternalStorageDirectory().getPath()
			+ File.separator + "fingerPrint"  ;
	/**
	 * 数据库名称
	 */
	public static final String DB_NAME = DB_DIR + File.separator +"fp.db";
	/**
	 * 表名
	 */
	public static final String TABLE_NAME = "finger";
	//首次创建数据库
	static {
		while(! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		File dbFolder = new File(DB_DIR);
		File dbFile = new File(DB_NAME);
		// 目录不存在则自动创建目录
		if (!dbFolder.exists()){
			dbFolder.mkdirs();
		}
		//数据库是否存在
		//	        if(!dbFile.exists()){
		//	        	try {
		//					dbFile.createNewFile();
		//创建数据库
		//					SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
		//					String sql = "create " + TABLE_NAME+"(" +
		//							"finger BLOB," +
		//							"name TEXT" +
		//							");";
		//					db.execSQL(sql);
		//					db.closeDevice();
		//				} catch (IOException e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}
		//	        }
	}

	public void createDb(){
		File dbFile = new File(DB_NAME);
		if(!dbFile.exists()){
			//      	try {
			//				dbFile.createNewFile();
			//创建数据库
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
			String sql = "create table " + TABLE_NAME+"(" +
					"finger BLOB," +
					"name TEXT" +
					");";
			db.execSQL(sql);
			db.close();
			//			} catch (IOException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
		}
	}

	/**
	 * 插入数据
	 * @param name
	 * @param finger
	 * @return
	 */
	public int insert(FingerData data){
		createDb();
		byte[] finger = data.getFinger();
		String name = data.getName();
		//打开数据库
		SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_NAME, null, 0);
		ContentValues values = new ContentValues();
		values.put("finger", finger);
		values.put("name", name);
		int result = (int)db.insert(TABLE_NAME, null, values);

		return result;
	}

	/**
	 * 查询所有指纹数据
	 * @return
	 */
	public List<FingerData> queryAllData(){
		createDb();
		List<FingerData> list = new ArrayList<FingerData>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase( DB_NAME, null, 0);
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			FingerData data = new FingerData();
			data.setFinger(cursor.getBlob(cursor.getColumnIndex("finger")));
			data.setName(cursor.getString(cursor.getColumnIndex("name")));
			list.add(data);
		}
		return list;
	}


}
