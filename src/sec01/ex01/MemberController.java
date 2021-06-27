package sec01.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")	//브라우저에서 요청 시 두 단계로 요청
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MemberDAO memberDAO;

	public void init() throws ServletException {
		memberDAO = new MemberDAO();	//MemberDAO생성
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo();	//URL에서 요청명을 가져옴
		System.out.println("action:" + action);
		
		if (action == null || action.equals("/listMembers.do")) {	//최초 요청이거나 action=/listMembers.do이면 회원목록 출력
			List<MemberVO> membersList = memberDAO.listMembers();	//회원정보 조회
			request.setAttribute("membersList", membersList);		//회원정보 바인딩
			nextPage = "/test01/listMembers.jsp";					//listMembers.jsp로 포워딩
			
		}else if (action.equals("/addMember.do")) {	//전송된 회원정보를 가져와서 테이블에 추가
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			MemberVO memberVO = new MemberVO(id, pw, name, address);
			memberDAO.addMember(memberVO);
			nextPage = "/member/listMembers.do";	//회원등록 후 다시 회원목록 출력
			
		}else if (action.equals("/memberForm.do")) {	//회원 가입창을 화면에 출력
			nextPage = "/test01/memberForm.jsp";		//memberForm.jsp로 포워딩
			
		}else {	//그 외 다른 action 값은 회원목록 출력
			List<MemberVO> membersList = memberDAO.listMembers();	//회원정보 조회
			request.setAttribute("membersList", membersList);		//회원정보 바인딩
			nextPage = "/test01/listMembers.jsp";					//listMembers.jsp로 포워딩
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);	//nextPage에 지정한 요청명으로 서블릿에 요청
		dispatch.forward(request, response);					//JSP로 포워딩
		
	}	//end doHandle

}	//end MemberController
