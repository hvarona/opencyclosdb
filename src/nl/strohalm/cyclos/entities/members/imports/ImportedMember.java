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
package nl.strohalm.cyclos.entities.members.imports;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.customization.fields.MemberCustomField;
import nl.strohalm.cyclos.entities.customization.fields.MemberCustomFieldValue;
import nl.strohalm.cyclos.utils.CustomFieldsContainer;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * Contains temporary data for an imported member
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name="imported_members")
public class ImportedMember extends Entity implements CustomFieldsContainer<MemberCustomField, MemberCustomFieldValue> {

    public static enum Relationships implements Relationship {

        IMPORT("import"), CUSTOM_VALUES("customValues"), RECORDS("records");

        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static enum Status implements StringValuedEnum {

        SUCCESS, MISSING_NAME, MISSING_USERNAME, INVALID_USERNAME, USERNAME_ALREADY_IN_USE, MISSING_EMAIL, INVALID_EMAIL, INVALID_CREATION_DATE, MISSING_CUSTOM_FIELD, INVALID_CUSTOM_FIELD, INVALID_BALANCE, BALANCE_LOWER_THAN_CREDIT_LIMIT, BALANCE_UPPER_THAN_CREDIT_LIMIT, INVALID_CREDIT_LIMIT, INVALID_UPPER_CREDIT_LIMIT, INVALID_RECORD_TYPE, INVALID_RECORD_TYPE_FIELD, MISSING_RECORD_FIELD, INVALID_RECORD_FIELD, UNKNOWN_ERROR, INVALID_CUSTOM_FIELD_VALUE_UNIQUE, INVALID_CUSTOM_FIELD_VALUE_MAX_LENGTH, INVALID_CUSTOM_FIELD_VALUE_MIN_LENGTH;

        @Override
        public String getValue() {
            return name();
        }
    }

    private static final long serialVersionUID = -4080042034080488479L;
    private MemberImport _import;
    private Status status;
    private String errorArgument1;
    private String errorArgument2;
    private String name;
    private String salt;
    private String username;
    private String password;
    private String email;
    private Integer lineNumber;
    private Calendar creationDate;
    private BigDecimal creditLimit;
    private BigDecimal upperCreditLimit;
    private BigDecimal initialBalance;
    private Collection<MemberCustomFieldValue> customValues;
    private Collection<ImportedMemberRecord> records;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "creation_date")
    public Calendar getCreationDate() {
        return creationDate;
    }

    @Column(name = "credit_limit", precision = 15, scale = 6)
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    @Transient
    @Override
    public Class<MemberCustomField> getCustomFieldClass() {
        return MemberCustomField.class;
    }

    @Transient
    @Override
    public Class<MemberCustomFieldValue> getCustomFieldValueClass() {
        return MemberCustomFieldValue.class;
    }

    @OneToMany(targetEntity = MemberCustomFieldValue.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "imported_member_id")
    @Override
    public Collection<MemberCustomFieldValue> getCustomValues() {
        return customValues;
    }

    @Column(name = "email", length = 100)
    public String getEmail() {
        return email;
    }

    @Column(name = "error_argument1", length = 200)
    public String getErrorArgument1() {
        return errorArgument1;
    }

    @Column(name = "error_argument2", length = 200)
    public String getErrorArgument2() {
        return errorArgument2;
    }

    @ManyToOne(targetEntity = MemberImport.class)
    @JoinColumn(name = "import_id")
    public MemberImport getImport() {
        return _import;
    }

    @Column(name = "initial_balance", precision = 15, scale = 6)
    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    @Column(name = "line_number")
    public Integer getLineNumber() {
        return lineNumber;
    }

    @Column(name = "name", length = 100)
    @Override
    public String getName() {
        return name;
    }

    @Column(name = "password", length = 64)
    public String getPassword() {
        return password;
    }

    @OneToMany(targetEntity = ImportedMemberRecord.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "imported_member_id")
    public Collection<ImportedMemberRecord> getRecords() {
        return records;
    }

    @Column(name = "salt", length = 32)
    public String getSalt() {
        return salt;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    public Status getStatus() {
        return status;
    }

    @Column(name = "upper_credit_limit", precision = 15, scale = 6)
    public BigDecimal getUpperCreditLimit() {
        return upperCreditLimit;
    }

    @Column(name = "username", length = 64)
    public String getUsername() {
        return username;
    }

    public void setCreationDate(final Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreditLimit(final BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    @Override
    public void setCustomValues(final Collection<MemberCustomFieldValue> customValues) {
        this.customValues = customValues;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setErrorArgument1(final String errorArgument) {
        errorArgument1 = errorArgument;
    }

    public void setErrorArgument2(final String errorArgument2) {
        this.errorArgument2 = errorArgument2;
    }

    public void setImport(final MemberImport _import) {
        this._import = _import;
    }

    public void setInitialBalance(final BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void setLineNumber(final Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setRecords(final Collection<ImportedMemberRecord> records) {
        this.records = records;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    public void setStatus(final Status status) {
        this.status = status;
        if (status != null && status != Status.SUCCESS) {
            creditLimit = BigDecimal.ZERO;
            initialBalance = null;
            upperCreditLimit = null;
        }
    }

    public void setUpperCreditLimit(final BigDecimal upperCreditLimit) {
        this.upperCreditLimit = upperCreditLimit;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return getId() + " - " + getName() + " (" + getUsername() + ")";
    }

}
