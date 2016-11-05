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
package nl.strohalm.cyclos.entities.groups;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.AccountType;
import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.entities.members.Member;

/**
 * A group for member's operators
 *
 * @author luis
 */
@Entity
@DiscriminatorValue(value = "O")
public class OperatorGroup extends Group {

    public static enum Relationships implements Relationship {
        MEMBER("member"), MAX_AMOUNT_PER_DAY_BY_TRANSFER_TYPE("maxAmountPerDayByTransferType"), CAN_VIEW_INFORMATION_OF("canViewInformationOf");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 6092174598805612471L;
    private Member member;
    private Map<TransferType, BigDecimal> maxAmountPerDayByTransferType;
    private Collection<AccountType> canViewInformationOf;

    @Override
    public BasicGroupSettings getBasicSettings() {
        return member.getGroup().getBasicSettings();
    }

    @ManyToMany(targetEntity = AccountType.class)
    @JoinTable(name = "group_operator_account_information_permissions",
            joinColumns = @JoinColumn(name = "owner_group_id"),
            inverseJoinColumns = @JoinColumn(name = "account_type_id"))
    public Collection<AccountType> getCanViewInformationOf() {
        return canViewInformationOf;
    }

    @ElementCollection
    @CollectionTable(name = "operator_groups_max_amount",
            joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "amount", precision = 15, scale = 6, nullable = false)
    @MapKeyJoinColumn(name = "transfer_type_id")
    public Map<TransferType, BigDecimal> getMaxAmountPerDayByTransferType() {
        return maxAmountPerDayByTransferType;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    @Transient
    @Override
    public Nature getNature() {
        return Nature.OPERATOR;
    }

    public void setCanViewInformationOf(final Collection<AccountType> canViewInformationOf) {
        this.canViewInformationOf = canViewInformationOf;
    }

    public void setMaxAmountPerDayByTransferType(final Map<TransferType, BigDecimal> maxAmountPerDayByTransferType) {
        this.maxAmountPerDayByTransferType = maxAmountPerDayByTransferType;
    }

    public void setMember(final Member member) {
        this.member = member;
    }

}
