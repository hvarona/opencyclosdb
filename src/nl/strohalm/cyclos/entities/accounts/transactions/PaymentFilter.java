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
package nl.strohalm.cyclos.entities.accounts.transactions;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.AccountType;
import nl.strohalm.cyclos.entities.groups.Group;

/**
 * A payment filter groups transfer types for reports or account history
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "payment_filters")
public class PaymentFilter extends Entity {

    public static enum Relationships implements Relationship {

        ACCOUNT_TYPE("accountType"), GROUPS("groups"), TRANSFER_TYPES("transferTypes");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -13720438518829957L;
    private AccountType accountType;
    private String name;
    private String description;
    private boolean showInAccountHistory;
    private boolean showInReports;
    private Collection<TransferType> transferTypes;
    private Collection< Group> groups;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = AccountType.class)
    @JoinColumn(name = "account_type_id", nullable = false)
    public AccountType getAccountType() {
        return accountType;
    }

    @Column
    public String getDescription() {
        return description;
    }

    @ManyToMany(targetEntity = Group.class)
    @JoinTable(name = "groups_payment_filters",
            joinColumns = @JoinColumn(name = "payment_filter_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Collection< Group> getGroups() {
        return groups;
    }

    @Column(nullable = false, length = 100)
    public String getName() {
        return name;
    }

    @ManyToMany(targetEntity = TransferType.class)
    @JoinTable(name = "transfer_types_payment_filters",
            joinColumns = @JoinColumn(name = "payment_filter_id"),
            inverseJoinColumns = @JoinColumn(name = "transfer_type_id"))
    public Collection<TransferType> getTransferTypes() {
        return transferTypes;
    }

    @Column(name = "show_in_account_history", nullable = false)
    public boolean isShowInAccountHistory() {
        return showInAccountHistory;
    }

    @Column(name = "show_in_reports", nullable = false)
    public boolean isShowInReports() {
        return showInReports;
    }

    public void setAccountType(final AccountType accountType) {
        this.accountType = accountType;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setGroups(final Collection< Group> groups) {
        this.groups = groups;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setShowInAccountHistory(final boolean showInAccountHistory) {
        this.showInAccountHistory = showInAccountHistory;
    }

    public void setShowInReports(final boolean showInReports) {
        this.showInReports = showInReports;
    }

    public void setTransferTypes(final Collection<TransferType> transferTypes) {
        this.transferTypes = transferTypes;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }

}
