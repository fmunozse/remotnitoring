package com.remotnitoring.service.dto;

import java.time.ZonedDateTime;

public class MonitorNodeDTO {
	
	private Long  nodeId;
	private String nodeName;	
	private Long numHeartbeats;
	private ZonedDateTime lastHeartbeat;
	private String ip;
	
	public MonitorNodeDTO () {
		super();
	}
		
	public MonitorNodeDTO (Long nodeId, String nodeName, Long numHeartbeats, ZonedDateTime lastHeartbeat) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.numHeartbeats = numHeartbeats;
		this.lastHeartbeat = lastHeartbeat;
	}
	
	public MonitorNodeDTO (Long nodeId, String nodeName, ZonedDateTime lastHeartbeat, String ip) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.numHeartbeats = 1L;
		this.lastHeartbeat = lastHeartbeat;
		this.ip = ip;
	}

	public ZonedDateTime getLastHeartbeat() {
		return lastHeartbeat;
	}

	public void setLastHeartbeat(ZonedDateTime lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
	}

	@Override
	public String toString() {
		return "MonitorNodeDTO [nodeId=" + nodeId + ", nodeName=" + nodeName + ", numHeartbeats=" + numHeartbeats
				+ ", lastHeartbeat=" + lastHeartbeat + "]";
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Long getNumHeartbeats() {
		return numHeartbeats;
	}

	public void setNumHeartbeats(Long numHeartbeats) {
		this.numHeartbeats = numHeartbeats;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	

}
