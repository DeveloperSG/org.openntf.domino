
package org.openntf.domino.extmgr;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.openntf.domino.extmgr.events.AbstractEMBridgeEvent;
import org.openntf.domino.extmgr.events.AdminPProcessRequestEvent;
import org.openntf.domino.extmgr.events.AgentOpenEvent;
import org.openntf.domino.extmgr.events.ClearPasswordEvent;
import org.openntf.domino.extmgr.events.FTDeleteIndexEvent;
import org.openntf.domino.extmgr.events.FTIndexEvent;
import org.openntf.domino.extmgr.events.FTSearchEvent;
import org.openntf.domino.extmgr.events.IEMBridgeEvent;
import org.openntf.domino.extmgr.events.MailSendNoteEvent;
import org.openntf.domino.extmgr.events.MediaRecoveryEvent;
import org.openntf.domino.extmgr.events.NIFOpenCollectionEvent;
import org.openntf.domino.extmgr.events.NSFAddToFolderEvent;
import org.openntf.domino.extmgr.events.NSFConflictHandlerEvent;
import org.openntf.domino.extmgr.events.NSFDbCloseEvent;
import org.openntf.domino.extmgr.events.NSFDbCompactEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyACLEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyNoteEvent;
import org.openntf.domino.extmgr.events.NSFDbCopyTemplateACLEvent;
import org.openntf.domino.extmgr.events.NSFDbCreateACLFromTemplateEvent;
import org.openntf.domino.extmgr.events.NSFDbCreateAndCopyEvent;
import org.openntf.domino.extmgr.events.NSFDbCreateEvent;
import org.openntf.domino.extmgr.events.NSFDbDeleteEvent;
import org.openntf.domino.extmgr.events.NSFDbDeleteNotesEvent;
import org.openntf.domino.extmgr.events.NSFDbNoteLockEvent;
import org.openntf.domino.extmgr.events.NSFDbNoteUnlockEvent;
import org.openntf.domino.extmgr.events.NSFDbOpenExtendedEvent;
import org.openntf.domino.extmgr.events.NSFDbRenameEvent;
import org.openntf.domino.extmgr.events.NSFDbReopenEvent;
import org.openntf.domino.extmgr.events.NSFHookNoteOpenEvent;
import org.openntf.domino.extmgr.events.NSFHookNoteUpdateEvent;
import org.openntf.domino.extmgr.events.NSFNoteAttachFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteCipherDecryptEvent;
import org.openntf.domino.extmgr.events.NSFNoteCipherExtractFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteCloseEvent;
import org.openntf.domino.extmgr.events.NSFNoteCopyEvent;
import org.openntf.domino.extmgr.events.NSFNoteCreateEvent;
import org.openntf.domino.extmgr.events.NSFNoteDecryptEvent;
import org.openntf.domino.extmgr.events.NSFNoteDeleteEvent;
import org.openntf.domino.extmgr.events.NSFNoteDetachFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteExtractFileEvent;
import org.openntf.domino.extmgr.events.NSFNoteOpenByUNIDEvent;
import org.openntf.domino.extmgr.events.NSFNoteOpenEvent;
import org.openntf.domino.extmgr.events.NSFNoteOpenExtendedEvent;
import org.openntf.domino.extmgr.events.NSFNoteUpdateExtendedEvent;
import org.openntf.domino.extmgr.events.NSFNoteUpdateMailBoxEvent;
import org.openntf.domino.extmgr.events.RouterJournalMessageEvent;
import org.openntf.domino.extmgr.events.SECAuthenticationEvent;
import org.openntf.domino.extmgr.events.SMTPCommandEvent;
import org.openntf.domino.extmgr.events.SMTPConnectEvent;
import org.openntf.domino.extmgr.events.SMTPDisconnectEvent;
import org.openntf.domino.extmgr.events.SMTPMessageAcceptEvent;
import org.openntf.domino.extmgr.events.TerminateNSFEvent;
import org.openntf.domino.extmgr.events.UnknownEMEvent;

public class ExtensionManagerBridge {

	private static final String EM_EVENT_PREFIX = "[[event:"; // $NON-NLS-1$
	private static final String EM_NSFHOOKEVENT_PREFIX = "[[dbhookevent:"; // $NON-NLS-1$
	private static final String EM_EVENT_POSTFIX = "]];";

	private static final HashMap<Integer, Class<?>> eventsMap = new HashMap<Integer, Class<?>>();

	static {
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCLOSE, NSFDbCloseEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEUPDATE, NSFNoteUpdateExtendedEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEUPDATEXTENDED, NSFNoteUpdateExtendedEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCREATE, NSFDbCreateEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBDELETE, NSFDbDeleteEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTECREATE, NSFNoteCreateEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEDELETE, NSFNoteDeleteEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_ADMINPPROCESSREQUEST, AdminPProcessRequestEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEOPEN, NSFNoteOpenEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTECLOSE, NSFNoteCloseEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEOPENBYUNID, NSFNoteOpenByUNIDEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_FTINDEX, FTIndexEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_FTSEARCH, FTSearchEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYKEY , NIFFindByKeyEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYNAME , NIFFindByNameEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFOPENNOTE , NIFOpenNoteEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFREADENTRIES , NIFReadEntriesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFUPDATECOLLECTION , NIFUpdateCollectionEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCOMPACT, NSFDbCompactEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBDELETENOTES, NSFDbDeleteNotesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFDBWRITEOBJECT , NSFDbWriteObjectEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NIFOPENCOLLECTION, NIFOpenCollectionEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFCLOSECOLLECTION , NIFCloseCollectionEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBRENAME, NSFDbRenameEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBREOPEN, NSFDbReopenEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBOPENEXTENDED, NSFDbOpenExtendedEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEOPENEXTENDED, NSFNoteOpenExtendedEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_TERMINATENSF, TerminateNSFEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEDECRYPT, NSFNoteDecryptEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFCONFLICTHANDLER, NSFConflictHandlerEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_MAILSENDNOTE, MailSendNoteEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_CLEARPASSWORD, ClearPasswordEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHFREETIMESEARCH, SCHFreeTimeSearchEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHRETRIEVE, SCHRetrieveEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHSRVRETRIEVE, SCHSrvRetrieveEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCOMPACTEXTENDED, NSFDbCompactEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCOPYNOTE, NSFDbCopyNoteEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTECOPY, NSFNoteCopyEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEATTACHFILE, NSFNoteAttachFileEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEDETACHFILE, NSFNoteDetachFileEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEEXTRACTFILE, NSFNoteExtractFileEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEATTACHOLE2OBJECT, NSFNoteAttachOLE2ObjectEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDELETEOLE2OBJECT, NSFNoteDeleteOLE2ObjectEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEEXTRACTOLE2OBJECT, NSFNoteExtractOLE2ObjectEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCOPY, NSFDbCopyEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCREATEANDCOPY, NSFDbCreateAndCopyEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCOPYACL, NSFDbCopyACLEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCOPYTEMPLATEACL, NSFDbCopyTemplateACLEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBCREATEACLFROMTEMPLATE, NSFDbCreateACLFromTemplateEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_FTDELETEINDEX, FTDeleteIndexEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_FTSEARCHEXT, FTSearchExtEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP, NameLookupEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTEUPDATEMAILBOX, NSFNoteUpdateMailBoxEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_AGENTOPEN, AgentOpenEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_AGENTRUN, AgentRunEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_AGENTCLOSE, AgentCloseEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_SECAUTHENTICATION, SECAuthenticationEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP2, NameLookup2Event.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFADDTOFOLDER, NSFAddToFolderEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_ROUTERJOURNALMESSAGE, RouterJournalMessageEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_SMTPCONNECT, SMTPConnectEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_SMTPCOMMAND, SMTPCommandEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_SMTPMESSAGEACCEPT, SMTPMessageAcceptEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_SMTPDISCONNECT, SMTPDisconnectEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVECOPYNOTES, NSFArchiveCopyNotesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVEDELETENOTES, NSFArchiveDeleteNotesEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_MEDIARECOVERY_NOTE, MediaRecoveryEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTECIPHERDECRYPT, NSFNoteCipherDecryptEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFNOTECIPHEREXTRACTFILE, NSFNoteCipherExtractFileEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBNOTELOCK, NSFDbNoteLockEvent.class);
		eventsMap.put(IEMBridgeEvent.EM_NSFDBNOTEUNLOCK, NSFDbNoteUnlockEvent.class);

		eventsMap.put(IEMBridgeEvent.HOOK_EVENT_NOTE_UPDATE, NSFHookNoteUpdateEvent.class);
		eventsMap.put(IEMBridgeEvent.HOOK_EVENT_NOTE_OPEN, NSFHookNoteOpenEvent.class);
	}

	/**
	 * 
	 */
	private ExtensionManagerBridge() {
	}

	/**
	 * @param commandBuffer
	 * @return
	 */
	public static IEMBridgeEvent getEventFromCommand(String commandBuffer) {
		boolean bIsEvent = commandBuffer.startsWith(EM_EVENT_PREFIX);
		boolean bIsNSFHookEvent = commandBuffer.startsWith(EM_NSFHOOKEVENT_PREFIX);
		if (!bIsEvent && !bIsNSFHookEvent) {
			return null;
		}

		String prefix = bIsEvent ? EM_EVENT_PREFIX : EM_NSFHOOKEVENT_PREFIX;
		commandBuffer = commandBuffer.substring(prefix.length());
		int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		if (iIndex < 0) {
			return null;
		}

		try {
			int eventId = Integer.parseInt(commandBuffer.substring(0, iIndex).trim());
			return getEvent(eventId, commandBuffer.substring(iIndex + EM_EVENT_POSTFIX.length()));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * @param eventId
	 * @param eventBuffer
	 * @return
	 */
	private static IEMBridgeEvent getEvent(final int eventId, final String eventBuffer) {
		AbstractEMBridgeEvent retEvent = null;
		Class<?> eventClass = eventsMap.get(eventId);
		if (eventClass != null) {
			try {
				Constructor<?> constructor = null;
				try {
					constructor = eventClass.getConstructor();
					retEvent = (AbstractEMBridgeEvent) constructor.newInstance();
				} catch (Throwable t) {
					//Try with eventId
					constructor = eventClass.getConstructor(Integer.TYPE);
					retEvent = (AbstractEMBridgeEvent) constructor.newInstance(eventId);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		if (retEvent != null && retEvent.parseEventBuffer(eventBuffer)) {
			return retEvent;
		}
		return new UnknownEMEvent();
	}

	/**
	 * @param emEvent
	 * @return
	 */
	public static Class<?> getEMEventClass(final IEMBridgeEvent emEvent) {
		return getEMEventClass(emEvent.getEventId());
	}

	/**
	 * @param eventId
	 * @return
	 */
	public static Class<?> getEMEventClass(final int eventId) {
		return eventsMap.get(eventId);
	}

}