package org.securityrat.casemanagement.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A AccessToken.
 */
@Entity
@Table(name = "access_token")
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_date")
    private ZonedDateTime expirationDate;

    @NotNull
    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "refresh_token")
    private String refreshToken;

    @ManyToOne
    @JsonIgnoreProperties("accessTokens")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("accessTokens")
    private TicketSystemInstance ticketInstance;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public AccessToken token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public AccessToken expirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSalt() {
        return salt;
    }

    public AccessToken salt(String salt) {
        this.salt = salt;
        return this;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AccessToken refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public AccessToken user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TicketSystemInstance getTicketInstance() {
        return ticketInstance;
    }

    public AccessToken ticketInstance(TicketSystemInstance ticketSystemInstance) {
        this.ticketInstance = ticketSystemInstance;
        return this;
    }

    public void setTicketInstance(TicketSystemInstance ticketSystemInstance) {
        this.ticketInstance = ticketSystemInstance;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessToken)) {
            return false;
        }
        return id != null && id.equals(((AccessToken) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", salt='" + getSalt() + "'" +
            ", refreshToken='" + getRefreshToken() + "'" +
            "}";
    }
}
