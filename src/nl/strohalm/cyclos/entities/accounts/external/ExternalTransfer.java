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
package nl.strohalm.cyclos.entities.accounts.external;

import java.math.BigDecimal;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.external.ExternalTransferType.Action;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.FormatObject;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * Represents a payment in a real bank, that backs a payment in Cyclos
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "external_transfers")
public class ExternalTransfer extends Entity {

    public static enum Relationships implements Relationship {

        ACCOUNT("account"), TYPE("type"), TRANSFER_IMPORT("transferImport"), MEMBER("member");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * The status of a payment inside cyclos
     *
     * @author luis
     */
    public static enum Status implements StringValuedEnum {

        PENDING("N"), CHECKED("C"), PROCESSED("P");

        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    /**
     * The status of a payment inside cyclos
     *
     * @author luis
     */
    public static enum SummaryStatus {

        INCOMPLETE_PENDING, COMPLETE_PENDING, CHECKED, PROCESSED, TOTAL;
    }

    private static final long serialVersionUID = 8537065201842074164L;
    private ExternalAccount account;
    private ExternalTransferImport transferImport;
    private ExternalTransferType type;
    private Status status = Status.PENDING;
    private Calendar date;
    private BigDecimal amount;
    private String description;
    private Member member;
    private Integer lineNumber;
    private String comments;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = ExternalAccount.class)
    @JoinColumn(name = "account_id", nullable = false)
    public ExternalAccount getAccount() {
        return account;
    }

    @Column(precision = 15, scale = 6)
    public BigDecimal getAmount() {
        return amount;
    }

    @Column
    public String getComments() {
        return comments;
    }

    @Column
    public Calendar getDate() {
        return date;
    }

    @Column
    public String getDescription() {
        return description;
    }

    @Column(name = "line_number")
    public Integer getLineNumber() {
        return lineNumber;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status getStatus() {
        return status;
    }

    @Transient
    public SummaryStatus getSummaryStatus() {
        if (status != null) {
            switch (status) {
                case PENDING:
                    return isComplete() ? SummaryStatus.COMPLETE_PENDING : SummaryStatus.INCOMPLETE_PENDING;
                case CHECKED:
                    return SummaryStatus.CHECKED;
                case PROCESSED:
                    return SummaryStatus.PROCESSED;
            }
        }
        return null;
    }

    @ManyToOne(targetEntity = ExternalTransferImport.class)
    @JoinColumn(name = "import_id")
    public ExternalTransferImport getTransferImport() {
        return transferImport;
    }

    @ManyToOne(targetEntity = ExternalTransferType.class)
    @JoinColumn(name = "type_id")
    public ExternalTransferType getType() {
        return type;
    }

    @Transient
    public boolean isComplete() {
        return !(type == null || ((type == null || type.getAction() != Action.IGNORE) && member == null) || date == null || amount == null);
    }

    public void setAccount(final ExternalAccount account) {
        this.account = account;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setLineNumber(final Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setMember(final Member member) {
        this.member = member;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setTransferImport(final ExternalTransferImport transferImport) {
        this.transferImport = transferImport;
    }

    public void setType(final ExternalTransferType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getId() + " - date: " + FormatObject.formatObject(getDate()) + ", amount: " + FormatObject.formatObject(getAmount()) + ", member: " + member;
    }

}
