package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	Connection conn; // 데이터베이스에 접근할 수 있도록 설정
	PreparedStatement pstmt; // 데이터베이스에서 쿼리를 실행시켜주는 객체
	ResultSet rs; // 데이터베이스의 테이블의 결과를 리턴받아 자바에 저장해주는 객체

//	데이터베이스의 커넥션풀을 사용하도록 설정하는 메서드
	public void getCon() {
		try {
//			외부에서 데이터를 읽어들여야하기에
			Context initctx = new InitialContext();
//			톰캣 서버에 정보를 담아놓은 곳으로 이동
			Context envctx = (Context) initctx.lookup("java:comp/env");
//			데이터소스 객체를 선언
			DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
//			데이터 소스를 기준으로 커넥션을 연결
			conn = ds.getConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	하나의 새로운 게시글이 넘어와서 저장되는 메서드
	public void insertBoard(BoardBean bean) {
		getCon();
//		빈클래스에 넘어오지 않았던 데이터들을 초기화해주어야한다.
		int ref = 0; // 글그룹을 의미 = 쿼리를 실행시켜서 가장큰 ref값을 가져온흐 +1을 해주면 됨.
		int re_step = 1; // 새글이기에 =부모글이기에
		int re_level = 1;

		try {
			// 가장 큰 ref값을 읽어오는 쿼리 준비
			String refsql = "select max(ref) from board";
			// 쿼리실행 객체
			pstmt = conn.prepareStatement(refsql);
			// 쿼리실행후 결과를 리턴
			rs = pstmt.executeQuery();
			if (rs.next()) { // 결과 값이 있다면
				ref = rs.getInt(1) + 1; // 최대값에 +1을 해줘서 글그룹을 설정
			}
			// 실제로 게시글 전체값을 테이블에 저장
			String sql = "insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			// ?에 값 대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());
			// 쿼리를 실행하시오
			pstmt.executeUpdate();
			// 자원반납
			conn.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
