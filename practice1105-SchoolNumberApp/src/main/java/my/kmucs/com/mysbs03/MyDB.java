package my.kmucs.com.mysbs03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Koo on 2016-11-05.
 */

//내부클래스 생성
public class MyDB extends SQLiteOpenHelper {

    //생성자
    public MyDB(Context context){
        super(context, "sbsHakbun", null, 1); //1번버전을 만든다.
    }

    @Override //최초실행시
    public void onCreate(SQLiteDatabase db) {
        //최초 DB 만들때만 실행
        String sql = "CREATE TABLE member(_id INTEGER PRIMARY KEY AUTOINCREMENT, hakbun INTEGER, name CHAR(10), address CHAR(50))"; //table 이름 : member

        //sql실행
        db.execSQL(sql);

    }

    @Override //업데이트할때
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //버젼에 업그레이드가 있을때
        String sql = "DROP TABLE IF EXISTS member"; //만약 member이라는 테이블이 존재한다면 TABLE을 날려버려라!
        db.execSQL(sql);

        //날려버리고 새로 실행하는 것이 필요하다
        onCreate(db);
    }
}

