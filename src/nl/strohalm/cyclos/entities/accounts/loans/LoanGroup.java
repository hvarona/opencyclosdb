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
package nl.strohalm.cyclos.entities.accounts.loans;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.customization.fields.LoanGroupCustomField;
import nl.strohalm.cyclos.entities.customization.fields.LoanGroupCustomFieldValue;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.CustomFieldsContainer;

/**
 * A loan group represents a group of members that receive a loan. This is
 * common in microcredit operations. The transfer itself is credited in the
 * responsible member account.
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name="loan_groups")
public class LoanGroup extends Entity implements CustomFieldsContainer<LoanGroupCustomField, LoanGroupCustomFieldValue>, Comparable<LoanGroup> {

    public static enum Relationships implements Relationship {
        CUSTOM_VALUES("customValues"), LOANS("loans"), MEMBERS("members");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -5589958336162094616L;
    private String description;
    private Collection<Loan> loans;
    private Collection<Member> members;
    private String name;
    private Collection<LoanGroupCustomFieldValue> customValues;

    public int compareTo(final LoanGroup o) {
        return getName().compareTo(o.getName());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Transient
    public Class<LoanGroupCustomField> getCustomFieldClass() {
        return LoanGroupCustomField.class;
    }

    @Transient
    public Class<LoanGroupCustomFieldValue> getCustomFieldValueClass() {
        return LoanGroupCustomFieldValue.class;
    }

    @OneToMany(targetEntity = LoanGroupCustomFieldValue.class)
    @JoinColumn(name = "loan_group_id")
    public Collection<LoanGroupCustomFieldValue> getCustomValues() {
        return customValues;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @OneToMany(targetEntity = Loan.class)
    @JoinColumn(name = "loan_group_id")
    public Collection<Loan> getLoans() {
        return loans;
    }

    @ManyToMany(targetEntity = Member.class)
    @JoinTable(name = "members_loan_groups",
            joinColumns = @JoinColumn(name = "loan_group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    public Collection<Member> getMembers() {
        return members;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setCustomValues(final Collection<LoanGroupCustomFieldValue> customValues) {
        this.customValues = customValues;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setLoans(final Collection<Loan> loans) {
        this.loans = loans;
    }

    public void setMembers(final Collection<Member> members) {
        this.members = members;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }
}
