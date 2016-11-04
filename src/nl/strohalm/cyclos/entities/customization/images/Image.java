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

import java.sql.Blob;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.ads.Ad;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * Stores an image
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "images")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subclass")
public abstract class Image extends Entity {

    public static enum Nature implements StringValuedEnum {

        SYSTEM("sys"), CUSTOM("ctm"), STYLE("sty"), MEMBER("mbr"), AD("ad");

        public static Nature getByOwner(final Entity owner) {
            if (owner instanceof Member) {
                return Image.Nature.MEMBER;
            } else {
                return Image.Nature.AD;
            }
        }

        private final String value;

        private Nature(final String value) {
            this.value = value;
        }

        public Class<? extends Image> getEntityType() {
            switch (this) {
                case AD:
                    return AdImage.class;
                case CUSTOM:
                    return CustomImage.class;
                case SYSTEM:
                    return SystemImage.class;
                case MEMBER:
                    return MemberImage.class;
                case STYLE:
                    return StyleImage.class;
            }
            return null;
        }

        public String getOwnerProperty() {
            switch (this) {
                case AD:
                    return "ad";
                case MEMBER:
                    return "member";
            }
            return null;
        }

        public Class<? extends Entity> getOwnerType() {
            switch (this) {
                case AD:
                    return Ad.class;
                case MEMBER:
                    return Member.class;
            }
            return null;
        }

        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 3550019581431328393L;
    private String contentType;
    private Blob image;
    private Integer imageSize;
    private Calendar lastModified;
    private String name;
    private Blob thumbnail;
    private Integer thumbnailSize;

    protected Image() {
    }

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "content_type", nullable = false, length = 100)
    public String getContentType() {
        return contentType;
    }

    @Column(name = "image", nullable = false, length = 16000000)
    public Blob getImage() {
        return image;
    }

    @Column(name = "image_size", nullable = false)
    public Integer getImageSize() {
        return imageSize;
    }

    @Column(name = "last_modified", nullable = false)
    public Calendar getLastModified() {
        return lastModified;
    }

    @Column(name = "name", nullable = false, length = 100)
    @Override
    public String getName() {
        return name;
    }

    @Transient
    public abstract Nature getNature();

    /**
     * Returns the name without extension
     */
    @Transient
    public String getSimpleName() {
        final int pos = name == null ? -1 : name.lastIndexOf('.');
        if (pos < 0) {
            return name;
        }
        return name.substring(0, pos);
    }

    @Column(name = "thumbnail", length = 16000000)
    public Blob getThumbnail() {
        return thumbnail;
    }

    @Column(name = "rhumbnail_size", nullable = false)
    public Integer getThumbnailSize() {
        return thumbnailSize;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public void setImage(final Blob image) {
        this.image = image;
    }

    public void setImageSize(final Integer imageSize) {
        this.imageSize = imageSize;
    }

    public void setLastModified(final Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setThumbnail(final Blob thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setThumbnailSize(final Integer thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }
}
