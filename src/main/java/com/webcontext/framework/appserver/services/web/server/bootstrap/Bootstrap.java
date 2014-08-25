package com.webcontext.framework.appserver.services.web.server.bootstrap;

/**
 * An annotation to build some BootStrap capabilities for our GenericServer.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 * 
 */
public @interface Bootstrap {
	/**
	 * Type of Group for Bootstrap operation.
	 * 
	 * @author 212391884
	 *
	 */
	public enum GroupType {
		DEFAULT("default"), APPLICATION("application"), SERVICE("service"), REPOSITORY(
				"repository");

		private String type;

		GroupType(String type) {
			this.type = type;
		}

		public String toString() {
			return this.type;
		}
	}

	public int priority() default 0;

	public GroupType group() default GroupType.DEFAULT;
}
