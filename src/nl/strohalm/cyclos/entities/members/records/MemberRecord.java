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
package nl.strohalm.cyclos.entities.members.records;

import java.util.Calendar;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Indexable;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.customization.fields.MemberRecordCustomField;
import nl.strohalm.cyclos.entities.customization.fields.MemberRecordCustomFieldValue;
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.CustomFieldsContainer;

/**
 * A member record is a set of values for a member record type
 *
 * @author Jefferson Magno
 */
@javax.persistence.Entity
@Table(name = "member_records")
public class MemberRecord extends Entity implements CustomFieldsContainer<MemberRecordCustomField, MemberRecordCustomFieldValue>, Indexable {

    public static enum Relationships implements Relationship {

        TYPE("type"), ELEMENT("element"), BY("by"), MODIFIED_BY("modifiedBy"), CUSTOM_VALUES("customValues");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -3314730704013643086L;
    private MemberRecordType type;
    private Element element;
    private Element by;
    private Calendar date;
    private Element modifiedBy;
    private Calendar lastModified;
    private Collection<MemberRecordCustomFieldValue> customValues;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "by_id")
    public Element getBy() {
        return by;
    }

    @Transient
    @Override
    public Class<MemberRecordCustomField> getCustomFieldClass() {
        return MemberRecordCustomField.class;
    }

    @Transient
    @Override
    public Class<MemberRecordCustomFieldValue> getCustomFieldValueClass() {
        return MemberRecordCustomFieldValue.class;
    }

    @OneToMany(targetEntity = MemberRecordCustomFieldValue.class)
    @JoinColumn(name = "member_record_id")
    @Override
    public Collection<MemberRecordCustomFieldValue> getCustomValues() {
        return customValues;
    }

    @Column(nullable = false, updatable = false)
    public Calendar getDate() {
        return date;
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "element_id")
    public Element getElement() {
        return element;
    }

    @Column(name = "last_modified")
    public Calendar getLastModified() {
        return lastModified;
    }

    @Transient
    public Member getMember() {
        final Element element = getElement();
        return element instanceof Member ? (Member) element : null;
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "modified_by_id")
    public Element getModifiedBy() {
        return modifiedBy;
    }

    @ManyToOne(targetEntity = MemberRecordType.class)
    @JoinColumn(name = "member_record_type_id")
    public MemberRecordType getType() {
        return type;
    }

    public void setBy(final Element by) {
        this.by = by;
    }

    @Override
    public void setCustomValues(final Collection<MemberRecordCustomFieldValue> customValues) {
        this.customValues = customValues;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setElement(final Element receiver) {
        element = receiver;
    }

    public void setLastModified(final Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public void setModifiedBy(final Element modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setType(final MemberRecordType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getId() + " type: " + type + " for " + element;
    }

}
