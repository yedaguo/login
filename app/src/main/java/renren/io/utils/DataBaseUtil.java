package renren.io.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseUtil extends SQLiteOpenHelper {
    private  Context context;

    private static final String Substitute= "create table Substitute(" +
            "sid integer primary key autoincrement,"+"id integer"+"billingDate text,"+"paymentDate text,"+"signature text,"+"paymentStatus integer,"+"orderNum text,"
            +"sectionNumber text,"+"tranches text,"+"employeeId text,"+"basicWage real,"+"chargeunitPrice real,"+"num integer,"+"personnelName text,"+"personnelPic text,"
            +"idCard text,"+"payPic text,"+"createDate text,"+"updateDate text,"+"createUser integer)";

    public DataBaseUtil (Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        this.context = context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
