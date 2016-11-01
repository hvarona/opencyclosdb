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

import java.util.Calendar;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.utils.FormatObject;

/**
 * Contains a set of imported transfers from a file
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "external_transfer_imports")
public class ExternalTransferImport extends Entity {

    public static enum Relationships implements Relationship {

        ACCOUNT("account"), BY("by"), TRANSFERS("transfers");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -8862140825847721639L;
    private Calendar date;
    private Element by;
    private ExternalAccount account;
    private Collection<ExternalTransfer> transfers;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = ExternalAccount.class)
    @JoinColumn(name = "account_id")
    public ExternalAccount getAccount() {
        return account;
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

    @OneToMany(targetEntity = ExternalTransfer.class)
    @JoinColumn(name = "import_id")
    public Collection<ExternalTransfer> getTransfers() {
        return transfers;
    }

    public void setAccount(final ExternalAccount account) {
        this.account = account;
    }

    public void setBy(final Element by) {
        this.by = by;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setTransfers(final Collection<ExternalTransfer> transfers) {
        this.transfers = transfers;
    }

    @Override
    public String toString() {
        return getId() + ", account: " + account + ", date: " + FormatObject.formatObject(date);
    }

}
