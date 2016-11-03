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
package nl.strohalm.cyclos.entities.accounts.guarantees;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.Currency;
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.utils.StringValuedEnum;

@javax.persistence.Entity
@Table(name = "payment_obligations")
public class PaymentObligation extends Entity {

    public static enum Relationships implements Relationship {

        GUARANTEE("guarantee"), CURRENCY("currency"), LOGS("logs"), BUYER("buyer"), SELLER("seller");
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

        REGISTERED("RG"), PUBLISHED("P"), ACCEPTED("A"), REJECTED("RJ"), EXPIRED("E");
        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = -3493507277972263881L;

    private Status status;
    private String description;
    private BigDecimal amount;
    private Calendar expirationDate;
    private Calendar maxPublishDate;
    private Calendar registrationDate;

    private Guarantee guarantee;
    private Currency currency;
    private Collection<PaymentObligationLog> logs;
    private Member buyer;
    private Member seller;

    /**
     * Change the payment obligation's status and adds a new payment obligation
     * log to it
     *
     * @param status the new payment obligation's status
     * @param by the author of the change
     * @return the new PaymentObligationLog added to this payment obligation
     */
    public PaymentObligationLog changeStatus(final Status status, final Element by) {
        setStatus(status);

        if (logs == null) {
            logs = new ArrayList<PaymentObligationLog>();
        }
        final PaymentObligationLog log = getNewLog(status, by);
        logs.add(log);

        return log;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "amount", precision = 15, scale = 6)
    public BigDecimal getAmount() {
        return amount;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "buyer_id")
    public Member getBuyer() {
        return buyer;
    }

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency_id", nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "expiration_date", nullable = false)
    public Calendar getExpirationDate() {
        return expirationDate;
    }

    @ManyToOne(targetEntity = Guarantee.class)
    @JoinColumn(name = "guarantee_id", nullable = false)
    public Guarantee getGuarantee() {
        return guarantee;
    }

    @OneToMany(targetEntity = PaymentObligationLog.class)
    @JoinColumn(name = "payment_obligation_id")
    public Collection<PaymentObligationLog> getLogs() {
        return logs;
    }

    @Column(name = "max_publish_date", nullable = false)
    public Calendar getMaxPublishDate() {
        return maxPublishDate;
    }

    public PaymentObligationLog getNewLog(final Status status, final Element by) {
        final PaymentObligationLog log = new PaymentObligationLog();
        log.setPaymentObligation(this);
        log.setDate(Calendar.getInstance());
        log.setStatus(status);
        log.setBy(by);

        // TODO: should I add the created log to the logs' collection?
        return log;
    }

    @Column(name = "registration_date", nullable = false)
    public Calendar getRegistrationDate() {
        return registrationDate;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "seller_id")
    public Member getSeller() {
        return seller;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setBuyer(final Member buyer) {
        this.buyer = buyer;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setExpirationDate(final Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setGuarantee(final Guarantee guarantee) {
        this.guarantee = guarantee;
    }

    public void setLogs(final Collection<PaymentObligationLog> logs) {
        this.logs = logs;
    }

    public void setMaxPublishDate(final Calendar maxPublishDate) {
        this.maxPublishDate = maxPublishDate;
    }

    public void setRegistrationDate(final Calendar registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setSeller(final Member seller) {
        this.seller = seller;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PO: " + getId() + " - " + status;
    }

    @Override
    protected void appendVariableValues(final Map<String, Object> variables, final LocalSettings localSettings) {
        final String pattern = getCurrency().getPattern();
        variables.put("amount", localSettings.getUnitsConverter(pattern).toString(getAmount()));
        variables.put("buyer_member", getBuyer().getName());
        variables.put("buyer_login", getBuyer().getUsername());
        variables.put("seller_member", getSeller().getName());
        variables.put("seller_login", getSeller().getUsername());
    }
}
