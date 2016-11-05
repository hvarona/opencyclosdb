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
package nl.strohalm.cyclos.entities.members.adInterests;

import java.math.BigDecimal;
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
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.Currency;
import nl.strohalm.cyclos.entities.ads.Ad;
import nl.strohalm.cyclos.entities.ads.AdCategory;
import nl.strohalm.cyclos.entities.groups.GroupFilter;
import nl.strohalm.cyclos.entities.members.Member;

@javax.persistence.Entity
@Table(name = "ad_interests")
public class AdInterest extends Entity {

    public static enum Relationships implements Relationship {
        MEMBER("member"), CATEGORY("category"), CURRENCY("currency");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 7809184121372381237L;

    private Member owner;
    private String title;
    private Ad.TradeType type;
    private AdCategory category;
    private Member member;
    private GroupFilter groupFilter;
    private BigDecimal initialPrice;
    private BigDecimal finalPrice;
    private Currency currency;
    private String keywords;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = AdCategory.class)
    @JoinColumn(name = "ad_category_id")
    public AdCategory getCategory() {
        return category;
    }

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency_id")
    public Currency getCurrency() {
        return currency;
    }

    @Column(name = "final_price", precision = 15, scale = 6)
    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    @ManyToOne(targetEntity = GroupFilter.class)
    @JoinColumn(name = "group_filter_id")
    public GroupFilter getGroupFilter() {
        return groupFilter;
    }

    @Column(name = "initial_price", precision = 15, scale = 6)
    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    @Column(name = "keywords")
    public String getKeywords() {
        return keywords;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "owner_id", nullable = false)
    public Member getOwner() {
        return owner;
    }

    @Column(name = "title", length = 100, nullable = false)
    public String getTitle() {
        return title;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    public Ad.TradeType getType() {
        return type;
    }

    public void setCategory(final AdCategory category) {
        this.category = category;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public void setFinalPrice(final BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public void setGroupFilter(final GroupFilter groupFilter) {
        this.groupFilter = groupFilter;
    }

    public void setInitialPrice(final BigDecimal initialPrice) {
        this.initialPrice = initialPrice;
    }

    public void setKeywords(final String keywords) {
        this.keywords = keywords;
    }

    public void setMember(final Member member) {
        this.member = member;
    }

    public void setOwner(final Member owner) {
        this.owner = owner;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setType(final Ad.TradeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return title != null ? title : "";
    }

}
