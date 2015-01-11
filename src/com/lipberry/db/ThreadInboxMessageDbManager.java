package com.lipberry.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.lipberry.model.TndividualThreadMessage;
public class ThreadInboxMessageDbManager {
	private static final String TAG = ArticleDbManager.class.getSimpleName();
	private static String TABLE_EDUCATION_LIST = "thread_inbox";
	
	public static final String TABLE_PRIMARY_KEY = "_id";
	
	private static String parent_id="parent_id",article_title="article_title",subject="subject",to_nickname="to_nickname",
			child_flag="child_flag",from_nickname="from_nickname",article_url="article_url",read_flag="read_flag",message="message",id="id",
	category_prefix="category_prefix",article_id="article_id",article_flag="article_flag",to_id="to_id",
	from_id="from_id",created_at="created_at",from_username="from_username",from_avatar="from_avatar",
	to_avatar="to_avatar";


	private static final String CREATE_TABLE_EDUCATION_LIST = "create table " + TABLE_EDUCATION_LIST + " ( "
			+ TABLE_PRIMARY_KEY + " integer primary key autoincrement, "
			+ parent_id + " text, " 
			+ article_title + " text, " 
			+ subject + " text, "
			+ to_nickname + " text, "
			+ child_flag + " text, "
			+ from_nickname + " text, "
			+ article_url + " text, "
			+ read_flag + " text, "
			+ message + " text, "
			+ id + " text, "
			+ category_prefix + " text, "
			+ article_id + " text, "
			+ article_flag + " text, "
			+ to_id + " text, "
			+ from_id + " text, "
			+ from_username + " text, "
			+ from_avatar + " text, "
			+ to_avatar + " text, "
			+ created_at + " text);";
	public static long insert(SQLiteDatabase db, TndividualThreadMessage inmessage) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put(parent_id , inmessage.getParent_id());
		cv.put(article_title , inmessage.getArticle_title());
		cv.put(subject , inmessage.getSubject());
		cv.put(to_nickname , inmessage.getTo_nickname());
		cv.put(child_flag , inmessage.getChild_flag());
		cv.put(from_nickname , inmessage.getFrom_nickname());
		cv.put(article_url , inmessage.getArticle_url());
		cv.put(read_flag , inmessage.getRead_flag());
		cv.put(category_prefix , inmessage.getCategory_prefix());
		cv.put(message , inmessage.getMessage());
		cv.put(id , inmessage.getId());
		cv.put(from_id , inmessage.getFrom_id());
		cv.put(from_username , inmessage.getFrom_username());
		cv.put(article_flag , inmessage.getArticle_flag());
		cv.put(to_id , inmessage.getTo_id());
		cv.put(article_id , inmessage.getArticle_id());
		cv.put(to_avatar , inmessage.getTo_avatar());
		cv.put(from_avatar , inmessage.getFrom_avatar());
		cv.put(created_at , inmessage.getCreated_at());
		return db.insert(TABLE_EDUCATION_LIST, null, cv);
	}
	public static void createTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EDUCATION_LIST);
	}
	public static void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION_LIST);
	}
	public static List<TndividualThreadMessage> retrieve(SQLiteDatabase db) throws SQLException {
		List<TndividualThreadMessage> thradlist = new ArrayList<TndividualThreadMessage>();
		Cursor c = db.query(TABLE_EDUCATION_LIST, null, null, null, null, null, null);
		if(c != null && c.getCount() > 0){
			c.moveToFirst();
			while(!c.isAfterLast()){
				String parent_id_local = c.getString(c.getColumnIndex(parent_id));

				String article_title_local = c.getString(c.getColumnIndex(article_title));
				String subject_local = c.getString(c.getColumnIndex(subject));
				String  to_nickname_local = c.getString(c.getColumnIndex( to_nickname));
				String from_nickname_local = c.getString(c.getColumnIndex(from_nickname));
				String article_url_local = c.getString(c.getColumnIndex(article_url));
				String read_flag_local = c.getString(c.getColumnIndex(read_flag));
				String message_local = c.getString(c.getColumnIndex(message));
				String id_local = c.getString(c.getColumnIndex(id));
				String category_prefix_local = c.getString(c.getColumnIndex(category_prefix));
				String article_id_local = c.getString(c.getColumnIndex(article_id));
				String article_flag_local = c.getString(c.getColumnIndex(article_flag));
				String to_id_local = c.getString(c.getColumnIndex(to_id));
				String from_id_local = c.getString(c.getColumnIndex(from_id));
				String from_username_local = c.getString(c.getColumnIndex(from_username));

				String to_avatar_local = c.getString(c.getColumnIndex(to_avatar));
				String from_avatar_local = c.getString(c.getColumnIndex(from_avatar));
				String created_at_local = c.getString(c.getColumnIndex(created_at));
				TndividualThreadMessage thrdmsz=new TndividualThreadMessage(parent_id_local,article_title_local, subject_local, to_nickname_local,
						read_flag_local, from_nickname_local, article_url_local, read_flag_local, message_local, id_local,
						category_prefix_local, article_id_local, article_flag_local, to_id_local, from_id_local, created_at_local,
						from_username_local, from_avatar_local, to_avatar_local);
				thradlist.add(thrdmsz);
				c.moveToNext();
			}
		}c.close();
		return thradlist;
	}
	
	public static long update(SQLiteDatabase db,  TndividualThreadMessage inmessage) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put(parent_id , inmessage.getParent_id());
		cv.put(article_title , inmessage.getArticle_title());
		cv.put(subject , inmessage.getSubject());
		cv.put(to_nickname , inmessage.getTo_nickname());
		cv.put(child_flag , inmessage.getChild_flag());
		cv.put(from_nickname , inmessage.getFrom_nickname());
		cv.put(article_url , inmessage.getArticle_url());
		cv.put(read_flag , inmessage.getRead_flag());
		cv.put(category_prefix , inmessage.getCategory_prefix());
		cv.put(message , inmessage.getMessage());
		cv.put(id , inmessage.getId());
		cv.put(from_id , inmessage.getFrom_id());
		cv.put(from_username , inmessage.getFrom_username());
		cv.put(article_flag , inmessage.getArticle_flag());
		cv.put(to_id , inmessage.getTo_id());
		cv.put(article_id , inmessage.getArticle_id());
		cv.put(to_avatar , inmessage.getTo_avatar());
		cv.put(from_avatar , inmessage.getFrom_avatar());
		cv.put(created_at , inmessage.getCreated_at());
		String[] whereArgs = new String[] { inmessage.getId() };
		return db.update(TABLE_EDUCATION_LIST, cv, id + "=?", whereArgs); 
	}


	public static boolean isExist(SQLiteDatabase db, String url) throws SQLException {
		boolean itemExist = false;
		String[] whereArgs = new String[] {url};
		Cursor c = db.query(TABLE_EDUCATION_LIST,null,id + "=?" , whereArgs, null, null, null);
		if ((c != null) && (c.getCount() > 0)) {
			itemExist = true;
		}
		c.close();
		return itemExist;
	}


	public static void insertOrupdate(SQLiteDatabase db, TndividualThreadMessage threadmessage){
		if(isExist(db, threadmessage.getId())){
			update(db, threadmessage);
		}
		else{
			insert(db, threadmessage);
		}
	}
}
