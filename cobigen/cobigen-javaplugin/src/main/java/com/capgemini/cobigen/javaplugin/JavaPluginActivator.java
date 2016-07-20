package com.capgemini.cobigen.javaplugin;

import java.util.List;

import com.capgemini.cobigen.extension.IGeneratorPluginActivator;
import com.capgemini.cobigen.extension.IMerger;
import com.capgemini.cobigen.extension.ITriggerInterpreter;
import com.capgemini.cobigen.javaplugin.merger.JavaMerger;
import com.google.common.collect.Lists;

/**
 *
 * @author mbrunnli (06.04.2014)
 */
public class JavaPluginActivator implements IGeneratorPluginActivator {

    @Override
    public List<IMerger> bindMerger() {
        List<IMerger> merger = Lists.newLinkedList();
        merger.add(new JavaMerger("javamerge", false));
        merger.add(new JavaMerger("javamerge_override", true));
        return merger;
    }

    @Override
    public List<ITriggerInterpreter> bindTriggerInterpreter() {
        return Lists.<ITriggerInterpreter> newArrayList(new JavaTriggerInterpreter("java"));
    }

}
