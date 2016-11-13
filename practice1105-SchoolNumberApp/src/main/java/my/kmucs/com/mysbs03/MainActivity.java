package my.kmucs.com.mysbs03;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    //사용할 객체 생성
    EditText editText01, editText02, editText03;
    Button resetBtn, selectBtn, saveBtn, listBtn;
    MyDB mydb;
    SQLiteDatabase sqlite;
    TextView hakbun, name, address, userId;
    Intent i; //listBtn과 함께 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText01 = (EditText)findViewById(R.id.et01);
        editText02 = (EditText)findViewById(R.id.et02);
        editText03 = (EditText)findViewById(R.id.et03);
        resetBtn = (Button)findViewById(R.id.btn01);
        selectBtn = (Button)findViewById(R.id.btn02);
        saveBtn = (Button)findViewById(R.id.btn03);
        listBtn = (Button)findViewById(R.id.btn04);
        hakbun = (TextView)findViewById(R.id.hakbun);
        name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);
        userId = (TextView)findViewById(R.id.userId);
        i = new Intent(this, HakbunListViewActivity.class);

        //데이터베이스 연결
        mydb = new MyDB(this);




        //버튼이벤트장착 : 초기화버튼
        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //데이터베이스열고
                sqlite = mydb.getWritableDatabase(); //읽기쓰기가능한속성

                mydb.onUpgrade(sqlite, 1 ,2); //1번 버젼 지우고 2번버젼 만들겠다.
                sqlite.close();
            }
        });

        //버튼이벤트장착 : 저장(입력)버튼
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //데이터베이스열고
                sqlite = mydb.getWritableDatabase(); //읽기쓰기가능한속성

                String hakbunStr = editText01.getText().toString();
                String nameStr = editText02.getText().toString();
                String addressStr = editText03.getText().toString();

                //value값은 학번, 이름, 주소 순서 : member(hakbun, name, address)만 적은 이유는 _id값은 안넣어주려고 그런거임
                String sql = "INSERT INTO member(hakbun, name, address) VALUES('"+ hakbunStr +"','"+ nameStr +"','"+ addressStr +"')";  //변수명을 넣어주고싶으면 '"+ 변수명 +"' 해서 하자


                Log.d("SBS07", sql);
                sqlite.execSQL(sql);  //실행해라

                //데이터베이스 닫고
                sqlite.close();
                Toast.makeText(getApplicationContext(), "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //버튼이벤트장착 : 조회버튼 롱클릭
        selectBtn.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                getListView();
                return false;
            }
        });

        //버튼이벤트장착 : 조회버튼 숏클릭
        selectBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //데이터베이스 열고
                sqlite = mydb.getReadableDatabase(); //읽기만 가능한 속성, 왜냐햐면 조회는 수정이 필요가 없으니까

                String sql = "SELECT * FROM member"; //*은 전부를 뜻한다, 이테이블의 전부를 가져와라.
                Cursor cursor;
                cursor = sqlite.rawQuery(sql, null); //인자가 두개가 들어감, 두번째꺼는 그냥 null로 넣어줌..커서가 널에 위치하고 있겠다. 몇번 째 raw 위치에 커서를 위치할 것인가

                String hakbunStr    = "학번\r\n";
                String nameStr      = "이름\r\n";
                String addressStr   = "주소\r\n";
                String userIdStr    = "sql\r\n";

                while(cursor.moveToNext()){ //다음으로 갈수 있는 커서가 있다면
                    userIdStr += cursor.getString(0) + "\r\n";
                    hakbunStr += cursor.getString(1) + "\r\n";
                    nameStr   += cursor.getString(2) + "\r\n";
                    addressStr+= cursor.getString(3) + "\r\n";
                }

                //텍스트화면에 출력
                userId.setText(userIdStr);
                hakbun.setText(hakbunStr);
                name.setText(nameStr);
                address.setText(addressStr);
                cursor.close(); //커서 닫아줘야함

                //데이터베이스 닫아줌
                sqlite.close();
            }
        });

        //버튼이벤트장착 : 리스트뷰intent
        listBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getListView();
            }
        });
    }

    //함수 생성 (hakbunactivity로 넘어가는 함수
    public void getListView(){
        startActivity(i);
    }
}


