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
package nl.strohalm.cyclos.entities.services;

import java.util.Set;
import javax.persistence.CascadeType;
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
import nl.strohalm.cyclos.entities.access.Channel;
import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.entities.groups.MemberGroup;
import nl.strohalm.cyclos.entities.members.Member;

/**
 * A remote host that may access specific web services
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "service_clients")
public class ServiceClient extends Entity {

    public static enum Relationships implements Relationship {

        MEMBER("member"), CHANNEL("channel"), PERMISSIONS("permissions"), DO_PAYMENT_TYPES("doPaymentTypes"), RECEIVE_PAYMENT_TYPES("receivePaymentTypes"), CHARGEBACK_PAYMENT_TYPES("chargebackPaymentTypes"), MANAGE_GROUPS("mnageGroups");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -7219751905408334999L;
    private String name;
    private String username;
    private String password;
    private String addressBegin;
    private String addressEnd;
    private String hostname;
    private Member member;
    private boolean credentialsRequired;
    private boolean ignoreRegistrationValidations;
    private Channel channel;
    private Set<ServiceOperation> permissions;
    private Set<TransferType> doPaymentTypes;
    private Set<TransferType> receivePaymentTypes;
    private Set<TransferType> chargebackPaymentTypes;
    private Set<MemberGroup> manageGroups;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "address_begin")
    public String getAddressBegin() {
        return addressBegin;
    }

    @Column(name = "address_end")
    public String getAddressEnd() {
        return addressEnd;
    }

    @ManyToOne(targetEntity = Channel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "channel_id")
    public Channel getChannel() {
        return channel;
    }

    @ManyToMany(targetEntity = TransferType.class)
    @JoinTable(name = "service_clients_chargeback_payment_types",
            joinColumns = @JoinColumn(name = "service_client_id"),
            inverseJoinColumns = @JoinColumn(name = "transfer_type_id"))
    public Set<TransferType> getChargebackPaymentTypes() {
        return chargebackPaymentTypes;
    }

    @ManyToMany(targetEntity = TransferType.class)
    @JoinTable(name = "service_clients_do_payment_types",
            joinColumns = @JoinColumn(name = "service_client_id"),
            inverseJoinColumns = @JoinColumn(name = "transfer_type_id"))
    public Set<TransferType> getDoPaymentTypes() {
        return doPaymentTypes;
    }

    @Column(length = 100, nullable = false)
    public String getHostname() {
        return hostname;
    }

    @ManyToMany(targetEntity = MemberGroup.class)
    @JoinTable(name = "service_clients_manage_groups",
            joinColumns = @JoinColumn(name = "service_client_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Set<MemberGroup> getManageGroups() {
        return manageGroups;
    }

    @ManyToOne(targetEntity = Member.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    @Column(length = 100, nullable = false)
    @Override
    public String getName() {
        return name;
    }

    @Column(length = 100)
    public String getPassword() {
        return password;
    }

    @ElementCollection(targetClass = ServiceOperation.class)
    @CollectionTable(name = "service_client_permissions",
            joinColumns = @JoinColumn(name = "service_client_id"))
    @Column(name = "operation", nullable = false)
    @Enumerated(EnumType.STRING)
    public Set<ServiceOperation> getPermissions() {
        return permissions;
    }

    @ManyToMany(targetEntity = TransferType.class)
    @JoinTable(name = "service_clients_receive_payment_types",
            joinColumns = @JoinColumn(name = "service_client_id"),
            inverseJoinColumns = @JoinColumn(name = "transfer_type_id"))
    public Set<TransferType> getReceivePaymentTypes() {
        return receivePaymentTypes;
    }

    @Column(length = 100)
    public String getUsername() {
        return username;
    }

    @Column(name = "credentials_required", nullable = false)
    public boolean isCredentialsRequired() {
        return credentialsRequired;
    }

    @Column(name = "ignore_registration_validations", nullable = false)
    public boolean isIgnoreRegistrationValidations() {
        return ignoreRegistrationValidations;
    }

    public void setAddressBegin(final String addressBegin) {
        this.addressBegin = addressBegin;
    }

    public void setAddressEnd(final String addressEnd) {
        this.addressEnd = addressEnd;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }

    public void setChargebackPaymentTypes(final Set<TransferType> chargebackPaymentTypes) {
        this.chargebackPaymentTypes = chargebackPaymentTypes;
    }

    public void setCredentialsRequired(final boolean credentialsRequired) {
        this.credentialsRequired = credentialsRequired;
    }

    public void setDoPaymentTypes(final Set<TransferType> doPaymentTypes) {
        this.doPaymentTypes = doPaymentTypes;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public void setIgnoreRegistrationValidations(final boolean ignoreRegistrationValidations) {
        this.ignoreRegistrationValidations = ignoreRegistrationValidations;
    }

    public void setManageGroups(final Set<MemberGroup> manageGroups) {
        this.manageGroups = manageGroups;
    }

    public void setMember(final Member member) {
        this.member = member;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setPermissions(final Set<ServiceOperation> permissions) {
        this.permissions = permissions;
    }

    public void setReceivePaymentTypes(final Set<TransferType> receivePaymentTypes) {
        this.receivePaymentTypes = receivePaymentTypes;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }

}
