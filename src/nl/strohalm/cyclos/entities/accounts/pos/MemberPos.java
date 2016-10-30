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
package nl.strohalm.cyclos.entities.accounts.pos;

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

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 *
 * @author rodrigo
 */
@javax.persistence.Entity
@Table(name = "member_pos")
public class MemberPos extends Entity {

    public static enum Relationships implements Relationship {
        POS("pos"), MEMBER("member");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Status implements StringValuedEnum {
        PENDING("P"), ACTIVE("A"), BLOCKED("B"), PIN_BLOCKED("K");

        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    private static final long serialVersionUID = 7644873097426472662L;

    private boolean allowMakePayment;
    private Calendar date;
    private Integer maxSchedulingPayments;
    private Integer numberOfCopies;
    private Integer resultPageSize;
    private Member member;
    private Pos pos;
    private Status status;
    private String posName;
    private String posPin;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(nullable = false)
    public Calendar getDate() {
        return date;
    }

    @Column(name = "max_scheduling_payments", nullable = false)
    public Integer getMaxSchedulingPayments() {
        return maxSchedulingPayments;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "owner_id")
    public Member getMember() {
        return member;
    }

    @Column(name = "number_of_copies", nullable = false)
    public Integer getNumberOfCopies() {
        return numberOfCopies;
    }

    @ManyToOne(targetEntity = Pos.class)
    @JoinColumn(name = "pos_id")
    public Pos getPos() {
        return pos;
    }

    @Column(name = "pos_name", length = 64)
    public String getPosName() {
        return posName;
    }

    @Column(name = "pos_pin", length = 64)
    public String getPosPin() {
        return posPin;
    }

    @Column(name = "result_page_size", nullable = false)
    public Integer getResultPageSize() {
        return resultPageSize;
    }

    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    @Column(name = "allow_make_payment", nullable = false)
    public boolean isAllowMakePayment() {
        return allowMakePayment;
    }

    public void setAllowMakePayment(final boolean allowMakePayment) {
        this.allowMakePayment = allowMakePayment;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setMaxSchedulingPayments(final Integer maxSchedulingPayments) {
        this.maxSchedulingPayments = maxSchedulingPayments;
    }

    public void setMember(final Member member) {
        this.member = member;
    }

    public void setNumberOfCopies(final Integer numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void setPos(final Pos pos) {
        this.pos = pos;
    }

    public void setPosName(final String posName) {
        this.posName = posName;
    }

    public void setPosPin(final String posPin) {
        this.posPin = posPin;
    }

    public void setResultPageSize(final Integer resultPageSize) {
        this.resultPageSize = resultPageSize;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getId() + " - " + posName;
    }

}
