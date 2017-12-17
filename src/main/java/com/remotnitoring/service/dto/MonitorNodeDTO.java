package com.remotnitoring.service.dto;

public class MonitorNodeDTO {
	
	private Long  nodeId;
	private String nodeName;
	
	private Long numHeartbeats;
	
	public MonitorNodeDTO (Long nodeId, String nodeName, Long numHeartbeats) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
		this.numHeartbeats = numHeartbeats;
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
