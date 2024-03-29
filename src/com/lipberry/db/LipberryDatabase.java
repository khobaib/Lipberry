package com.lipberry.db;

import java.util.ArrayList;
import java.util.List;

import com.lipberry.model.Article;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.LikeMember;
import com.lipberry.model.TndividualThreadMessage;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class LipberryDatabase {
	private static final String TAG = LipberryDatabase.class.getSimpleName();
	private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;
    private static final String DATABASE_NAME = "lipberry_db2";
    private static final int DATABASE_VERSION = 3;
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	ThreadInboxMessageDbManager.createTable(db);

        	SentMessageDbManager.createTable(db);
        	InboxDbManager.createTable(db);
            ArticleDbManager.createTable(db);
            LikedMemberDbManager.createTable(db);
            ArticleDbManagerFollowing.createTable(db);
            LikedMemberDbManagerFollowing.createTable(db);
         }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	ThreadInboxMessageDbManager.dropTable(db);

        	SentMessageDbManager.dropTable(db);
        	InboxDbManager.dropTable(db);
            ArticleDbManager.dropTable(db);
            LikedMemberDbManager.dropTable(db);
            ArticleDbManagerFollowing.dropTable(db);
            LikedMemberDbManagerFollowing.dropTable(db);
            onCreate(db);
        }
    }
    public LipberryDatabase(Context ctx) {
        mContext = ctx;
    }
    public LipberryDatabase open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }
    public void droptableSentMessageDbManager() {
    	
    	SentMessageDbManager.dropTable(this.db);
    
    }
    public void createtableThreadMessage() {
    	ThreadInboxMessageDbManager.createTable(db);
    }
    public void createtableSentMessageDbManager() {
    	SentMessageDbManager.createTable(db);
    }
    public void droptableInboxDbManager() {
    	
    	InboxDbManager.dropTable(this.db);
    
    }
    public void createtableInboxDbManager() {
    	InboxDbManager.createTable(db);
    }
    public void droptableLikemember() {
    	LikedMemberDbManager.dropTable(this.db);
    }
    public void droptableArticle() {
    	ArticleDbManager.dropTable(this.db);
    }
    public void droptableLikememberfollowing() {
    	LikedMemberDbManagerFollowing.dropTable(this.db);
    }
    
    public void droptableArticlefollowing() {
    	ArticleDbManagerFollowing.dropTable(this.db);
    }
    
    public void insertOrUpdateThreadMessageInboxList(ArrayList<TndividualThreadMessage>threadinblist) {
    	for (int i=0;i<threadinblist.size();i++){
    		insertOrUpdateThreadInboxMessage(threadinblist.get(i));
    	}
    	
    }
    public void insertOrUpdateThreadInboxMessage(TndividualThreadMessage inboxdbmanager) {
    	ThreadInboxMessageDbManager.insertOrupdate(this.db, inboxdbmanager);
    	//InboxDbManager.insertOrupdate(this.db, inboxdbmanager);
    }
    public List<TndividualThreadMessage> retrieveThreadInboxtMessage() {
     	 return ThreadInboxMessageDbManager.retrieve(this.db);
     }
    
    
    
    public void insertOrUpdateSentMessageList(ArrayList<InboxMessage>inblist) {
    	for (int i=0;i<inblist.size();i++){
    		insertOrUpdateSentMessage(inblist.get(i));
    	}
    }
    public void insertOrUpdateInboxMessageList(ArrayList<InboxMessage>inblist) {
    	for (int i=0;i<inblist.size();i++){
    		insertOrUpdateInboxMessage(inblist.get(i));
    	}
    	
    }
    public void insertOrUpdateSentMessage(InboxMessage inboxdbmanager) {
    	SentMessageDbManager.insertOrupdate(this.db, inboxdbmanager);
    	//InboxDbManager.insertOrupdate(this.db, inboxdbmanager);
    }
    public void insertOrUpdateInboxMessage(InboxMessage inboxdbmanager) {
    	InboxDbManager.insertOrupdate(this.db, inboxdbmanager);
    	//InboxDbManager.insertOrupdate(this.db, inboxdbmanager);
    }
    public List<InboxMessage> retrieveSentMessage() {
      	 return SentMessageDbManager.retrieve(this.db);
      }
    public List<InboxMessage> retrieveInboxMessage() {
   	 return InboxDbManager.retrieve(this.db);
   }
     public void insertOrUpdateArticlefollowing(Article article) {
    	for(int i=0;i<article.getLikedmemberlist().size();i++){
    		insertOrUpdateLikememberfollowing(article.getLikedmemberlist().get(i));
    	}
        ArticleDbManagerFollowing.insertOrupdate(this.db, article);
    }
    public void insertOrUpdateLikememberfollowing(LikeMember likeMember) {
    	LikedMemberDbManagerFollowing.insertOrupdate(this.db, likeMember);
    }
    public List<LikeMember> retrieveLikeMemberListfollowing() {
    	 return LikedMemberDbManagerFollowing.retrieve(this.db);
    }
    public List<Article> retrieveArticleListfollowing() {
    	List< LikeMember>likeMemberslist=retrieveLikeMemberListfollowing();
    	return  ArticleDbManagerFollowing.retrieve(this.db,likeMemberslist);
    }
    public void insertOrUpdateArticle(Article article) {
    	for(int i=0;i<article.getLikedmemberlist().size();i++){
    		insertOrUpdateLikemember(article.getLikedmemberlist().get(i));
    	}
        ArticleDbManager.insertOrupdate(this.db, article);
    }
    public void insertOrUpdateLikemember(LikeMember likeMember) {
    	LikedMemberDbManager.insertOrupdate(this.db, likeMember);
    }
    public List<LikeMember> retrieveLikeMemberList() {
    	 return LikedMemberDbManager.retrieve(this.db);
    }
    public List<Article> retrieveArticleList() {
    	List< LikeMember>likeMemberslist=retrieveLikeMemberList();
        return ArticleDbManager.retrieve(this.db,likeMemberslist);
    }
    
    
}
