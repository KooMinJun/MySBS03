package my.kmucs.com.mysbs03;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Koo on 2016-11-05.
 */

public class HakbunListViewActivity extends Activity {
    Button closeBtn;
    ListView hakList01;
    MyDB mydb;
    SQLiteDatabase sqlite;
    HakbunAdapter hakbunAdapter;
    Cursor cursor;
    Intent i;

    //ListView 넣으려고
    ArrayList<String> arrlist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        closeBtn = (Button)findViewById(R.id.sBtn);
        hakList01 = (ListView)findViewById(R.id.List01);

        arrlist = new ArrayList<String>();

        //데이터베이스 연결
        mydb = new MyDB(this);

        i = new Intent(this, HakbunDetailActivity.class);

        //arrlist에 데이터베이스 파일들을 읽어서 대입, 데이터 셋팅
        getHakbunForCursorAdapter();
        /*여기서부턴 이제 HakbunAdapter activity를 만든후에 사용안함
        //getHankbun();
        //데이터베이스에서 가져온 객체를 장착
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrlist);
        hakList01.setAdapter(adapter);
        */

        //listview 클릭 시 이벤트 구현
        hakList01.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position", position + "");
                Log.d("id", id+"");

                //final int selectId = (int)id;
                final int _id = (int)view.getTag(); //tag를 이용해서 삭제위치를 정하자

                //팝업창
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(HakbunListViewActivity.this); //얘가 어디 나와야해, 이건데 여기서는 HakbunListViewActivity 화면에서 나오게 하는 것이다.
                alertDlg.setTitle(R.string.alert_title_question);
                alertDlg.setMessage(R.string.alert_msg_delete);

                //positive버튼
                alertDlg.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("긍정버튼", which+"");
                        deleteHakbun(_id);
                        dialog.dismiss(); //dialog종료
                        refresh(); //reload

                    }
                });
                //negative버튼
                alertDlg.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("부정버튼", which+"");
                        i.putExtra("_id",_id);
                        //startActivity(i);

                        //intent를 전달한 곳에서 성공적으로 저장하고 종료했다. 그때 실행
                        //조심할 점은 데이터를 리턴받을 꺼라는 가정하에서 사용 (여기선 HakbunDetailActivity에서 setResult와 짝을 이룸)
                        startActivityForResult(i, 1);
                        dialog.dismiss(); //dialog종료
                    }
                });

                alertDlg.show();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //액티비티 종료
            }
        });

    }

    //편집화면에서 삭제버튼 누를때 사용할 함수
    public void deleteHakbun(int _id){
        sqlite = mydb.getWritableDatabase();
        String sql = "DELETE FROM member where _id = " + _id;
        sqlite.execSQL(sql);
        sqlite.close();
    }

    //reload되는 함수
    private void refresh(){
        getHakbunForCursorAdapter();
    }

    //select 조회하기
    public void getHankbun(){
        //데이터베이스 열고
        sqlite = mydb.getReadableDatabase(); //읽기만 가능한 속성, 왜냐햐면 조회는 수정이 필요가 없으니까
        String sql = "SELECT * FROM member"; //*은 전부를 뜻한다, 이테이블의 전부를 가져와라.
        cursor = sqlite.rawQuery(sql, null); //인자가 두개가 들어감, 두번째꺼는 그냥 null로 넣어줌..커서가 널에 위치하고 있겠다. 몇번 째 raw 위치에 커서를 위치할 것인가

        while(cursor.moveToNext()){ //다음으로 갈수 있는 커서가 있다면
            arrlist.add(cursor.getString(1)); // 0: hakbun, 1:name, 2:address -> 여기서는 이름 가져오겠다는 것이다.
        }

        cursor.close(); //커서 닫아줘야함
        //데이터베이스 닫아줌
        sqlite.close();
    }


    //커서가 어디까지 가서 끝낼건지 이런 동작은 startManagingCursor를 써서 자동으로 알아서하게해라, 위에꺼는 우리가 직접 while문 돌려줬음
    public void getHakbunForCursorAdapter(){
        //데이터베이스 열고
        sqlite = mydb.getReadableDatabase(); //읽기만 가능한 속성, 왜냐햐면 조회는 수정이 필요가 없으니까
        String sql = "SELECT * FROM member"; //*은 전부를 뜻한다, 이테이블의 전부를 가져와라.

        cursor = sqlite.rawQuery(sql, null); //인자가 두개가 들어감, 두번째꺼는 그냥 null로 넣어줌..커서가 널에 위치하고 있겠다. 몇번 째 raw 위치에 커서를 위치할 것인가

        if(cursor.getCount() > 0){
            startManagingCursor(cursor); //cursor의 관리를 시작해라.
            hakbunAdapter = new HakbunAdapter(this, cursor);
            hakList01.setAdapter(hakbunAdapter);
        }
    }

    //어플 종료시 생명주기 끝내려고 만든함수
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        sqlite.close();
    }

    //startActivityforResult와 쌍을이룸
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //startActivityForResult 사용 시 requestCode로 분기
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                if(data.getBooleanExtra("updateResult", false)){
                    //updateResult에 값이 안담겨올경우 false이다.
                    Log.d("update >>>>>>", "성공");
                }
                else{
                    Log.d("update >>>>>", "실패");
                }
            }
        }
        else if(requestCode == 2){
            //여러개일 경우
        }
        else if(requestCode == 3){
            //여러개일 경우
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

