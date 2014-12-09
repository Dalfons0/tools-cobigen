package com.capgemini.cobigen.model;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.cobigen.config.entity.Matcher;
import com.capgemini.cobigen.config.entity.Trigger;
import com.capgemini.cobigen.config.entity.VariableAssignment;
import com.capgemini.cobigen.exceptions.InvalidConfigurationException;
import com.capgemini.cobigen.exceptions.PluginProcessingException;
import com.capgemini.cobigen.extension.ITriggerInterpreter;
import com.capgemini.cobigen.extension.to.MatcherTo;
import com.capgemini.cobigen.extension.to.VariableAssignmentTo;
import com.capgemini.cobigen.validator.InputValidator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Resolves all context variables for a given input and its trigger
 *
 * @author mbrunnli (03.06.2014)
 */
public class ContextVariableResolver {

    /**
     * Logger instance
     */
    private static final Logger LOG = LoggerFactory.getLogger(ContextVariableResolver.class);

    /**
     * Input object for which a new object model should be created
     */
    private Object input;

    /**
     * Trigger, which has been activated for the given input
     */
    private Trigger trigger;

    /**
     * Creates a new {@link ModelBuilder} instance for the given properties
     *
     * @param input
     *            object for which a new object model should be created
     * @param trigger
     *            which has been activated for the given input
     * @author mbrunnli (09.04.2014)
     */
    public ContextVariableResolver(Object input, Trigger trigger) {

        if (input == null || trigger == null || trigger.getMatcher() == null) {
            throw new IllegalArgumentException(
                "Cannot create Model from input == null || trigger == null || trigger.getMatcher() == null");
        }
        this.input = input;
        this.trigger = trigger;
    }

    /**
     * Resolves all {@link VariableAssignment}s by using the given {@link ITriggerInterpreter}
     *
     * @param triggerInterpreter
     *            to be used
     * @return the mapping of variable to value
     * @throws InvalidConfigurationException
     *             if there are {@link VariableAssignment}s, which could not be resolved
     * @author mbrunnli (08.04.2014)
     */
    public Map<String, String> resolveVariables(ITriggerInterpreter triggerInterpreter)
        throws InvalidConfigurationException {

        Map<String, String> variables = Maps.newHashMap();
        for (Matcher m : trigger.getMatcher()) {
            MatcherTo matcherTo = new MatcherTo(m.getType(), m.getValue(), input);
            if (triggerInterpreter.getMatcher().matches(matcherTo)) {
                try {
                    Map<String, String> resolvedVariables =
                        triggerInterpreter.getMatcher()
                            .resolveVariables(matcherTo, getVariableAssignments(m));
                    InputValidator.validateResolvedVariables(resolvedVariables);
                    variables.putAll(resolvedVariables);
                } catch (Throwable e) {
                    LOG.error(
                        "The matcher '{}' has been exited abruptly. Please state this as a Bug in plug-in development respository.",
                        triggerInterpreter.getMatcher().getClass().getCanonicalName(), e);
                    throw new PluginProcessingException(
                        "The matcher '"
                            + triggerInterpreter.getMatcher().getClass().getCanonicalName()
                            + "' has been exited abruptly. Please state this as a Bug in plug-in development respository.",
                        e);
                }
            }
        }
        return variables;
    }

    /**
     * Retrieves all {@link VariableAssignment}s from the given {@link Matcher} and converts them into
     * transfer objects
     *
     * @param m
     *            {@link Matcher} to retrieve the {@link VariableAssignment}s from
     * @return a {@link List} of {@link VariableAssignmentTo}s
     * @author mbrunnli (08.04.2014)
     */
    private List<VariableAssignmentTo> getVariableAssignments(Matcher m) {

        List<VariableAssignmentTo> variableAssignments = Lists.newLinkedList();
        for (VariableAssignment va : m.getVariableAssignments()) {
            variableAssignments.add(new VariableAssignmentTo(va.getType(), va.getVarName(), va.getValue()));
        }
        return variableAssignments;
    }
}
