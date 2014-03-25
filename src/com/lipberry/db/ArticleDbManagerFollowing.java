package com.lipberry.db;

import java.util.ArrayList;
import java.util.List;

import com.lipberry.model.Article;
import com.lipberry.model.LikeMember;
import com.lipberry.model.Member;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;



public class ArticleDbManagerFollowing {
    
    private static final String TAG = ArticleDbManager.class.getSimpleName();
    
    private static String TABLE_EDUCATION_LIST = "article_list_table_follow";
    public static final String TABLE_PRIMARY_KEY = "_id";
    private static String article_id = "article_id";
    private static String article_photo = "article_photo";
    private static String article_url = "article_url";
    private static String article_title = "article_title";
    private static String article_description = "article_description";
    private static String like_count = "like_count";
    private static String comment_count = "comment_count";
    private static String category_name = "category_name";
    private static String like_url = "like_url";
    private static String dislike_url = "dislike_url";
    private static String comment_url = "comment_url";
    private static String member_id = "member_id";
    private static String member_photo = "member_photo";
    private static final String CREATE_TABLE_EDUCATION_LIST = "create table " + TABLE_EDUCATION_LIST + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, "
    		+ article_id + " text, " 
            + article_photo + " text, "
            
            + article_url + " text, "
            + article_title + " text, "
            + article_description + " text, "
            + like_count + " text, "
            + comment_count + " text, "
            + like_url + " text, "
            + dislike_url + " text, "
            + comment_url + " text, "
              + member_id + " text, "
            + member_photo + " text, "
            + category_name + " text);";
    
  
    public static long insert(SQLiteDatabase db, Article article) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(article_id , article.getArticle_id());
        cv.put(article_photo , article.getArticle_photo());
        cv.put(article_url , article.getArticle_url());
        cv.put(article_title , article.getArticle_title());
        cv.put(article_description , article.getArticle_description());
        cv.put(like_count , article.getLike_count());
        cv.put(comment_count , article.getComment_count());
        cv.put(like_url , article.getLike_url());
        cv.put(dislike_url , article.getDislike_url());
        cv.put(comment_url , article.getComment_url());
        cv.put(member_id , article.getMember_id());
        cv.put(member_photo , article.getMember_photo());
     //   cv.put(category_name , article.getCategory_name());
        return db.insert(TABLE_EDUCATION_LIST, null, cv);
    }
    
    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EDUCATION_LIST);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION_LIST);
    }
    
    
   
    
    public static List<Article> retrieve(SQLiteDatabase db,List<LikeMember>likeMemberslistAll) throws SQLException {
        List<Article> articleList = new ArrayList<Article>();
        
        Cursor c = db.query(TABLE_EDUCATION_LIST, null, null, null, null, null, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            while(!c.isAfterLast()){
               
            	String article_id_local = c.getString(c.getColumnIndex(article_id));
            	String article_photo_local = c.getString(c.getColumnIndex(article_photo));
            	String  article_url_local = c.getString(c.getColumnIndex( article_url));
            	String article_title_local = c.getString(c.getColumnIndex(article_title));
            	String article_description_local = c.getString(c.getColumnIndex(article_description));
            	String like_count_local = c.getString(c.getColumnIndex(like_count));
            	String comment_count_local = c.getString(c.getColumnIndex(comment_count));
            	String category_name_local = c.getString(c.getColumnIndex(category_name));

            	String like_url_local = c.getString(c.getColumnIndex(like_url));
            	String dislike_url_local = c.getString(c.getColumnIndex(dislike_url));
            	String comment_url_local = c.getString(c.getColumnIndex(comment_url));
            	
            	
            	String member_id_local = c.getString(c.getColumnIndex(member_id));
            	String member_photo_local = c.getString(c.getColumnIndex(member_photo));
            	
            	
            	ArrayList<LikeMember>likedmemberlist=new ArrayList<LikeMember>();
            	likedmemberlist.clear();
            	for(int i=0;i<likeMemberslistAll.size();i++){
            		if(likeMemberslistAll.get(i).getForeign_key_article_id().equals(article_id_local)){
            		likedmemberlist.add(likeMemberslistAll.get(i))	;
            		}
            		
            	}
            	
                Article article=new Article(likedmemberlist, article_id_local, article_photo_local, article_url_local, article_title_local,
                		article_description_local, like_count_local, comment_count_local,like_url_local,dislike_url_local,
                		comment_url_local,member_id_local,member_photo_local);
            	articleList.add(article);
                c.moveToNext();
            }
        }
        return articleList;
        
    }
    
    
    public static long update(SQLiteDatabase db, Article article) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(article_photo , article.getArticle_photo());
        cv.put(article_url , article.getArticle_url());
        cv.put(article_title , article.getArticle_title());
        cv.put(article_description , article.getArticle_description());
        cv.put(like_count , article.getLike_count());
        cv.put(comment_count , article.getComment_count());
        cv.put(like_url , article.getLike_url());
        cv.put(dislike_url , article.getDislike_url());
        cv.put(comment_url , article.getComment_url());
        cv.put(member_id , article.getMember_id());
        cv.put(member_photo , article.getMember_photo());
     //   cv.put(category_name , article.getCategory_name());
       // cv.put(category_name , article.getCategory_name());
        
        return db.update(TABLE_EDUCATION_LIST, cv, article_id + "=" + article.getArticle_id(), null); 
    }
    
    
    public static boolean isExist(SQLiteDatabase db, String id) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_EDUCATION_LIST, null, article_id + "=" + id, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        return itemExist;
    }
    
    
    public static void insertOrupdate(SQLiteDatabase db, Article article){
        if(isExist(db, article.getArticle_id())){
            update(db, article);
        }
        else{
            insert(db, article);
        }
    }
    
  

}
