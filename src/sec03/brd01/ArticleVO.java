package sec03.brd01;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;

public class ArticleVO {
	private int level;
	private int articleNO;
	private int parentNO;
	private String title;
	private String content;
	private String imageFileName;
	private String id;
	private Date writeDate;
	
	public ArticleVO() {
		
	}

	public ArticleVO(int level, int articleNO, int parentNO, String title, String content, String imageFileName, String id) {
		setLevel(level);
		setArticleNO(articleNO);
		setParentNO(parentNO);
		setTitle(title);
		setContent(content);
		setImageFileName(imageFileName);
		setId(id);
	}

	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if(level<0) {
			System.out.println("level를 입력해주세요.");
		}else {
			this.level = level;
		}
	}

	public int getArticleNO() {
		return articleNO;
	}

	public void setArticleNO(int articleNO) {
		if(articleNO<0) {
			System.out.println("articleNO를 입력해주세요.");
		}else {
			this.articleNO = articleNO;
		}
	}

	public int getParentNO() {
		return parentNO;
	}

	public void setParentNO(int parentNO) {
		if(parentNO<0) {
			System.out.println("parentNO를 입력해주세요.");
		}else {
			this.parentNO = parentNO;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(title!=null) {
			this.title = title;
		}else {
			System.out.println("title를 입력해주세요.");
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if(content!=null) {
			this.content = content;
		}else {
			System.out.println("content를 입력해주세요.");
		}
	}
	
	public String getImageFileName() {
		try {
			if (imageFileName != null && imageFileName.length() != 0) {
				imageFileName = URLDecoder.decode(imageFileName, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		try {
			this.imageFileName = URLEncoder.encode(imageFileName, "UTF-8");	//파일이름에 특수문자가 있을 경우 인코딩
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if(id!=null) {
			this.id = id;
		}else {
			System.out.println("id를 입력해주세요.");
		}
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		if(writeDate!=null) {
			this.writeDate = writeDate;
		}else {
			System.out.println("writeDate를 입력해주세요.");
		}
	}
	
}
