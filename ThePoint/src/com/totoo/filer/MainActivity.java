package com.totoo.filer;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView listview;
	private TextView tv_path;
	EditText ed_input;
	OnClickListener copyWaitListener;
	DialogInterface.OnClickListener CreateFSListener;
	View.OnClickListener AutoPickListener;// {0,1}
	View.OnClickListener CreateModeSwitcher;
	private Button sdcarinteriorbutton;
	private Button interiorbutton;
	Button SDbtn;
	public static EditText filename;
	public static RadioGroup switchRadio;
	public static Button btnFileCreate;
	public static Button btnFileDelete;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftleft:
			Intent ii = new Intent(MainActivity.this, Tel.class);
			startActivity(ii);
			this.finish();
		case R.id.left://
			// // SD��

			if ("�֙C��ă���" == SDbtn.getText()) {
				SDbtn.setText("�ⲿ�惦SD��");
				createfilelist("/mnt/");
				tv_path.setText("�֙C�Ă}��");
				FileService.newfilepath = "/mnt/";
			} else {
				SDbtn.setText("�֙C��ă���");
				createfilelist("/mnt/sdcard");
				tv_path.setText("SD���濨");
				FileService.newfilepath = "/mnt/sdcard";
			}
			// ReportFileStatus();
			break;
		case R.id.btn:// �֙C���dλ��
			try {
				FileService.newfilepath = "/";
				upgradeRootPermission("/");
				createfilelist("/");
				tv_path.setText("��Ŀ�");
			} catch (Exception e) {
				e.printStackTrace();
				try {
					FileService.newfilepath = "/mnt/sdcard/pictures";
					createfilelist("/mnt/sdcard/pictures");
					tv_path.setText("�ҵ���Ƭ");
					try {
						FileService.newfilepath = "/system/media/audio";
						createfilelist("	/system/media/audio");
						tv_path.setText("��");
					} catch (Exception ee) {
					}
				} catch (Exception ee) {
				}
			}
			// ReportFileStatus();
			break;

		case R.id.right:// �d���ˆ�

			Builder builder = new Builder(MainActivity.this);
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View layoutview = inflater.inflate(R.layout.create_menu, null);
			ed_input = (EditText) layoutview.findViewById(R.id.etinput);
			builder.setView(layoutview);
			builder.setNegativeButton("�Q������", CreateFSListener);
			builder.show();
			btnFileCreate = (Button) layoutview// һ�����o,���c�ГQ
					.findViewById(R.id.btnMode);
			btnFileCreate.setOnClickListener(CreateModeSwitcher);

			// ReportFileStatus();
			break;

		case R.id.rightright://
			// // ճ��

			//
			//
			Toast.makeText(MainActivity.this, FileService.copypath, 0).show();
			if (FileService.copypath != null) {
				if (FileService.copy()) {

					Toast.makeText(MainActivity.this, "��İ�����͵�,Ոע�����", 0)
							.show();

					createfilelist(FileService.newfilepath);
				} else {
					Toast.makeText(MainActivity.this, "��]����סĿ��", 0).show();
				}
			}
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.lv_main);
		tv_path = (TextView) findViewById(R.id.tv_path);
		sdcarinteriorbutton = (Button) findViewById(R.id.left);
		SDbtn = (Button) findViewById(R.id.left);

		// FileService.newfilepath=fe.name ;
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				try {
					person fe = (person) ((dpt) adapterView.getAdapter())
							.getItem(position);
					FileService.newfilepath = fe.name;
					if(new File(fe.name).isDirectory()){
						Intent intent=new Intent();
						intent.setAction(null);
						;}
					else{
					// ReportFileStatus();
					createfilelist(fe.name);}
				} catch (Exception e) {
					Toast.makeText(MainActivity.this, "�M��ȥ��,����������ⱻ���_", 0).show();
				}
			}

		});

		// FileService.FileSelected(fe.name) ;
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, int position, long l) {
				try {
					person fe = (person) ((dpt) adapterView.getAdapter())
							.getItem(position);
					FileService.copypath = fe.name;
					// ReportFileStatus();
					View viewMenu = LayoutInflater.from(MainActivity.this)
							.inflate(R.layout.copy_menu, null);
					Builder builderCreate = new Builder(MainActivity.this);// �����ˆ�
					switchRadio = (RadioGroup) viewMenu
							.findViewById(R.id.FileModeSwich);
					btnFileCreate = (Button) viewMenu
							.findViewById(R.id.btnCreate);
					btnFileDelete = (Button) viewMenu
							.findViewById(R.id.btnDelete);
					btnFileCreate.setOnClickListener(AutoPickListener);
					btnFileDelete.setOnClickListener(AutoPickListener);
					builderCreate.setView(viewMenu);
					builderCreate.setNegativeButton("�Q������", copyWaitListener);
					builderCreate.show();
				} catch (Exception e) {
				}
				return false;
			}
		});

		listview.setAdapter(new dpt(init(), MainActivity.this));
		FileService.newfilepath = "/system/media/audio";

		CreateFSListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (FileService.create(ed_input.getText().toString()))
					Toast.makeText(MainActivity.this, "��������", 1).show();
				else

					Toast.makeText(MainActivity.this, "�Ѵ�ð�", 1).show();
				createfilelist(FileService.newfilepath);
			}

		};
		copyWaitListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (R.id.RC == switchRadio.getCheckedRadioButtonId()) {
					FileService.copyMode = true;// �}�u

				} else if (R.id.RD == switchRadio.getCheckedRadioButtonId()) {
					FileService.copyMode = false;
					FileService.copy();
					FileService.copyMode = true;
				}
				createfilelist(FileService.newfilepath);
			}
		};
		AutoPickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (R.id.RC == switchRadio.getCheckedRadioButtonId()) {
					FileService.copyMode = true;// �}�u

				} else if (R.id.RD == switchRadio.getCheckedRadioButtonId()) {
					FileService.copyMode = false;// �h��
					FileService.copy();
					FileService.copyMode = true;
				}
				createfilelist(FileService.newfilepath);
			}

		};
		CreateModeSwitcher = new View.OnClickListener() {
			int count = 0;

			@Override
			public void onClick(View v) {
				count++;
				if (0 == count % 2) {
					FileService.createMode = true;// ���ļ�
				} else {
					FileService.createMode = false;// ��Ŀ�
				}

				// ReportFileStatus();
			}

		};
	}

	public boolean onTouch(View v, MotionEvent event) {
		// Toast.makeText(MainActivity.this, "�c��"+count++,0).show();
		return false;
	}

	// public void ReportFileStatus() {
	// Toast.makeText(
	// MainActivity.this,
	// FileService.copypath + ":" + FileService.newfilepath + ";����ģʽ:"
	// + FileService.copyMode + ":����ģʽ:"
	// + FileService.createMode + ";", 0).show();
	// }

	public void createfilelist(String path) {
		FileService.filelist = new ArrayList<person>();
		listview.setAdapter(new dpt(FileService.getfilepath(path),
				MainActivity.this));
		tv_path.setText(path);
	}

	List<person> init() {
		ArrayList<person> data = new ArrayList<person>();
		for (int i = 0; i < 11; i++) {
			data.add(new person("icon" + i + "_Android", i, "�D��"));
		}
		return data;
	}

	public static boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // �л���root�ʺ�
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}
}

class person {
	public String name;
	public Integer pic;
	public String tel;
	public Integer textColor = Color.WHITE;

	person(String name1, int pic1, String tel1) {
		this.name = name1;
		this.pic = pic1;
		this.tel = tel1;
	}

	person(String name1, String _tel) {
		this.name = name1;
		this.tel = _tel;
	}

	public person() {
	}

	public person(int o1, String o2, String o3, int o4) {
		pic = o1;
		name = o2;
		tel = o3;
		textColor = o4;
	}

}class dpt extends BaseAdapter {
	List<person> list;
	Context con;

	public dpt(List<person> list, Context con) {
		this.list = list;
		this.con = con;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewhold vh = null;
		if (convertView == null) {
			vh = new viewhold();
			convertView = LayoutInflater.from(con).inflate(R.layout.li,
					null);
			vh.iv = (ImageView) convertView.findViewById(R.id.img_ico);
			vh.name = (TextView) convertView.findViewById(R.id.tv_name);
			vh.tel = (TextView) convertView.findViewById(R.id.tv_message);
			convertView.setTag(vh);
		} else {
			vh = (viewhold) convertView.getTag();
		}
		person p =null;
			p=	list.get(position);
		vh.iv.setImageResource(img[p.pic]);
		vh.name.setText(new File(p.name).getName());
		vh.tel.setText(p.tel);
		vh.tel.setTextColor(p.textColor);
		vh.name.setTextColor(p.textColor);
		return convertView;
	}

public class viewhold {
	public ImageView iv;
	 public TextView name;
	 public TextView tel;
}
int[] img={R.drawable.ic_launcher,
		R.drawable.apk,
		R.drawable.file,
		R.drawable.folder,
		R.drawable.jpg,
		R.drawable.mp,
		R.drawable.mp3,
		R.drawable.txt,
		R.drawable.zip,
		R.drawable.uptohigh,
		R.drawable.root};
}
// public int getAntiColor(){
// if(Color.WHITE!=textColor)
// return Color.WHITE;
// else
// return Color.GRAY;
// }

