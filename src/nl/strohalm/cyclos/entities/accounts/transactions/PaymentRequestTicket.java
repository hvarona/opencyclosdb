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
package nl.strohalm.cyclos.entities.accounts.transactions;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.access.Channel;

/**
 * A ticket used to request payments from other channels
 *
 * @author luis
 */
@Entity
@DiscriminatorValue(value = "R")
public class PaymentRequestTicket extends Ticket {

    public static enum Relationships implements Relationship {
        FROM_CHANNEL("fromChannel"), TO_CHANNEL("toChannel");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -7788824718013935541L;
    private Channel fromChannel;
    private Channel toChannel;
    private String traceData;

    @ManyToOne(targetEntity = Channel.class)
    @JoinColumn(name = "from_channel_id")
    public Channel getFromChannel() {
        return fromChannel;
    }

    @ManyToOne(targetEntity = Channel.class)
    @JoinColumn(name = "to_channel_id")
    public Channel getToChannel() {
        return toChannel;
    }

    /**
     * Returns the data set by the client at the moment of requesting a
     * payment.<br>
     * It depends on the client side then there is no guarantee of uniqueness
     * between different clients.<br>
     */
    @Column(name = "trace_data", nullable = false, length = 50)
    public String getTraceData() {
        return traceData;
    }

    public void setFromChannel(final Channel fromChannel) {
        this.fromChannel = fromChannel;
    }

    public void setToChannel(final Channel toChannel) {
        this.toChannel = toChannel;
    }

    public void setTraceData(final String traceData) {
        this.traceData = traceData;
    }
}
