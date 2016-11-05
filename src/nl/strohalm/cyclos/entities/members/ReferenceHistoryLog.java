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
package nl.strohalm.cyclos.entities.members;

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
import nl.strohalm.cyclos.utils.Period;

/**
 * This is a log of given references from one member to another
 *
 * @author jefferson
 */
@javax.persistence.Entity
@Table(name = "reference_history")
public class ReferenceHistoryLog extends Entity {

    public static enum Relationships implements Relationship {
        FROM("from"), TO("to");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 2003079010258584099L;

    private Member from;
    private Member to;
    private Reference.Level level;
    private Period period;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "from_member_id")
    public Member getFrom() {
        return from;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    public Reference.Level getLevel() {
        return level;
    }

    @Transient
    public Period getPeriod() {
        return period;
    }

    @Column(name = "star_date")
    public Calendar getPeriodStart() {
        return period.getBegin();
    }

    @Column(name = "end_Date")
    public Calendar getPeriodEnd() {
        return period.getEnd();
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "to_member_id")
    public Member getTo() {
        return to;
    }

    public void setFrom(final Member from) {
        this.from = from;
    }

    public void setLevel(final Reference.Level level) {
        this.level = level;
    }

    public void setPeriod(final Period period) {
        this.period = period;
    }

    public void setPeriodStart(final Calendar start) {
        period.setBegin(start);
    }

    public void setPeriodEnd(final Calendar end) {
        period.setEnd(end);
    }

    public void setTo(final Member to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return getId() + " - " + level + ", from " + from + ", to " + to;
    }

}
