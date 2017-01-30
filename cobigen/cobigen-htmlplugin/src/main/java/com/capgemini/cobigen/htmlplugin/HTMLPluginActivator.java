package com.capgemini.cobigen.htmlplugin;

import java.util.List;

import com.capgemini.cobigen.api.extension.GeneratorPluginActivator;
import com.capgemini.cobigen.api.extension.Merger;
import com.capgemini.cobigen.api.extension.TriggerInterpreter;
import com.capgemini.cobigen.htmlplugin.merger.HTMLMerger;
import com.google.common.collect.Lists;

/**
 * JSON Plug-in Activator to be registered in the PluginRegistry of CobiGen.
 */
public class HTMLPluginActivator implements GeneratorPluginActivator {

    @Override
    public List<Merger> bindMerger() {
        List<Merger> merger = Lists.newLinkedList();
        merger.add(new HTMLMerger("htmlng2", false));
        merger.add(new HTMLMerger("htmlng2_override", true));
        return merger;
    }

    @Override
    public List<TriggerInterpreter> bindTriggerInterpreter() {
        return null;
    }

}
