package com.lipberry.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lipberry.model.LikeMember;
public class LikedMemberDbManagerFollowing {
	private static final String TAG = ArticleDbManager.class.getSimpleName();
	private static String TABLE_EDUCATION_LIST = "liked_member_list_table_follow";
	public static final String TABLE_PRIMARY_KEY = "_id";
	private static String foreign_key_article_id = "foreign_key_article_id";
	private static String nickname = "nickname";
	private static String member_url = "member_url";
	private static final String CREATE_TABLE_EDUCATION_LIST = "create table " + TABLE_EDUCATION_LIST + " ( "
			+ TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + foreign_key_article_id + " text, " 
			+ nickname + " text , " + member_url + " text);";
	public static long insert(SQLiteDatabase db, LikeMember likeMember) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put(foreign_key_article_id , likeMember.getForeign_key_article_id());
		cv.put(nickname , likeMember.getNickname());
		cv.put(member_url , likeMember.getMemberUrl());
		return db.insert(TABLE_EDUCATION_LIST, null, cv);
	}
	public static void createTable(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EDUCATION_LIST);
	}
	public static void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION_LIST);
	}
	public static List<LikeMember> retrieve(SQLiteDatabase db) throws SQLException {
		List<LikeMember> likeMemberslist = new ArrayList<LikeMember>();
		Cursor c = db.query(TABLE_EDUCATION_LIST, null, null, null, null, null, null);
		if(c != null && c.getCount() > 0){
			c.moveToFirst();
			while(!c.isAfterLast()){
				String foreign_key_article_id_local = c.getString(c.getColumnIndex(foreign_key_article_id));
				String nickname_local = c.getString(c.getColumnIndex(nickname));
				String  member_url_local = c.getString(c.getColumnIndex( member_url));
				LikeMember likemember=new LikeMember(nickname_local, member_url_local, foreign_key_article_id_local);
				likeMemberslist.add(likemember);
				c.moveToNext();
			}
		}
		c.close();
		return likeMemberslist;
	}
	public static long update(SQLiteDatabase db, LikeMember likeMember) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put(foreign_key_article_id , likeMember.getForeign_key_article_id());
		cv.put(nickname , likeMember.getNickname());
		cv.put(member_url ,likeMember.getMemberUrl());
		String[] whereArgs = new String[] {likeMember.getNickname()};
		return db.update(TABLE_EDUCATION_LIST, cv, nickname + "=?", whereArgs); 
	}
	public static boolean isExist(SQLiteDatabase db, String url) throws SQLException {
		boolean itemExist = false;
		Log.i("memberurl", member_url);
		String[] whereArgs = new String[] { url };
		Cursor c = db.query(TABLE_EDUCATION_LIST,null,nickname + "=?" , whereArgs, null, null, null);
		if ((c != null) && (c.getCount() > 0)) {
			itemExist = true;
		}
		c.close();
		return itemExist;
	}
	public static void insertOrupdate(SQLiteDatabase db, LikeMember likeMember){
		if(isExist(db, likeMember.getNickname())){
			update(db, likeMember);
		}
		else{
			insert(db, likeMember);
		}
	}

}
