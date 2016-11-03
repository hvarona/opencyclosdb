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
package nl.strohalm.cyclos.entities.customization.images;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.ads.Ad;

/**
 * An image that belongs to an advertisement
 *
 * @author luis
 */
@javax.persistence.Entity
@DiscriminatorValue(value = "ad")
public class AdImage extends OwneredImage {

    public static enum Relationships implements Relationship {
        AD("ad");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -3594787484075069707L;

    private Ad ad;

    public AdImage() {
        super();
    }

    @ManyToOne(targetEntity = Ad.class)
    @JoinColumn(name = "ad_id")
    public Ad getAd() {
        return ad;
    }

    @Transient
    @Override
    public Nature getNature() {
        return Nature.AD;
    }

    @Transient
    @Override
    public Entity getOwner() {
        return ad;
    }

    @Column(name = "order_number")
    @Override
    public Integer getOrder() {
        return super.getOrder();
    }

    @Column(name = "caption", length = 255)
    @Override
    public String getCaption() {
        return super.getCaption();
    }

    public void setAd(final Ad ad) {
        this.ad = ad;
    }

    @Override
    public void setOwner(final Entity owner) {
        ad = (Ad) owner;
    }
}
