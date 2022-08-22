package tk.yommail.ecrt.Misc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "items";
    private static final String ID_COLUMN = "ID";
    private static final String TITLE_COLUMN = "title";
    private static final String PRICE_COLUMN = "price";
    private static final String AMOUNT_COLUMN = "amount";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (" + ID_COLUMN + " INTEGER PRIMARY KEY, " +
                TITLE_COLUMN + " TEXT, " +
                PRICE_COLUMN + " DOUBLE(10, 2), " +
                AMOUNT_COLUMN + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addItem (int id, String title, double price, int amount) {
        int amountInDatabase = getAmountInDatabase(id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result;

        if (amountInDatabase != 0) {
            contentValues.put(AMOUNT_COLUMN, amountInDatabase + amount);
            result = db.update(TABLE_NAME, contentValues, ID_COLUMN + " = " + id, null);
        } else {
            contentValues.put(ID_COLUMN, id);
            contentValues.put(TITLE_COLUMN, title);
            contentValues.put(PRICE_COLUMN, price);
            contentValues.put(AMOUNT_COLUMN, amount);
            result = db.insert(TABLE_NAME, null, contentValues);
        }

        return result != -1;
    }

    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, TITLE_COLUMN + " = ?", new String[]{task});
        db.close();
    }

    public int getAmountInDatabase (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + AMOUNT_COLUMN + " FROM " + TABLE_NAME +
                " WHERE " + ID_COLUMN + "=" + id, null);
        int amount = 0;
        while (cursor.moveToNext()) {
            amount += cursor.getInt(cursor.getColumnIndex(AMOUNT_COLUMN));
        }
        cursor.close();
        db.close();
        return amount;
    }

    public ArrayList<Item> getItems () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Item> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            items.add(new Item(
                    cursor.getInt(cursor.getColumnIndex(ID_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(TITLE_COLUMN)),
                    cursor.getDouble(cursor.getColumnIndex(PRICE_COLUMN)),
                    cursor.getInt(cursor.getColumnIndex(AMOUNT_COLUMN))
            ));
        }
        cursor.close();
        db.close();
        return items;
    }

    public double getItemsSubtotal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PRICE_COLUMN + ", " + AMOUNT_COLUMN + " FROM " + TABLE_NAME, null);
        double subtotal = 0.0;
        while (cursor.moveToNext()) {
            double price = cursor.getDouble(cursor.getColumnIndex(PRICE_COLUMN));
            int amount = cursor.getInt(cursor.getColumnIndex(AMOUNT_COLUMN));

            subtotal = Math.floor((double) amount * price * 100) / 100;
        }
        return subtotal;
    }

    public void clearItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }
}
