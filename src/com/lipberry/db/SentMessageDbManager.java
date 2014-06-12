package com.lipberry.db;

import java.util.ArrayList;
import java.util.List;

import com.lipberry.model.Article;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.LikeMember;
import com.lipberry.model.Member;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
public class SentMessageDbManager {
	private static final String TAG = ArticleDbManager.class.getSimpleName();
	private static String TABLE_EDUCATION_LIST = "sent_message";
	public static final String TABLE_PRIMARY_KEY = "_id";
	private static String message = "message";
	private static String from_id = "from_id";
	private static String subject = "subject";
	private static String message_url = "message_url";
	private static String message_id = "message_id";
	private static String thread_count = "thread_count";
	private static String from_username = "from_username";
	private static String from_nickname = "from_nickname";
	private static String read_flag = "read_flag";
	private static String to_nickname = "to_nickname";
	private static String article_title = "article_title";
	private static String child_flag = "child_flag";
	private static String from_avatar = "from_avatar";
	private  static String to_avatar="to_avatar";
	private static String article_url="article_url";
	private static String category_prefix="category_prefix";
	private static String article_id="article_id";
	private static String article_flag="article_flag";
	private static String to_id="to_id";
	private static String created_at="created_at";


	private static final String CREATE_TABLE_EDUCATION_LIST = "create table " + TABLE_EDUCATION_LIST + " ( "
			+ TABLE_PRIMARY_KEY + " integer primary key autoincrement, "
			+ message + " text, " 
			+ from_id + " text, "
			+ subject + " text, "
			+ message_url + " text, "
			+ message_id + " text, "
			+ thread_count + " text, "
			+ from_username + " text, "
			+ from_nickname + " text, "
			+ read_flag + " text, "
			+ to_nickname + " text, "
			+ article_title + " text, "
			+ child_flag + " text, "
			+ from_avatar + " text, "
			+ article_url + " text, "
			+ category_prefix + " text, "
			+ article_id + " text, "
			+ article_flag + " text, "
			+ to_id + " text, "
			+ created_at + " text);";
	public static long insert(SQLiteDatabase db, InboxMessage inmessage) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put(message , inmessage.getMessage());
		cv.put(from_id , inmessage.getFrom_id());
		cv.put(subject , inmessage.getSubject());
		cv.put(message_url , inmessage.getMessage_url());
		cv.put(message_id , inmessage.getMessage_id());
		cv.put(thread_count , inmessage.getThread_count());
		cv.put(from_username , inmessage.getFrom_username());
		cv.put(from_nickname , inmessage.getFrom_nickname());
		cv.put(read_flag , inmessage.getRead_flag());
		cv.put(to_nickname , inmessage.getTo_nickname());
		cv.put(article_title , inmessage.getArticle_Title());
		cv.put(child_flag , inmessage.getChild_flag());
		cv.put(from_avatar , inmessage.getFrom_avatar());
		cv.put(article_url , inmessage.getArticle_url());
		cv.put(category_prefix , inmessage.getCategory_prefix());
		cv.put(article_id , inmessage.getArticle_id());
		cv.put(article_flag , inmessage.getArticle_flag());
		cv.put(to_id , inmessage.getTo_id());
		cv.put(created_at , inmessage.getCreated_at());
		return db.insert(TABLE_EDUCATION_LIST, null, cv);
	}
	public static void createTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EDUCATION_LIST);
	}
	public static void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION_LIST);
	}
	public static List<InboxMessage> retrieve(SQLiteDatabase db) throws SQLException {
		List<InboxMessage> inboxlist =new ArrayList<InboxMessage>();
		Cursor c = db.query(TABLE_EDUCATION_LIST, null, null, null, null, null, null);
		if(c != null && c.getCount() > 0){
			c.moveToFirst();
			while(!c.isAfterLast()){
				String message_local = c.getString(c.getColumnIndex(message));
				String from_id_local = c.getString(c.getColumnIndex(from_id));
				String  subject_local = c.getString(c.getColumnIndex( subject));
				String message_url_local = c.getString(c.getColumnIndex(message_url));
				String message_id_local = c.getString(c.getColumnIndex(message_id));
				String thread_count_local = c.getString(c.getColumnIndex(thread_count));
				String from_username_local = c.getString(c.getColumnIndex(from_username));
				String from_nickname_local = c.getString(c.getColumnIndex(from_nickname));
				String read_flag_local = c.getString(c.getColumnIndex(read_flag));
				String to_nickname_local = c.getString(c.getColumnIndex(to_nickname));
				String article_title_local = c.getString(c.getColumnIndex(article_title));
				String child_flag_local = c.getString(c.getColumnIndex(child_flag));
				String from_avatar_local = c.getString(c.getColumnIndex(from_avatar));
				String article_url_local = c.getString(c.getColumnIndex(article_url));
				String category_prefix_local = c.getString(c.getColumnIndex(category_prefix));
				String article_id_local = c.getString(c.getColumnIndex(article_id));
				String article_flag_local = c.getString(c.getColumnIndex(article_flag));
				String to_id_local = c.getString(c.getColumnIndex(to_id));
				String created_at_local = c.getString(c.getColumnIndex(created_at));
				InboxMessage inmsz=new InboxMessage(message_local, from_id_local, subject_local, message_url_local,
						message_id_local, thread_count_local, from_username_local, from_nickname_local, read_flag_local, 
						to_nickname_local, article_title_local, child_flag_local, from_avatar_local, from_avatar_local, 
						article_url_local, category_prefix_local, article_id_local, article_flag_local, to_id_local, 
						created_at_local);
				inboxlist.add(inmsz);
				c.moveToNext();
			}
		}
		return inboxlist;
	}
	public static long update(SQLiteDatabase db, InboxMessage inmessage) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put(message , inmessage.getMessage());
		cv.put(from_id , inmessage.getFrom_id());
		cv.put(subject , inmessage.getSubject());
		cv.put(message_url , inmessage.getMessage_url());
		cv.put(message_id , inmessage.getMessage_id());
		cv.put(thread_count , inmessage.getThread_count());
		cv.put(from_username , inmessage.getFrom_username());
		cv.put(from_nickname , inmessage.getFrom_nickname());
		cv.put(read_flag , inmessage.getRead_flag());
		cv.put(to_nickname , inmessage.getTo_nickname());
		cv.put(article_title , inmessage.getArticle_Title());
		cv.put(child_flag , inmessage.getChild_flag());
		cv.put(from_avatar , inmessage.getFrom_avatar());
		cv.put(article_url , inmessage.getArticle_url());
		cv.put(category_prefix , inmessage.getCategory_prefix());
		cv.put(article_id , inmessage.getArticle_id());
		cv.put(article_flag , inmessage.getArticle_flag());
		cv.put(to_id , inmessage.getTo_id());
		cv.put(created_at , inmessage.getCreated_at());
		return db.update(TABLE_EDUCATION_LIST, cv, message_id + "=" + inmessage.getMessage_id(), null); 
	}
	public static boolean isExist(SQLiteDatabase db, String id) throws SQLException {
		boolean itemExist = false;
		Cursor c = db.query(TABLE_EDUCATION_LIST, null, message_id + "=" + id, null, null, null, null);
		if ((c != null) && (c.getCount() > 0)) {
			itemExist = true;
		}
		return itemExist;
	}
	public static void insertOrupdate(SQLiteDatabase db, InboxMessage inmessage){
		if(isExist(db, inmessage.getMessage_id())){
			update(db, inmessage);
		}
		else{
			insert(db, inmessage);
		}
	}
}
