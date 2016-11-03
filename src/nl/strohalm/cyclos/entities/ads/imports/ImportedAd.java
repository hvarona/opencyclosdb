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
package nl.strohalm.cyclos.entities.ads.imports;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
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
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.ads.Ad.TradeType;
import nl.strohalm.cyclos.entities.ads.AdCategory;
import nl.strohalm.cyclos.entities.customization.fields.AdCustomField;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.CustomFieldsContainer;
import nl.strohalm.cyclos.utils.Period;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * An imported ad, not yet processed
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "imported_ads")
public class ImportedAd extends Entity implements CustomFieldsContainer<AdCustomField, ImportedAdCustomFieldValue> {

    public static enum Relationships implements Relationship {
        AD_IMPORT("adImport"), IMPORTED_CATEGORY("importedCategory"), EXISTING_CATEGORY("existingCategory"), CUSTOM_VALUES("customValues"), OWNER("owner");

        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum Status implements StringValuedEnum {
        SUCCESS, INVALID_CATEGORY, MISSING_CATEGORY, TOO_MANY_CATEGORY_LEVELS, INVALID_OWNER, MISSING_OWNER, MISSING_TITLE, MISSING_DESCRIPTION, MISSING_PUBLICATION_PERIOD, INVALID_PUBLICATION_START, INVALID_PUBLICATION_END, PUBLICATION_BEGIN_AFTER_END, MAX_PUBLICATION_EXCEEDED, INVALID_PRICE, INVALID_CUSTOM_FIELD, MISSING_CUSTOM_FIELD, UNKNOWN_ERROR;

        public String getValue() {
            return name();
        }
    }

    private static final long serialVersionUID = 2434556061835999931L;
    private AdImport _import;
    private Integer lineNumber;
    private Status status;
    private String errorArgument1;
    private String errorArgument2;
    private AdCategory existingCategory;
    private ImportedAdCategory importedCategory;
    private Collection<ImportedAdCustomFieldValue> customValues;
    private String description;
    private boolean html;
    private boolean externalPublication;
    private Member owner;
    private boolean permanent;
    private BigDecimal price;
    private Period publicationPeriod;
    private String title;
    private TradeType tradeType;

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Transient
    public Class<AdCustomField> getCustomFieldClass() {
        return AdCustomField.class;
    }

    @Transient
    public Class<ImportedAdCustomFieldValue> getCustomFieldValueClass() {
        return ImportedAdCustomFieldValue.class;
    }

    @OneToMany(targetEntity = ImportedAdCustomFieldValue.class)
    @JoinColumn(name = "imported_ad_id")
    public Collection<ImportedAdCustomFieldValue> getCustomValues() {
        return customValues;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "error_argument1", length = 200)
    public String getErrorArgument1() {
        return errorArgument1;
    }

    @Column(name = "error_argument2", length = 200)
    public String getErrorArgument2() {
        return errorArgument2;
    }

    @ManyToOne(targetEntity = AdCategory.class)
    @JoinColumn(name = "existing_category_id")
    public AdCategory getExistingCategory() {
        return existingCategory;
    }

    @ManyToOne(targetEntity = ImportedAd.class)
    @JoinColumn(name = "import_id")
    public AdImport getImport() {
        return _import;
    }

    @ManyToOne(targetEntity = AdCategory.class)
    @JoinColumn(name = "imported_category_id")
    public ImportedAdCategory getImportedCategory() {
        return importedCategory;
    }

    @Column(name = "line_number")
    public Integer getLineNumber() {
        return lineNumber;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "owner_id")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    public Status getStatus() {
        return status;
    }

    @Column(name = "title", length = 100)
    public String getTitle() {
        return title;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type")
    public TradeType getTradeType() {
        return tradeType;
    }

    @Column(name = "external_upblication", nullable = false)
    public boolean isExternalPublication() {
        return externalPublication;
    }

    @Column(name = "is_html", nullable = false)
    public boolean isHtml() {
        return html;
    }

    @Column(name = "permanent", nullable = false)
    public boolean isPermanent() {
        return permanent;
    }

    public void setCustomValues(final Collection<ImportedAdCustomFieldValue> customValues) {
        this.customValues = customValues;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setErrorArgument1(final String errorArgument1) {
        this.errorArgument1 = errorArgument1;
    }

    public void setErrorArgument2(final String errorArgument2) {
        this.errorArgument2 = errorArgument2;
    }

    public void setExistingCategory(final AdCategory existingCategory) {
        this.existingCategory = existingCategory;
    }

    public void setExternalPublication(final boolean externalPublication) {
        this.externalPublication = externalPublication;
    }

    public void setHtml(final boolean html) {
        this.html = html;
    }

    public void setImport(final AdImport adImport) {
        _import = adImport;
    }

    public void setImportedCategory(final ImportedAdCategory importedCategory) {
        this.importedCategory = importedCategory;
    }

    public void setLineNumber(final Integer lineNumber) {
        this.lineNumber = lineNumber;
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

    public void setPublicationStart(Calendar start) {
        publicationPeriod.setBegin(start);
    }

    public void setPublicationEnd(Calendar end) {
        publicationPeriod.setEnd(end);
    }

    public void setStatus(final Status status) {
        this.status = status;
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

}
