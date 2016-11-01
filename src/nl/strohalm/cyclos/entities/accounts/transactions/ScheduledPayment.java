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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.Account;
import nl.strohalm.cyclos.entities.accounts.AccountOwner;
import nl.strohalm.cyclos.entities.customization.fields.PaymentCustomFieldValue;
import nl.strohalm.cyclos.entities.members.Element;

/**
 * A scheduled payment is a set of transfers grouped in a single logical entity.
 *
 * @author luis, Jefferson Magno
 */
@Entity
@Table(name = "scheduled_payments")
public class ScheduledPayment extends Payment {

    public static enum Relationships implements Relationship {
        TRANSFERS("transfers");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 7335050802424888764L;
    private boolean reserveAmount;
    private boolean showToReceiver;
    private List<Transfer> transfers;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "type_id")
    @Override
    public TransferType getType() {
        return super.getType();
    }

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "to_account_id", nullable = false)
    @Override
    public Account getTo() {
        return super.getTo();
    }

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "from_account_id", nullable = false)
    @Override
    public Account getFrom() {
        return super.getFrom();
    }

    @Column(nullable = false)
    @Override
    public Calendar getDate() {
        return super.getDate();
    }

    @Column(nullable = false)
    @Override
    public BigDecimal getAmount() {
        return super.getAmount();
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "by_id")
    @Override
    public Element getBy() {
        return super.getBy();
    }

    @Column(name = "process_date")
    @Override
    public Calendar getProcessDate() {
        return super.getProcessDate();
    }

    @Column(name = "feedback_deadline")
    @Override
    public Calendar getTransactionFeedbackDeadline() {
        return super.getTransactionFeedbackDeadline();
    }

    @Column
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @OneToMany(targetEntity = PaymentCustomFieldValue.class)
    @JoinColumn(name = "scheduled_payment_id")
    @Override
    public Collection<PaymentCustomFieldValue> getCustomValues() {
        return super.getCustomValues();
    }

    /**
     * Returns the date of the first not processed transfer
     */
    @Transient
    @Override
    public Calendar getActualDate() {
        Calendar actualDate = null;
        Calendar lastDate = null;
        for (final Transfer transfer : transfers) {
            lastDate = transfer.getDate();
            if (transfer.getProcessDate() == null) {
                actualDate = transfer.getDate();
                break;
            }
        }
        return actualDate != null ? actualDate : lastDate;
    }

    @Transient
    @Override
    public Account getActualFrom() {
        return getFrom();
    }

    @Transient
    @Override
    public AccountOwner getActualFromOwner() {
        return getFromOwner();
    }

    @Transient
    @Override
    public Account getActualTo() {
        return getTo();
    }

    @Transient
    @Override
    public AccountOwner getActualToOwner() {
        return getToOwner();
    }

    /**
     * Returns the index of the first not processed transfer
     */
    @Transient
    public Integer getFirstOpenPaymentIndex() {
        int index = 0;
        for (final Transfer transfer : transfers) {
            index++;
            if (transfer.getProcessDate() == null) {
                break;
            }
        }
        return (index > 0) ? index : null;
    }

    /**
     * Returns the first not processed transfer or null if all transfers had
     * already been processed
     */
    @Transient
    public Transfer getFirstOpenTransfer() {
        Transfer firstOpenPayment = null;
        for (final Transfer transfer : transfers) {
            if (transfer.getProcessDate() == null) {
                firstOpenPayment = transfer;
                break;
            }
        }
        return firstOpenPayment;
    }

    @Transient
    @Override
    public Nature getNature() {
        return Nature.SCHEDULED_PAYMENT;
    }

    @Transient
    public int getNumberOfParcels() {
        return transfers.size();
    }

    @Transient
    public BigDecimal getProcessedPaymentAmount() {
        BigDecimal total = new BigDecimal(0);
        for (final Transfer transfer : transfers) {
            if (transfer.getProcessDate() != null) {
                total = total.add(transfer.getAmount());
            }
        }
        return total;
    }

    /**
     * Returns thenumber of processed payments
     */
    @Transient
    public int getProcessedPaymentCount() {
        int count = 0;
        for (final Transfer transfer : transfers) {
            if (transfer.getProcessDate() != null) {
                count++;
            }
        }
        return count;
    }

    @OneToMany(targetEntity = Transfer.class)
    @JoinColumn(name = "scheduled_payment_id")
    public List<Transfer> getTransfers() {
        return transfers;
    }

    @Column(name = "reserve_amount", nullable = false)
    public boolean isReserveAmount() {
        return reserveAmount;
    }

    @Column(name = "show_to_receiver", nullable = false)
    public boolean isShowToReceiver() {
        return showToReceiver;
    }

    public void setReserveAmount(final boolean reserveAmount) {
        this.reserveAmount = reserveAmount;
    }

    public void setShowToReceiver(final boolean showToReceiver) {
        this.showToReceiver = showToReceiver;
    }

    public void setTransfers(final List<Transfer> transfers) {
        this.transfers = transfers;
    }

}
