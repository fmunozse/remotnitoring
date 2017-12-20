package com.remotnitoring.service.dto;

import java.time.ZonedDateTime;

public class MonitorNodeDTO {
	
	private Long  nodeId;
	private String nodeName;	
	private Long numHeartbeats;
	private ZonedDateTime lastHeartbeat;
	
	public MonitorNodeDTO () {
		super();
	}
		
	public MonitorNodeDTO (Long nodeId, String nodeName, Long numHeartbeats, ZonedDateTime lastHeartbeat) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.numHeartbeats = numHeartbeats;
		this.lastHeartbeat = lastHeartbeat;
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
	

}
