/*
    This file is part of Cyclos (www.cyclos.org).
    A project of the Social Trade Organisation (www.socialtrade.org).

    Cyclos is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Cyclos is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Cyclos; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 */
package nl.strohalm.cyclos.entities.ads;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Indexable;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.Currency;
import nl.strohalm.cyclos.entities.customization.fields.AdCustomField;
import nl.strohalm.cyclos.entities.customization.fields.AdCustomFieldValue;
import nl.strohalm.cyclos.entities.customization.images.AdImage;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.utils.CustomFieldsContainer;
import nl.strohalm.cyclos.utils.Period;
import nl.strohalm.cyclos.utils.StringValuedEnum;

import org.apache.commons.collections.CollectionUtils;

/**
 * A product / service advertisement by a member
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "ads")
public class Ad extends Entity implements CustomFieldsContainer<AdCustomField, AdCustomFieldValue>, Indexable {

    public static enum Relationships implements Relationship {
        CATEGORY("category"), CURRENCY("currency"), CUSTOM_VALUES("customValues"), IMAGES("images"), OWNER("owner");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static enum Status {
        ACTIVE(true), PERMANENT(true), SCHEDULED(false), EXPIRED(false);
        private final boolean active;

        private Status(final boolean active) {
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }
    }

    public static enum TradeType implements StringValuedEnum {
        OFFER("O"), SEARCH("S");
        private final String value;

        private TradeType(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 1552286239776108655L;
    private AdCategory category;
    private Collection<AdCustomFieldValue> customValues;
    private String description;
    private boolean externalPublication;
    private Collection<AdImage> images;
    private Member owner;
    private boolean permanent;
    private Currency currency;
    private BigDecimal price;
    private Period publicationPeriod;
    private String title;
    private TradeType tradeType;
    private Calendar creationDate;
    private Calendar deleteDate;
    private boolean html;
    private boolean membersNotified;

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = AdCategory.class)
    @JoinColumn(name = "category_id")
    public AdCategory getCategory() {
        return category;
    }

    @Column(name = "creation_date", updatable = false)
    public Calendar getCreationDate() {
        return creationDate;
    }

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency_id")
    public Currency getCurrency() {
        return currency;
    }

    @Transient
    @Override
    public Class<AdCustomField> getCustomFieldClass() {
        return AdCustomField.class;
    }

    @Transient
    @Override
    public Class<AdCustomFieldValue> getCustomFieldValueClass() {
        return AdCustomFieldValue.class;
    }

    @OneToMany(targetEntity = AdCustomFieldValue.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ad_id")
    @Override
    public Collection<AdCustomFieldValue> getCustomValues() {
        return customValues;
    }

    @Column(name = "delete_date")
    public Calendar getDeleteDate() {
        return deleteDate;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @OneToMany(targetEntity = AdImage.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ad_id")
    public Collection<AdImage> getImages() {
        return images;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "owner_id", nullable = false)
    public Member getOwner() {
        return owner;
    }

    @Column(name = "price", precision = 15, scale = 6)
    public BigDecimal getPrice() {
        return price;
    }

    @Transient
    public Period getPublicationPeriod() {
        return publicationPeriod;
    }

    @Column(name = "publication_start")
    public Calendar getPublicationStart() {
        return publicationPeriod.getBegin();
    }

    @Column(name = "publication_end")
    public Calendar getPublicationEnd() {
        return publicationPeriod.getEnd();
    }

    @Transient
    public Status getStatus() {
        if (permanent) {
            return Status.PERMANENT;
        } else {
            final Calendar begin = publicationPeriod == null ? null : publicationPeriod.getBegin();
            final Calendar end = publicationPeriod == null ? null : publicationPeriod.getEnd();
            final Calendar now = Calendar.getInstance();
            if (begin != null && begin.after(now)) {
                return Status.SCHEDULED;
            } else if (end != null && end.before(now)) {
                return Status.EXPIRED;
            }
        }
        return Status.ACTIVE;
    }

    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    public TradeType getTradeType() {
        return tradeType;
    }

    @Transient
    public boolean isDeleted() {
        return getDeleteDate() != null;
    }

    @Column(name = "external_publication", nullable = false)
    public boolean isExternalPublication() {
        return externalPublication;
    }

    @Transient
    public boolean isHasImages() {
        return CollectionUtils.isNotEmpty(getImages());
    }

    @Column(name = "is_html", nullable = false)
    public boolean isHtml() {
        return html;
    }

    @Column(name = "members_notified", nullable = false)
    public boolean isMembersNotified() {
        return membersNotified;
    }

    @Column(name = "permanent", nullable = false)
    public boolean isPermanent() {
        return permanent;
    }

    public void setCategory(final AdCategory category) {
        this.category = category;
    }

    public void setCreationDate(final Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    @Override
    public void setCustomValues(final Collection<AdCustomFieldValue> customValues) {
        this.customValues = customValues;
    }

    public void setDeleteDate(final Calendar deleteDate) {
        this.deleteDate = deleteDate;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setExternalPublication(final boolean externalPublication) {
        this.externalPublication = externalPublication;
    }

    public void setHtml(final boolean html) {
        this.html = html;
    }

    public void setImages(final Collection<AdImage> images) {
        this.images = images;
    }

    public void setMembersNotified(final boolean notified) {
        membersNotified = notified;
    }

    public void setOwner(final Member owner) {
        this.owner = owner;
    }

    public void setPermanent(final boolean permanent) {
        this.permanent = permanent;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setPublicationPeriod(final Period publicationPeriod) {
        this.publicationPeriod = publicationPeriod;
    }

    public void setPublicationStart(Calendar begin) {
        publicationPeriod.setBegin(begin);
    }

    public void setPublicationEnd(Calendar end) {
        publicationPeriod.setEnd(end);
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setTradeType(final TradeType tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public String toString() {
        return getId() + " - " + title;
    }

    @Override
    protected void appendVariableValues(final Map<String, Object> variables, final LocalSettings localSettings) {
        variables.put("title", getTitle());
        variables.put("price", localSettings.getNumberConverter().toString(getPrice()));
    }
}
