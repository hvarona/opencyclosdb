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
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import nl.strohalm.cyclos.services.transactions.TransactionSummaryVO;
import nl.strohalm.cyclos.utils.Amount;
import nl.strohalm.cyclos.utils.Period;

/**
 * Status about a brokering commission. Stores data about commissions paid from
 * system to broker or commissions paid by members to brokers. Transaction
 * related to broker commission contracts are not included in the status
 *
 * @author Jefferson Magno
 */
@javax.persistence.Entity
@Table(name = "brokering_commission_status")
public class BrokeringCommissionStatus extends Entity {

    public static enum Relationships implements Relationship {
        BROKERING("brokering"), BROKER_COMMISSION("brokerCommission");

        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -4791497274620475610L;

    private Brokering brokering;
    private BrokerCommission brokerCommission;
    private BrokerCommission.When when;
    private Amount amount;
    private Period period;
    private Calendar creationDate;
    private Calendar expiryDate;
    private Integer maxCount;
    private TransactionSummaryVO total;

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

    @ManyToOne(targetEntity = BrokerCommission.class)
    @JoinColumn(name = "broker_commission_id", nullable = false)
    public BrokerCommission getBrokerCommission() {
        return brokerCommission;
    }

    @ManyToOne(targetEntity = Brokering.class)
    @JoinColumn(name = "brokering_id", nullable = false)
    public Brokering getBrokering() {
        return brokering;
    }

    @Column(name = "creation_date")
    public Calendar getCreationDate() {
        return creationDate;
    }

    @Column(name = "expiry_date")
    public Calendar getExpiryDate() {
        return expiryDate;
    }

    @Column(name = "max_count")
    public Integer getMaxCount() {
        return maxCount;
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

    @Embedded
    public TransactionSummaryVO getTotal() {
        return total;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "when_apply", nullable = false)
    public BrokerCommission.When getWhen() {
        return when;
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

    public void setCreationDate(final Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public void setExpiryDate(final Calendar expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setMaxCount(final Integer maxCount) {
        this.maxCount = maxCount;
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

    public void setTotal(final TransactionSummaryVO total) {
        this.total = total;
    }

    public void setWhen(final BrokerCommission.When when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return "" + getId();
    }

}
