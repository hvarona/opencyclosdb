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
package nl.strohalm.cyclos.entities.accounts.fees.transaction;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.groups.BrokerGroup;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * A fee that represents a broker commission
 *
 * @author luis
 */
@Entity
@DiscriminatorValue(value = "B")
public class BrokerCommission extends TransactionFee {

    public static enum Relationships implements Relationship {

        BROKER_GROUPS("brokerGroups");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum When implements StringValuedEnum {

        ALWAYS("A"), COUNT("C"), DAYS("D");
        private final String value;

        private When(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum WhichBroker implements StringValuedEnum {

        SOURCE("S"), DESTINATION("D");
        private final String value;

        private WhichBroker(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 3069444935878230607L;

    private Integer count;
    private When when;
    private WhichBroker whichBroker;
    private boolean allBrokerGroups = true;
    private Collection<BrokerGroup> brokerGroups;

    @ManyToMany(targetEntity = BrokerGroup.class)
    @JoinTable(name = "broker_groups_transaction_fees",
            joinColumns = @JoinColumn(name = "transaction_fee_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Collection<BrokerGroup> getBrokerGroups() {
        return brokerGroups;
    }

    @Column(name = "when_count")
    public Integer getCount() {
        return count;
    }

    @Transient
    @Override
    public Nature getNature() {
        return Nature.BROKER;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "when_apply")
    public When getWhen() {
        return when;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "which_broker")
    public WhichBroker getWhichBroker() {
        return whichBroker;
    }

    @Column(name = "all_broker_groups", nullable = false)
    public boolean isAllBrokerGroups() {
        return allBrokerGroups;
    }

    public void setAllBrokerGroups(final boolean allBrokerGroups) {
        this.allBrokerGroups = allBrokerGroups;
    }

    public void setBrokerGroups(final Collection<BrokerGroup> brokerGroups) {
        this.brokerGroups = brokerGroups;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    public void setWhen(final When when) {
        this.when = when;
    }

    public void setWhichBroker(final WhichBroker whichBroker) {
        this.whichBroker = whichBroker;
    }

}
