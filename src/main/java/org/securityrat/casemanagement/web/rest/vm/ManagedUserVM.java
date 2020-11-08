package org.securityrat.casemanagement.web.rest.vm;

import org.securityrat.casemanagement.service.dto.UserDTO;

/**
 * View Model extending the UserDTO, which is meant to be used in the appUser management UI.
 */
public class ManagedUserVM extends UserDTO {

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
