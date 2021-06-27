package sec01.ex01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	private DataSource dataFactory;
	private Connection conn;
	private PreparedStatement pstmt;

	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/MyDB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<MemberVO> listMembers() {
		List<MemberVO> membersList = new ArrayList();
		try {
			conn = dataFactory.getConnection();
			String query = "select * from  users";	//SQL문
			System.out.println(query);
			pstmt = conn.prepareStatement(query);	//ps객체 생성 & SQL문 전달
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String name = rs.getString("name");
				String address = rs.getString("address");
				MemberVO memberVO = new MemberVO(id, pw, name, address);	//조회한 회원정보를 레코드별로 MemberVO객체의 속성에 저장
				membersList.add(memberVO);	//memberList에 MemberVO객체들을 차례로 저장
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return membersList;
	}

	public void addMember(MemberVO m) {
		try {
			conn = dataFactory.getConnection();
			String id = m.getId();
			String pw = m.getPw();
			String name = m.getName();
			String address = m.getAddress();
			String query = "INSERT INTO users(id, pw, name, address)" + " VALUES(?, ? ,? ,?)";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);	//ps객체 생성 & SQL문 전달
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, address);
			pstmt.executeUpdate();					//SQL문 실행
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MemberVO findMember(String _id) {
		MemberVO memInfo = null;
		try {
			conn = dataFactory.getConnection();
			String query = "select * from  users where id=?";	//전달된 id로 정보 조회
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, _id);
			System.out.println(query);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String id = rs.getString("id");
			String pw = rs.getString("pw");
			String name = rs.getString("name");
			String address = rs.getString("address");
			memInfo = new MemberVO(id, pw, name, address);
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memInfo;
	}
	
	public void modMember(MemberVO memberVO) {
		String id = memberVO.getId();
		String pw = memberVO.getPw();
		String name = memberVO.getName();
		String address = memberVO.getAddress();
		try {
			conn = dataFactory.getConnection();
			String query = "update users set pw=?,name=?,address=?  where id=?";	//전달된 정보를 update문으로 수정
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, pw);
			pstmt.setString(2, name);
			pstmt.setString(3, address);
			pstmt.setString(4, id);
			pstmt.executeUpdate();	//SQL문 실행
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delMember(String id) {
		try {
			conn = dataFactory.getConnection();
			String query = "delete from users where id=?";	//delete문으로 전달된 id의 회원 삭제
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,id);
			pstmt.executeUpdate();	//SQL문 실행
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}//end MemberDAO
