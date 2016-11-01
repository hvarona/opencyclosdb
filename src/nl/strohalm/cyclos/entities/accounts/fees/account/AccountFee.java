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
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import nl.strohalm.cyclos.entities.accounts.MemberAccountType;
import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.entities.groups.MemberGroup;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.utils.Amount;
import nl.strohalm.cyclos.utils.DateHelper;
import nl.strohalm.cyclos.utils.Period;
import nl.strohalm.cyclos.utils.StringValuedEnum;
import nl.strohalm.cyclos.utils.TimePeriod;
import nl.strohalm.cyclos.utils.TimePeriod.Field;
import nl.strohalm.cyclos.utils.WeekDay;

/**
 * Taxes are charged over all accounts of a given type
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "account_fees")
public class AccountFee extends Entity {

    public enum ChargeMode implements StringValuedEnum {
        FIXED("FA", Amount.Type.FIXED), VOLUME_PERCENTAGE("VP", Amount.Type.PERCENTAGE), NEGATIVE_VOLUME_PERCENTAGE("NV", Amount.Type.PERCENTAGE), BALANCE_PERCENTAGE("BP", Amount.Type.PERCENTAGE), NEGATIVE_BALANCE_PERCENTAGE("NB", Amount.Type.PERCENTAGE);

        private final String value;
        private final Amount.Type amountType;

        private ChargeMode(final String value, final Amount.Type amountType) {
            this.value = value;
            this.amountType = amountType;
        }

        public Amount.Type getAmountType() {
            return amountType;
        }

        @Override
        public String getValue() {
            return value;
        }

        public boolean isBalance() {
            return this == BALANCE_PERCENTAGE || this == NEGATIVE_BALANCE_PERCENTAGE;
        }

        public boolean isFixed() {
            return this == FIXED;
        }

        public boolean isNegative() {
            return this == NEGATIVE_BALANCE_PERCENTAGE || this == NEGATIVE_VOLUME_PERCENTAGE;
        }

        public boolean isVolume() {
            return this == VOLUME_PERCENTAGE || this == NEGATIVE_VOLUME_PERCENTAGE;
        }
    }

    public enum InvoiceMode implements StringValuedEnum {
        NOT_ENOUGH_CREDITS("C"), NEVER("N"), ALWAYS("A");
        private final String value;

        private InvoiceMode(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum PaymentDirection implements StringValuedEnum {
        TO_SYSTEM("S"), TO_MEMBER("M");
        private final String value;

        private PaymentDirection(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public static enum Relationships implements Relationship {
        ACCOUNT_TYPE("accountType"), GROUPS("groups"), LOGS("logs"), TRANSFER_TYPE("transferType");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum RunMode implements StringValuedEnum {
        MANUAL("M"), SCHEDULED("S");
        private final String value;

        private RunMode(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = -3296543930198792794L;

    private MemberAccountType accountType;

    private String name;
    private String description;
    private ChargeMode chargeMode;
    private boolean enabled;
    private Calendar enabledSince;
    private RunMode runMode;
    private TimePeriod recurrence;
    private Byte day;
    private Byte hour;
    private InvoiceMode invoiceMode;
    private PaymentDirection paymentDirection;
    private BigDecimal amount;
    private BigDecimal freeBase;
    private TransferType transferType;
    private Collection<MemberGroup> groups;
    private Collection<AccountFeeLog> logs;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = MemberAccountType.class)
    @JoinColumn(name = "account_type_id", nullable = false)
    public MemberAccountType getAccountType() {
        return accountType;
    }

    @Column(nullable = false, precision = 15, scale = 6)
    public BigDecimal getAmount() {
        return amount;
    }

    @Transient
    public Amount getAmountValue() {
        final Amount amount = new Amount();
        amount.setType(chargeMode.getAmountType());
        amount.setValue(this.amount);
        return amount;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_mode", nullable = false)
    public ChargeMode getChargeMode() {
        return chargeMode;
    }

    @Column(length = 2)
    public Byte getDay() {
        return day;
    }

    @Column
    public String getDescription() {
        return description;
    }

    @Column(name = "enabled_since")
    public Calendar getEnabledSince() {
        return enabledSince;
    }

    @Column(name = "free_base", precision = 15, scale = 6)
    public BigDecimal getFreeBase() {
        return freeBase;
    }

    @ManyToMany(targetEntity = MemberGroup.class)
    @JoinTable(name = "groups_account_fees",
            joinColumns = @JoinColumn(name = "account_fee_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Collection<MemberGroup> getGroups() {
        return groups;
    }

    @Column(length = 2)
    public Byte getHour() {
        return hour;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_mode")
    public InvoiceMode getInvoiceMode() {
        return invoiceMode;
    }

    @Transient
    public AccountFeeLog getLastExecution() {
        // Logs are expected to be order by date descending
        return logs == null || logs.isEmpty() ? null : logs.iterator().next();
    }

    @OneToMany(targetEntity = AccountFeeLog.class)
    @JoinColumn(name = "account_fee_id")
    public Collection<AccountFeeLog> getLogs() {
        return logs;
    }

    @Column(nullable = false, length = 100)
    @Override
    public String getName() {
        return name;
    }

    @Transient
    public Calendar getNextExecutionDate() {
        if (runMode != RunMode.SCHEDULED || enabledSince == null) {
            return null;
        }
        // Resolve the period
        Period period;
        final AccountFeeLog lastLog = getLastExecution();
        if (lastLog == null) {
            // The account fee has never ran. Get the full period which contains the enabled since date
            period = recurrence.currentPeriod(enabledSince);
        } else {
            // Get the next period, counting from the last execution
            final Calendar begin = DateHelper.truncateNextDay(lastLog.getPeriod().getEnd());
            period = recurrence.periodStartingAt(begin);

            // If the fee was re-enabled long after the last execution, use the enabledSince instead
            if (enabledSince.after(period.getEnd())) {
                period = recurrence.currentPeriod(enabledSince);
            }
        }

        Calendar executionDate = DateHelper.truncateNextDay(period.getEnd());
        if (enabledSince.after(executionDate)) {
            executionDate = DateHelper.truncateNextDay(recurrence.currentPeriod(enabledSince).getEnd());
        }
        switch (recurrence.getField()) {
            case WEEKS:
                while (executionDate.get(Calendar.DAY_OF_WEEK) != day) {
                    executionDate.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case MONTHS:
                executionDate.set(Calendar.DAY_OF_MONTH, day);
        }
        if (hour != null) {
            executionDate.set(Calendar.HOUR_OF_DAY, hour);
        }
        return executionDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_direction", nullable = false)
    public PaymentDirection getPaymentDirection() {
        return paymentDirection;
    }

    @Transient
    public TimePeriod getRecurrence() {
        return recurrence;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_field")
    public Field getRecurrenceField() {
        return recurrence.getField();
    }

    @Column(name = "recurrence_number")
    public int getRecurrenceNumber() {
        return recurrence.getNumber();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "run_mode", nullable = false)
    public RunMode getRunMode() {
        return runMode;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "transfer_type_id", nullable = false)
    public TransferType getTransferType() {
        return transferType;
    }

    @Transient
    public WeekDay getWeekDay() {
        if (recurrence != null && recurrence.getField() == TimePeriod.Field.WEEKS) {
            return WeekDay.valueOf(day);
        }
        return null;
    }

    @Column(nullable = false)
    public boolean isEnabled() {
        return enabled;
    }

    @Transient
    public boolean isManual() {
        return runMode == RunMode.MANUAL;
    }

    @Transient
    public boolean isMemberToSystem() {
        return paymentDirection == PaymentDirection.TO_SYSTEM;
    }

    public void setAccountType(final MemberAccountType accountType) {
        this.accountType = accountType;
    }

    public void setAmount(final BigDecimal value) {
        amount = value;
    }

    public void setChargeMode(final ChargeMode chargeMode) {
        this.chargeMode = chargeMode;
    }

    public void setDay(final Byte day) {
        this.day = day;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabledSince(final Calendar enabledSince) {
        this.enabledSince = enabledSince;
    }

    public void setFreeBase(final BigDecimal freeBase) {
        this.freeBase = freeBase;
    }

    public void setGroups(final Collection<MemberGroup> groups) {
        this.groups = groups;
    }

    public void setHour(final Byte hour) {
        this.hour = hour;
    }

    public void setInvoiceMode(final InvoiceMode invoiceMode) {
        this.invoiceMode = invoiceMode;
    }

    public void setLogs(final Collection<AccountFeeLog> logs) {
        this.logs = logs;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPaymentDirection(final PaymentDirection paymentDirection) {
        this.paymentDirection = paymentDirection;
    }

    public void setRecurrence(final TimePeriod recurrence) {
        this.recurrence = recurrence;
    }

    public void setRecurrenceField(Field field) {
        recurrence.setField(field);
    }

    public void setRecurrenceNumber(int number) {
        recurrence.setNumber(number);
    }

    public void setRunMode(final RunMode type) {
        runMode = type;
    }

    public void setTransferType(final TransferType transferType) {
        this.transferType = transferType;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }

    @Override
    protected void appendVariableValues(final Map<String, Object> variables, final LocalSettings localSettings) {
        variables.put("account_fee", name);
    }

}
