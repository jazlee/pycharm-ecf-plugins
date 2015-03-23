package ecf.pycharm.plugin.actions;

import com.intellij.CommonBundle;
import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import ecf.pycharm.plugin.openapi.CommonDataKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * Copyright (c) 2013 Cipta Solusi Pratama. All rights reserved.
 * <p/>
 * This product and it's source code is protected by patents, copyright laws and
 * international copyright treaties, as well as other intellectual property
 * laws and treaties. The product is licensed, not sold.
 * <p/>
 * The source code and sample programs in this package or parts hereof
 * as well as the documentation shall not be copied, modified or redistributed
 * without permission, explicit or implied, of the author.
 * <p/>
 * User: jaimy
 * Date: 11/2/13
 * Time: 11:25 AM
 */
public abstract class CreateFromECFTemplateAction<T extends PsiElement> extends AnAction {
    protected static final Logger LOG = Logger.getInstance("#ecf.pycharm.pluginCreateFromECFTemplateAction");

    public CreateFromECFTemplateAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    @Override
    public final void actionPerformed(AnActionEvent anActionEvent) {
        final DataContext dataContext = anActionEvent.getDataContext();
        final IdeView ideView = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (ideView == null) return;

        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final VirtualFile vf = project.getBaseDir();
        // final PsiDirectory psiDirectory = ideView.getOrChooseDirectory();
        final PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(vf);
        if (psiDirectory == null || project == null) return;

        final AttributesDefaults defaults = getAttributesDefaults(dataContext);
        final CreateECFComponentDlg.Builder builder = CreateECFComponentDlg.createDialog(project, psiDirectory, defaults);
        buildDialog(project, psiDirectory, builder);

        final Ref<String> selectedTemplateName = Ref.create(null);
        final T createdElement = builder.show(getErrorTitle(), getDefaultTemplateName(psiDirectory), new CreateECFComponentDlg.FileCreator<T>() {

            @Nullable
            @Override
            public T createFile(@NotNull String name, @NotNull CreateECFComponentDlg dialog) {
                final String templateName = dialog.getTextKindCombo().getSelectedName();
                selectedTemplateName.set(templateName);
                return CreateFromECFTemplateAction.this.createFile(name, templateName, dialog, psiDirectory);
            }

            @NotNull
            @Override
            public String getActionName(@NotNull String name, @NotNull CreateECFComponentDlg dialog) {
                final String templateName = dialog.getTextKindCombo().getSelectedName();
                return CreateFromECFTemplateAction.this.getActionName(psiDirectory, name, templateName, dialog);
            }
        });
        if (createdElement != null) {
            CreateECFComponentDlg dialog = builder.getDialog();
            elementCreated(dialog, createdElement);
            ideView.selectElement(createdElement);
            postProcess(createdElement, selectedTemplateName.get(), builder.getCustomProperties());
        }

    }

    protected void postProcess(T createdElement, String s, Map<String, String> customProperties) {
    }

    protected abstract void buildDialog(Project project, PsiDirectory psiDirectory, CreateECFComponentDlg.Builder builder);

    protected abstract String getActionName(PsiDirectory psiDirectory, String name, String templateName, CreateECFComponentDlg dialog);

    @Nullable
    protected abstract T createFile(String name, String templateName, CreateECFComponentDlg dialog, PsiDirectory psiDirectory);

    @Nullable
    protected String getDefaultTemplateName(PsiDirectory psiDirectory) {
        String property = getDefaultTemplateProperty();
        return property == null ? null : PropertiesComponent.getInstance(psiDirectory.getProject()).getValue(property);
    }

    @Nullable
    protected String getDefaultTemplateProperty() {
        return null;
    }

    public String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }

    protected void elementCreated(CreateECFComponentDlg dialog, PsiElement createdElement) {
    }

    @Nullable
    public AttributesDefaults getAttributesDefaults(DataContext dataContext) {
        return null;
    }

}
