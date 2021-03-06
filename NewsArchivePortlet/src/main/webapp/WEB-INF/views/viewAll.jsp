<%@ page import="ua.dp.stud.StudPortalLib.model.News" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.kernel.servlet.ImageServletTokenUtil" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="ua.dp.stud.StudPortalLib.util.ImageService" %>
<%@ page import="ua.dp.stud.StudPortalLib.util.CustomFunctions" %>
<%@ taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet" %>
<%@include file="include.jsp" %>


<%@ taglib prefix="theme" uri="http://liferay.com/tld/theme" %>


<portlet:defineObjects/>
<%
    Collection<News> news = (Collection) request.getAttribute("news");
    int pagesCount = (Integer) request.getAttribute("pagesCount");
    int currentPage = (Integer) request.getAttribute("currentPage");

    ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
    String imagePath = new StringBuilder(themeDisplay.getPortalURL()).append('/')
            .append(themeDisplay.getPathImage()).append("/image_gallery?img_id=").toString();
    ImageService imageService = new ImageService();
    //todo: to controller
    int nearbyPages = 2; //number of pages to show to left and right of current
    int overallPages = 7; //overall number of pages
    int leftPageNumb = Math.max(1, currentPage - nearbyPages),
            rightPageNumb = Math.min(pagesCount, currentPage + nearbyPages);
    boolean skippedBeginning = false,
            skippedEnding = false;

    if (pagesCount <= overallPages) {
        leftPageNumb = 1;                 //all pages are shown
        rightPageNumb = pagesCount;
    } else {
        if (currentPage > 2 + nearbyPages) { //if farther then page #1 + '...' + nearby pages
            skippedBeginning = true;        // will look like 1 .. pages
        } else {
            leftPageNumb = 1;             //shows all first pages
            rightPageNumb = 2 + 2 * nearbyPages; //#1 + nearby pages + current + nearby pages
        }

        if (currentPage < pagesCount - (nearbyPages + 1)) { //if farther then nearby + '...' + last
            skippedEnding = true;         //will look like pages .. lastPage
        } else {
            leftPageNumb = (pagesCount - 1) - 2 * nearbyPages;  //shows all last pages:
            rightPageNumb = pagesCount;
        }
    }
    //todo:locale
    Locale locale = (Locale) request.getSession().getAttribute("org.apache.struts.action.LOCALE");
    String language = locale.getLanguage();
    String country = locale.getCountry();
    ResourceBundle res = ResourceBundle.getBundle("messages", new Locale(language, country));

%>

<html>
<head>
</head>
<body>
    <%if (request.isUserInRole("Administrator") || request.isUserInRole("User")) { %>
		<portlet:renderURL var="addNewsUrl">
			<portlet:param name="mode" value="add" />
		</portlet:renderURL>
        <div class="portlet-content-controlpanel fs20">
        <a style="float: right" href="${addNewsUrl}">
            <div class="panelbtn panelbtn-right icon-pcpfile" aria-hidden="true"></div>
            <!--<spring:message code="viewAll.addNews"/>-->
        </a>
        </div>
    <%}%>
<div id="contentDiv">

    <liferay-ui:success message='<%=res.getString("msg.successAdd")%>' key="success-add"/>

    <% if (!news.isEmpty()) {%>
    <table id="newsTable">
        <%
            for (News currentNews : news) {

        %>
        <tr>
            <td width="100%">
                <div width="100%">
                    <a href='<portlet:renderURL><portlet:param name="newsID" value="<%=currentNews.getId().toString()%>"/></portlet:renderURL>'>
                        <img src="<%= imageService.getPathToMicroblogImage(currentNews.getMainImage(),currentNews) %>"
                             class="newsImage">
                    </a>

                    <div class="newsHeader">
                        <a href='<portlet:renderURL><portlet:param name="newsID" value="<%=currentNews.getId().toString()%>"/></portlet:renderURL>'>
                            <%=currentNews.getTopic()%>

                        </a>
                    </div>
                    <div class="newsText">
                        <%--50 as said Anton--%>
                        <%= CustomFunctions.truncateWords(currentNews.getText(), 50) %>
                    </div>
                        <% if (request.isUserInRole("Administrator")) { %>
                        <a style="float: right" href='<portlet:renderURL><portlet:param name="newsId" value="<%=currentNews.getId().toString()%>"/><portlet:param name="mode" value="delete" /></portlet:renderURL>'
                           onclick='return confirm("<spring:message code="form.confDelete"/>")'>
                            <!--<spring:message code="form.delete"/>-->
                            <div class="panelbtn panelbtn-right fs20 icon-pcpremove" aria-hidden="true"></div>
                            </a>
                        <%}%>
                </div>
                <div width="100%" align="right">
                    <table width="90%">
                        <tr>
                            <td width="60">
                                <img width="60" class="newsDecorImage"
                                     src="${pageContext.request.contextPath}/images/news-decor-line-left-end.png"/>
                            </td>
                            <td width="auto" align="left">
                                <img width="100%" class="newsDecorImage"
                                     src="${pageContext.request.contextPath}/images/news-decor-line.png"/>
                            </td>
                            <td width="52">
                                <img width="52" class="newsDecorImage"
                                     src="${pageContext.request.contextPath}/images/news-decor-center.png"/>
                            </td>
                            <td width="auto" align="right">
                                <img width="100%" class="newsDecorImage"
                                     src="${pageContext.request.contextPath}/images/news-decor-line.png"/>
                            </td>
                            <td width="50">
                                <img width="50" class="newsDecorImage"
                                     src="${pageContext.request.contextPath}/images/news-decor-line-right-end.png"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
        <%}%>
    </table>
    <%}%>

    <table width="90%">
        <tr>
            <td width="80" align="left">
			<portlet:actionURL name="pagination" var="pagPrev">
				<portlet:param name="direction" value="prev"/>
				<portlet:param name="pageNumber" value="<%=String.valueOf(currentPage)%>"/>
			</portlet:actionURL>
                <a href="${pagPrev}">
                    <img class="paginationImage"
                         src="${pageContext.request.contextPath}/images/pagin-left.png"/>
                </a>
            </td>

            <td width="auto" align="center" valign="center">
                <%-- PAGINATION --%>
                <%if (skippedBeginning) {%>
                <%-- HIDING FIRST PAGES --%>
                <a href="<portlet:actionURL name="pagination"><portlet:param name="pageNumber" value="1"/></portlet:actionURL>">1</a>
                <label > ... </label>
                <%}%>

                <%-- SHOWING CURRENT PAGE NEAREST FROM LEFT AND RIGHT --%>
                <%
                    for (int pageNumb = leftPageNumb; pageNumb <= rightPageNumb; ++pageNumb) {
                        if (pageNumb != currentPage) {
                %>
                <a href="<portlet:actionURL name="pagination"><portlet:param name="pageNumber" value="<%=String.valueOf(pageNumb)%>"/></portlet:actionURL>"><%=pageNumb%>
                </a>
                <%} else {%>
                <label style="color: #28477C; font-size: 40px;" ><%=pageNumb%>
                </label>
                <%}%>
                <%}%>

                <%if (skippedEnding) {%>
                <%-- HIDING LAST PAGES --%>
                <label> ... </label>
                <a href="<portlet:actionURL name="pagination"><portlet:param name="pageNumber" value="<%=String.valueOf(pagesCount)%>"/></portlet:actionURL>"><%=pagesCount%>
                </a>
                <%}%>
            </td>
            <td width="80" align="right">
				<portlet:actionURL name="pagination" var="pagNext">
					<portlet:param name="direction" value="next"/>
					<portlet:param name="pageNumber" value="<%=String.valueOf(currentPage)%>"/>
				</portlet:actionURL>
                <a href="${pagNext}">
                    <img class="paginationImage"
                         src="${pageContext.request.contextPath}/images/pagin-right.png"/>
                </a>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
