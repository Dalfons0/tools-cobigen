package com.capgemini.cobigen.eclipse.wizard.generate.control;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import com.capgemini.cobigen.eclipse.generator.CobiGenWrapper;
import com.capgemini.cobigen.extension.to.TemplateTo;

/**
 * Running this process as issued in {@link IRunnableWithProgress} performs the generation tasks of the
 * generation wizard for each selected pojo.
 *
 * @author trippl (22.04.2013)
 */
public class GenerateBatchSelectionProcess extends AbstractGenerateSelectionProcess {

    /**
     * {@link List} containing the types of the selected inputs
     */
    private List<IType> inputTypes;

    /**
     * {@link IPackageFragment}, which should be the input for the generation process
     */
    private IPackageFragment container;

    /**
     * Creates a new process ({@link IRunnableWithProgress}) for performing the generation tasks
     *
     * @param shell
     *            on which to display error messages
     * @param javaGeneratorWrapper
     *            with which to generate the contents
     * @param templatesToBeGenerated
     *            {@link Set} of template ids to be generated
     * @param inputTypes
     *            {@link List} containing the types of the selected pojos
     *
     * @author trippl (22.04.2013)
     */
    public GenerateBatchSelectionProcess(Shell shell, CobiGenWrapper javaGeneratorWrapper,
        List<TemplateTo> templatesToBeGenerated, List<IType> inputTypes) {

        super(shell, javaGeneratorWrapper, templatesToBeGenerated);
        this.inputTypes = inputTypes;
    }

    /**
     * Creates a new process ({@link IRunnableWithProgress}) for performing the generation tasks
     *
     * @param shell
     *            on which to display error messages
     * @param javaGeneratorWrapper
     *            with which to generate the contents
     * @param templatesToBeGenerated
     *            {@link Set} of template ids to be generated
     * @param container
     *            selected {@link IPackageFragment} for the generation
     * @author mbrunnli (04.06.2014)
     */
    public GenerateBatchSelectionProcess(Shell shell, CobiGenWrapper javaGeneratorWrapper,
        List<TemplateTo> templatesToBeGenerated, IPackageFragment container) {

        super(shell, javaGeneratorWrapper, templatesToBeGenerated);
        this.container = container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean performGeneration(IProgressMonitor monitor) throws Exception {

        if (inputTypes != null && inputTypes.size() == 0 && container == null) {
            return false;
        }

        final IProject proj = cobigenWrapper.getGenerationTargetProject();
        if (proj != null) {
            // if (inputTypes != null) {
            // monitor.beginTask("GenerateHandler files ", templatesToBeGenerated.size());
            // for (TemplateTo temp : templatesToBeGenerated) {
            // if (temp.getMergeStrategy() == null) {
            // cobigenWrapper.generate(temp, true);
            // } else {
            // cobigenWrapper.generate(temp, false);
            // }
            // monitor.worked(1);
            // }
            // } else if (container != null) {
            // cobigenWrapper.setInput(container);
            // monitor.beginTask("GenerateHandler files for " + container.getElementName() + "...",
            // templatesToBeGenerated.size());
            // for (TemplateTo temp : templatesToBeGenerated) {
            // TemplateTo t = cobigenWrapper.getTemplateForId(temp.getId(), temp.getTriggerId());
            // if (t.getMergeStrategy() == null) {
            // cobigenWrapper.generate(t, true);
            // } else {
            // cobigenWrapper.generate(t, false);
            // }
            // }
            // monitor.worked(1);
            // } else {
            // LOG.error("Programmer error: GenerateBatchSelectionProcess was instantiated with null resources");
            // }
            for (TemplateTo temp : templatesToBeGenerated) {
                if (temp.getMergeStrategy() == null) {
                    cobigenWrapper.generate(temp, true);
                } else {
                    cobigenWrapper.generate(temp, false);
                }
                monitor.worked(1);
            }
            return true;
        } else {
            return false;
        }
    }
}
