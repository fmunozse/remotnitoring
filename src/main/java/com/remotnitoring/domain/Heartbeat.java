package com.remotnitoring.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A Heartbeat.
 */
@Entity
@Table(name = "heartbeat")
public class Heartbeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @Size(max = 100)
    @Column(name = "ip", length = 100)
    private String ip;

    @ManyToOne(optional = false)
    @NotNull
    private Node node;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public Heartbeat timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public Heartbeat ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Node getNode() {
        return node;
    }

    public Heartbeat node(Node node) {
        this.node = node;
        return this;
    }

    public void setNode(Node node) {
        this.node = node;
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
        Heartbeat heartbeat = (Heartbeat) o;
        if (heartbeat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), heartbeat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Heartbeat{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", ip='" + getIp() + "'" +
            "}";
    }
}
