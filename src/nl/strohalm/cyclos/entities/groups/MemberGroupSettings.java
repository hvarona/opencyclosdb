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

import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.utils.DataObject;
import nl.strohalm.cyclos.utils.RangeConstraint;
import nl.strohalm.cyclos.utils.StringValuedEnum;
import nl.strohalm.cyclos.utils.TimePeriod;

/**
 * Settings of a member group
 *
 * @author luis
 */
@Embeddable
public class MemberGroupSettings extends DataObject {

    /**
     * Determines in which registrations the e-mail validation will be used
     *
     * @author luis
     */
    public static enum EmailValidation implements StringValuedEnum {

        /**
         * Either a public registration or an user editing his own profile
         */
        USER("U"),
        /**
         * An admin registering an user / editing an user profile
         */
        ADMIN("A"),
        /**
         * A broker registering an user / editing an user profile
         */
        BROKER("B"),
        /**
         * Either a registration or profile modification by web service
         */
        WEB_SERVICE("W");

        private final String value;

        private EmailValidation(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    /**
     * Controls the external advertisement publication
     *
     * @author luis
     */
    public static enum ExternalAdPublication implements StringValuedEnum {

        ALLOW_CHOICE("C"), ENABLED("E"), DISABLED("D");

        private final String value;

        private ExternalAdPublication(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = -5279193799739646568L;

    // Access and external access
    private Set<EmailValidation> emailValidation;
    private RangeConstraint pinLength = new RangeConstraint(4, 4);
    private int maxPinWrongTries = 3;
    private TimePeriod pinBlockTimeAfterMaxTries = new TimePeriod(1, TimePeriod.Field.DAYS);

    // Notifications
    private TransferType smsChargeTransferType;

    // this is the amount to be charged for the a smsAdditionalCharged
    private BigDecimal smsChargeAmount;
    private int smsFree = 0;
    private int smsAdditionalCharged = 1;
    private int smsShowFreeThreshold = 50;
    private TimePeriod smsAdditionalChargedPeriod = new TimePeriod(1, TimePeriod.Field.MONTHS);
    private String smsContextClassName;

    // Registration
    private boolean sendPasswordByEmail = true;

    private TimePeriod expireMembersAfter = null;

    private MemberGroup groupAfterExpiration = null;

    private int maxImagesPerMember = 3;

    // Advertisements
    private int maxAdsPerMember = 10;

    private boolean enablePermanentAds = true;
    private TimePeriod defaultAdPublicationTime = new TimePeriod(1, TimePeriod.Field.MONTHS);
    private TimePeriod maxAdPublicationTime = new TimePeriod(3, TimePeriod.Field.MONTHS);

    private ExternalAdPublication externalAdPublication = ExternalAdPublication.ENABLED;
    private int maxAdImagesPerMember = 3;
    private int maxAdDescriptionSize = 2048;
    // Scheduled payments
    private int maxSchedulingPayments = 36;

    private TimePeriod maxSchedulingPeriod = new TimePeriod(3, TimePeriod.Field.YEARS);
    // Loans
    private boolean viewLoansByGroup = true;
    private boolean repayLoanByGroup = true;
    // Pos
    private boolean allowMakePayment = false;
    private int maxPosSchedulingPayments = 6;
    private int numberOfCopies = 2;
    private int resultPageSize = 5;
    private boolean showPosWebPaymentDescription = false;

    @Transient
    public TimePeriod getDefaultAdPublicationTime() {
        return defaultAdPublicationTime;
    }

    @Column(name = "member_default_ad_publication_number")
    public int getDefaultAdPublicationTimeNumber() {
        return defaultAdPublicationTime.getNumber();
    }

    @Column(name = "member_default_ad_publication_field")
    public TimePeriod.Field getDefaultAdPublicationTimeField() {
        return defaultAdPublicationTime.getField();
    }

    @ElementCollection(targetClass = Enum.class)
    @CollectionTable(name = "member_groups_email_validation",
            joinColumns = @JoinColumn(name = "group_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public Set<EmailValidation> getEmailValidation() {
        return emailValidation;
    }

    @Transient
    public TimePeriod getExpireMembersAfter() {
        return expireMembersAfter;
    }

    @Column(name = "member_expire_number")
    public int getExpireMembersAfterNumber() {
        return expireMembersAfter.getNumber();
    }

    @Column(name = "member_expire_field")
    public TimePeriod.Field getExpireMembersAfterField() {
        return expireMembersAfter.getField();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "member_external_ad_publication")
    public ExternalAdPublication getExternalAdPublication() {
        return externalAdPublication;
    }

    @ManyToOne(targetEntity = MemberGroup.class)
    @JoinColumn(name = "member_expire_group_id")
    public MemberGroup getGroupAfterExpiration() {
        return groupAfterExpiration;
    }

    @Column(name = "member_max_ad_description_size", nullable = false)
    public int getMaxAdDescriptionSize() {
        return maxAdDescriptionSize;
    }

    @Column(name = "member_max_ad_images_per_member", nullable = false)
    public int getMaxAdImagesPerMember() {
        return maxAdImagesPerMember;
    }

    @Transient
    public TimePeriod getMaxAdPublicationTime() {
        return maxAdPublicationTime;
    }

    @Column(name = "member_pin_block_field")
    public TimePeriod.Field getMaxAdPublicationTimeField() {
        return maxAdPublicationTime.getField();
    }

    @Column(name = "member_pin_block_number")
    public int getMaxAdPublicationTimeNumber() {
        return maxAdPublicationTime.getNumber();
    }

    @Column(name = "member_max_ads_per_member")
    public int getMaxAdsPerMember() {
        return maxAdsPerMember;
    }

    @Column(name = "member_max_images_per_member")
    public int getMaxImagesPerMember() {
        return maxImagesPerMember;
    }

    @Column(name = "member_max_pin_tries")
    public int getMaxPinWrongTries() {
        return maxPinWrongTries;
    }

    @Column(name = "member_max_pos_scheduling_payments", nullable = false)
    public int getMaxPosSchedulingPayments() {
        return maxPosSchedulingPayments;
    }

    @Column(name = "member_max_scheduling_payments", nullable = false)
    public int getMaxSchedulingPayments() {
        return maxSchedulingPayments;
    }

    @Transient
    public TimePeriod getMaxSchedulingPeriod() {
        return maxSchedulingPeriod;
    }

    @Column(name = "member_max_scheduling_period_field")
    public TimePeriod.Field getMaxSchedulingPeriodField() {
        return maxSchedulingPeriod.getField();
    }

    @Column(name = "member_max_scheduling_period_number")
    public int getMaxSchedulingPeriodNumber() {
        return maxSchedulingPeriod.getNumber();
    }

    @Column(name = "member_number_of_copies", nullable = false)
    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    @Transient
    public TimePeriod getPinBlockTimeAfterMaxTries() {
        return pinBlockTimeAfterMaxTries;
    }

    @Column(name = "pin_block_time_after_max_tries_number")
    public int getPinBlockTimeAfterMaxTriesNumber() {
        return pinBlockTimeAfterMaxTries.getNumber();
    }

    @Column(name = "pin_block_time_after_max_tries_field")
    public TimePeriod.Field getPinBlockTimeAfterMaxTriesField() {
        return pinBlockTimeAfterMaxTries.getField();
    }

    @Transient
    public RangeConstraint getPinLength() {
        return pinLength;
    }

    @Column(name = "min_pin_length")
    public Integer getPinLengthMin() {
        return pinLength.getMin();
    }

    @Column(name = "max_pin_length")
    public Integer getPinLengthMax() {
        return pinLength.getMax();
    }

    @Column(name = "member_result_page_size", nullable = false)
    public int getResultPageSize() {
        return resultPageSize;
    }

    @Column(name = "sms_additional_charged_count", nullable = false)
    public int getSmsAdditionalCharged() {
        return smsAdditionalCharged;
    }

    @Transient
    public TimePeriod getSmsAdditionalChargedPeriod() {
        return smsAdditionalChargedPeriod;
    }

    @Column(name = "sms_additional_charged_period_number")
    public int getSmsAdditionalChargedPeriodNumber() {
        return smsAdditionalChargedPeriod.getNumber();
    }

    @Column(name = "sms_additional_charged_period_field")
    public TimePeriod.Field getSmsAdditionalChargedPeriodField() {
        return smsAdditionalChargedPeriod.getField();
    }

    @Column(name = "sms_charge_amount", precision = 15, scale = 6)
    public BigDecimal getSmsChargeAmount() {
        return smsChargeAmount;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "sms_charge_transfer_type_id")
    public TransferType getSmsChargeTransferType() {
        return smsChargeTransferType;
    }

    @Column(name = "sms_context_class_name")
    public String getSmsContextClassName() {
        return smsContextClassName;
    }

    @Column(name = "sms_fre_count", nullable = false)
    public int getSmsFree() {
        return smsFree;
    }

    @Column(name = "sms_show_free_threshold", nullable = false)
    public int getSmsShowFreeThreshold() {
        return smsShowFreeThreshold;
    }

    @Column(name = "member_allow_make_payment", nullable = false)
    public boolean isAllowMakePayment() {
        return allowMakePayment;
    }

    @Transient
    public boolean isAllowsMultipleScheduledPayments() {
        return isAllowsScheduledPayments() && maxSchedulingPayments > 1;
    }

    @Transient
    public boolean isAllowsScheduledPayments() {
        return maxSchedulingPayments > 0 && maxSchedulingPeriod != null && maxSchedulingPeriod.isValid();
    }

    @Column(name = "member_enable_permanent_ads", nullable = false)
    public boolean isEnablePermanentAds() {
        return enablePermanentAds;
    }

    @Column(name = "member_repay_loan_by_group", nullable = false)
    public boolean isRepayLoanByGroup() {
        return repayLoanByGroup;
    }

    @Column(name = "member_send_password_by_email", nullable = false)
    public boolean isSendPasswordByEmail() {
        return sendPasswordByEmail;
    }

    @Column(name = "member_show_posweb_pmt_dsc", nullable = false)
    public boolean isShowPosWebPaymentDescription() {
        return showPosWebPaymentDescription;
    }

    @Column(name = "member_view_loans_by_group", nullable = false)
    public boolean isViewLoansByGroup() {
        return viewLoansByGroup;
    }

    public void setAllowMakePayment(final boolean allowMakePayment) {
        this.allowMakePayment = allowMakePayment;
    }

    public void setDefaultAdPublicationTime(final TimePeriod defaultAdPublicationTime) {
        this.defaultAdPublicationTime = defaultAdPublicationTime;
    }

    public void setDefaultAdPublicationTimeNumber(final int number) {
        this.defaultAdPublicationTime.setNumber(number);
    }

    public void setDefaultAdPublicationTimeField(final TimePeriod.Field field) {
        this.defaultAdPublicationTime.setField(field);
    }

    public void setEmailValidation(final Set<EmailValidation> emailValidation) {
        this.emailValidation = emailValidation;
    }

    public void setEnablePermanentAds(final boolean enablePermanentAds) {
        this.enablePermanentAds = enablePermanentAds;
    }

    public void setExpireMembersAfter(final TimePeriod expireMemberAfter) {
        expireMembersAfter = expireMemberAfter;
    }

    public void setExpireMembersAfterNumber(final int number) {
        expireMembersAfter.setNumber(number);
    }

    public void setExpireMembersAfterField(final TimePeriod.Field field) {
        expireMembersAfter.setField(field);
    }

    public void setExternalAdPublication(final ExternalAdPublication externalAdPublication) {
        this.externalAdPublication = externalAdPublication;
    }

    public void setGroupAfterExpiration(final MemberGroup groupAfterExpiration) {
        this.groupAfterExpiration = groupAfterExpiration;
    }

    public void setMaxAdDescriptionSize(final int maxAdDescriptionSize) {
        this.maxAdDescriptionSize = maxAdDescriptionSize;
    }

    public void setMaxAdImagesPerMember(final int maxAdImagesPerMember) {
        this.maxAdImagesPerMember = maxAdImagesPerMember;
    }

    public void setMaxAdPublicationTime(final TimePeriod maxAdPublicationTime) {
        this.maxAdPublicationTime = maxAdPublicationTime;
    }

    public void setMaxAdPublicationTimeNumber(final int number) {
        this.maxAdPublicationTime.setNumber(number);
    }

    public void setMaxAdPublicationTimeField(final TimePeriod.Field field) {
        this.maxAdPublicationTime.setField(field);
    }

    public void setMaxAdsPerMember(final int maxAdsPerMember) {
        this.maxAdsPerMember = maxAdsPerMember;
    }

    public void setMaxImagesPerMember(final int maxImagesPerMember) {
        this.maxImagesPerMember = maxImagesPerMember;
    }

    public void setMaxPinWrongTries(final int maxPinWrongTries) {
        this.maxPinWrongTries = maxPinWrongTries;
    }

    public void setMaxPosSchedulingPayments(final int maxPosSchedulingPayments) {
        this.maxPosSchedulingPayments = maxPosSchedulingPayments;
    }

    public void setMaxSchedulingPayments(final int maxSchedulingPayments) {
        this.maxSchedulingPayments = maxSchedulingPayments;
    }

    public void setMaxSchedulingPeriod(final TimePeriod maxSchedulingPeriod) {
        this.maxSchedulingPeriod = maxSchedulingPeriod;
    }

    public void setMaxSchedulingPeriodNumber(final int number) {
        this.maxSchedulingPeriod.setNumber(number);
    }

    public void setMaxSchedulingPeriodField(final TimePeriod.Field field) {
        this.maxSchedulingPeriod.setField(field);
    }

    public void setNumberOfCopies(final int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void setPinBlockTimeAfterMaxTries(final TimePeriod pinBlockTimeAfterMaxTries) {
        this.pinBlockTimeAfterMaxTries = pinBlockTimeAfterMaxTries;
    }

    public void setPinBlockTimeAfterMaxTriesNumber(final int number) {
        this.pinBlockTimeAfterMaxTries.setNumber(number);
    }

    public void setPinBlockTimeAfterMaxTriesField(final TimePeriod.Field field) {
        this.pinBlockTimeAfterMaxTries.setField(field);
    }

    public void setPinLength(final RangeConstraint pinLength) {
        this.pinLength = pinLength;
    }

    public void setPinLengthMin(final Integer pinLength) {
        this.pinLength.setMin(pinLength);
    }

    public void setPinLengthMax(final Integer pinLength) {
        this.pinLength.setMax(pinLength);
    }

    public void setRepayLoanByGroup(final boolean repayLoansByGroup) {
        repayLoanByGroup = repayLoansByGroup;
    }

    public void setResultPageSize(final int resultPageSize) {
        this.resultPageSize = resultPageSize;
    }

    public void setSendPasswordByEmail(final boolean sendPasswordByMail) {
        sendPasswordByEmail = sendPasswordByMail;
    }

    public void setShowPosWebPaymentDescription(final boolean showPosWebPaymentDescription) {
        this.showPosWebPaymentDescription = showPosWebPaymentDescription;
    }

    public void setSmsAdditionalCharged(final int smsAdditionalCharged) {
        this.smsAdditionalCharged = smsAdditionalCharged;
    }

    public void setSmsAdditionalChargedPeriod(final TimePeriod smsAdditionalChargedPeriod) {
        this.smsAdditionalChargedPeriod = smsAdditionalChargedPeriod;
    }

    public void setSmsAdditionalChargedPeriodNumber(final int number) {
        this.smsAdditionalChargedPeriod.setNumber(number);
    }

    public void setSmsAdditionalChargedPeriodField(final TimePeriod.Field field) {
        this.smsAdditionalChargedPeriod.setField(field);
    }

    public void setSmsChargeAmount(final BigDecimal smsChargeAmount) {
        this.smsChargeAmount = smsChargeAmount;
    }

    public void setSmsChargeTransferType(final TransferType smsChargeTransferType) {
        this.smsChargeTransferType = smsChargeTransferType;
    }

    public void setSmsContextClassName(final String smsContextClassName) {
        this.smsContextClassName = smsContextClassName;
    }

    public void setSmsFree(final int smsFree) {
        this.smsFree = smsFree;
    }

    public void setSmsShowFreeThreshold(final int smsShowFreeThreshold) {
        this.smsShowFreeThreshold = smsShowFreeThreshold;
    }

    public void setViewLoansByGroup(final boolean viewLoansByGroup) {
        this.viewLoansByGroup = viewLoansByGroup;
    }

}
