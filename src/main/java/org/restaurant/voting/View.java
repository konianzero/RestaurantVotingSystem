package org.restaurant.voting;

import javax.validation.groups.Default;

public class View {
    // Validate only form REST
    public interface Web extends Default {}

    // Validate only when DB save/update
    public interface Persist extends Default {}
}