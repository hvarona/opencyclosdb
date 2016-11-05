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
package nl.strohalm.cyclos.entities.members.preferences;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import nl.strohalm.cyclos.entities.accounts.guarantees.GuaranteeType;
import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.entities.alerts.MemberAlert;
import nl.strohalm.cyclos.entities.alerts.SystemAlert;
import nl.strohalm.cyclos.entities.groups.MemberGroup;
import nl.strohalm.cyclos.entities.members.Administrator;
import nl.strohalm.cyclos.entities.members.messages.MessageCategory;

/**
 * Notification Preference for an administrator
 *
 * @author luis
 * @author Lucas Geiss
 */
@javax.persistence.Entity
@Table(name = "admin_notification_preferences")
public class AdminNotificationPreference extends Entity {

    public static enum Relationships implements Relationship {
        ADMIN("admin"), TRANSFER_TYPES("transferTypes"), MESSAGE_CATEGORIES("messageCategories"), SYSTEM_ALERTS("systemAlerts"), MEMBER_ALERTS("memberAlerts"), NEW_PENDING_PAYMENTS("newPendingPayments"), NEW_MEMBERS("newMembers");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -4791497274620475610L;
    private boolean applicationErrors;
    private boolean systemInvoices;
    private Administrator admin;
    private Set<TransferType> transferTypes;
    private Set<TransferType> newPendingPayments;
    private Set<GuaranteeType> guaranteeTypes;
    private Set<MessageCategory> messageCategories;
    private Set<SystemAlert.Alerts> systemAlerts;
    private Set<MemberAlert.Alerts> memberAlerts;
    private Set<MemberGroup> newMembers;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Administrator.class)
    @JoinColumn(name = "admin_id")
    public Administrator getAdmin() {
        return admin;
    }

    @ManyToMany(targetEntity = GuaranteeType.class)
    @JoinTable(name = "admin_preferences_guarantee_types",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "guarantee_type_id"))
    public Set<GuaranteeType> getGuaranteeTypes() {
        return guaranteeTypes;
    }

    @ElementCollection(targetClass = Enum.class)
    @CollectionTable(name = "admin_preferences_member_alerts",
            joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    public Set<MemberAlert.Alerts> getMemberAlerts() {
        return memberAlerts;
    }

    @ManyToMany(targetEntity = MessageCategory.class)
    @JoinTable(name = "admin_preferences_message_categories",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "message_category_id"))
    public Set<MessageCategory> getMessageCategories() {
        return messageCategories;
    }

    @ManyToMany(targetEntity = MemberGroup.class)
    @JoinTable(name = "admin_preferences_new_members",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Set<MemberGroup> getNewMembers() {
        return newMembers;
    }

    @ManyToMany(targetEntity = TransferType.class)
    @JoinTable(name = "admin_preferences_new_pending_payments",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "transfer_type_id"))
    public Set<TransferType> getNewPendingPayments() {
        return newPendingPayments;
    }

    @ElementCollection(targetClass = Enum.class)
    @CollectionTable(name = "admin_preferences_system_alerts",
            joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    public Set<SystemAlert.Alerts> getSystemAlerts() {
        return systemAlerts;
    }

    @ManyToMany(targetEntity = TransferType.class)
    @JoinTable(name = "admin_preferences_transfer_types",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "transfer_type_id"))
    public Set<TransferType> getTransferTypes() {
        return transferTypes;
    }

    @Column(name = "application_errors", nullable = false)
    public boolean isApplicationErrors() {
        return applicationErrors;
    }

    @Column(name = "system_invoices", nullable = false)
    public boolean isSystemInvoices() {
        return systemInvoices;
    }

    public void setAdmin(final Administrator admin) {
        this.admin = admin;
    }

    public void setApplicationErrors(final boolean applicationErrors) {
        this.applicationErrors = applicationErrors;
    }

    public void setGuaranteeTypes(final Set<GuaranteeType> guaranteeTypes) {
        this.guaranteeTypes = guaranteeTypes;
    }

    public void setMemberAlerts(final Set<MemberAlert.Alerts> memberAlerts) {
        this.memberAlerts = memberAlerts;
    }

    public void setMessageCategories(final Set<MessageCategory> messageCategories) {
        this.messageCategories = messageCategories;
    }

    public void setNewMembers(final Set<MemberGroup> newMembers) {
        this.newMembers = newMembers;
    }

    public void setNewPendingPayments(final Set<TransferType> newPendingPayments) {
        this.newPendingPayments = newPendingPayments;
    }

    public void setSystemAlerts(final Set<SystemAlert.Alerts> systemAlerts) {
        this.systemAlerts = systemAlerts;
    }

    public void setSystemInvoices(final boolean systemInvoices) {
        this.systemInvoices = systemInvoices;
    }

    public void setTransferTypes(final Set<TransferType> transferTypes) {
        this.transferTypes = transferTypes;
    }

    @Override
    public String toString() {
        return getId() + " - " + admin;
    }
}
