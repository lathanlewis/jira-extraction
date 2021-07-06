package com.devfactory.base;

import lombok.val;

import javax.annotation.PostConstruct;

/**
 * Defines product settings.
 *
 * It's expected that products implement it like this:
 * <code>
 * {@literal @Singleton}
 * public class Environment extends BaseEnvironment {
 *     {@literal @Getter} private final String shortName = "eng"; // 3-5 letters
 * }
 * </code>
 */
public abstract class BaseEnvironment {

    public static final String VAR_STAGE = "STAGE";

    // short name is hardcoded because it is not to change
    public abstract String getShortName();

    public final String getStage() {
        // stage is defined using an environment variable set at deployment time
        return System.getenv(VAR_STAGE);
    }

    @PostConstruct
    public final void validateShortName() {
        val shortName = this.getShortName();
        if (shortName == null) {
            throw new IllegalStateException("Environment.shortName must be defined");
        } else if (!shortName.matches("[a-z]{3,5}")) {
            throw new IllegalStateException("Environment.shortName must be 3-5 letters");
        }
    }

    @PostConstruct
    public final void validateStage() {
        val stage = this.getStage();
        if (stage == null) {
            throw new IllegalStateException(VAR_STAGE + " environment variable must be defined");
        } else if (!stage.matches("[a-z][a-z0-9]{2,5}")) {
            throw new IllegalStateException(VAR_STAGE + " must be 3-6 letters or numbers and start with a letter");
        }
    }

}
