package sec03.brd01;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dataFactory;
	Connection conn;
	PreparedStatement pstmt;

	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/MyDB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List selectAllArticles() {
		List articlesList = new ArrayList();
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT LEVEL,articleNO,parentNO,title,content,id,writeDate" 
			             + " from t_board"
					     + " START WITH  parentNO=0" + " CONNECT BY PRIOR articleNO=parentNO"
					     + " ORDER SIBLINGS BY articleNO DESC";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("level");	//각 글에 level 속성 저장
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO article = new ArticleVO();	//글 정보를 ArticleVO 속성에 설정
				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				articlesList.add(article);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articlesList;
		
	}
	
	private int getNewArticleNO() {
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT  max(articleNO) from t_board ";	//기본 글 번호 중 가장 큰 번호 조회
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(query);
			if (rs.next())
				return (rs.getInt(1) + 1);	//가장 큰 번호에 1을 더해 반환
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int insertNewArticle(ArticleVO article) {
		int articleNO = getNewArticleNO();	//새글을 추가하기 전에 새 글에 대한 번호를 가져옴
		
		try {
			conn = dataFactory.getConnection();
			int parentNO = article.getParentNO();
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();
			String query = "INSERT INTO t_board (articleNO, parentNO, title, content, imageFileName, id)"
					+ " VALUES (?, ? ,?, ?, ?, ?)";	//insert문으로 글 정보 추가
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleNO;	//SQL문으로 새글을 추가하고 새글번호를 반환
		
	}	//end insertNewArticle

	public ArticleVO selectArticle(int articleNO) {
		ArticleVO article=new ArticleVO();
		try{
		conn = dataFactory.getConnection();
		String query ="select articleNO,parentNO,title,content,  NVL(imageFileName, 'null') as imageFileName, id, writeDate"
			                     +" from t_board" 
			                     +" where articleNO=?";	//전달받은 글번호로 정보를 조회
		System.out.println(query);
		pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, articleNO);
		ResultSet rs =pstmt.executeQuery();
		rs.next();
		int _articleNO =rs.getInt("articleNO");
		int parentNO=rs.getInt("parentNO");
		String title = rs.getString("title");
		String content =rs.getString("content");
		String imageFileName = URLEncoder.encode(rs.getString("imageFileName"), "UTF-8"); //파일이름에 특수문자가 있을 경우 인코딩
		if(imageFileName.equals("null")) {
			imageFileName = null;
		}
		String id = rs.getString("id");
		Date writeDate = rs.getDate("writeDate");

		article.setArticleNO(_articleNO);
		article.setParentNO (parentNO);
		article.setTitle(title);
		article.setContent(content);
		article.setImageFileName(imageFileName);
		article.setId(id);
		article.setWriteDate(writeDate);
		rs.close();
		pstmt.close();
		conn.close();
		}catch(Exception e){
		e.printStackTrace();	
		}
		return article;
		
	}

	public void updateArticle(ArticleVO article) {
		int articleNO = article.getArticleNO();
		String title = article.getTitle();
		String content = article.getContent();
		String imageFileName = article.getImageFileName();
		
		try {
			conn = dataFactory.getConnection();
			String query = "update t_board  set title=?,content=?";
			
			if (imageFileName != null && imageFileName.length() != 0) {	//수정된 이미지파일이 있을 때만 SQL문에 추가
				query += ",imageFileName=?";
			}
			query += " where articleNO=?";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			
			if (imageFileName != null && imageFileName.length() != 0) {	//이미지파일을 수정하는 경우
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			} else {	//이미지파일을 수정하지 않는 경우
				pstmt.setInt(3, articleNO);
			}
			
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}	//end BoardDAO class
