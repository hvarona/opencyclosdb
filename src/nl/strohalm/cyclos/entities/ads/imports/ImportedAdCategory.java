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
package nl.strohalm.cyclos.entities.ads.imports;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.ads.AdCategory;

/**
 * An imported ad category, which was still not processed
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "imported_ad_categories")
public class ImportedAdCategory extends Entity {

    public static enum Relationships implements Relationship {
        AD_IMPORT("adImport"), IMPORTED_PARENT("importedParent"), EXISTING_PARENT("existingParent");

        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 3881658859416912467L;

    private AdImport adImport;
    private String name;
    private ImportedAdCategory importedParent;
    private AdCategory existingParent;

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = AdImport.class)
    @JoinColumn(name = "ad_import_id", nullable = false)
    public AdImport getAdImport() {
        return adImport;
    }

    @ManyToOne(targetEntity = AdImport.class)
    @JoinColumn(name = "existing_parent_id")
    public AdCategory getExistingParent() {
        return existingParent;
    }

    @Transient
    public String getFullName() {
        if (existingParent == null && importedParent == null) {
            return name;
        } else if (existingParent != null) {
            return existingParent.getFullName() + ": " + name;
        } else {
            return importedParent.getFullName() + ": " + name;
        }
    }

    @ManyToOne(targetEntity = AdImport.class)
    @JoinColumn(name = "imported_parent_id")
    public ImportedAdCategory getImportedParent() {
        return importedParent;
    }

    @Column(name = "name", length = 100)
    @Override
    public String getName() {
        return name;
    }

    public void setAdImport(final AdImport adImport) {
        this.adImport = adImport;
    }

    public void setExistingParent(final AdCategory existingParent) {
        this.existingParent = existingParent;
    }

    public void setImportedParent(final ImportedAdCategory importedParent) {
        this.importedParent = importedParent;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }

}
