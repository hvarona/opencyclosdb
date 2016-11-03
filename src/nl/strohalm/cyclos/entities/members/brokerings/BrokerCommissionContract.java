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
import java.util.Calendar;
import java.util.Map;
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
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.utils.Amount;
import nl.strohalm.cyclos.utils.Period;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * Broker commission contract
 *
 * @author Jefferson Magno
 */
@javax.persistence.Entity
@Table(name = "broker_commission_contracts")
public class BrokerCommissionContract extends Entity {

    public static enum Relationships implements Relationship {
        BROKERING("brokering"), BROKER_COMMISSION("brokerCommission");

        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static enum Status implements StringValuedEnum {
        PENDING("P"), ACCEPTED("T"), DENIED("D"), EXPIRED("E"), ACTIVE("A"), CLOSED("L"), SUSPENDED("S"), CANCELLED("C");

        private String value;

        private Status(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = -4791497274620475610L;

    private Brokering brokering;
    private BrokerCommission brokerCommission;
    private Period period;
    private Amount amount;
    private Status status;
    private Status statusBeforeSuspension;
    private Element cancelledBy;

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

    @Column(name = "amount_value", precision = 15, scale = 6, nullable = false)
    public BigDecimal getAmountValue() {
        return amount.getValue();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "amount_typee", nullable = false)
    public Amount.Type getAmountType() {
        return amount.getType();
    }

    @ManyToOne(targetEntity = BrokerCommission.class)
    @JoinColumn(name = "broker_commission_id", updatable = false)
    public BrokerCommission getBrokerCommission() {
        return brokerCommission;
    }

    @ManyToOne(targetEntity = Brokering.class)
    @JoinColumn(name = "brokering_id", updatable = false)
    public Brokering getBrokering() {
        return brokering;
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "cancelled_by_id")
    public Element getCancelledBy() {
        return cancelledBy;
    }

    @Transient
    public Period getPeriod() {
        return period;
    }

    @Column(name = "start_date", nullable = false)
    public Calendar getStartDate() {
        return period.getBegin();
    }

    @Column(name = "end_date")
    public Calendar getEndDate() {
        return period.getEnd();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public Status getStatus() {
        return status;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status_before_suspension")
    public Status getStatusBeforeSuspension() {
        return statusBeforeSuspension;
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

    public void setBrokerCommission(final BrokerCommission brokerCommission) {
        this.brokerCommission = brokerCommission;
    }

    public void setBrokering(final Brokering brokering) {
        this.brokering = brokering;
    }

    public void setCancelledBy(final Element cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public void setPeriod(final Period period) {
        this.period = period;
    }

    public void setStartDate(Calendar start) {
        this.period.setBegin(start);
    }

    public void setEndDate(Calendar end) {
        this.period.setEnd(end);
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setStatusBeforeSuspension(final Status statusBeforeSuspension) {
        this.statusBeforeSuspension = statusBeforeSuspension;
    }

    @Override
    public String toString() {
        return "" + getId();
    }

    @Override
    protected void appendVariableValues(final Map<String, Object> variables, final LocalSettings localSettings) {
        variables.put("broker", getBrokering().getBroker().getName());
        variables.put("broker_login", getBrokering().getBroker().getUsername());
        variables.put("member", getBrokering().getBrokered().getName());
        variables.put("member_login", getBrokering().getBrokered().getUsername());
        variables.put("start_date", localSettings.getRawDateConverter().toString(getPeriod().getBegin()));
        variables.put("end_date", localSettings.getRawDateConverter().toString(getPeriod().getEnd()));
        variables.put("amount", localSettings.getAmountConverter().toString(getAmount()));
    }

}
