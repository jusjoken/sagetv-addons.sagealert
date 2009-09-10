/*
 *      Copyright 2009 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
package com.google.code.sagetvaddons.sagealert.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.sagetvaddons.sagealert.client.Client;
import com.google.code.sagetvaddons.sagealert.client.IsDataStoreSerializable;
import com.google.code.sagetvaddons.sagealert.client.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;
import com.google.code.sagetvaddons.sagealert.client.SmtpSettings;

/**
 * @author dbattams
 *
 */
final class DataStore {
	static private final ThreadLocal<DataStore> THREAD_DATA_STORES = new ThreadLocal<DataStore>() {
		@Override
		protected DataStore initialValue() {
			try {
				return new DataStore();
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
	};

	/**
	 * Return the thread local instance of the data store; create it if necessary
	 * @return An instance of the data store
	 */
	static final DataStore getInstance() {
		return THREAD_DATA_STORES.get();
	}
	
	static private final Logger LOG = Logger.getLogger(DataStore.class);
	static private final Logger SQL_LOG = Logger.getLogger("com.google.code.sagetvaddons.sagealert.server.SQLLogger");
	
	static private final String ERR_MISSING_TABLE = "^no such table.+";
	static public final String CLNT_SETTING_PREFIX = "sage.client.";
	static private final String SQL_ERROR = "SQL error";
	static private final String SMTP_SETTINGS = "SmtpSettings";
	static private final void logQry(String qry, Object... params) {
		if(SQL_LOG.isTraceEnabled()) {
			if(params != null && params.length > 0)
				qry = qry.concat(" " + Arrays.toString(params));
			SQL_LOG.trace(qry);
		}
	}
	
	static private boolean dbInitialized = false;
	
	private Connection conn;
	private File dataStore;
	
	private DataStore() throws ClassNotFoundException, IOException {
		Class.forName("org.sqlite.JDBC");
		dataStore = new File("sagealert.sqlite");
		openConnection();
		synchronized(DataStore.class) {
			if(!dbInitialized) {
				loadDDL();
//				upgradeSchema();
				dbInitialized = true;
				LOG.debug("Using '" + dataStore.getAbsolutePath() + "' as SQLite database file.");
			}
		}
	}
	
	private void openConnection() throws IOException {
		try {
			if(conn != null && !conn.isClosed())
				return;
			conn = DriverManager.getConnection("jdbc:sqlite:" + dataStore.getAbsolutePath());
		} catch(SQLException e) {
			String msg = "Error opening data store";
			LOG.trace(msg, e);
			throw new IOException(msg, e);
		}
	}
	
	private void loadDDL() throws IOException
	{
		try {
			conn.createStatement().executeQuery("SELECT * FROM reporters").close();
		} catch(SQLException e) {
			Statement stmt = null;
			if(!e.getMessage().matches(ERR_MISSING_TABLE)) {
				String msg = "Error on initial data store read";
				LOG.trace(msg, e);
				throw new IOException(msg, e);
			}

			// Create tables
			String[] qry = {
					"CREATE TABLE reporters (type LONG VARCHAR NOT NULL, key LONG VARCHAR NOT NULL, data LONG VARCHAR, PRIMARY KEY(type, key))",
					"CREATE TABLE listeners (event VARCHAR(255) NOT NULL, type LONG VARCHAR NOT NULL, key LONG VARCHAR NOT NULL, PRIMARY KEY(event, type, key))",
					"CREATE TABLE settings (var VARCHAR(32) NOT NULL, val VARCHAR(255) NOT NULL, PRIMARY KEY(var))",
					"INSERT INTO settings (var, val) VALUES ('schema', '1')"
			};

			try	{
				conn.setAutoCommit(false);
				stmt = conn.createStatement();
				for(String q : qry) {
					logQry(q);
					stmt.executeUpdate(q);
				}
				conn.commit();
			} catch(SQLException e2) {
				String msg = "Error initializing data store";
				try	{
					conn.rollback();
				} catch(SQLException e3) {
					LOG.trace(msg, e3);
				}
				LOG.trace(msg, e2);
				throw new IOException(msg);
			}
			finally	{
				if(stmt != null) {
					try	{
						stmt.close();
					} catch(SQLException e4) {
						String msg = "Unable to cleanup data store resources";
						LOG.trace(msg, e4);
						throw new IOException(msg);
					}
				}

				try	{
					conn.setAutoCommit(true);
				} catch(SQLException e3) {
					String msg = "Unable to reset data store auto commit";
					LOG.trace(msg, e3);
					throw new IOException(msg);
				}
			}
		}
		return;
	}
	
	/**
	 * Save a reporter to the data store
	 * @param reporter The reporter to be saved
	 */
	public void saveReporter(NotificationServerSettings reporter) {
		String qry = "REPLACE INTO reporters (type, key, data) VALUES (?, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			String type = reporter.getClass().getCanonicalName();
			String key = reporter.getDataStoreKey();
			String data = reporter.getDataStoreData();
			logQry(qry, type, key, data);
			pstmt.setString(1, type);
			pstmt.setString(2, key);
			pstmt.setString(3, data);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace("pStmt close error", e); }
		}
	}
	
	/**
	 * Get a list of all reporters of a given type
	 * @param type The type (FQ class name) of reporters to retrieve
	 * @return A list of all reporters of the given type; this list may be empty, but not null
	 */
	public List<NotificationServerSettings> getReporters(String type) {
		String qry = "SELECT key, data FROM reporters WHERE type = ?";
		List<NotificationServerSettings> objs = new ArrayList<NotificationServerSettings>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			pstmt = conn.prepareStatement(qry);
			logQry(qry, type);
			pstmt.setString(1, type);
			rset = pstmt.executeQuery();
			while(rset.next())
				objs.add(reflectReporter(type, rset.getString(1), rset.getString(2)));
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			try {
				if(rset != null)
					rset.close();
				if(pstmt != null)
					pstmt.close();
			} catch(SQLException e) {
				LOG.trace(SQL_ERROR, e);
			}
		}
		return objs;
	}
	
	/**
	 * Delete all reporters of a given type
	 * @param type The type of reporters to delete
	 */
	public void deleteReporters(Class<?> type) {
		String qry = "DELETE FROM reporters WHERE type = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			String clsName = type.getCanonicalName();
			logQry(qry, clsName);
			pstmt.setString(1, clsName);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Delete a specific reporter
	 * @param type The type of reporter to be deleted (FQ class name)
	 * @param key The key that describe the unique reporter to be deleted
	 */
	public void deleteReporter(String type, String key) {
		String qry = "DELETE FROM reporters WHERE type = ? AND key = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			logQry(qry, type, key);
			pstmt.setString(1, type);
			pstmt.setString(2, key);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Get a list of all reporters stored in the data store
	 * @return A list of all reporters in the data store; the list may be empty, but not null
	 */
	public List<NotificationServerSettings> getAllReporters() {
		String qry = "SELECT type, key, data FROM reporters";
		List<NotificationServerSettings> reporters = new ArrayList<NotificationServerSettings>();
		Statement stmt = null;
		ResultSet rset = null;
		try {
			stmt = conn.createStatement();
			logQry(qry);
			rset = stmt.executeQuery(qry);
			while(rset.next())
				reporters.add(reflectReporter(rset.getString(1), rset.getString(2), rset.getString(3)));
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		}  finally {
			try {
				if(rset != null)
					rset.close();
				if(stmt != null)
					stmt.close();
			} catch(SQLException e) {
				LOG.trace(SQL_ERROR, e);
			}
		}
		return reporters;
	}
	
	/**
	 * Get all the registered handlers for a given event
	 * @param event A description of the event
	 * @return A list of all handlers for the given event description
	 */
	public List<NotificationServerSettings> getHandlers(SageEventMetaData event) {
		String qry = "SELECT l.type, l.key, r.data FROM listeners AS l OUTER JOIN reporters AS r ON (l.type = r.type AND l.key = r.key) WHERE event = '" + event.getClassName() + "'";
		List<NotificationServerSettings> handlers = new ArrayList<NotificationServerSettings>();
		Statement stmt = null;
		ResultSet rset = null;
		try {
			stmt = conn.createStatement();
			logQry(qry);
			rset = stmt.executeQuery(qry);
			while(rset.next())
				handlers.add(reflectReporter(rset.getString(1), rset.getString(2), rset.getString(3)));
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			try {
				if(rset != null)
					rset.close();
				if(stmt != null)
					stmt.close();
			} catch(SQLException e) {
				LOG.trace(SQL_ERROR, e);
			}
		}
		return handlers;
	}
	
	private NotificationServerSettings reflectReporter(String clsName, String key, String data) {
		NotificationServerSettings obj = null;
		try {
			Class<?> cls = Class.forName(clsName);
			Constructor<?> ctor = cls.getConstructor((Class<?>[])null);
			ctor.setAccessible(true);
			obj = (NotificationServerSettings)ctor.newInstance((Object[])null);
			Method method = cls.getDeclaredMethod("unserialize", new Class<?>[] {String.class, String.class});
			method.setAccessible(true);
			method.invoke(obj, key, data);
		} catch(ClassNotFoundException e) {
			LOG.trace("Class not found", e);
		} catch(NoSuchMethodException e) {
			LOG.trace("No such method", e);
		} catch(InvocationTargetException e) {
			LOG.trace("Invocation target exception", e);
		} catch(InstantiationException e) {
			LOG.trace("Instantiation exception", e);
		} catch(IllegalAccessException e) {
			LOG.trace("Illegal access", e);
		}
		return obj;
	}

	/**
	 * Register the given handlers for the given event
	 * @param event A description of the event to attach the handlers to
	 * @param handlers The list of handlers to attach to the given event
	 */
	public void registerHandlers(SageEventMetaData event, List<NotificationServerSettings> handlers) {
		String qry = "REPLACE INTO listeners (event, type, key) VALUES (?, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			String eventName = event.getClassName();
			pstmt.setString(1, eventName);
			for(IsDataStoreSerializable obj : handlers) {
				String clsName = obj.getClass().getCanonicalName();
				String key = obj.getDataStoreKey();
				logQry(qry, eventName, clsName, key);
				pstmt.setString(2, clsName);
				pstmt.setString(3, key);
				pstmt.addBatch();
			}
			if(handlers.size() > 0)
				pstmt.executeBatch();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Remove the given handlers from the given event
	 * @param event The event description to detach the handlers from
	 * @param handlers The list of handlers to detach from the given event
	 */
	public void removeHandlers(SageEventMetaData event, List<NotificationServerSettings> handlers) {
		String qry = "DELETE FROM listeners WHERE event = '" + event.getClassName() + "' AND type = ? AND key = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			for(IsDataStoreSerializable s : handlers) {
				String clsName = s.getClass().getCanonicalName();
				String key = s.getDataStoreKey();
				logQry(qry, clsName, key);
				pstmt.setString(1, clsName);
				pstmt.setString(2, key);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Remove the handlers from all events; this only removes the links in the data store, it does NOT remove them from the in memory manager, you must also remove them from the manager for events to stop firing to them
	 * @param handlers The list of handlers to delete
	 */
	public void removeHandlers(List<NotificationServerSettings> handlers) {
		String qry = "DELETE FROM listeners WHERE type = ? AND key = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			for(IsDataStoreSerializable s : handlers) {
				String clsName = s.getClass().getCanonicalName();
				String key = s.getDataStoreKey();
				logQry(qry, clsName, key);
				pstmt.setString(1, clsName);
				pstmt.setString(2, key);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Remove all handlers from a given event; this only removes them from the data store; active handlers remain active until the handler manager is told otherwise
	 * @param event The event for which all handlers should be removed
	 */
	public void removeAllHandlers(SageEventMetaData event) {
		String qry = "DELETE FROM listeners WHERE event = '" + event.getClassName() + "'";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			logQry(qry);
			stmt.executeUpdate(qry);
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(stmt != null)
				try { stmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Retrieve a setting from the data store
	 * @param var The name of the setting to get
	 * @param defaultVal The default value to return if the setting does not exist in the data store; null is allowed
	 * @return The value of the given setting or defaultVal if the setting is not in the data store
	 */
	public String getSetting(String var, String defaultVal) {
		String qry = "SELECT val FROM settings WHERE var = ?";
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			pstmt = conn.prepareStatement(qry);
			logQry(qry, var);
			pstmt.setString(1, var);
			rset = pstmt.executeQuery();
			if(rset.next())
				return rset.getString(1);
			return defaultVal;
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
			return defaultVal;
		} finally {
			try {
				if(rset != null)
					rset.close();
				if(pstmt != null)
					pstmt.close();
			} catch(SQLException e) {
				LOG.trace(SQL_ERROR, e);
			}
		}
	}
	
	/**
	 * Get a setting from the data store
	 * @param var The setting to get
	 * @return The value of the setting or null if the setting does not exist in the data store
	 */
	public String getSetting(String var) {
		return getSetting(var, null);
	}
	
	/**
	 * Store a setting in the data store; replaces the value if the setting name already exists in the data store
	 * @param var The name of the setting
	 * @param val The value of the setting
	 */
	public void setSetting(String var, String val) {
		String qry = "REPLACE INTO settings (var, val) VALUES (?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			logQry(qry);
			pstmt.setString(1, var);
			pstmt.setString(2, val);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Retrieve the collection of all clients stored in the data store
	 * @return A Collection of Client instances; the list may be empty, but not null
	 */
	public Collection<Client> getClients() {
		String qry = "SELECT var, val FROM settings WHERE var LIKE '" + CLNT_SETTING_PREFIX + "%'";
		Collection<Client> list = new ArrayList<Client>();
		Statement stmt = null;
		ResultSet rset = null;
		try {
			stmt = conn.createStatement();
			logQry(qry);
			rset = stmt.executeQuery(qry);
			while(rset.next()) {
				String data = rset.getString(2);
				list.add(buildClient(rset.getString(1).substring(CLNT_SETTING_PREFIX.length()), data));
			}
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
			LOG.error(e);
		} finally {
			try {
				if(rset != null)
					rset.close();
				if(stmt != null)
					stmt.close();
			} catch(SQLException e) {
				LOG.trace(SQL_ERROR, e);
			}
		}
		return list;
	}
	
	/**
	 * Retrieve a client by id
	 * @param id The unique client id to be retrieved
	 * @return A Client instance representing the given Client id
	 */
	public Client getClient(String id) {
		return buildClient(id, getSetting(CLNT_SETTING_PREFIX + id, "0"));
	}
	
	// First char of data is 0/1 for notify flag; rest of string is alias
	private Client buildClient(String id, String data) {
		String alias;
		if(data.length() > 1)
			alias = data.substring(1);
		else
			alias = "";
		boolean notify = false;
		if(data.charAt(0) == '1')
			notify = true;
		return new Client(id, alias, notify);		
	}
	
	/**
	 * Save a Client to the data store
	 * @param c The Client instance to be saved
	 */
	public void saveClient(Client c) {
		setSetting(CLNT_SETTING_PREFIX + c.getId(), (c.doNotify() ? "1" : "0") + c.getAlias());
	}
	
	/**
	 * Delete the list of clients from the data store
	 * @param clients The list of clients to be deleted
	 */
	public void deleteClients(Collection<Client> clients) {
		String qry = "DELETE FROM settings WHERE var = ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(qry);
			for(Client c : clients) {
				pstmt.setString(1, CLNT_SETTING_PREFIX + c.getId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch(SQLException e) {
			LOG.trace(SQL_ERROR, e);
			LOG.error(e);
		} finally {
			if(pstmt != null)
				try { pstmt.close(); } catch(SQLException e) { LOG.trace(SQL_ERROR, e); }
		}
	}
	
	/**
	 * Register a new client with SageAlert
	 * @param id The client id
	 * @return True if the new client was registered or false if the client already existed in the data store
	 */
	public boolean registerClient(String id) {
		id = massageClientId(id);
		if(getSetting(CLNT_SETTING_PREFIX + id) != null)
			return false;
		
		saveClient(buildClient(id, "0"));
		return true;
	}
	
	/**
	 * Same as getClient() except it returns a new Client with the alias set to the ID if the given Client is not in the data store
	 * @param id The Client id to search for
	 * @return The Client instance; initialized if not found
	 */
	public Client findClient(String id) {
		id = massageClientId(id);
		return buildClient(id, getSetting(CLNT_SETTING_PREFIX + id, "0" + id));
	}
	
	static private String massageClientId(String id) {
		Pattern p = Pattern.compile("[^\\d]*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*");
		Matcher m = p.matcher(id);
		if(m.matches()) {
			LOG.debug("Client id [" + id + "] looks like an IP address, converted id to: '" + m.group(1) + "'");
			id = m.group(1);
		}
		return id;
	}

	/**
	 * Unserialize and construct the SmtpServer settings object
	 * @return The SmtpServer settings object
	 */
	public SmtpSettings getSmtpSettings() {
		String jsonEnc = getSetting(SMTP_SETTINGS);
		if(jsonEnc == null)
			return null;
		
		JSONObject jobj = null;
		try {
			jobj = new JSONObject(jsonEnc);
			return new SmtpSettings(jobj.getString("host"), jobj.getInt("port"), jobj.getString("user"), jobj.getString("pwd"), jobj.getString("from"), jobj.getBoolean("ssl"));
		} catch(JSONException e) {
			LOG.trace("JSON error", e);
			LOG.error(e);
			return null;
		}
	}

	/**
	 * Serialize and save the SmtpServer settings object to the data store
	 * @param settings The object to be written to the data store
	 */
	public void saveSmtpSettings(SmtpSettings settings) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("host", settings.getHost());
			jobj.put("port", settings.getPort());
			jobj.put("user", settings.getUser());
			jobj.put("pwd", settings.getPwd());
			jobj.put("from", settings.getSenderAddress());
			jobj.put("ssl", settings.useSsl());
			setSetting(SMTP_SETTINGS, jobj.toString());
		} catch(JSONException e) {
			LOG.trace("JSON error", e);
			LOG.error(e);
		}
	}
}
