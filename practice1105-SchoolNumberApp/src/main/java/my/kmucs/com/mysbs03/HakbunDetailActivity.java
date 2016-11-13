package my.kmucs.com.mysbs03;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Koo on 2016-11-05.
 */

public class HakbunDetailActivity extends Activity {
    Intent i;
    Button detailBtn, stopBtn;
    EditText detailText01, detailText02, detailText03;
    TextView seqNum;
    MyDB mydb;
    SQLiteDatabase sqlite;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakbundetail);

        mydb = new MyDB(this);
        detailBtn = (Button)findViewById(R.id.detailBtn);
        stopBtn = (Button)findViewById(R.id.stopBtn);

        detailText01 = (EditText)findViewById(R.id.detailText01);
        detailText02 = (EditText)findViewById(R.id.detailText02);
        detailText03 = (EditText)findViewById(R.id.detailText03);

        seqNum = (TextView)findViewById(R.id.seqNum);

        i = getIntent();
        final int _id = i.getExtras().getInt("_id");

        seqNum.setText("seqNum : " + _id);

        //단건조회
        getHakbunForCursorAdapter(_id);

        //데이터 수정
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = updateHakbun(_id);

                //받아온것을 다시 돌려줄것이다. 그리고 종료할것이다.
                i.putExtra("updateResult", flag);
                setResult(RESULT_OK, i); //인텐트호출의 결과를 넣어주겠다.
                finish();

            }
        });

        //인텐트 종료
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //인텐트종료
            }
        });
    }

    //데이터수정함수
    public boolean updateHakbun(int _id){
        String hakbun = detailText01.getText().toString();
        String name   = detailText02.getText().toString();
        String address = detailText03.getText().toString();
        String sql     = "UPDATE member set hakbun='"+hakbun+"', name='"+name+"', address='"+address+"' where _id='"+_id+"'";
        //UPDATE member set hakbun='1', name='2', address='3' where _id='1234'
        try{
            sqlite = mydb.getWritableDatabase();
            sqlite.execSQL(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //단건조회
    //커서가 어디까지 가서 끝낼건지 이런 동작은 startManagingCursor를 써서 자동으로 알아서하게해라, 위에꺼는 우리가 직접 while문 돌려줬음
    public void getHakbunForCursorAdapter(int _id){
        //데이터베이스 열고
        sqlite = mydb.getReadableDatabase(); //읽기만 가능한 속성, 왜냐햐면 조회는 수정이 필요가 없으니까
        String sql = "SELECT * FROM member where _id = " + _id; //*은 전부를 뜻한다, 이테이블의 전부를 가져와라.

        cursor = sqlite.rawQuery(sql, null); //인자가 두개가 들어감, 두번째꺼는 그냥 null로 넣어줌..커서가 널에 위치하고 있겠다. 몇번 째 raw 위치에 커서를 위치할 것인가

        String hakbunStr    = "";
        String nameStr      = "";
        String addressStr   = "";

        if(cursor.getCount() > 0){
            cursor.moveToNext(); //커서의 처음위치는 -1이기 때문에 0으로 옮겨줘야한다.
            hakbunStr = cursor.getString(1);
            nameStr   = cursor.getString(2);
            addressStr= cursor.getString(3);
        }

        detailText01.setText(hakbunStr);
        detailText02.setText(nameStr);
        detailText03.setText(addressStr);
        cursor.close();
        sqlite.close();
    }
}
