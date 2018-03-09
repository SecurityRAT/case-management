package org.securityrat.casemanagement.service.dto;


import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the (remote) RequirementSet entity.
 */
public class RequirementSetDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    private Integer showOrder;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequirementSetDTO requirementSetDTO = (RequirementSetDTO) o;
        if(requirementSetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requirementSetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequirementSetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
