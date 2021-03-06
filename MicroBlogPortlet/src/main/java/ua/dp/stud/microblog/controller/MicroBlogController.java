package ua.dp.stud.microblog.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import ua.dp.stud.StudPortalLib.model.News;
import ua.dp.stud.StudPortalLib.service.NewsService;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.Collection;

/**
 * Controller for view mode
 */
@Controller
@RequestMapping(value = "view")
public class MicroBlogController {
    private static final String NEWS_ARCHIVE_REFERENCE_NAME = "NewsArchive_WAR_studnewsArchive";

    @Autowired
    @Qualifier(value = "newsService")
    //todo: change variable name
    private NewsService service;

    public void setService(NewsService service) {
        this.service = service;
    }

    /**
     * Method for rendering view mode
     *
     * @param request
     * @param response
     * @return
     * @throws javax.portlet.PortletModeException
     *
     */
    //todo: use try ... catch block
    @RenderMapping
    public ModelAndView showView(RenderRequest request, RenderResponse response) throws SystemException, PortalException {
        ModelAndView model = new ModelAndView();
        model.setViewName("viewAll");

        Collection<News> news = service.getNewsOnMainPage();
        ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId= themeDisplay.getScopeGroupId();
        long plid = LayoutLocalServiceUtil.getDefaultPlid(groupId, false, NEWS_ARCHIVE_REFERENCE_NAME);

        model.addObject("news", news);
        model.addObject("newsArchivePageID",plid);
        return model;
    }

    @RenderMapping(params = "mode=remove")
    public ModelAndView remove(RenderRequest request, RenderResponse response) throws SystemException, PortalException
    {
        Integer newsId = Integer.valueOf(request.getParameter("newsID"));
        News news = service.getNewsById(newsId);
        news.setOnMainpage(false);
        service.updateNews(news);
        //todo: move common logic to private helper method ot use redirect/forward
        return showView(request, response);
    }
}

