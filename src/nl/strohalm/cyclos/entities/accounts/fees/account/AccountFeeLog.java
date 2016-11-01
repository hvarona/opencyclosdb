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
package nl.strohalm.cyclos.entities.accounts.fees.account;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.transactions.Invoice;
import nl.strohalm.cyclos.entities.accounts.transactions.Transfer;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.Amount;
import nl.strohalm.cyclos.utils.FormatObject;
import nl.strohalm.cyclos.utils.Period;

/**
 * An account fee log records an account fee execution (either manual or
 * scheduled fees)
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "account_fee_logs")
public class AccountFeeLog extends Entity {

    public static enum Relationships implements Relationship {
        ACCOUNT_FEE("accountFee"), TRANSFERS("transfers"), INVOICES("invoices"), PENDING_TO_CHARGE("pendingToCharge");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -1715437658356438694L;

    private AccountFee accountFee;
    private Calendar date;
    private Calendar finishDate;
    private BigDecimal freeBase;
    private Period period;
    private BigDecimal amount;
    private Integer totalMembers;
    private int failedMembers;
    private boolean rechargingFailed;
    private int rechargeAttempt;
    private Collection<Transfer> transfers;
    private Collection<Invoice> invoices;
    private Collection<Member> pendingToCharge;

    public AccountFeeLog() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = AccountFee.class)
    @JoinColumn(name = "account_fee_id")
    public AccountFee getAccountFee() {
        return accountFee;
    }

    @Column(nullable = false, precision = 15, scale = 6)
    public BigDecimal getAmount() {
        return amount;
    }

    @Transient
    public Amount getAmountValue() {
        final Amount amount = new Amount();
        amount.setType(accountFee.getChargeMode().getAmountType());
        amount.setValue(this.amount);
        return amount;
    }

    @Column(nullable = false)
    public Calendar getDate() {
        return date;
    }

    @Column(name = "failed_members", nullable = false)
    public int getFailedMembers() {
        return failedMembers;
    }

    @Column(name = "finish_date")
    public Calendar getFinishDate() {
        return finishDate;
    }

    @Column(name = "free_base", precision = 15, scale = 6)
    public BigDecimal getFreeBase() {
        return freeBase;
    }

    @OneToMany(targetEntity = Invoice.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_fee_log_id")
    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    @ManyToMany(targetEntity = Member.class, cascade = CascadeType.ALL)
    @JoinTable(name = "members_pending_charge",
            joinColumns = @JoinColumn(name = "account_fee_log_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    public Collection<Member> getPendingToCharge() {
        return pendingToCharge;
    }

    @Transient
    public Period getPeriod() {
        return period;
    }

    @Column(name = "begin_date")
    public Calendar getPeriodBegin() {
        return period.getBegin();
    }

    @Column(name = "end_date")
    public Calendar getPeriodEnd() {
        return period.getEnd();
    }

    @Column(name = "recharge_attempt", nullable = false)
    public int getRechargeAttempt() {
        return rechargeAttempt;
    }

    @Column(name = "total_members")
    public Integer getTotalMembers() {
        return totalMembers;
    }

    @OneToMany(targetEntity = Transfer.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_fee_log_id")
    public Collection<Transfer> getTransfers() {
        return transfers;
    }

    @Transient
    public boolean isFinished() {
        return finishDate != null;
    }

    @Column(name = "recharging_failed", nullable = false)
    public boolean isRechargingFailed() {
        return rechargingFailed;
    }

    @Transient
    public boolean isRunning() {
        return finishDate == null;
    }

    public void setAccountFee(final AccountFee accountFee) {
        this.accountFee = accountFee;
    }

    public void setAmount(final BigDecimal value) {
        amount = value;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setFailedMembers(final int failedMembers) {
        this.failedMembers = failedMembers;
    }

    public void setFinishDate(final Calendar finishDate) {
        this.finishDate = finishDate;
    }

    public void setFreeBase(final BigDecimal freeBase) {
        this.freeBase = freeBase;
    }

    public void setInvoices(final Collection<Invoice> invoices) {
        this.invoices = invoices;
    }

    public void setPendingToCharge(final Collection<Member> pendingToCharge) {
        this.pendingToCharge = pendingToCharge;
    }

    public void setPeriod(final Period period) {
        this.period = period;
    }

    public void setPeriodBegin(Calendar date) {
        period.setBegin(date);
    }

    public void setPeriodEnd(Calendar date) {
        period.setEnd(date);
    }

    public void setRechargeAttempt(final int rechargeAttempt) {
        this.rechargeAttempt = rechargeAttempt;
    }

    public void setRechargingFailed(final boolean rechargingFailed) {
        this.rechargingFailed = rechargingFailed;
    }

    public void setTotalMembers(final Integer totalMembers) {
        this.totalMembers = totalMembers;
    }

    public void setTransfers(final Collection<Transfer> transfers) {
        this.transfers = transfers;
    }

    @Override
    public String toString() {
        return getId() + " - " + accountFee + " at " + FormatObject.formatObject(date);
    }

}
