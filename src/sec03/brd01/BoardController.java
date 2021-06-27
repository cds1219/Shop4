package sec03.brd01;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ARTICLE_IMAGE_REPO = "D:\\board\\article_image";	//첨부 이미지 저장 위치
	BoardService boardService;
	ArticleVO articleVO;

	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();	//서블릿 초기화 시 boardService 객체 생성
		articleVO = new ArticleVO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String action = request.getPathInfo();	//요청
		System.out.println("action:" + action);
		
		try {
			List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
			if (action == null) {
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList);
				nextPage = "/board01/listArticles.jsp";
				
			} else if (action.equals("/listArticles.do")) {	//전체글 조회
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList);	//조회된 글 목록을 articlesList로 바인딩
				nextPage = "/board01/listArticles.jsp";				//listArticles.jsp로 포워딩
				
			} else if (action.equals("/articleForm.do")) {
				nextPage = "/board01/articleForm.jsp";	//글쓰기 창
				
			} else if (action.equals("/addArticle.do")) {	//새 글 추가
				int articleNO=0;
				Map<String, String> articleMap = upload(request, response);	//파일 업로드 기능
				String title = articleMap.get("title");						//articleMap에 저장된 글 정보를 가져옴
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");

				articleVO.setParentNO(0);	//새 글의 부모 글 번호를 0으로
				articleVO.setId("e");	//새 글 작성자 id를 e로
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				articleNO=boardService.addArticle(articleVO);	//테이블에 새글을 추가 후 글번호를 가져옴
				
				if(imageFileName!=null && imageFileName.length()!=0) {	//파일을 첨부한 경우에만
				    File srcFile = new 	File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);	//temp폴더에 임시로 업로드 파일 객체 생성
					File destDir = new File(ARTICLE_IMAGE_REPO +"\\"+articleNO);	
					destDir.mkdirs();	//ARTICLE_IMAGE_REPO 경로 하위에 글번호로 폴더를 생성
					FileUtils.moveFileToDirectory(srcFile, destDir, true);	//temp폴더의 파일을 글번호 폴더로 이동
				}
				PrintWriter pw = response.getWriter();
				pw.print("<script>" 
				         +"  alert('새글을 추가했습니다.');" 
						 +" location.href='"+request.getContextPath()+"/board/listArticles.do';"
				         +"</script>");
						//새글 등록 메시지를 나타낸 후 JS_location 객체의 링크를 글목록에 요청
				return;
				
			}else {
				nextPage = "/board01/listArticles.jsp";
			}
			
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	//end doHandle
	
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);	//글 이미지 저장 폴더에 대해 파일 객체 생성
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			List items = upload.parseRequest(request);
			
			for (int i = 0; i < items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i);
				
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
					//새글을 맵에 키,밸류로 저장 후 반환하고 title, content를 맵에 저장
					
				} else {
					System.out.println("파라미터이름:" + fileItem.getFieldName());
					System.out.println("파일이름:" + fileItem.getName());
					System.out.println("파일크기:" + fileItem.getSize() + "bytes");
					//articleMap.put(fileItem.getFieldName(), fileItem.getName());
					
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}

						String fileName = fileItem.getName().substring(idx + 1);
						System.out.println("파일명:" + fileName);
						articleMap.put(fileItem.getFieldName(), fileName);  //익스플로러에서 업로드 파일의 경로 제거 후 map에 파일명 저장
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);	//첨부파일을 temp폴더에 먼저 업로드
						fileItem.write(uploadFile);

					}	//end if - 업로드한 파일이 존재하는 경우 업로드한 파일의 파일 이름으로 저장소에 업로드
				}	//end else
			}	//end for
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleMap;
		
	}	//end Map

}	//end BoardController
