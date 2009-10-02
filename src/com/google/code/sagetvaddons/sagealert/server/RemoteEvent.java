/**
 * 
 */
package com.google.code.sagetvaddons.sagealert.server;

import java.net.InetAddress;

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;

/**
 * @author dbattams
 *
 */
final class RemoteEvent implements SageEvent {
	/**
	 * The metadata for this event.
	 */
	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(RemoteEvent.class.getCanonicalName(), "Remote Events", "Remote events are events triggered via the SageAlert XML-RPC API.");
	
	private String title;
	private String desc;
	private InetAddress source;
	
	RemoteEvent(String title, String desc, InetAddress source) {
		this.title = title;
		this.desc = desc;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getEventMetaData()
	 */
	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getMediumDescription()
	 */
	@Override
	public String getMediumDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSource()
	 */
	@Override
	public InetAddress getSource() {
		return source;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return title;
	}
}
