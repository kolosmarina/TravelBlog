<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="/WEB-INF/tld/custom.tld" %>

<html>
<head>
    <title>Travelling</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@ include file="header.jsp" %>
<%--ИНФОРМАЦИЯ О ЧАСТЯХ СВЕТА С ФОТО И ССЫЛКАМИ, СТАТИЧЕСКАЯ ИНФОРМАЦИЯ--%>
<div class="container">
    <div class="row">
        <div class="col-sm-9 text-center">
            <p style="text-transform: uppercase; font-weight: bold;"><fmt:message key="column.directions"/>
                &#8681;</p>
        </div>
        <div class="col-sm-3 text-center">
            <p style="text-transform: uppercase; font-weight: bold;"><fmt:message key="column.newPosts"/>
                &#8681;</p>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-9 text-center">
            <div class="row">
                <div class="col-sm-4">
                    <div class="card border-warning" style="border-width: thick">
                        <img src="${pageContext.request.contextPath}/staticImage/1.jpg" alt="1"
                             class="img-responsive img-thumbnail">
                        <div class="card-img-overlay">
                            <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldParts[0]}&command=open_posts_page"
                               class="btn btn-warning btn-sm"><fmt:message key="ref.europe"/></a>
                        </div>
                    </div>
                    <br>
                </div>
                <div class="col-sm-4">
                    <div class="card border-warning" style="border-width: thick">
                        <img src="${pageContext.request.contextPath}/staticImage/2.jpg" alt="2"
                             class="img-responsive img-thumbnail">
                        <div class="card-img-overlay">
                            <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldParts[1]}&command=open_posts_page"
                               class="btn btn-warning btn-sm"><fmt:message key="ref.asia"/></a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div class="card border-warning" style="border-width: thick">
                        <img src="${pageContext.request.contextPath}/staticImage/3.jpg" alt="3"
                             class="img-responsive img-thumbnail">
                        <div class="card-img-overlay">
                            <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldParts[2]}&command=open_posts_page"
                               class="btn btn-warning btn-sm"><fmt:message key="ref.africa"/></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row equal">
                <div class="col-sm-4">
                    <div class="card border-warning" style="border-width: thick">
                        <img src="${pageContext.request.contextPath}/staticImage/4.jpg" alt="4"
                             class="img-responsive img-thumbnail">
                        <div class="card-img-overlay">
                            <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldParts[3]}&command=open_posts_page"
                               class="btn btn-warning btn-sm"><fmt:message key="ref.northAmerica"/></a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div class="card border-warning" style="border-width: thick">
                        <img src="${pageContext.request.contextPath}/staticImage/5.jpg" alt="5"
                             class="img-responsive img-thumbnail">
                        <div class="card-img-overlay">
                            <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldParts[4]}&command=open_posts_page"
                               class="btn btn-warning btn-sm"><fmt:message key="ref.oceania"/></a>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div class="card border-warning" style="border-width: thick">
                        <img src="${pageContext.request.contextPath}/staticImage/6.jpg" alt="6"
                             class="img-responsive img-thumbnail">
                        <div class="card-img-overlay ">
                            <a href="${pageContext.request.contextPath}/travelling?worldPart=${requestScope.worldParts[5]}&command=open_posts_page"
                               class="btn btn-warning btn-sm"><fmt:message key="ref.southAmerica"/></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row equal">
                <div class="col-sm-12 text-center">
                    <br>
                    <p style="text-transform: uppercase; font-family: Segoe Print, monospace; font-size: 150%;">
                        &#9992; Travel and dream &#9992;</p>
                    <br>
                    &#169; <fmt:message key="text.quote"/>
                </div>
            </div>
        </div>
        <%--ИНФОРМАЦИЯ О НОВЫХ ПОСТАХ, ДИНАМИЧЕСКАЯ ИНФОРМАЦИЯ--%>
        <div class="col-sm-3 text-center">
            <c:forEach items="${requestScope.posts}" var="post">
                <jsp:useBean id="dateValue" class="java.util.Date"/>
                <jsp:setProperty name="dateValue" property="time" value="${post.publishDate}"/>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="card border-success" style="border-width: thick">
                            <img src="${pageContext.request.contextPath}/images?&photoId=${post.mainPhotoId}"
                                 alt="${post.mainPhotoId}"
                                 class="img-responsive img-thumbnail">
                            <div class="card-img-overlay ">
                                <a href="${pageContext.request.contextPath}/travelling?postId=${post.id}&command=open_post_page"
                                   class="btn btn-success btn-sm"><fmt:formatDate value="${dateValue}"
                                                                                  pattern="dd.MM.yyyy HH:mm"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <%--СОБСТВЕННЫЕ ТЕГИ--%>
    <div class="row">
        <div class="col-sm-12 text-center">
            <ctg:general-info color="blue"/>
        </div>
    </div>
</div>
</body>
</html>
