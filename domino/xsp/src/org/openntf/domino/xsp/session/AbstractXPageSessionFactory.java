package org.openntf.domino.xsp.session;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.ODAPlatform;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public abstract class AbstractXPageSessionFactory implements ISessionFactory {
	private static final long serialVersionUID = 1L;

	protected String currentApiPath_;

	protected Session wrapSession(final lotus.domino.Session raw, final boolean selfCreated) {
		org.openntf.domino.impl.Session sess = (org.openntf.domino.impl.Session) Factory.fromLotus(raw, Session.SCHEMA, null);
		sess.setSessionFactory(this);
		sess.setNoRecycle(!selfCreated);

		boolean allFix = true;
		AutoMime autoMime = AutoMime.WRAP_32K;
		boolean mimeFriendly = true;
		if (NotesContext.getCurrentUnchecked() != null) {
			allFix = ODAPlatform.isAppAllFix(null);
			autoMime = ODAPlatform.getAppAutoMime(null);
			mimeFriendly = ODAPlatform.isAppMimeFriendly(null);
		}

		if (allFix) {
			for (Fixes fix : Fixes.values()) {
				sess.setFixEnable(fix, true);
			}
		}
		sess.setAutoMime(autoMime);

		if (mimeFriendly) {
			sess.setConvertMIME(false);
		}
		if (selfCreated && currentApiPath_ != null) {
			Database db = sess.getCurrentDatabase();
			if (db == null) {
				db = sess.getDatabase(currentApiPath_);
				setCurrentDatabase(sess, db);
			}
		}

		return sess;
	}

	/**
	 * This method may be overwritten by special XPage databases
	 * 
	 * @param sess
	 * @param db
	 */
	protected void setCurrentDatabase(final Session sess, final Database db) {
		sess.setCurrentDatabase(db);
	}
}
