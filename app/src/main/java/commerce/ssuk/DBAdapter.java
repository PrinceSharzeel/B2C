package commerce.ssuk;

/**
 * Created by princes on 25-May-17.
 */
     import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
     import android.widget.Toast;

public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TIME="time";

    public static final String KEY_QUANTITY= "quantity";

    public static final String KEY_CONTACT= "contact";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "SSUK";
    private static final String DATABASE_TABLE = "Cart";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE =
            "create table "+DATABASE_TABLE+" (_id integer primary key autoincrement, "
                    + "name text,email text not null,quantity integer not null,time text not null,contact text not null);";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }
    }


    public void DeleteTable()
    {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
    }


    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    public void Init()
    {
        DBHelper.close();
    }


    //---insert a contact into the database---
    public long insertCart(String name, String email,String quantiy,String time,String contact)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_QUANTITY,quantiy);
        initialValues.put(KEY_TIME,time);

        initialValues.put(KEY_CONTACT,contact);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }


    //---deletes a particular contact---
    public boolean deleteContact(String name,String contact)
    {
        return db.delete(DATABASE_TABLE,"name=? AND contact=?",new String[] {name,contact}) > 0;

    }
    public boolean deleteAllItems(String contact)
    {
        return db.delete(DATABASE_TABLE,"contact=?",new String[] {contact}) > 0;



    }
    public void deleteAll()
    {
        db.execSQL("delete from "+ DATABASE_TABLE);



    }

    //---retrieves all the contacts---
    public Cursor getAllCart
    (String contact)
    {
        //return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,KEY_EMAIL,KEY_QUANTITY,KEY_TIME,KEY_CONTACT},
           //     null, null, null, null, null,null);

        String query = "select * from " + DATABASE_TABLE + " where "+ KEY_CONTACT + " = '" + contact + "'";

        Cursor cur =db.rawQuery(query, null);
        return cur;


    }


    //---retrieves a particular contact---
    public String checkItem(String name,String contact) throws SQLException
    {

        String query = "select * from "+DATABASE_TABLE+" where name=\""+ name + "\" AND contact=\""+contact+"\"";

        Cursor cs =db.rawQuery(query, null);
        if(cs.getCount()<=0){Log.e("newnull","cursornull");cs.close();  return "true";}
         cs.moveToFirst();
        String a=cs.getString(3);
        cs.close();
        return a;

    }

/*
    public Cursor ItemCount()throws  SQLException
    {
        //String qcount="SELECT "+KEY_NAME+"+ , COUNT(*) FROM "+DATABASE_TABLE+" GROUP BY "+KEY_NAME+";";

       String qount="SELECT\n" +
               "\tname,\n" +
               "\tcount(*)\n" +
               "FROM\n" +
               "\tCart\n" +
               "GROUP BY\n" +
               "\tname;";
        return db.rawQuery(qount,null);



    }*/


    //---updates a contact---
    public boolean updateCart(String name,String quantity,String time,String contact)
    {
        ContentValues args = new ContentValues();


        args.put(KEY_QUANTITY, quantity);
        args.put(KEY_TIME,time);
       return db.update(DATABASE_TABLE,
                args,
                KEY_NAME + " = ? AND " + KEY_CONTACT + " = ?",
                new String[]{name, contact})>0;
        //return db.update(DATABASE_TABLE, args, KEY_NAME +"='" + name+"'", null) > 0;
    }


    public int NumberOfItems(String contact)
    {  int i=0; try{
        Cursor c = db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,KEY_EMAIL,KEY_QUANTITY,KEY_TIME},


                null, null, null, null, null,null);
        String query = "select * from " + DATABASE_TABLE + " where "+ KEY_CONTACT + " = '" + contact + "'";

        Cursor cur =db.rawQuery(query, null);
        if (cur.moveToFirst())
        {
            do {

              //  Item it =new Item(c.getString(1),"Rs."+c.getString(2),R.drawable.car1);

                //db.deleteContact(Integer.parseInt(c.getString(0)));
                i++;




            } while (cur.moveToNext());
        }}
    catch(Exception e){ return 0;}
       return i;

    }
}