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
package nl.strohalm.cyclos.entities.members.brokerings;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.fees.transaction.BrokerCommission;
import nl.strohalm.cyclos.entities.accounts.fees.transaction.BrokerCommission.When;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.Amount;

/**
 * Default commission settings for a broker
 *
 * @author Jefferson Magno
 */
@javax.persistence.Entity
@Table(name = "default_broker_commissions")
public class DefaultBrokerCommission extends Entity {

    public static enum Relationships implements Relationship {
        BROKER("broker"), BROKER_COMMISSION("brokerCommission");

        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum Status {
        ACTIVE, INACTIVE, SUSPENDED,
    }

    private static final long serialVersionUID = -4791497274620475610L;
    private Member broker;
    private BrokerCommission brokerCommission;
    private Amount amount;
    private Integer count;
    private When when;
    private boolean setByBroker;
    private boolean suspended;

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Transient
    public Amount getAmount() {
        return amount;
    }

    @Column(name = "amount", precision = 15, scale = 6, nullable = false)
    public BigDecimal getAmountValue() {
        return amount.getValue();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "amount_typee", nullable = false)
    public Amount.Type getAmountType() {
        return amount.getType();
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "broker_id", nullable = false, updatable = false)
    public Member getBroker() {
        return broker;
    }

    @ManyToOne(targetEntity = BrokerCommission.class)
    @JoinColumn(name = "broker_commission_id", nullable = false)
    public BrokerCommission getBrokerCommission() {
        return brokerCommission;
    }

    @Column(name = "when_count")
    public Integer getCount() {
        return count;
    }

    @Transient
    public Status getStatus() {
        if (suspended) {
            return Status.SUSPENDED;
        } else if (brokerCommission.isEnabled()) {
            return Status.ACTIVE;
        } else {
            return Status.INACTIVE;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "when_apply", nullable = false)
    public When getWhen() {
        return when;
    }

    @Column(name = "set_by_broker")
    public boolean isSetByBroker() {
        return setByBroker;
    }

    @Column(name = "suspended")
    public boolean isSuspended() {
        return suspended;
    }

    public void setAmount(final Amount amount) {
        this.amount = amount;
    }

    public void setAmountValue(BigDecimal value) {
        amount.setValue(value);
    }

    public void setAmountType(Amount.Type type) {
        amount.setType(type);
    }

    public void setBroker(final Member broker) {
        this.broker = broker;
    }

    public void setBrokerCommission(final BrokerCommission brokerCommission) {
        this.brokerCommission = brokerCommission;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    public void setSetByBroker(final boolean setByBroker) {
        this.setByBroker = setByBroker;
    }

    public void setSuspended(final boolean suspended) {
        this.suspended = suspended;
    }

    public void setWhen(final When when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return getId() + " - " + broker;
    }

}
