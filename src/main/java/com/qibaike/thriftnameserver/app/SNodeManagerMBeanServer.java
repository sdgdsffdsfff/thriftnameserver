package com.qibaike.thriftnameserver.app;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qibaike.thriftnameserver.service.SNodeManager;
import com.qibaike.thriftnameserver.service.SNodeManagerMBean;

public class SNodeManagerMBeanServer {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public void start() {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		SNodeManagerMBean sNodeManagerMBean = SNodeManager.getInstance();
		try {
			mbs.registerMBean(sNodeManagerMBean, new ObjectName(
					"SNodeManagerMBean:name=sNodeManagerMBean"));
		} catch (Exception e) {
			log.error("start fail !", e);
		}
	}
}
