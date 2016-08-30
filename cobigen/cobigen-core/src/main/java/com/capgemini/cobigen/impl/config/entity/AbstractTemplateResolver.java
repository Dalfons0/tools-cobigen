package com.capgemini.cobigen.impl.config.entity;

import java.util.Map;

import com.capgemini.cobigen.api.extension.TriggerInterpreter;
import com.capgemini.cobigen.impl.config.resolver.PathExpressionResolver;
import com.capgemini.cobigen.impl.model.ContextVariableResolver;

/**
 * Implements the common functionality of template destination path resolving
 * @author mbrunnli (16.10.2014)
 */
public abstract class AbstractTemplateResolver {

    /**
     * Relative path for the result.
     */
    private String unresolvedDestinationPath;

    /**
     * The trigger's id the template is assigned to
     */
    private Trigger trigger;

    /**
     * Used interpreter for the given trigger
     */
    private TriggerInterpreter triggerInterpreter;

    /**
     * Initializes all fields
     * @param unresolvedDestinationPath
     *            unresolved (raw) destination path from the templates configuration
     * @param trigger
     *            trigger the template has been retrieved from
     * @param triggerInterpreter
     *            the trigger has been interpreted with
     * @author mbrunnli (16.10.2014)
     */
    public AbstractTemplateResolver(String unresolvedDestinationPath, Trigger trigger,
        TriggerInterpreter triggerInterpreter) {
        this.unresolvedDestinationPath = unresolvedDestinationPath;
        this.trigger = trigger;
        this.triggerInterpreter = triggerInterpreter;
    }

    /**
     * Returns the unresolved destination path defined in the templates configuration
     * @return the unresolved destination path
     * @author mbrunnli (16.10.2014)
     */
    public String getUnresolvedDestinationPath() {
        return unresolvedDestinationPath;
    }

    /**
     * Sets the unresolved destination path defined in the templates configuration
     * @param unresolvedDestinationPath
     *            the unresolved destination path
     * @author mbrunnli (07.12.2014)
     */
    public void setUnresolvedDestinationPath(String unresolvedDestinationPath) {
        this.unresolvedDestinationPath = unresolvedDestinationPath;
    }

    /**
     * Returns the destination path the generated resources should be generated to
     * @return the destination path
     * @param input
     *            the destination path should be resolved for
     * @author mbrunnli (09.04.2014)
     */
    public String resolveDestinationPath(Object input) {
        Map<String, String> variables =
            new ContextVariableResolver(input, trigger).resolveVariables(triggerInterpreter);
        return new PathExpressionResolver(variables).evaluateExpressions(unresolvedDestinationPath);
    }

    /**
     * Returns the field 'trigger'
     * @return value of trigger
     * @author mbrunnli (16.10.2014)
     */
    protected Trigger getTrigger() {
        return trigger;
    }
}
