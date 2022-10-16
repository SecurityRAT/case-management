package org.securityrat.casemanagement.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.securityrat.casemanagement.domain.enumeration.TicketSystem;

/**
 * A TicketSystemInstance.
 */
@Entity
@Table(name = "ticket_system_instance")
public class TicketSystemInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TicketSystem type;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "consumer_key")
    private String consumerKey;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @OneToMany(mappedBy = "ticketInstance")
    private Set<AccessToken> accessTokens = new HashSet<>();

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

    public TicketSystemInstance name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TicketSystem getType() {
        return type;
    }

    public TicketSystemInstance type(TicketSystem type) {
        this.type = type;
        return this;
    }

    public void setType(TicketSystem type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public TicketSystemInstance url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public TicketSystemInstance consumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
        return this;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getClientId() {
        return clientId;
    }

    public TicketSystemInstance clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public TicketSystemInstance clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Set<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public TicketSystemInstance accessTokens(Set<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
        return this;
    }

    public TicketSystemInstance addAccessToken(AccessToken accessToken) {
        this.accessTokens.add(accessToken);
        accessToken.setTicketInstance(this);
        return this;
    }

    public TicketSystemInstance removeAccessToken(AccessToken accessToken) {
        this.accessTokens.remove(accessToken);
        accessToken.setTicketInstance(null);
        return this;
    }

    public void setAccessTokens(Set<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketSystemInstance)) {
            return false;
        }
        return id != null && id.equals(((TicketSystemInstance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TicketSystemInstance{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", consumerKey='" + getConsumerKey() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", clientSecret='" + getClientSecret() + "'" +
            "}";
    }
}
