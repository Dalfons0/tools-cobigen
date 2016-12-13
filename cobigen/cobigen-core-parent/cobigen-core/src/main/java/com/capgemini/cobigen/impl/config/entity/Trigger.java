package com.capgemini.cobigen.impl.config.entity;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Storage class for trigger data provided within the context.xml
 * @author trippl (05.04.2013)
 *
 */
public class Trigger {

    /**
     * Technical id
     */
    private String id;

    /**
     * Identifies the {@link Trigger}.
     */
    private String type;

    /**
     * Folder in which the configuration.xml is located in
     */
    private String templateFolder;

    /**
     * Input charset, which should be used to read the inputs
     */
    private Charset inputCharset;

    /**
     * All available matchers
     */
    private List<Matcher> matchers;

    /**
     * All available container matchers
     */
    private List<ContainerMatcher> containerMatchers;

    /**
     * Creates a new {@link Trigger} for the given data
     * @param id
     *            for the trigger
     * @param type
     *            of the trigger {@link #type}
     * @param templateFolder
     *            the trigger's {@link #templateFolder}
     * @param inputCharset
     *            which should be used to read the inputs
     * @param matcher
     *            all declared {@link Matcher}s for this trigger
     * @param containerMatchers
     *            all declared {@link ContainerMatcher}s for this trigger
     * @author trippl (05.04.2013)
     */
    public Trigger(String id, String type, String templateFolder, Charset inputCharset, List<Matcher> matcher,
        List<ContainerMatcher> containerMatchers) {
        this.id = id;
        this.type = type;
        this.templateFolder = templateFolder;
        this.inputCharset = inputCharset;
        matchers = matcher == null ? new LinkedList<Matcher>() : matcher;
        this.containerMatchers =
            containerMatchers == null ? new LinkedList<ContainerMatcher>() : containerMatchers;
    }

    /**
     * Liefert das Feld 'id' zurück.
     * @return Wert von id
     * @author mbrunnli (08.04.2014)
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the {@link Trigger}'s {@link #type}
     * @return the trigger id
     * @author trippl (05.04.2013)
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the template folder name
     * @return the template folder name
     * @author mbrunnli (08.04.2014)
     */
    public String getTemplateFolder() {
        return templateFolder;
    }

    /**
     * Returns the field 'inputCharset'
     * @return value of inputCharset
     * @author mbrunnli (06.06.2014)
     */
    public Charset getInputCharset() {
        return inputCharset;
    }

    /**
     * Returns all available matchers
     * @return all available matchers
     * @author mbrunnli (08.04.2014)
     */
    public List<Matcher> getMatcher() {
        return matchers;
    }

    /**
     * Returns the field 'containerMatchers'
     * @return value of containerMatchers
     * @author mbrunnli (13.10.2014)
     */
    public List<ContainerMatcher> getContainerMatchers() {
        return containerMatchers;
    }

    /**
     * {@inheritDoc}
     * @author trippl (22.04.2013)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Trigger) {
            return ((Trigger) obj).getId().equals(getId());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * @author mbrunnli (09.04.2014)
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * {@inheritDoc}
     * @author mbrunnli (Jun 17, 2015)
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id='" + id + "']";
    }

}
