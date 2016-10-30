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
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 *
 * @author rodrigo
 */
@javax.persistence.Entity
@Table(name = "pos")
public class Pos extends Entity {

    public static enum Relationships implements Relationship {
        MEMBER_POS("memberPos");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum Status implements StringValuedEnum {
        UNASSIGNED("U"), ASSIGNED("A"), DISCARDED("D");
        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private String posId;
    private String description;
    private MemberPos memberPos;
    private Collection<PosLog> posLog;
    private Status status;

    private static final long serialVersionUID = -6054597340850484757L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(length = 100)
    public String getDescription() {
        return description;
    }

    @ManyToOne(targetEntity = MemberPos.class)
    @JoinColumn(name = "member_pos_id")
    public MemberPos getMemberPos() {
        return memberPos;
    }

    @Column(name = "pos_id", length = 64, unique = true)
    public String getPosId() {
        return posId;
    }

    @OneToMany(targetEntity = PosLog.class)
    @JoinColumn(name = "pos_id")
    public Collection<PosLog> getPosLog() {
        return posLog;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setMemberPos(final MemberPos memberPos) {
        this.memberPos = memberPos;
    }

    public void setPosId(final String posId) {
        this.posId = posId;
    }

    public void setPosLog(final Collection<PosLog> posLog) {
        this.posLog = posLog;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getId() + " - " + posId;
    }

    @Override
    protected void appendVariableValues(final Map<String, Object> variables, final LocalSettings localSettings) {
        variables.put("pos_id", getPosId());
    }

}
