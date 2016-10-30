/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.strohalm.cyclos;

/**
 *
 * @author henry
 */
public class DatabaseClassList {

    final static Class[] classList = new Class[]{
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
        //nl.strohalm.cyclos.entities.accounts.cards
        nl.strohalm.cyclos.entities.accounts.cards.Card.class,
        nl.strohalm.cyclos.entities.accounts.cards.CardType.class,
        nl.strohalm.cyclos.entities.accounts.cards.CardLog.class,
        //nl.strohalm.cyclos.entities.accounts.pos
        nl.strohalm.cyclos.entities.accounts.pos.MemberPos.class,
        nl.strohalm.cyclos.entities.accounts.pos.Pos.class,
        nl.strohalm.cyclos.entities.accounts.pos.PosLog.class,
        //nl.strohalm.cyclos.entities.customization.binaryfiles
        nl.strohalm.cyclos.entities.customization.binaryfiles.BinaryFile.class,
        //nl.strohalm.cyclos.entities.infotexts.InfoText
        nl.strohalm.cyclos.entities.infotexts.InfoText.class,
        //nl.strohalm.cyclos.entities.members
        nl.strohalm.cyclos.entities.members.Contact.class,
        nl.strohalm.cyclos.entities.members.Element.class,
        nl.strohalm.cyclos.entities.members.Member.class,
        nl.strohalm.cyclos.entities.members.Operator.class,
        //nl.strohalm.cyclos.entities.members.remarks
        nl.strohalm.cyclos.entities.members.remarks.BrokerRemark.class,
        nl.strohalm.cyclos.entities.members.remarks.GroupRemark.class,
        nl.strohalm.cyclos.entities.members.remarks.Remark.class,
        //nl.strohalm.cyclos.entities.groups
        nl.strohalm.cyclos.entities.groups.Group.class,
        nl.strohalm.cyclos.entities.groups.SystemGroup.class,
        nl.strohalm.cyclos.entities.groups.MemberGroup.class,
        //nl.strohalm.cyclos.entities.customization.fields
        nl.strohalm.cyclos.entities.customization.fields.CustomField.class,
        nl.strohalm.cyclos.entities.customization.fields.CustomFieldPossibleValue.class,
        nl.strohalm.cyclos.entities.customization.fields.MemberCustomField.class,
        //nl.strohalm.cyclos.entities.services
        nl.strohalm.cyclos.entities.services.ServiceClient.class,
        //nl.strohalm.cyclos.entities.settings
        nl.strohalm.cyclos.entities.settings.Setting.class
    };
}
