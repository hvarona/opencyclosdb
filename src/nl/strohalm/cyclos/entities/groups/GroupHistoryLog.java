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
package nl.strohalm.cyclos.entities.groups;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.utils.Period;

@javax.persistence.Entity
@Table(name = "group_history_logs")
public class GroupHistoryLog extends Entity {

    public static enum Relationships implements Relationship {

        ELEMENT("element"), GROUP("group");

        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 6840703744916377438L;
    private Element element;
    private Group group;
    private Period period;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "element_id", nullable = false)
    public Element getElement() {
        return element;
    }

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "group_id", nullable = false)
    public Group getGroup() {
        return group;
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

    public void setElement(final Element element) {
        this.element = element;
    }

    public void setGroup(final Group group) {
        this.group = group;
    }

    public void setPeriod(final Period period) {
        this.period = period;
    }

    public void setStartDate(Calendar begin) {
        period.setBegin(begin);
    }

    public void setEndDate(Calendar end) {
        period.setEnd(end);
    }

    @Override
    public String toString() {
        String string = getId() + ": " + getGroup().getName() + " - begin: " + period.getBegin();
        if (period.getEnd() != null) {
            string += " - end: " + period.getEnd();
        }
        return string;
    }

}
