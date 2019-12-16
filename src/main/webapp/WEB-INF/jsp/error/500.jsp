<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../view/locale.jsp" %>
<html>
<head>
    <title>Error page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <style>
        body {
            background-repeat: no-repeat;
            background-image: url("${pageContext.request.contextPath}/staticImage/background_error.png");
            -moz-background-size: 100%; /* Firefox 3.6+ */
            -webkit-background-size: 100%; /* Safari 3.1+ и Chrome 4.0+ */
            -o-background-size: 100%; /* Opera 9.6+ */
            background-size: 100%; /* Современные браузеры */
            background-position: center center;
        }
    </style>
</head>
<body>
<div class="container text-center">
    <div class="row h-50">
        <div class="col-sm-12 text-sm-center">
            <h1 style="margin: 30% 0 0 0"><fmt:message key="text.500Error"/></h1>
            <br>
            Request from ${pageContext.errorData.requestURI} is failed
            <br/>
            Servlet name: ${pageContext.errorData.servletName}
            <br/>
            Status code: ${pageContext.errorData.statusCode}
            <br/>
            Exception: ${pageContext.exception}
            <br/>
            Message from exception: ${pageContext.exception.message}
        </div>
    </div>
    <%--ОТОБРАЖЕНИЕ ФОРМЫ ДЛЯ ПЕРЕХОДА НА ГЛАВНУЮ СТРАНИЦУ--%>
    <div class="row text-center">
        <div class="col-sm-12 text-sm-center">
            <form name="MainPage" action="${pageContext.request.contextPath}/travelling">
                <input type="hidden" name="command" value="open_main_page"/>
                <input type="submit" class="btn btn-success"
                       value="<fmt:message key="button.MainPage"/>">
            </form>
        </div>
    </div>
</div>
</body>
</html>
