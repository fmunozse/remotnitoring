package com.remotnitoring.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


/**
 * A Node.
 */
@Entity
@Table(name = "node")
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")  
    private String description;

    @Lob
    @Column(name = "secret")
    @Type(type = "org.hibernate.type.TextType")
    private String secret;

    @Column(name = "renew_day")
    private LocalDate renewDay;

    @Size(max = 200)
    @Column(name = "model", length = 200)
    private String model;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Node name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Node description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecret() {
        return secret;
    }

    public Node secret(String secret) {
        this.secret = secret;
        return this;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public LocalDate getRenewDay() {
        return renewDay;
    }

    public Node renewDay(LocalDate renewDay) {
        this.renewDay = renewDay;
        return this;
    }

    public void setRenewDay(LocalDate renewDay) {
        this.renewDay = renewDay;
    }

    public String getModel() {
        return model;
    }

    public Node model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public User getUser() {
        return user;
    }

    public Node user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        Node node = (Node) o;
        if (node.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), node.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Node{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", secret='" + getSecret() + "'" +
            ", renewDay='" + getRenewDay() + "'" +
            ", model='" + getModel() + "'" +
            "}";
    }
}
