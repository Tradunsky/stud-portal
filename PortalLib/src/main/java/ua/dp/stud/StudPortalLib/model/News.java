package ua.dp.stud.StudPortalLib.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity class instances of which will be stored in DB
 *
 * @author Roman Lukash
 * @author Vladislav Pikus
 */
@Entity
@Table(name = "newstable")
@PrimaryKeyJoinColumn(name="id", referencedColumnName="id")
public class News extends BaseImagesSupport implements Serializable {
    private static final int NEWS_TEXT_LENGTH = 10000;

    /**
     * Default constructor
     */
    public News() {
        onMainpage = false;
        inCalendar = false;
        approved=false;
    }


    /**
     * constructor with params
     *
     * @param topic       topic of news
     * @param text        text of news
     * @param author      author of news
     * @param publication Date when news was create
     * @param publicationInCalendar Date when news was publishing in cal
     */
    public News(String topic, String text, String author, Date publication, Date publicationInCalendar,
                Category category, Boolean approved, Boolean inCalendar, Boolean onMainpage) {
        this.topic = topic;
        this.text = text;
        this.author = author;
        this.publication = publication;
        this.publicationInCalendar = publicationInCalendar;
        this.approved = approved;
        this.inCalendar = inCalendar;
        this.onMainpage = onMainpage;
        this.category = category;
    }

    //Fields//
    private String topic;
    private String text;
    private String author;
    private Date publication;
    private Date publicationInCalendar;
    private Boolean inCalendar;
    private Boolean approved;
    private Boolean onMainpage;
    private Category category;
    private Boolean orgApproved;
    private Organization baseOrg;
    private String comment;

    @Column
    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    /**
     * Return administrator approved
     * @return "true" if administrator of organization approved, "false" another way
     */
    @Column(name = "orgApproved")
    public Boolean getOrgApproved()
    {
        return orgApproved;
    }

    public void setOrgApproved(Boolean orgApproved)
    {
        this.orgApproved = orgApproved;
    }

    /**
     * Return the organization that owns this news
     * @return base organization object
     */
    @ManyToOne
    @JoinColumn(name = "Org_Base")
    public Organization getBaseOrg()
    {
        return this.baseOrg;
    }

    public void setBaseOrg(Organization baseOrg)
    {
        this.baseOrg = baseOrg;
    }

    /**
     * return Topic of news
     *
     * @return topic
     */
    @Column(name = "title")
    public String getTopic() {
        return topic;
    }

    /**
     * Set Topic of news
     *
     * @param topic Topic of news
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * return Text of news
     *
     * @return text
     */
    @Column(name = "text", length = NEWS_TEXT_LENGTH)
    public String getText() {
        return text;
    }

    /**
     * Set Text of news
     *
     * @param text Text of news
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * return Author of news
     *
     * @return author
     */
    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    /**
     * Set Author of news
     *
     * @param author author of news
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * return Date when news was create in full format
     *
     * @return publication
     */
    @Column(name = "publication")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPublication() {
        return publication;
    }

    /**
     * Set Date when news was create in full format
     *
     * @param date
     */
    public void setPublication(Date date) {
        this.publication = date;
    }

    /**
     * return Category for organization
     * @return Category object
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Returns a positive value if news in calendar
     * @return 0 if News is not belong for calendar, other value
     */
    @Column(name = "calendar")
    public Boolean getInCalendar() {
        return inCalendar;
    }

    public void setInCalendar(Boolean inCalendar) {
        this.inCalendar = inCalendar;
    }

    /**
     * Returns "true" if admin approved this News
     * @return "true" if administrator approved, "false" another way
     */
    @Column(name = "approved")
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    /**
     * Returns count of News on main page
     * @return count of News on main page
     */
    @Column(name = "onmainpage")
    public Boolean getOnMainpage() {
        return onMainpage;
    }

    public void setOnMainpage(Boolean onMainpage) {
        this.onMainpage = onMainpage;
    }

    /**
     * Convert News to string, contains topic, text, author, publication date
     * @return string with topic, text, author, publication date
     */
    @Override
    public String toString()
    {
        return new StringBuffer().append("News[").append("topic=").append(topic)
                .append(", text=").append(text).append(']')
                .append(", author=").append(author).append(']')
                .append(", publication=").append(publication).append(']').toString();
    }

    @Override
    public int hashCode()
    {
        return  new HashCodeBuilder(17, 257).append(this.topic).append(this.text).append(this.author)
                .append(this.publication).append(this.publicationInCalendar).append(this.inCalendar)
                .append(this.approved).append(this.onMainpage).append(this.category).append(this.orgApproved)
                .append(this.baseOrg).append(this.comment).toHashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof News))
            return false;
        final News other = (News) obj;
        return new EqualsBuilder().append(other.topic, topic).append(other.text, text)
                .append(other.author, author).append(other.publication, publication)
                .append(other.publicationInCalendar, publicationInCalendar).append(other.inCalendar, inCalendar)
                .append(other.approved, approved).append(other.onMainpage, onMainpage).append(other.category, category)
                .append(other.orgApproved, orgApproved).append(other.baseOrg, baseOrg).append(other.comment, comment)
                .isEquals();
    }

    /**
     * Returns date for publication in calendar
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPublicationInCalendar() {
        return publicationInCalendar;
    }

    public void setPublicationInCalendar(Date publicationInCalendar) {
        this.publicationInCalendar = publicationInCalendar;
    }
}