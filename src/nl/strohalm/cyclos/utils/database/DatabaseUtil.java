/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.strohalm.cyclos.utils.database;

import javax.persistence.EntityManager;

/**
 *
 * @author henry
 */
public class DatabaseUtil {

    public static final int MSSQL = 0;
    public static final int MYSQL = 1;
    public static final int MARIADB = 1;
    public static final int POSTGRESQL = 2;
    public static final int POSTGRESQLSSL = 3;
    public static final int POSTGRESQLSSLNOVEF = 4;
    public static final int H2DATABASE = 5;
    public static final int H2DATABASEEMBEDDED = 6;
    public static final int H2DATABASEMEMORY = 7;

    private static boolean isHibernate = true;
    private static int dbType = MARIADB;
    private static String host = "localhost";
    private static String dbName = "CyclosDB";
    private static String dbPort = "3307";
    private static String username = "root";
    private static String password = "1234";

    private static boolean isOpen = false;

    final static Class[] CLASSES_LIST = new Class[]{
        //nl.strohalm.cyclos.entities
        nl.strohalm.cyclos.entities.Application.class,
        nl.strohalm.cyclos.entities.IndexOperation.class,
        //nl.strohalm.cyclos.entities.access
        nl.strohalm.cyclos.entities.access.AdminUser.class,
        nl.strohalm.cyclos.entities.access.Channel.class,
        nl.strohalm.cyclos.entities.access.ChannelPrincipal.class,
        nl.strohalm.cyclos.entities.access.LoginHistoryLog.class,
        nl.strohalm.cyclos.entities.access.MemberUser.class,
        nl.strohalm.cyclos.entities.access.OperatorUser.class,
        nl.strohalm.cyclos.entities.access.PasswordHistoryLog.class,
        nl.strohalm.cyclos.entities.access.PermissionDeniedTrace.class,
        nl.strohalm.cyclos.entities.access.Session.class,
        nl.strohalm.cyclos.entities.access.User.class,
        nl.strohalm.cyclos.entities.access.UsernameChangeLog.class,
        nl.strohalm.cyclos.entities.access.WrongCredentialAttempt.class,
        nl.strohalm.cyclos.entities.access.WrongUsernameAttempt.class,
        //nl.strohalm.cyclos.entities.accounts
        nl.strohalm.cyclos.entities.accounts.ARateParameters.class,
        nl.strohalm.cyclos.entities.accounts.Account.class,
        nl.strohalm.cyclos.entities.accounts.AccountLimitLog.class,
        nl.strohalm.cyclos.entities.accounts.AccountLock.class,
        nl.strohalm.cyclos.entities.accounts.AccountRates.class,
        nl.strohalm.cyclos.entities.accounts.AccountType.class,
        nl.strohalm.cyclos.entities.accounts.AmountReservation.class,
        nl.strohalm.cyclos.entities.accounts.BaseTransferAmountReservation.class,
        nl.strohalm.cyclos.entities.accounts.ClosedAccountBalance.class,
        nl.strohalm.cyclos.entities.accounts.Currency.class,
        nl.strohalm.cyclos.entities.accounts.DRateParameters.class,
        nl.strohalm.cyclos.entities.accounts.InitializableRateParameters.class,
        nl.strohalm.cyclos.entities.accounts.IRateParameters.class,
        nl.strohalm.cyclos.entities.accounts.InstallmentAmountReservation.class,
        nl.strohalm.cyclos.entities.accounts.MemberAccount.class,
        nl.strohalm.cyclos.entities.accounts.MemberAccountType.class,
        nl.strohalm.cyclos.entities.accounts.MemberGroupAccountSettings.class,
        nl.strohalm.cyclos.entities.accounts.PendingAuthorizationAmountReservation.class,
        nl.strohalm.cyclos.entities.accounts.ScheduledPaymentAmountReservation.class,
        nl.strohalm.cyclos.entities.accounts.RateParameters.class,
        nl.strohalm.cyclos.entities.accounts.SystemAccount.class,
        nl.strohalm.cyclos.entities.accounts.SystemAccountType.class,
        nl.strohalm.cyclos.entities.accounts.TransferAuthorizationAmountReservation.class,
        //nl.strohalm.cyclos.entities.accounts.cards
        nl.strohalm.cyclos.entities.accounts.cards.Card.class,
        nl.strohalm.cyclos.entities.accounts.cards.CardType.class,
        nl.strohalm.cyclos.entities.accounts.cards.CardLog.class,
        //nl.strohalm.cyclos.entities.accounts.external
        nl.strohalm.cyclos.entities.accounts.external.ExternalAccount.class,
        nl.strohalm.cyclos.entities.accounts.external.ExternalTransfer.class,
        nl.strohalm.cyclos.entities.accounts.external.ExternalTransferImport.class,
        nl.strohalm.cyclos.entities.accounts.external.ExternalTransferType.class,
        //nl.strohalm.cyclos.entities.accounts.external.filemapping
        nl.strohalm.cyclos.entities.accounts.external.filemapping.CSVFileMapping.class,
        nl.strohalm.cyclos.entities.accounts.external.filemapping.CustomFileMapping.class,
        nl.strohalm.cyclos.entities.accounts.external.filemapping.FieldMapping.class,
        nl.strohalm.cyclos.entities.accounts.external.filemapping.FileMapping.class,
        nl.strohalm.cyclos.entities.accounts.external.filemapping.FileMappingWithFields.class,
        //nl.strohalm.cyclos.entities.accounts.fees.account
        nl.strohalm.cyclos.entities.accounts.fees.account.AccountFee.class,
        nl.strohalm.cyclos.entities.accounts.fees.account.AccountFeeAmount.class,
        nl.strohalm.cyclos.entities.accounts.fees.account.AccountFeeLog.class,
        nl.strohalm.cyclos.entities.accounts.fees.account.MemberAccountFeeLog.class,
        //nl.strohalm.cyclos.entities.accounts.fees.transaction
        nl.strohalm.cyclos.entities.accounts.fees.transaction.BrokerCommission.class,
        nl.strohalm.cyclos.entities.accounts.fees.transaction.SimpleTransactionFee.class,
        nl.strohalm.cyclos.entities.accounts.fees.transaction.TransactionFee.class,
        //nl.strohalm.cyclos.entities.accounts.guarantees
        nl.strohalm.cyclos.entities.accounts.guarantees.Certification.class,
        nl.strohalm.cyclos.entities.accounts.guarantees.CertificationLog.class,
        nl.strohalm.cyclos.entities.accounts.guarantees.Guarantee.class,
        nl.strohalm.cyclos.entities.accounts.guarantees.GuaranteeLog.class,
        nl.strohalm.cyclos.entities.accounts.guarantees.GuaranteeType.class,
        nl.strohalm.cyclos.entities.accounts.guarantees.PaymentObligation.class,
        nl.strohalm.cyclos.entities.accounts.guarantees.PaymentObligationLog.class,
        //nl.strohalm.cyclos.entities.accounts.loans
        nl.strohalm.cyclos.entities.accounts.loans.Loan.class,
        nl.strohalm.cyclos.entities.accounts.loans.LoanGroup.class,
        nl.strohalm.cyclos.entities.accounts.loans.LoanPayment.class,
        //nl.strohalm.cyclos.entities.accounts.pos
        nl.strohalm.cyclos.entities.accounts.pos.MemberPos.class,
        nl.strohalm.cyclos.entities.accounts.pos.Pos.class,
        nl.strohalm.cyclos.entities.accounts.pos.PosLog.class,
        //nl.strohalm.cyclos.entities.accounts.transactions
        nl.strohalm.cyclos.entities.accounts.transactions.AuthorizationLevel.class,
        nl.strohalm.cyclos.entities.accounts.transactions.Invoice.class,
        nl.strohalm.cyclos.entities.accounts.transactions.InvoicePayment.class,
        nl.strohalm.cyclos.entities.accounts.transactions.PaymentFilter.class,
        nl.strohalm.cyclos.entities.accounts.transactions.ScheduledPayment.class,
        nl.strohalm.cyclos.entities.accounts.transactions.Transfer.class,
        nl.strohalm.cyclos.entities.accounts.transactions.TransferAuthorization.class,
        nl.strohalm.cyclos.entities.accounts.transactions.TransferType.class,
        nl.strohalm.cyclos.entities.accounts.transactions.PaymentRequestTicket.class,
        nl.strohalm.cyclos.entities.accounts.transactions.Ticket.class,
        nl.strohalm.cyclos.entities.accounts.transactions.TraceNumber.class,
        nl.strohalm.cyclos.entities.accounts.transactions.WebShopTicket.class,
        //nl.strohalm.cyclos.entities.ads
        nl.strohalm.cyclos.entities.ads.Ad.class,
        nl.strohalm.cyclos.entities.ads.AdCategory.class,
        //nl.strohalm.cyclos.entities.ads.imports
        nl.strohalm.cyclos.entities.ads.imports.AdImport.class,
        nl.strohalm.cyclos.entities.ads.imports.ImportedAd.class,
        nl.strohalm.cyclos.entities.ads.imports.ImportedAdCategory.class,
        nl.strohalm.cyclos.entities.ads.imports.ImportedAdCustomFieldValue.class,
        //nl.strohalm.cyclos.entities.alerts
        nl.strohalm.cyclos.entities.alerts.Alert.class,
        nl.strohalm.cyclos.entities.alerts.ErrorLogEntry.class,
        nl.strohalm.cyclos.entities.alerts.MemberAlert.class,
        nl.strohalm.cyclos.entities.alerts.SystemAlert.class,
        //nl.strohalm.cyclos.entities.customization.binaryfiles
        nl.strohalm.cyclos.entities.customization.binaryfiles.BinaryFile.class,
        //nl.strohalm.cyclos.entities.customization.documents
        nl.strohalm.cyclos.entities.customization.documents.Document.class,
        nl.strohalm.cyclos.entities.customization.documents.DocumentPage.class,
        nl.strohalm.cyclos.entities.customization.documents.DynamicDocument.class,
        nl.strohalm.cyclos.entities.customization.documents.MemberDocument.class,
        nl.strohalm.cyclos.entities.customization.documents.StaticDocument.class,
        //nl.strohalm.cyclos.entities.customization.fields
        nl.strohalm.cyclos.entities.customization.fields.AdCustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.AdCustomFieldValue.class,
        nl.strohalm.cyclos.entities.customization.fields.AdminCustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.AdminCustomFieldValue.class,
        nl.strohalm.cyclos.entities.customization.fields.CustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.CustomFieldValue.class,
        nl.strohalm.cyclos.entities.customization.fields.CustomFieldPossibleValue.class,
        nl.strohalm.cyclos.entities.customization.fields.LoanGroupCustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.LoanGroupCustomFieldValue.class,
        nl.strohalm.cyclos.entities.customization.fields.MemberCustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.MemberCustomFieldValue.class,
        nl.strohalm.cyclos.entities.customization.fields.MemberRecordCustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.MemberRecordCustomFieldValue.class,
        nl.strohalm.cyclos.entities.customization.fields.OperatorCustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.OperatorCustomFieldValue.class,
        nl.strohalm.cyclos.entities.customization.fields.PaymentCustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.PaymentCustomFieldValue.class,
        //nl.strohalm.cyclos.entities.customization.files
        nl.strohalm.cyclos.entities.customization.files.CustomizedFile.class,
        nl.strohalm.cyclos.entities.customization.files.File.class,
        //nl.strohalm.cyclos.entities.customization.images
        nl.strohalm.cyclos.entities.customization.images.AdImage.class,
        nl.strohalm.cyclos.entities.customization.images.CustomImage.class,
        nl.strohalm.cyclos.entities.customization.images.MemberImage.class,
        nl.strohalm.cyclos.entities.customization.images.StyleImage.class,
        nl.strohalm.cyclos.entities.customization.images.SystemImage.class,
        nl.strohalm.cyclos.entities.customization.images.Image.class,
        //nl.strohalm.cyclos.entities.customization.translationMessages
        nl.strohalm.cyclos.entities.customization.translationMessages.TranslationMessage.class,
        //nl.strohalm.cyclos.entities.groups
        nl.strohalm.cyclos.entities.groups.AdminGroup.class,
        nl.strohalm.cyclos.entities.groups.BrokerGroup.class,
        nl.strohalm.cyclos.entities.groups.Group.class,
        nl.strohalm.cyclos.entities.groups.GroupFilter.class,
        nl.strohalm.cyclos.entities.groups.GroupHistoryLog.class,
        nl.strohalm.cyclos.entities.groups.SystemGroup.class,
        nl.strohalm.cyclos.entities.groups.MemberGroup.class,
        nl.strohalm.cyclos.entities.groups.OperatorGroup.class,
        //nl.strohalm.cyclos.entities.infotexts.InfoText
        nl.strohalm.cyclos.entities.infotexts.InfoText.class,
        //nl.strohalm.cyclos.entities.members
        nl.strohalm.cyclos.entities.members.Administrator.class,
        nl.strohalm.cyclos.entities.members.Contact.class,
        nl.strohalm.cyclos.entities.members.Element.class,
        nl.strohalm.cyclos.entities.members.GeneralReference.class,
        nl.strohalm.cyclos.entities.members.Member.class,
        nl.strohalm.cyclos.entities.members.Operator.class,
        nl.strohalm.cyclos.entities.members.PendingEmailChange.class,
        nl.strohalm.cyclos.entities.members.PendingMember.class,
        nl.strohalm.cyclos.entities.members.Reference.class,
        nl.strohalm.cyclos.entities.members.ReferenceHistoryLog.class,
        nl.strohalm.cyclos.entities.members.RegistrationAgreement.class,
        nl.strohalm.cyclos.entities.members.RegistrationAgreementLog.class,
        nl.strohalm.cyclos.entities.members.TransactionFeedback.class,
        //nl.strohalm.cyclos.entities.members.adInterests
        nl.strohalm.cyclos.entities.members.adInterests.AdInterest.class,
        //nl.strohalm.cyclos.entities.members.brokerings
        nl.strohalm.cyclos.entities.members.brokerings.BrokerCommissionContract.class,
        nl.strohalm.cyclos.entities.members.brokerings.Brokering.class,
        nl.strohalm.cyclos.entities.members.brokerings.BrokeringCommissionStatus.class,
        nl.strohalm.cyclos.entities.members.brokerings.DefaultBrokerCommission.class,
        //nl.strohalm.cyclos.entities.members.imports
        nl.strohalm.cyclos.entities.members.imports.ImportedMember.class,
        nl.strohalm.cyclos.entities.members.imports.ImportedMemberRecord.class,
        nl.strohalm.cyclos.entities.members.imports.ImportedMemberRecordCustomFieldValue.class,
        nl.strohalm.cyclos.entities.members.imports.MemberImport.class,
        //nl.strohalm.cyclos.entities.members.messages
        nl.strohalm.cyclos.entities.members.messages.Message.class,
        nl.strohalm.cyclos.entities.members.messages.MessageCategory.class,
        //nl.strohalm.cyclos.entities.members.preferences
        nl.strohalm.cyclos.entities.members.preferences.AdminNotificationPreference.class,
        nl.strohalm.cyclos.entities.members.preferences.NotificationPreference.class,
        //nl.strohalm.cyclos.entities.members.printsettings
        nl.strohalm.cyclos.entities.members.printsettings.ReceiptPrinterSettings.class,
        //nl.strohalm.cyclos.entities.members.records
        nl.strohalm.cyclos.entities.members.records.MemberRecord.class,
        nl.strohalm.cyclos.entities.members.records.MemberRecordType.class,
        //nl.strohalm.cyclos.entities.members.remarks
        nl.strohalm.cyclos.entities.members.remarks.BrokerRemark.class,
        nl.strohalm.cyclos.entities.members.remarks.GroupRemark.class,
        nl.strohalm.cyclos.entities.members.remarks.Remark.class,
        //nl.strohalm.cyclos.entities.services
        nl.strohalm.cyclos.entities.services.ServiceClient.class,
        //nl.strohalm.cyclos.entities.settings
        nl.strohalm.cyclos.entities.settings.Setting.class,
        //nl.strohalm.cyclos.entities.sms
        nl.strohalm.cyclos.entities.sms.MemberSmsStatus.class,
        nl.strohalm.cyclos.entities.sms.MemberSmsStatusLock.class,
        nl.strohalm.cyclos.entities.sms.SmsLog.class,
        nl.strohalm.cyclos.entities.sms.SmsMailing.class,
        nl.strohalm.cyclos.entities.sms.SmsType.class
    };

    public static void openDatabase() {
        if (!isOpen) {
            if (isHibernate) {
                HibernateUtil.setDbType(dbType);
                HibernateUtil.setUser(username);
                HibernateUtil.setPwd(password);
                HibernateUtil.setServerUrl(host, dbPort, dbName);
                HibernateUtil.getSession().close();
            } else {
                OpenJPAUtil.setDbType(dbType);
                OpenJPAUtil.setUser(username);
                OpenJPAUtil.setPwd(password);
                OpenJPAUtil.setServerUrl(host, dbPort, dbName);
                OpenJPAUtil.getCurrentEntityManager();
            }
            isOpen = true;
        }
    }

    public static EntityManager getEntityManager() {
        openDatabase();
        if (isHibernate) {
            return HibernateUtil.getSession();
        } else {
            return OpenJPAUtil.getEntityManager();
        }
    }

    public static EntityManager getCurrentEntityManager() {
        openDatabase();
        if (isHibernate) {
            return HibernateUtil.getCurrentSession();
        } else {
            return OpenJPAUtil.getCurrentEntityManager();
        }
    }

    public static boolean isIsHibernate() {
        return isHibernate;
    }

    public static void setIsHibernate(boolean isHibernate) {
        DatabaseUtil.isHibernate = isHibernate;
    }

    public static int getDbType() {
        return dbType;
    }

    public static void setDbType(int dbType) {
        DatabaseUtil.dbType = dbType;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        DatabaseUtil.host = host;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        DatabaseUtil.dbName = dbName;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DatabaseUtil.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DatabaseUtil.password = password;
    }

    public static String getDbPort() {
        return dbPort;
    }

    public static void setDbPort(String dbPort) {
        DatabaseUtil.dbPort = dbPort;
    }

}
