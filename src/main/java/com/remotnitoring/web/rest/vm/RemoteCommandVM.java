package com.remotnitoring.web.rest.vm;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class RemoteCommandVM {

	@NotNull
	private long idRequestRemoteCommand;
	
	private String description;
	
	private String command;
	
    private String logResult;

    public long getIdRequestRemoteCommand() {
		return idRequestRemoteCommand;
	}

	public void setIdRequestRemoteCommand(long idRequestRemoteCommand) {
		this.idRequestRemoteCommand = idRequestRemoteCommand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getLogResult() {
		return logResult;
	}

	public void setLogResult(String logResult) {
		this.logResult = logResult;
	}

	public String getCodReturn() {
		return codReturn;
	}

	public void setCodReturn(String codReturn) {
		this.codReturn = codReturn;
	}

	private String codReturn;
	
}
