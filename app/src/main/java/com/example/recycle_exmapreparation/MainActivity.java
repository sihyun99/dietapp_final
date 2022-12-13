package com.example.recycle_exmapreparation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    private String TAG = "MainActivity";
    private Context mContext;
    private ArrayList<Data> mArrayList, mFilteredList;//필터링할 데이터 담을 어레이리스트
    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText edit_name, edit_number, edit_search , edit_evaluate,edit_time,edit_quantity,edit_date,edit_area;
    private Button btn_save;
    private DBHelper mDbHelper;
    private SQLiteDatabase db;
    private TextView textView_Date;
    ImageView imageView;
    String imgName = "osz.png";
    private DatePickerDialog.OnDateSetListener callbackMethod;


    /////////이미지관련




    public void bt1(View view) {    // 이미지 선택 누르면 실행됨 이미지 고를 갤러리 오픈
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData(); //--->>>db에 저장
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    imageView.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    instream.close();   // 스트림 닫아주기
                    saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
                    Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getCacheDir(), imgName);    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기  sqlDB = myDBHelper.getWritableDatabase();
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void bt2(View view) {    // 이미지 삭제
        try {
            File file = getCacheDir();  // 내부저장소 캐시 경로를 받아오기
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {    // 배열의 크기만큼 반복
                if (flist[i].getName().equals(imgName)) {   // 삭제하고자 하는 이름과 같은 파일명이 있으면 실행
                    flist[i].delete();  // 파일 삭제
                    Toast.makeText(getApplicationContext(), "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }

    }

    ///////////////










///////날짜
public void InitializeView()
{
    textView_Date = (TextView)findViewById(R.id.edit_date);
}

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()


        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
            }
        };
    }
//////////////장소관련

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2022, 11, 27);

        dialog.show();
    }
    ///////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("식사입력");
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        mContext = MainActivity.this;
        edit_name = findViewById (R.id.edit_name);
        edit_number=findViewById(R.id.edit_number);
        edit_evaluate = findViewById (R.id.edit_evaluate);
        edit_time = findViewById (R.id.edit_time);
        edit_quantity = findViewById (R.id.edit_quantity);
        edit_date = findViewById (R.id.edit_date);
        edit_area = findViewById (R.id.edit_area);

        this.InitializeView();
        this.InitializeListener();

        btn_save = findViewById (R.id.btn_save);
        mRecyclerView = findViewById (R.id.recycler);


        Button page1button = (Button) findViewById(R.id.button7); //버튼수정

        page1button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainpageActivity.class);
                startActivity(intent1);
            }

        });


        ///////이미지관련////////
        imageView = findViewById(R.id.imageView2);

        try {
            String imgpath = getCacheDir() + "/" + imgName;   // 내부 저장소에 저장되어 있는 이미지 경로
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageView.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
            Toast.makeText(getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }
/////////////장소입력관련
        Button btnMap=(Button) findViewById(R.id.button3);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent1);
            }
        });

        EditText textView_Area = (EditText)findViewById(R.id.edit_area);

        Button getAdrs=(Button) findViewById(R.id.button4);
        getAdrs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                String address = intent1.getStringExtra("주소");
                textView_Area.setText(address);
            }
        });
        //////////////////////////


        Button page3button = (Button) findViewById(R.id.btn_save);

        page3button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), page2.class);
                startActivity(intent1);
            }

        });


        //DBHelper 객체를 선언해줍니다.
        mDbHelper = new DBHelper (mContext);
        //쓰기모드에서 데이터 저장소를 불러옵니다.
        db = mDbHelper.getWritableDatabase ();

        initRecyclerView ();

        //버튼 클릭이벤트
        //이름과 전화번호를 입력한 후 버튼을 클릭하면 어레이리스트에 데이터를 담고 리사이클러뷰에 띄운다.
        btn_save.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (edit_area.getText ().length () == 0 &&edit_date.getText ().length () == 0 &&edit_date.getText ().length () == 0 &&edit_quantity.getText ().length () == 0 &&edit_time.getText ().length () == 0 &&
                        edit_name.getText ().length () == 0 && edit_number.getText ().length () == 0 &&
                        edit_evaluate.getText ().length () == 0) {
                    Toast.makeText (mContext, "식사정보를 입력해주세요", Toast.LENGTH_SHORT).show ();
                } else {
                    String name = edit_name.getText ().toString ();
                    String number = edit_number.getText ().toString ();
                    String evaluate = edit_evaluate.getText ().toString ();
                    String time = edit_time.getText ().toString ();
                    String quantity = edit_quantity.getText ().toString ();
                    String date = edit_date.getText ().toString ();
                    String area = edit_area.getText ().toString ();

                    edit_name.setText ("");
                    edit_number.setText ("");
                    edit_evaluate.setText ("");
                    edit_time.setText ("");
                    edit_quantity.setText ("");
                    edit_date.setText ("");
                    edit_area.setText ("");




                    Data data = new Data (name, number,evaluate,time,quantity,date,area);

                    mArrayList.add (data);
                    mAdapter.notifyItemInserted (mArrayList.size () - 1);

                    //데이터를 테이블에 삽입합니다.
                    insertNumber (name, number,evaluate,time,quantity,date,area);

                }
            }
        });

        //리사이클러뷰 클릭 이벤트
        mAdapter.setOnItemClickListener (new Adapter.OnItemClickListener () {

            //아이템 클릭시 토스트메시지0
            @Override
            public void onItemClick(View v, int position) {
                String name = mArrayList.get (position).getName ();
                String number = mArrayList.get (position).getNumber ();
                String evaluate = mArrayList.get (position).getEvaluate ();
                String time = mArrayList.get (position).getTime ();
                String quantity = mArrayList.get (position).getQuantity ();
                String date = mArrayList.get (position).getDate ();
                String area = mArrayList.get (position).getArea ();
                //Toast.makeText (mContext, "이름 : " + name + "\n전화번호 : " + number, Toast.LENGTH_SHORT).show ();

                //인텐트 객체 생성.
                //Intent 에 putExtra 로  name, number 담는다.
                //startActivity 를 사용해 DetailActivity 를 호출한다.
                Intent intent = new Intent (mContext, DetailActivity.class);
                intent.putExtra ("name", name);
                intent.putExtra ("number", number);
                intent.putExtra ("evaluate", evaluate);
                intent.putExtra ("time", time);
                intent.putExtra ("quantity", quantity);
                intent.putExtra ("date", date);
                intent.putExtra ("area", area);
                startActivity (intent);
            }

            //수정
            @Override
            public void onEditClick(View v, int position) {
                String name = mArrayList.get (position).getName ();
                String number = mArrayList.get (position).getNumber ();
                String evaluate = mArrayList.get (position).getEvaluate ();
                String time = mArrayList.get (position).getTime ();
                String quantity = mArrayList.get (position).getQuantity ();
                String date = mArrayList.get (position).getDate ();
                String area = mArrayList.get (position).getArea ();

                editItem (name, number,evaluate,time,quantity,date,area,position);
            }

            //삭제
            @Override
            public void onDeleteClick(View v, int position) {
                String name = mArrayList.get (position).getName ();
                String number = mArrayList.get (position).getNumber ();
                String evaluate = mArrayList.get (position).getEvaluate ();
                String time = mArrayList.get (position).getTime ();
                String quantity = mArrayList.get (position).getQuantity ();
                String date = mArrayList.get (position).getDate ();
                String area = mArrayList.get (position).getArea ();

                //
                deleteNumber (name, number,evaluate,time,quantity,date,area);

                mArrayList.remove (position);
                mAdapter.notifyItemRemoved (position);
            }

        });

    }

    //SQLite 데이터 수정
    //newName 은 수정된 값, oldName 수정전 값
    private void updateNumber(String oldName, String oldNumber, String oldEvaluate, String oldTime,String oldQuantity,String oldDate,String oldArea,
                              String newName, String newNumber, String newEvaluate, String newTime,String newQuantity,String newDate,String newArea ){
        //수정된 값들을 values 에 추가한다.
        ContentValues values = new ContentValues();
        values.put(DBHelper.FeedEntry.COLUMN_NAME_NAME, newName);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_NUMBER, newNumber);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_EVALUATE, newEvaluate);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_TIME, newTime);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_QUANTITY, newQuantity);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_DATE, newDate);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_AREA, newArea);

        // WHERE 절 수정될 열을 찾는다.
        String selection = DBHelper.FeedEntry.COLUMN_NAME_NAME + " LIKE ?" +
                " AND "+ DBHelper.FeedEntry.COLUMN_NAME_NUMBER + " LIKE ?"+
                " AND "+ DBHelper.FeedEntry.COLUMN_NAME_EVALUATE + " LIKE ?"+
                " AND "+ DBHelper.FeedEntry.COLUMN_NAME_TIME + " LIKE ?"+
                " AND "+ DBHelper.FeedEntry.COLUMN_NAME_QUANTITY + " LIKE ?"+
                " AND "+ DBHelper.FeedEntry.COLUMN_NAME_DATE + " LIKE ?"+
                " AND "+ DBHelper.FeedEntry.COLUMN_NAME_AREA + " LIKE ?";
        String[] selectionArgs = { oldName, oldNumber,oldEvaluate,oldTime,oldQuantity,oldDate,oldArea};

        db.update(DBHelper.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    //SQLite 데이터 삭제
    private void deleteNumber(String name, String number, String evaluate,String time,String quantity,String date, String area) {
        //WHERE 절 삭제될 열을 찾는다.
        String selection = DBHelper.FeedEntry.COLUMN_NAME_NAME + " LIKE ?" +
                " and " + DBHelper.FeedEntry.COLUMN_NAME_NUMBER + " LIKE ?"+
                " and " + DBHelper.FeedEntry.COLUMN_NAME_EVALUATE + " LIKE ?"+
                " and " + DBHelper.FeedEntry.COLUMN_NAME_TIME + " LIKE ?"+
                " and " + DBHelper.FeedEntry.COLUMN_NAME_QUANTITY + " LIKE ?"+
                " and " + DBHelper.FeedEntry.COLUMN_NAME_DATE + " LIKE ?"+
                " and " + DBHelper.FeedEntry.COLUMN_NAME_AREA + " LIKE ?";
        String[] selectionArgs = {name, number,evaluate,time,quantity,date,area};
        db.delete (DBHelper.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    //SQLite 데이터 삽입
    private void insertNumber(String name, String number, String evaluate,String time,String quantity,String date, String area) {
        //쿼리를 직접 작성해서 입력하거나 values를 만들어서 하는 방법이 있다
        //후자를 이용하겠다.
        ContentValues values = new ContentValues ();
        values.put (DBHelper.FeedEntry.COLUMN_NAME_NAME, name);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_NUMBER, number);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_EVALUATE, evaluate);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_TIME, time);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_QUANTITY, quantity);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_DATE, date);
        values.put (DBHelper.FeedEntry.COLUMN_NAME_AREA, area);
        db.insert (DBHelper.FeedEntry.TABLE_NAME, null, values);
//        String sql = "INSERT INTO "+DBHelper.FeedEntry.TABLE_NAME+" values("+name+", "+number+");";
//        db.execSQL(sql);


    }

    //데이터 불러오기
    //Cursor를 사용해서 데이터를 불러옵니다.
    //while문을 사용해서 불러온 데이터를 mArrayList에 삽입합니다.
    private void loadData() {

        @SuppressLint("Recycle") Cursor c = db.rawQuery ("SELECT * FROM " + DBHelper.FeedEntry.TABLE_NAME, null);
        while (c.moveToNext ()) {
//            Log.d (TAG, c.getString (c.getColumnIndex (DBHelper.FeedEntry._ID))
//                    + " name-"+c.getString(c.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_NAME))
//                    + " number-"+c.getString(c.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_NUMBER)));
            String name = c.getString (c.getColumnIndex (DBHelper.FeedEntry.COLUMN_NAME_NAME));
            String number = c.getString (c.getColumnIndex (DBHelper.FeedEntry.COLUMN_NAME_NUMBER));
            String evaluate = c.getString (c.getColumnIndex (DBHelper.FeedEntry.COLUMN_NAME_EVALUATE));
            String time = c.getString (c.getColumnIndex (DBHelper.FeedEntry.COLUMN_NAME_TIME));
            String quantity = c.getString (c.getColumnIndex (DBHelper.FeedEntry.COLUMN_NAME_QUANTITY));
            String date = c.getString (c.getColumnIndex (DBHelper.FeedEntry.COLUMN_NAME_DATE));
            String area = c.getString (c.getColumnIndex (DBHelper.FeedEntry.COLUMN_NAME_AREA));
            Data data = new Data (name, number,evaluate,time,quantity,date,area);

            mArrayList.add (data);
        }

        mAdapter.notifyDataSetChanged ();
    }

    //리사이클러뷰
    private void initRecyclerView() {
        //레이아웃메니저는 리사이클러뷰의 항목 배치를 어떻게 할지 정하고, 스크롤 동작도 정의한다.
        //수평/수직 리스트 LinearLayoutManager
        //그리드 리스트 GridLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager (mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager (layoutManager);
        mFilteredList = new ArrayList<> ();
        mArrayList = new ArrayList<> ();
        mAdapter = new Adapter (mContext, mArrayList);
        mRecyclerView.setAdapter (mAdapter);

        //저장된 데이터를 불러옵니다.
        loadData ();

    }

    //AlertDialog 를 사용해서 데이터를 수정한다.
    private void editItem(String name, String number, String evaluate,String time,String quantity,String date,String area ,int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        View view = LayoutInflater.from (this).inflate (R.layout.dialog, null, false);
        builder.setView (view);

        final AlertDialog dialog = builder.create ();

        final Button btn_edit = view.findViewById (R.id.btn_edit);
        final Button btn_cancel = view.findViewById (R.id.btn_cancel);
        final EditText edit_name = view.findViewById (R.id.edit_editName);
        final EditText edit_number = view.findViewById (R.id.edit_editNumber);
        final EditText edit_evaluate = view.findViewById (R.id.edit_editEvaluate);
        final EditText edit_time = view.findViewById (R.id.edit_editTime);
        final EditText edit_quantity = view.findViewById (R.id.edit_editQuantity);
        final EditText edit_date = view.findViewById (R.id.edit_editDate);
        final EditText edit_area = view.findViewById (R.id.edit_editArea);

        edit_name.setText (name);
        edit_number.setText (number);
        edit_evaluate.setText (evaluate);
        edit_time.setText (time);
        edit_quantity.setText (quantity);
        edit_date.setText (date);
        edit_area.setText (area);


        // 수정 버튼 클릭
        //어레이리스트 값을 변경한다.
        btn_edit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String editName = edit_name.getText ().toString ();
                String editNumber = edit_number.getText ().toString ();
                String editEvaluate = edit_evaluate.getText ().toString ();
                String editTime = edit_time.getText ().toString ();
                String editQuantity = edit_quantity.getText ().toString ();
                String editDate = edit_date.getText ().toString ();
                String editArea = edit_area.getText ().toString ();



                mArrayList.get (position).setName (editName);
                mArrayList.get (position).setNumber (editNumber);
                mArrayList.get (position).setEvaluate (editEvaluate);
                mArrayList.get (position).setTime (editTime);
                mArrayList.get (position).setQuantity (editQuantity);
                mArrayList.get (position).setDate (editDate);
                mArrayList.get (position).setArea (editArea);


                //데이터 수정
                updateNumber (name,number,evaluate,time,quantity,date,area,editName,editNumber,editEvaluate,editTime,editQuantity,editDate,editArea);

                mAdapter.notifyItemChanged (position);
                dialog.dismiss ();
            }
        });

        // 취소 버튼 클릭
        btn_cancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                dialog.dismiss ();
            }
        });

        dialog.show ();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        mDbHelper.close ();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        mAdapter.getFilter ().filter (charSequence);
    }

    //에딧텍스트에 입력받는 값을 감지한다.
    @Override
    public void afterTextChanged(Editable editable) {
        String searchText = edit_search.getText().toString();
        searchFilter(searchText);
    }

    //에딧텍스트 값을 받아 mFilteredList에 데이터를 추가한다.
    public void searchFilter(String searchText) {
        mFilteredList.clear();

        for (int i = 0; i < mArrayList.size(); i++) {
            if (mArrayList.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                mFilteredList.add(mArrayList.get(i));
            }
        }
        mAdapter.listFilter (mFilteredList);
    }

}
