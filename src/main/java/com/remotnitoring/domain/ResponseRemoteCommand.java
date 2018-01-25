package com.remotnitoring.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A ResponseRemoteCommand.
 */
@Entity
@Table(name = "response_remote_command")
public class ResponseRemoteCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "when_executed")
    private ZonedDateTime whenExecuted;

    @Lob
    @Column(name = "log_result")
    private String logResult;

    @Column(name = "cod_return")
    private String codReturn;

    @ManyToOne(optional = false)
    @NotNull
    private RequestRemoteCommand requestRemoteCommand;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getWhenExecuted() {
        return whenExecuted;
    }

    public ResponseRemoteCommand whenExecuted(ZonedDateTime whenExecuted) {
        this.whenExecuted = whenExecuted;
        return this;
    }

    public void setWhenExecuted(ZonedDateTime whenExecuted) {
        this.whenExecuted = whenExecuted;
    }

    public String getLogResult() {
        return logResult;
    }

    public ResponseRemoteCommand logResult(String logResult) {
        this.logResult = logResult;
        return this;
    }

    public void setLogResult(String logResult) {
        this.logResult = logResult;
    }

    public String getCodReturn() {
        return codReturn;
    }

    public ResponseRemoteCommand codReturn(String codReturn) {
        this.codReturn = codReturn;
        return this;
    }

    public void setCodReturn(String codReturn) {
        this.codReturn = codReturn;
    }

    public RequestRemoteCommand getRequestRemoteCommand() {
        return requestRemoteCommand;
    }

    public ResponseRemoteCommand requestRemoteCommand(RequestRemoteCommand requestRemoteCommand) {
        this.requestRemoteCommand = requestRemoteCommand;
        return this;
    }

    public void setRequestRemoteCommand(RequestRemoteCommand requestRemoteCommand) {
        this.requestRemoteCommand = requestRemoteCommand;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseRemoteCommand responseRemoteCommand = (ResponseRemoteCommand) o;
        if (responseRemoteCommand.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), responseRemoteCommand.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResponseRemoteCommand{" +
            "id=" + getId() +
            ", whenExecuted='" + getWhenExecuted() + "'" +
            ", logResult='" + getLogResult() + "'" +
            ", codReturn='" + getCodReturn() + "'" +
            "}";
    }
}
