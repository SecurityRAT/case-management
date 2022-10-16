package org.securityrat.casemanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.securityrat.casemanagement.domain.enumeration.TicketSystem;

/**
 * A TicketSystemInstance.
 */
@Entity
@Table(name = "ticket_system_instance")
public class TicketSystemInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
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
    @JsonIgnoreProperties(value = { "user", "ticketInstance" }, allowSetters = true)
    private Set<AccessToken> accessTokens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketSystemInstance id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TicketSystemInstance name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TicketSystem getType() {
        return this.type;
    }

    public TicketSystemInstance type(TicketSystem type) {
        this.type = type;
        return this;
    }

    public void setType(TicketSystem type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public TicketSystemInstance url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public TicketSystemInstance consumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
        return this;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getClientId() {
        return this.clientId;
    }

    public TicketSystemInstance clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public TicketSystemInstance clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Set<AccessToken> getAccessTokens() {
        return this.accessTokens;
    }

    public TicketSystemInstance accessTokens(Set<AccessToken> accessTokens) {
        this.setAccessTokens(accessTokens);
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
        if (this.accessTokens != null) {
            this.accessTokens.forEach(i -> i.setTicketInstance(null));
        }
        if (accessTokens != null) {
            accessTokens.forEach(i -> i.setTicketInstance(this));
        }
        this.accessTokens = accessTokens;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
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
