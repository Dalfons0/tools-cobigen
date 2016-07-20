package com.capgemini.cobigen.eclipse.wizard.generate.common;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.capgemini.cobigen.CobiGen;
import com.capgemini.cobigen.eclipse.common.constants.InfrastructureConstants;
import com.capgemini.cobigen.eclipse.generator.CobiGenWrapper;
import com.capgemini.cobigen.eclipse.generator.java.JavaGeneratorWrapper;
import com.capgemini.cobigen.eclipse.wizard.common.SelectFilesPage;
import com.capgemini.cobigen.eclipse.wizard.common.model.stubs.IJavaElementStub;
import com.capgemini.cobigen.eclipse.wizard.common.model.stubs.IResourceStub;

/**
 * The {@link SelectFilesPage} guides through the generation process
 *
 * @author mbrunnli (15.02.2013)
 */
public abstract class AbstractGenerateWizard extends Wizard {

    /**
     * The first page of the Wizard
     */
    protected SelectFilesPage page1;

    /**
     * Wrapper for the {@link CobiGen}
     */
    protected CobiGenWrapper cobigenWrapper;

    /**
     * Assigning logger to AbstractGenerateWizard
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractGenerateWizard.class);

    /**
     * Initializes the {@link CobiGenWrapper generator} instance
     * @param generator
     *            {@link CobiGenWrapper generator} to be used for generation
     * @author mbrunnli (06.12.2014)
     */
    public AbstractGenerateWizard(CobiGenWrapper generator) {
        cobigenWrapper = generator;
    }

    /**
     * Initializes the {@link JavaGeneratorWrapper}
     * @author mbrunnli (18.02.2013)
     */
    protected void initializeWizard() {

        page1 = new SelectFilesPage(cobigenWrapper, false);
        LOG.info("AbstractGenerateWizard initialized");
    }

    /**
     * {@inheritDoc}
     *
     * @author mbrunnli (15.02.2013)
     */
    @Override
    public boolean performFinish() {
        MDC.put(InfrastructureConstants.CORRELATION_ID, UUID.randomUUID().toString());
        LOG.info("Start performing wizard finish operation...");

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());

        if (!userConfirmed()) {
            return false;
        }

        page1.saveSelection();
        generateContents(dialog);

        LOG.info("Performing wizard finish operation completed.");
        MDC.remove(InfrastructureConstants.CORRELATION_ID);
        return true;
    }

    /**
     * Generates the contents to be generated and reports the progress to the user
     *
     * @param dialog
     *            {@link ProgressMonitorDialog} which should be used for reporting the progress
     * @author mbrunnli (11.04.2014)
     */
    protected abstract void generateContents(ProgressMonitorDialog dialog);

    /**
     * Checks whether files will be overwritten by the generation process and whether the user is aware of
     * this behavior and confirms it
     *
     * @return true, if the user confirms the changes being made or no files will be overwritten<br>
     *         false, otherwise
     * @author mbrunnli (18.02.2013)
     */
    private boolean userConfirmed() {
        LOG.info("Check for necessary user confirmation to be displayed.");

        List<Object> diff = page1.getSelectedResources();

        // Delete simulated resources
        Iterator<Object> it = diff.iterator();
        while (it.hasNext()) {
            Object r = it.next();
            if (r instanceof IResourceStub || r instanceof IJavaElementStub) {
                it.remove();
            }
        }
        // Delete mergable files
        Set<IFile> mergableFiles = cobigenWrapper.getMergeableFiles();
        it = diff.iterator();
        while (it.hasNext()) {
            Object r = it.next();
            Object iResource = null;
            if (r instanceof IJavaElement) {
                try {
                    iResource = ((IJavaElement) r).getCorrespondingResource();
                } catch (JavaModelException e) {
                    LOG.error(
                        "An internal java model exception occured while retrieving the java elements '{}' corresponding resource.",
                        ((IJavaElement) r).getElementName(), e);
                }
            } else {
                iResource = r;
            }
            if (iResource != null && mergableFiles.contains(iResource)) {
                it.remove();
            }
        }

        if (!diff.isEmpty()) {
            LOG.info("Opening dialog for user confirmation... waiting for user interaction.");
            MessageDialog dialog = new MessageDialog(getShell(), "Warning!", null,
                "You have selected resources that are already existent and will be overwritten when proceeding.\nDo you really want to replace the existing files by newly generated ones?",
                MessageDialog.WARNING, new String[] { "Yes", "No" }, 1);
            int result = dialog.open();
            LOG.info("Got user input. Continue processing...");
            if (result == 1 || result == SWT.DEFAULT) {
                LOG.info("Finish user confirmation checking: user indicates to not override existing files.");
                return false;
            }
        }
        LOG.info("Finish user confirmation checking.");
        return true;
    }
}
