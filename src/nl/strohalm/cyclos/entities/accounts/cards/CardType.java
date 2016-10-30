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
package nl.strohalm.cyclos.entities.accounts.cards;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.utils.RangeConstraint;
import nl.strohalm.cyclos.utils.StringValuedEnum;
import nl.strohalm.cyclos.utils.TimePeriod;
import nl.strohalm.cyclos.utils.TimePeriod.Field;
import nl.strohalm.cyclos.utils.conversion.CardNumberConverter;
import nl.strohalm.cyclos.utils.conversion.Converter;

/**
 * Represents a card type
 *
 * @author jefferson
 * @author rodrigo
 */
@javax.persistence.Entity
@Table(name = "card_types")
public class CardType extends Entity {

    public static enum CardSecurityCode implements StringValuedEnum {
        NOT_USED("N"), MANUAL("M"), AUTOMATIC("A");
        private final String value;

        private CardSecurityCode(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 4768078304847226115L;

    private String name;
    private String cardFormatNumber = "#### #### #### ####";
    private TimePeriod defaultExpiration = new TimePeriod(1, Field.YEARS);
    private CardSecurityCode cardSecurityCode = CardSecurityCode.NOT_USED;
    private boolean showCardSecurityCode = false;
    private boolean ignoreDayInExpirationDate = true;
    ;
    private RangeConstraint cardSecurityCodeLength = new RangeConstraint(4, 4);
    private int maxSecurityCodeTries = 3;
    private TimePeriod securityCodeBlockTime = new TimePeriod(1, Field.DAYS);
    private transient Converter<BigInteger> converter;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "card_format_number", length = 56)
    public String getCardFormatNumber() {
        return cardFormatNumber;
    }

    @Transient
    public Converter<BigInteger> getCardNumberConverter() {
        if (converter == null) {
            converter = new CardNumberConverter(cardFormatNumber);
        }
        return converter;
    }

    @Enumerated(EnumType.STRING)
    public CardSecurityCode getCardSecurityCode() {
        return cardSecurityCode;
    }

    @Transient
    public RangeConstraint getCardSecurityCodeLength() {
        return cardSecurityCodeLength;
    }

    @Column(name = "min_card_security_code_length")
    public Integer getMinCardSecurityCodeLength() {
        return cardSecurityCodeLength.getMin();
    }

    @Column(name = "max_card_security_code_length")
    public Integer getMaxCardSecurityCodeLength() {
        return cardSecurityCodeLength.getMax();
    }

    @Transient
    public TimePeriod getDefaultExpiration() {
        return defaultExpiration;
    }

    @Column(name = "default_expiration_field")
    @Enumerated(EnumType.STRING)
    public TimePeriod.Field getDefaultExpirationField() {
        return defaultExpiration.getField();
    }

    @Column(name = "default_expiration_number")
    public int getDefaultExpirationNumber() {
        return defaultExpiration.getNumber();
    }

    @Column(name = "max_security_code_tries")
    public int getMaxSecurityCodeTries() {
        return maxSecurityCodeTries;
    }

    @Column(length = 100, nullable = false)
    @Override
    public String getName() {
        return name;
    }

    @Transient
    public TimePeriod getSecurityCodeBlockTime() {
        return securityCodeBlockTime;
    }

    @Column(name = "security_code_block_time_field")
    @Enumerated(EnumType.STRING)
    public TimePeriod.Field getSecurityCodeBlockTimeField() {
        return securityCodeBlockTime.getField();
    }

    @Column(name = "security_code_block_time_number")
    public int getSecurityCodeBlockTimeNumber() {
        return securityCodeBlockTime.getNumber();
    }

    @Column(name = "ignore_day_in_expiration_date", nullable = false)
    public boolean isIgnoreDayInExpirationDate() {
        return ignoreDayInExpirationDate;
    }

    @Column(name = "show_card_security_code", nullable = false)
    public boolean isShowCardSecurityCode() {
        return showCardSecurityCode;
    }

    public void setCardFormatNumber(final String cardFormatNumber) {
        this.cardFormatNumber = cardFormatNumber;
        converter = null;
    }

    public void setCardSecurityCode(final CardSecurityCode cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
    }

    public void setCardSecurityCodeLength(final RangeConstraint cardSecurityCodeLength) {
        this.cardSecurityCodeLength = cardSecurityCodeLength;
    }

    public void setDefaultExpiration(final TimePeriod defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    public void setIgnoreDayInExpirationDate(final boolean ignoreDayInExpirationDate) {
        this.ignoreDayInExpirationDate = ignoreDayInExpirationDate;
    }

    public void setMaxSecurityCodeTries(final int maxSecurityCodeTries) {
        this.maxSecurityCodeTries = maxSecurityCodeTries;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSecurityCodeBlockTime(final TimePeriod securityCodeBlockTime) {
        this.securityCodeBlockTime = securityCodeBlockTime;
    }

    public void setShowCardSecurityCode(final boolean showCardSecurityCode) {
        this.showCardSecurityCode = showCardSecurityCode;
    }

    public void setDefaultExpirationField(TimePeriod.Field field) {
        defaultExpiration.setField(field);
    }

    public void setDefaultExpirationNumber(int number) {
        defaultExpiration.setNumber(number);
    }

    public void setSecurityCodeBlockTimeField(TimePeriod.Field field) {
        securityCodeBlockTime.setField(field);
    }

    public void setSecurityCodeBlockTimeNumber(int number) {
        securityCodeBlockTime.setNumber(number);
    }

    public void setMinCardSecurityCodeLength(Integer min) {
        cardSecurityCodeLength.setMin(min);
    }

    public void setMaxCardSecurityCodeLength(Integer max) {
        cardSecurityCodeLength.setMax(max);
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }

}
