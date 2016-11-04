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
package nl.strohalm.cyclos.entities.customization.fields;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.members.Member;

/**
 * The association between a custom field and a given entity. The value property
 * is transient, containing one of stringValue or the value of the related
 * possibleValue. Persistent are stringValue (when the field is not enumerated)
 * or possibleValue (when enumerated).
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "custom_field_values")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subclass")
public abstract class CustomFieldValue extends Entity {

    public static enum Relationships implements Relationship {

        FIELD("field"), POSSIBLE_VALUE("possibleValue");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -1364762150524373384L;

    private CustomField field;
    private CustomFieldPossibleValue possibleValue;
    private Member memberValue;
    private String value;
    private String stringValue;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = CustomField.class)
    @JoinColumn(name = "field_id")
    public CustomField getField() {
        return field;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_value_id")
    public Member getMemberValue() {
        return memberValue;
    }

    @Transient
    public abstract Object getOwner();

    @ManyToOne(targetEntity = CustomFieldPossibleValue.class)
    @JoinColumn(name = "possible_value_id")
    public CustomFieldPossibleValue getPossibleValue() {
        return possibleValue;
    }

    @Column(name = "string_value", length = 4000)
    public String getStringValue() {
        return stringValue;
    }

    @Transient
    public String getValue() {
        return value;
    }

    public void setField(final CustomField field) {
        this.field = field;
    }

    public void setMemberValue(final Member memberValue) {
        this.memberValue = memberValue;
        try {
            value = memberValue.getId().toString();
        } catch (final Exception e) {
            // Ok
        }
    }

    public abstract void setOwner(Object owner);

    public void setPossibleValue(final CustomFieldPossibleValue possibleValue) {
        this.possibleValue = possibleValue;
        try {
            value = possibleValue.getValue();
        } catch (final Exception e) {
            // Ok
        }
    }

    public void setStringValue(final String stringValue) {
        this.stringValue = stringValue;
        value = stringValue;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getId() + " - field: " + field + " = " + value;
    }
}
