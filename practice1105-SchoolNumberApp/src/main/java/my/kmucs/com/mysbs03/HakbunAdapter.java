package my.kmucs.com.mysbs03;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Koo on 2016-11-05.
 */

public class HakbunAdapter extends CursorAdapter {
    //생성자(필수)
    public HakbunAdapter(Context context, Cursor c) {
        super(context, c);
    }

    //커서가 가르키는 데이터를 보여줄 새로운 뷰를 만들어서 리턴하는 함수
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);//xml이 교체되서 새로운게 나타나는 것을 뿌려서(전개해서) 보여주겠다. :LayoutInflater
        View v = inflater.inflate(R.layout.activity_row, parent, false); //여기서 parent는 Listview인가..? 나를 감싸고 있는 부모 , row xml을 parent의 위에 보이게해주는 그런거?

        return v;
    }

    //뷰에 커서가 가르키는 데이터를 대입하는 기능
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvHakbun = (TextView)view.findViewById(R.id.tvHakbun); //view.find...해주는 이유는 이 클래스는 Activity를 extends하는게 아니라서그럼
        TextView tvName = (TextView)view.findViewById(R.id.tvName);
        TextView tvAddress = (TextView)view.findViewById(R.id.tvAddress);

        //데이터베이스에서 연결아직안됌
        String hakbun = cursor.getString(cursor.getColumnIndex("hakbun"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String address = cursor.getString(cursor.getColumnIndex("address"));
        int _id = cursor.getInt(cursor.getColumnIndex("_id"));

        Log.d("스트링 확인 : ", hakbun + ", " + name + ", " + address);

        tvHakbun.setText("학번 : " + hakbun);
        tvName.setText("이름 : " + name);
        tvAddress.setText("주소 : " + address);
        view.setTag(_id);

    }
}
