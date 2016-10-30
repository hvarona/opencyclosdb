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
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.entities.members.Member;

/**
 *
 * @author rodrigo
 */
@javax.persistence.Entity
@Table(name = "pos_logs")
public class PosLog extends Entity {

    private static final long serialVersionUID = -6747020477752004538L;

    private Pos pos;
    private Element by;
    private Calendar date;
    private Pos.Status posStatus;
    private MemberPos.Status memberPosStatus;
    private Member assignedTo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "assignet_to")
    public Member getAssignedTo() {
        return assignedTo;
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "by_id")
    public Element getBy() {
        return by;
    }

    @Column(nullable = false)
    public Calendar getDate() {
        return date;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "member_pos_status", nullable = false)
    public MemberPos.Status getMemberPosStatus() {
        return memberPosStatus;
    }

    @ManyToOne(targetEntity = Pos.class)
    @JoinColumn(name = "pos_id")
    public Pos getPos() {
        return pos;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "pos_status", nullable = false)
    public Pos.Status getPosStatus() {
        return posStatus;
    }

    public void setAssignedTo(final Member assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setBy(final Element by) {
        this.by = by;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setMemberPosStatus(final MemberPos.Status memberPosStatus) {
        this.memberPosStatus = memberPosStatus;
    }

    public void setPos(final Pos pos) {
        this.pos = pos;
    }

    public void setPosStatus(final Pos.Status posStatus) {
        this.posStatus = posStatus;
    }

    @Override
    public String toString() {
        return getId() + " - " + date;
    }
}
