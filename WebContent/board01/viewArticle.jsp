<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<head>
   <meta charset="UTF-8">
   <title>글보기</title>
   <style>
     #tr_btn_modify{
       display:none;
     }
   
   </style>
   <script  src="http://code.jquery.com/jquery-latest.min.js"></script> 
   <script type="text/javascript" >
     function backToList(obj){
	    obj.action="${contextPath}/board/listArticles.do";
	    obj.submit();
     }
 
	 function fn_enable(obj){	//수정하기 클릭 시 텍스트 박스 활성화
		 document.getElementById("i_title").disabled=false;
		 document.getElementById("i_content").disabled=false;
		 document.getElementById("i_imageFileName").disabled=false;
		 document.getElementById("tr_btn_modify").style.display="block";
		 document.getElementById("tr_btn").style.display="none";
	 }
	 
	 function fn_modify_article(obj){	//수정반영하기 클릭 시 컨트롤러에 수정 테이터 전송
		 obj.action="${contextPath}/board/modArticle.do";
		 obj.submit();
	 }
	 
	 function fn_remove_article(url,articleNO){
		 var form = document.createElement("form");	//JS로 동적으로 form태그 생성
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);
		 
	     var articleNOInput = document.createElement("input");	//JS로 동적으로 input태그 생성 후
	     articleNOInput.setAttribute("type","hidden");			
	     articleNOInput.setAttribute("name","articleNO");		//name를	articleNO와
	     articleNOInput.setAttribute("value", articleNO);		//value를 컨트롤러로 글번호 설정
		 
	     form.appendChild(articleNOInput);	//input태그를 form태그에 append
	     
	     document.body.appendChild(form);	//form태그를 body태그에 append
		 form.submit();						//서버에 요청
	 
	 }
	 
	 function fn_reply_form(url, parentNO){
		 var form = document.createElement("form");
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);	//전달된 요청명을 form태그의  action 속성 값에 설정
		 
	     var parentNOInput = document.createElement("input");	//함수 호출 시 전달된 articleNO값을 input태그를 이용해 컨트롤러에 전달
	     parentNOInput.setAttribute("type","hidden");	
	     parentNOInput.setAttribute("name","parentNO");
	     parentNOInput.setAttribute("value", parentNO);
		 
	     form.appendChild(parentNOInput);
	     document.body.appendChild(form);
		 form.submit();
	 }
	 
	 function readURL(input) {
	     if (input.files && input.files[0]) {
	         var reader = new FileReader();
	         reader.onload = function (e) {
	             $('#preview').attr('src', e.target.result);
	         }
	         reader.readAsDataURL(input.files[0]);
	     }
	 }  
 </script>
</head>
<body>
  <form name="frmArticle" method="post"  action="${contextPath}"  enctype="multipart/form-data">
  <table  border="0"  align="center">
  <tr>
   <td width="150" align="center" bgcolor="#FF9933">
      글번호
   </td>
   <td >
    <input type="text"  value="${article.articleNO }"  disabled />
    <input type="hidden" name="articleNO" value="${article.articleNO}"  />	<!-- 글수정 시 글번호를 컨트롤러로 전송하기 위해 미리 히든태그에 글번호 저장 -->
   </td>
  </tr>
  <tr>
   <td width="150" align="center" bgcolor="#FF9933">
      작성자 아이디
   </td>
   <td >
    <input type=text value="${article.id }" name="writer"  disabled />
   </td>
  </tr>
  <tr>
   <td width="150" align="center" bgcolor="#FF9933">
      제목 
   </td>
   <td>
    <input type=text value="${article.title }"  name="title"  id="i_title" disabled />
   </td>   
  </tr>
  <tr>
   <td width="150" align="center" bgcolor="#FF9933">
      내용
   </td>
   <td>
    <textarea rows="20" cols="60"  name="content"  id="i_content"  disabled />${article.content }</textarea>
   </td>  
  </tr>
 
<c:if test="${not empty article.imageFileName && article.imageFileName!='null' }">  <!-- imageFileName값이 있으면 표시 -->
<tr>
   <td width="150" align="center" bgcolor="#FF9933" rowspan="2">
      이미지
   </td>
   <td>
     <input  type= "hidden"   name="originalFileName" value="${article.imageFileName }" />	<!-- 이미지 수정에 대비해 hidden태그에 imageFileName 저장 -->
    <img src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${article.imageFileName}" id="preview"  /><br>
       <!-- 파일다운로드컨트롤러 서블릿에 이미지파일이름과 글번호를 전송해 이미지를 표시 -->
   </td>   
  </tr>  
  <tr>
    <td>	<!-- 수정된 이미지 파일 이름 전송 -->
       <input  type="file"  name="imageFileName " id="i_imageFileName"   disabled   onchange="readURL(this);"   />
    </td>
  </tr>
 </c:if>
  <tr>
	   <td width=20% align=center bgcolor=#FF9933>
	      등록일자
	   </td>
	   <td>
	    <input type=text value="<fmt:formatDate value="${article.writeDate}" />" disabled />
	   </td>   
  </tr>
  <tr   id="tr_btn_modify"  >
	   <td colspan="2"   align="center" >
	       <input type=button value="수정반영하기"   onClick="fn_modify_article(frmArticle)"  >
         <input type=button value="취소"  onClick="backToList(frmArticle)">
	   </td>   
  </tr>
    
  <tr  id="tr_btn"    >
   <td colspan=2 align=center>
	    <input type=button value="수정하기" onClick="fn_enable(this.form)">
	    <!-- 삭제하기 클릭 시 fn_remove_article JS함수 호출 & articleNO 전달 -->
	    <input type=button value="삭제하기" onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})">
	    <input type=button value="리스트로 돌아가기"  onClick="backToList(this.form)">
	    <!-- 답글쓰기 클릭 시 fn_reply_form 함수 호출 & 요청명 & 글번호 전달 -->
	    <input type=button value="답글쓰기"  onClick="fn_reply_form('${contextPath}/board/replyForm.do', ${article.articleNO})">
   </td>
  </tr>
 </table>
 </form>
</body>
</html>