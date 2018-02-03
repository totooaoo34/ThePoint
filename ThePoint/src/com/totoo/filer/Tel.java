package com.totoo.filer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.totoo.filer.R;

import android.R.bool;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Tel extends Activity implements TextWatcher, OnClickListener {
	Boolean newPserson = true;
	Cursor cursor;
	Button btnSearch;
	Button btnUp;
	Button btnDel;
	TextView tv_main;
	EditText et_table;
	ListView lv_tel;
	AutoCompleteTextView lenovo_tv;
	SQLiteDatabase dataBase;
	SQLiteDatabase dataBase1;
	// SQLiteDatabase dataBase2;
	// mySqlLiteDB SQLDBhelper;
	String waitUpdatePersonName;
	String SelectedDBname = "t_words";
	String SelectedTableName = "english";
	String SelectedValueName = "chinese";
	String DBfileName = "dictionary.db";
	String DBpath = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/download";
	String ContactPath = "content://com.android.contacts/raw_contacts";

	ContentResolver mCR;
	String ContactPaht = "content://com.android.contacts/raw_contacts";
	private String COLOMN_ID = "_id";
	private String COLOMN_NAME = "name";

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telmain);
		tv_main = (TextView) findViewById(R.id.result);
		btnSearch = (Button) findViewById(R.id.searchWord);
		et_table = (EditText) findViewById(R.id.et_table);
		btnUp = (Button) findViewById(R.id.UpdatePerson);
		btnDel = (Button) findViewById(R.id.DeletePerson);
		lenovo_tv = (AutoCompleteTextView) findViewById(R.id.word);
		lv_tel = (ListView) findViewById(R.id.lv_tel);
		lv_tel.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressLint("NewApi")
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
		lv_tel.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(Tel.this, "�фh��", 0).show();
				TextView tv_id = (TextView) view.findViewById(R.id.tv_message);
				String str = tv_id.getText().toString();
				mCR.delete(
						RawContacts.CONTENT_URI
								.buildUpon()
								.appendQueryParameter(
										ContactsContract.CALLER_IS_SYNCADAPTER,
										"true").build(),

						"_id=?", new String[] { str });
				getContacts();
				return false;
			}
		});
		et_table.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
if(et_table.getText().toString().equals(""))
	et_table.setText("�M��");
				return false;
			}
		});
		lenovo_tv.addTextChangedListener(this);
		btnSearch.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnUp.setOnClickListener(this);
		dataBase = openDatabaseHere();
		mCR = getContentResolver();
	};

	SQLiteDatabase openDatabaseHere() {
		String databaseFilename = DBpath + "/" + DBfileName;
		File dir = new File(DBpath);
		if (!dir.exists())
			dir.mkdir();
		if (!(new File(databaseFilename)).exists()) {
			InputStream is = getResources().openRawResource(R.raw.dictionary);
			try {
				FileOutputStream fos;
				fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (Exception e) {
			}
		}
		// dataBase2 = openOrCreateDatabase("person", MODE_PRIVATE, null);
		// SQLDBhelper = new mySqlLiteDB(Tel.this, "personTel.db", null, 1);
		// SQLDBhelper.getWritableDatabase();

		dataBase1 = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
		return dataBase1;
	}

	@Override
	public void afterTextChanged(Editable s) {
		try {
			cursor = dataBase.rawQuery("select " + SelectedTableName
					+ " as _id from " + SelectedDBname + " where "
					+ SelectedTableName + " like ?", new String[] { lenovo_tv
					.getText().toString() + "%" });
			lenovoDpt dictionaryAdapter = new lenovoDpt(Tel.this, cursor, true);
			lenovo_tv.setAdapter(dictionaryAdapter);

		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.searchWord:
			try {
				String str = lenovo_tv.getText().toString();
				String tableString = et_table.getText().toString();
				if ("person".equals(tableString)) {
					// dataBase = dataBase2;
					// SelectedDBname = "person";
					SelectedTableName = "name";
					SelectedValueName = "telphone";
				} else {
					dataBase = dataBase1;
					// SelectedDBname = "t_words";
					SelectedTableName = "english";
					SelectedValueName = "chinese";
				}
				String sql_string = "select " + SelectedTableName + " as _id, "
						+ SelectedValueName + " from " + SelectedDBname
						+ " where " + SelectedTableName + " like ?";
				cursor = dataBase.rawQuery(sql_string, new String[] { str });
				int i = cursor.getColumnIndex(SelectedValueName);
				String result = "δ�ҵ��õ���.";
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					result = cursor.getString(i);
				}
				result.replace("&amp;", "&");
				tv_main.setText(lenovo_tv.getText().toString() + "------��x:"
						+ result);

			} catch (Exception e) {
			}
			break;
		case R.id.DeletePerson:
			insertContactBatch();
			getContacts();
			break;
		case R.id.UpdatePerson:

			getContacts();

			// String stringName = lenovo_tv.getText().toString();
			// if (newPserson) {
			// try {
			// SQLDBhelper.put(dataBase, stringName);
			// waitUpdatePersonName = stringName;
			// } catch (Exception e) {
			// }
			// } else {
			// SQLDBhelper.update(dataBase,
			// SQLDBhelper.selectByNameFirstId(dataBase, stringName),
			// waitUpdatePersonName, stringName);
			// }
			// break;
		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		try {
			String str = lenovo_tv.getText().toString();
			cursor = dataBase.rawQuery("select " + SelectedTableName
					+ " as _id, chinese,telephone from " + SelectedDBname
					+ " where " + SelectedTableName + " = ?",
					new String[] { str });
			int i = cursor.getColumnIndex("chinese");
			String result = "δ�ҵ��õ���.";
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				result = cursor.getString(i);
			}
			result.replace("&amp;", "&");
			tv_main.setText(str + "------��x:" + result);
		} catch (Exception e) {
		}
	}

	class lenovoDpt extends CursorAdapter {
		LayoutInflater lif;

		public lenovoDpt(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			lif = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		public CharSequence convertToString(Cursor cursor) {
			return cursor == null ? "" : cursor.getString(0);
		}

		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			View v = lif.inflate(R.layout.word_list_item, null);
			((TextView) v).setText(getCursorAt(arg1));

			return v;
		}

		public void bindView(View arg0, Context arg1, Cursor arg2) {
			TextView t = (TextView) arg0;
			t.setText(getCursorAt(arg2));

		}
	}

	String getCursorAt(Cursor cursor) {
		try {
			String stirng = cursor.getString(0);
			tv_main.setText(stirng);
			return stirng;
		} catch (Exception e) {
			return "�]�Д���";
		}
	}

	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case 0:
	// SelectedDBname = "telphone";
	// SelectedTablename = "person";
	// tz("���ГQ�@ʾ�M��");
	// break;
	//
	// default:
	// SelectedDBname = "t_words";
	// SelectedTablename = "english";
	// tz("���ГQ���~��");
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	//
	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v,
	// ContextMenuInfo menuInfo) {
	// menu.add(1, 2, 2, "�M��");
	// menu.add(1, 1, 1, "���~��");
	// super.onCreateContextMenu(menu, v, menuInfo);
	// }

	void tz(String string) {
		Toast.makeText(Tel.this, string, 0).show();
	}

	//
	// static class mySqlLiteDB extends SQLiteOpenHelper {
	// public final static String TABLE_CONTACT = "person";
	// public final static String COLOMN_NAME = "name";
	// public final static String COLOMN_TEL = "telephone";
	//
	// // public final static String COLOMN_BIRTHDAY ="birthday";
	//
	// public mySqlLiteDB(Context context, String name, CursorFactory factory,
	// int version) {
	// super(context, name, factory, version);
	// }
	//
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// db.execSQL("create table person(_id integer primary key autoincrement,"
	// + "name varchar(20),tel varchar(11),telephone varchar(12))");
	// }
	//
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	// db.execSQL("alter table person add column address varchar(34)");
	// }
	//
	// public static void put(SQLiteDatabase db, String n) {
	// ContentValues data = new ContentValues();
	// data.put(COLOMN_NAME, n);
	// data.put(COLOMN_TEL, "[δ����̖�a]");
	// db.insert(TABLE_CONTACT, null, data);
	// }
	//
	// public static void update(SQLiteDatabase db, String id, String name,
	// String value) {
	// ContentValues data = new ContentValues();
	// data.put(COLOMN_NAME, name);
	// data.put(COLOMN_TEL, value);
	// db.update(TABLE_CONTACT, data, "_id=?", new String[] { id });
	//
	// }
	//
	// public static String selectByNameFirstId(SQLiteDatabase db, String name)
	// {
	//
	// Cursor c = db.rawQuery("select * from" + TABLE_CONTACT
	// + "where name = ?", (new String[] { name }));
	// return c.getString(c.getColumnIndex("_id"));
	//
	// }
	// }

	public class ContactBean {
		String name;
		String phone;
		String email;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public void getContacts() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Uri uri = Uri.parse(ContactPaht);
		Cursor c = mCR.query(uri, new String[] { "_id" }, null, null, null);
		while (c.moveToNext()) {
			int id = c.getInt(c.getColumnIndex("_id"));
			String dataPath = "content://com.android.contacts/raw_contacts/"
					+ id + "/data";
			Cursor c1 = mCR.query(Uri.parse(dataPath), new String[] { "data1",
					"mimetype" }, null, null, null);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(COLOMN_ID, id);
			while (c1.moveToNext()) {
				String mimetype = c1.getString(c1.getColumnIndex("mimetype"));
				String data1 = c1.getString(c1.getColumnIndex("data1"));
				// if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {//
				// �绰����
				// // contactInfo.setPhone(data1);
				// data.put(COLOMN_TEL, data1);
				// } else
				if ("vnd.android.cursor.item/name".equals(mimetype)) {// ����
					data.put(this.COLOMN_NAME, data1);
				}
				// else if ("vnd.android.cursor.item/email_v2".equals(mimetype))
				// {// ����
				// data.put(this.COLOMN_EMAIL, data1);
				// }
			}
			list.add(data);
		}
		SimpleAdapter sa = new SimpleAdapter(this, list, R.layout.li,
				new String[] { COLOMN_NAME, COLOMN_ID }, new int[] {
						R.id.tv_name, R.id.tv_message });
		lv_tel.setAdapter(sa);
	}

	@SuppressLint("NewApi")
	public void insertContactBatch() {
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

		String insertPath = "content://com.android.contacts/raw_contacts";
		String insertData = "content://com.android.contacts/data";
		// ����id
		ContentValues values = new ContentValues();
		ContentProviderOperation operation1 = ContentProviderOperation
				.newInsert(Uri.parse(insertPath)).withValues(values).build();
		operations.add(operation1);

		values.clear();
		ContentProviderOperation operation2 = ContentProviderOperation
				.newInsert(Uri.parse(insertData))
				.withValueBackReference("raw_contact_id", 0)
				.withValue("data1", "���ᬔ")
				.withValue("mimetype", "vnd.android.cursor.item/name").build();
		operations.add(operation2);
		values.clear();
		ContentProviderOperation operation3 = ContentProviderOperation
				.newInsert(Uri.parse(insertData))
				.withValueBackReference("raw_contact_id", 0)
				.withValue("data1", "1111345677")
				.withValue("mimetype", "vnd.android.cursor.item/phone_v2")
				.build();
		operations.add(operation3);

//		values.clear();
//		ContentProviderOperation operation4 = ContentProviderOperation
//				.newInsert(Uri.parse(insertData))
//				.withValueBackReference("raw_contact_id", 0)
//				// ������һ�εķ���ֵ
//				.withValue("data1", "xxx@xxx.com")
//				.withValue("mimetype", "vnd.android.cursor.item/email_v2")
//				.build();
//		operations.add(operation4);

		// ִ��
		try {
			mCR.applyBatch("com.android.contacts", operations);
		} catch (Exception e) {
		}
	}
	//

	// // TODO Auto-generated method stub

	//
	// // �������

	//
	// public void insertContact() {
	// // ����raw_contact��һ�����
	// ContentValues values = new ContentValues();
	// Uri uri = mCR.insert(Uri.parse(insertPath), values);
	// long id = ContentUris.parseId(uri);
	//
	// // ��������Ϣ���뵽data����
	// // ��������
	// values.clear();
	// values.put("raw_contact_id", id);
	// values.put("data1", "����");
	// values.put("mimetype", "vnd.android.cursor.item/name");
	// mCR.insert(Uri.parse(insertData), values);
	//
	// // ����绰
	// values.clear();
	// values.put("raw_contact_id", id);
	// values.put("data1", "1313123123");
	// values.put("mimetype", "vnd.android.cursor.item/phone_v2");
	// mCR.insert(Uri.parse(insertData), values);
	//
	// // ����email
	// values.clear();
	// values.put("raw_contact_id", id);
	// values.put("data1", "av@japan.com");
	// values.put("mimetype", "vnd.android.cursor.item/email_v2");
	// mCR.insert(Uri.parse(insertData), values);

}
