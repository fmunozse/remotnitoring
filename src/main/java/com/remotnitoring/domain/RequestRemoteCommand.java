package com.remotnitoring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.remotnitoring.domain.enumeration.StatusRemoteCommand;


/**
 * A RequestRemoteCommand.
 */
@Entity
@Table(name = "request_remote_command")
public class RequestRemoteCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Lob
    @Column(name = "command")
    @Type(type = "org.hibernate.type.TextType")
    private String command;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusRemoteCommand status;

    @ManyToOne(optional = false)
    @NotNull
    private Node node;

    @OneToMany(mappedBy = "requestRemoteCommand")
    @JsonIgnore
    private Set<ResponseRemoteCommand> responseRemoteCommands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public RequestRemoteCommand description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public RequestRemoteCommand command(String command) {
        this.command = command;
        return this;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public StatusRemoteCommand getStatus() {
        return status;
    }

    public RequestRemoteCommand status(StatusRemoteCommand status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusRemoteCommand status) {
        this.status = status;
    }

    public Node getNode() {
        return node;
    }

    public RequestRemoteCommand node(Node node) {
        this.node = node;
        return this;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Set<ResponseRemoteCommand> getResponseRemoteCommands() {
        return responseRemoteCommands;
    }

    public RequestRemoteCommand responseRemoteCommands(Set<ResponseRemoteCommand> responseRemoteCommands) {
        this.responseRemoteCommands = responseRemoteCommands;
        return this;
    }

    public RequestRemoteCommand addResponseRemoteCommand(ResponseRemoteCommand responseRemoteCommand) {
        this.responseRemoteCommands.add(responseRemoteCommand);
        responseRemoteCommand.setRequestRemoteCommand(this);
        return this;
    }

    public RequestRemoteCommand removeResponseRemoteCommand(ResponseRemoteCommand responseRemoteCommand) {
        this.responseRemoteCommands.remove(responseRemoteCommand);
        responseRemoteCommand.setRequestRemoteCommand(null);
        return this;
    }

    public void setResponseRemoteCommands(Set<ResponseRemoteCommand> responseRemoteCommands) {
        this.responseRemoteCommands = responseRemoteCommands;
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
        RequestRemoteCommand requestRemoteCommand = (RequestRemoteCommand) o;
        if (requestRemoteCommand.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requestRemoteCommand.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequestRemoteCommand{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", command='" + getCommand() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
