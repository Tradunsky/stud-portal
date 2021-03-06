package ua.dp.stud.StudPortalLib.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import ua.dp.stud.StudPortalLib.dao.OrganizationDao;
import ua.dp.stud.StudPortalLib.model.News;
import ua.dp.stud.StudPortalLib.model.Organization;
import ua.dp.stud.StudPortalLib.util.OrganizationType;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"classpath:/DaoTestContext.xml"})
@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrganizationDaoTest extends AbstractTransactionalJUnit4SpringContextTests {


    public OrganizationDaoTest() {
    }

    public void setDao(OrganizationDao dao) {
        this.dao = dao;
    }

    @Autowired
    private OrganizationDao dao;
    private static Organization org1;
    private static Organization org2;
    private static News n1;
    private static News n2;
    private static News n3;


    @Before
    @Rollback(false)
    public void setUpClass() {
        org1 = new Organization();
        org2 = new Organization();
        org1.setApproved(Boolean.TRUE);
        org2.setApproved(Boolean.TRUE);
        org1.setOrganizationType(OrganizationType.SPORTS);
        org1.setTitle("Sport org");
        org1.setText("We are sport!");
        org1.setAuthor("author1");
        org2.setOrganizationType(OrganizationType.OTHERS);
        org2.setTitle("Other org");
        org2.setText("We are other!");
        org2.setAuthor("author1");
        List<News> news = new ArrayList<News>();

        n1 = new News();
        n1.setText("text1");
        n1.setBaseOrg(org1);
        n1.setOrgApproved(true);

        n2 = new News();
        n2.setText("text2");
        n2.setBaseOrg(org1);
        n2.setOrgApproved(false);

        n3 = new News();
        n3.setText("text3");
        n3.setBaseOrg(org1);
        n3.setOrgApproved(true);

        news.add(n1);
        news.add(n2);
        news.add(n3);
        org1.setNewsList(news);

        dao.addOrganization(org1);
        dao.addOrganization(org2);
    }

    @Test
    public void getAllOrganizationsNewsByIdTests(){
        Integer id = org1.getId();
        Collection <News> news = dao.getAllOrganizationsNewsById(id,true);
        assertTrue(news.contains(n1));
        assertFalse(news.contains(n2));
    }

    @Test
    public void getOrganizationsNewsByIdOnPage(){
        Integer id = org1.getId();
        Collection <News> news = dao.getOrganizationsNewsByIdOnPage(id, 1, 2, true);
        assertEquals(2, news.size());
        assertTrue(news.contains(n1));
        assertTrue(news.contains(n3));
        assertFalse(news.contains(n2));
    }

    @Test
    public void getOrganizationsOnPageTest()
    {
        List<Organization> expResult = (List) dao.getOrganizationsOnPage(true, 1, 4);
        assertFalse(expResult.size() == 1);
        assertEquals(2, expResult.size());
        assertEquals(org1.getAuthor(), expResult.get(0).getAuthor());
    }

    @Test
    public void getAllOrganizationByAuthorTest()
    {
        List<Organization> expResult = (List) dao.getAllOrganizationByAuthor("author1");
        assertFalse(expResult.size() == 1);
        assertEquals(2, expResult.size());
        assertEquals(org1.getAuthor(), expResult.get(0).getAuthor());
    }

    @Test
    public void getCountByAuthorTest()
    {
        Integer result = 2;
        Integer expResult = dao.getCountByAuthor("author1");
        assertEquals(result, expResult);
    }

    @Test
    public void getPagesOrganizationByAuthorTest()
    {
        List<Organization> expResult = (List) dao.getPagesOrganizationByAuthor("author1", 1, 4);
        assertFalse(expResult.size() == 1);
        assertEquals(2, expResult.size());
        assertEquals(org1.getAuthor(), expResult.get(0).getAuthor());
    }

    @Test
    public void getCountByApprovedTest()
    {
        Integer result = 0;
        Integer expResult = dao.getCountByApprove(false);
        assertEquals(result, expResult);
    }

    @Test
    public void AddOrg() {
        Organization org3 = new Organization();
        org3.setApproved(Boolean.TRUE);
        assertNull(org3.getId());
        dao.addOrganization(org3);
        assertNotNull(org3.getId());
    }

    @Test
    public void getById() {
        Integer id = org1.getId();
        assertEquals(org1, dao.getOrganizationById(id));
    }

    @Test
    public void getAll() {
        Set<Organization> orgsList = new HashSet<Organization>(Arrays.asList(org1, org2));
        Set<Organization> fromDao = new HashSet<Organization>(dao.getAllOrganizations(true));

        assertEquals(orgsList, fromDao);
        assertEquals(2, fromDao.size());
    }

    @Test
    public void getByType() {
        OrganizationType type = OrganizationType.SPORTS;
        List<Organization> sportOrgs = (List<Organization>) dao.getOrganizationsByType(type);
        assertEquals(1, sportOrgs.size());
        assertEquals(org1, sportOrgs.get(0));
    }

    @Test
    public void pagination() {
        Organization o3 = new Organization();
        o3.setOrganizationType(OrganizationType.OTHERS);
        o3.setApproved(Boolean.TRUE);
        dao.addOrganization(o3);
        List<Organization> othersList = (List<Organization>) dao.getOrganizationsOnPage(1, 2, OrganizationType.OTHERS,true);
        assertTrue(othersList.contains(o3));
    }

 /*   @Test
    public void delete() {
        assertEquals(1, dao.deleteOrganization(org1));
        assertEquals(0, dao.deleteOrganization(org1));
    }*/

    @Test
    public void update(){
        Integer id = org1.getId();
        org1.setOrganizationType(OrganizationType.OTHERS);
        dao.updateOrganization(org1);
        Organization org = dao.getOrganizationById(id);
        assertEquals(OrganizationType.OTHERS, org.getOrganizationType());
    }

    @Test
    public void getCount() {
        assertEquals(2, (int)dao.getCount());
    }
}
