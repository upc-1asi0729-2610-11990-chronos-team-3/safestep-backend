package com.safestep.platform.iam.interfaces.rest.transform;

import com.safestep.platform.iam.domain.model.commands.SignUpCommand;
import com.safestep.platform.iam.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

public class SignUpCommandFromResourceAssembler {

    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        var roles = resource.roles() != null ? resource.roles() : new ArrayList<String>();
        return new SignUpCommand(resource.username(), resource.password(), roles);
    }
}
