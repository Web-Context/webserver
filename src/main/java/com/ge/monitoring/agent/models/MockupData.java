/**
 * 
 */
package com.ge.monitoring.agent.models;

/**
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 *
 */
public class MockupData {

	private String title = "";
	private String version;
	@SuppressWarnings("unused")
	private int value = 0;

	public MockupData() {
		title = "Random Value Sytem";
		version = "1.0";
		value = 0;
	}

	public MockupData(int value) {
		this();
		this.value = value;
	}

	public MockupData(String title, int i) {
		this(i);
		this.title=title;
	}
	public MockupData(String title, String version, int i) {
		this(title, i);
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
