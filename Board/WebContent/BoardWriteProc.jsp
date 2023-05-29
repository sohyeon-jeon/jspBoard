<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<body>
	<%
	request.setCharacterEncoding("UTF-8"); //한글처리
	%>
	<!-- 게시글 작성한 데이터를 한번에 읽음 -->
	<jsp:useBean id="boardbean" class="model.BoardBean">
		<jsp:setProperty name="boardbean" property="*" />
	</jsp:useBean>

	<%
	/* 데이터베이스쪽으로 빈클래스 넘겨줌 */
	BoardDAO bdao=new BoardDAO();

	/* 	데이터 저장 메소드 호출 */
	bdao.insertBoard(boardbean);
	%>
</body>
</html>