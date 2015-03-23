package ecf.pycharm.plugin.actions;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;
import com.intellij.util.SystemProperties;
import ecf.pycharm.plugin.ECFBundle;
import ecf.pycharm.plugin.openapi.CommonDataKeys;
import ecf.pycharm.plugin.templates.ECFTemplateUtil;
import org.apache.velocity.runtime.parser.ParseException;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

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
 * Time: 12:35 PM
 */
public class CreateECFComponentAction extends CreateECFTemplateInPackageAction {

    private static final String NAME_PROPERTY = "ecf.component.class.name";
    private static final String DESC_PROPERTY = "ecf.component.desc";
    private static final String SHORT_DESC_PROPERTY = "ecf.component.sdesc";
    private static final String MODEL_PROPERTY = "ecf.component.model";
    private static final String DB_TBL_SPACE_PROPERTY = "ecf.component.dbspace";
    private static final String IX_TBL_SPACE_PROPERTY = "ecf.component.ixspace";
    private static final String LB_TBL_SPACE_PROPERTY = "ecf.component.lbspace";

    public CreateECFComponentAction() {
        super("", "Create new ECF Component", PlatformIcons.FOLDER_ICON);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateECFComponentDlg.Builder builder) {
        builder.setTitle("Create ECF Component")
                .addKind("ECFClass", PlatformIcons.CLASS_ICON, ECFTemplateUtil.TEMPLATE_ECF_CLASS)
                .addKind("ECFApi", PlatformIcons.PACKAGE_ICON, ECFTemplateUtil.TEMPLATE_ECF_API)
                .addKind("ECFJob", PlatformIcons.PACKAGE_ICON, ECFTemplateUtil.TEMPLATE_ECF_JOB)
                .addKind("ECFController", PlatformIcons.PACKAGE_ICON, ECFTemplateUtil.TEMPLATE_ECF_CONTROLLER)
                .addKind("ECFModel", PlatformIcons.CLASS_ICON, ECFTemplateUtil.TEMPLATE_ECF_MODEL)
                .addKind("ECFBusinessObj", PlatformIcons.CLASS_ICON, ECFTemplateUtil.TEMPLATE_ECF_BOBJ);

        builder.setValidator(new InputValidatorEx() {
            @Nullable
            @Override
            public String getErrorText(String inputString) {
                return null;
            }

            @Override
            public boolean checkInput(String s) {
                return true;
            }

            @Override
            public boolean canClose(String s) {
                return !StringUtil.isEmptyOrSpaces(s) && getErrorText(s) == null;
            }
        });
    }

    @Nullable
    @Override
    public AttributesDefaults getAttributesDefaults(DataContext dataContext) {
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final AttributesDefaults defaults = new AttributesDefaults("__init__.py").withFixedName(true);
        if (project != null) {
            defaults.add("Package_name", project.getName());
            final PropertiesComponent properties = PropertiesComponent.getInstance();
            defaults.add("Author", SystemProperties.getUserName());
            defaults.addPredefined(ECFBundle.message("default.ecf.prop.object.name"), properties.getOrInit(NAME_PROPERTY, ""));
            defaults.addPredefined(ECFBundle.message("default.ecf.prop.desc"), properties.getOrInit(DESC_PROPERTY, ECFBundle.message("default.ecf.desc")));
            defaults.addPredefined(ECFBundle.message("default.ecf.prop.short.desc"), properties.getOrInit(SHORT_DESC_PROPERTY, ECFBundle.message("default.ecf.desc")));
            defaults.addPredefined(ECFBundle.message("default.ecf.prop.model"), properties.getOrInit(MODEL_PROPERTY, ""));
            defaults.addPredefined(ECFBundle.message("default.ecf.prop.db.tspace"), properties.getOrInit(DB_TBL_SPACE_PROPERTY, ECFBundle.message("default.db_tablespace")));
            defaults.addPredefined(ECFBundle.message("default.ecf.prop.ix.tspace"), properties.getOrInit(IX_TBL_SPACE_PROPERTY, ECFBundle.message("default.ix_tablespace")));
            defaults.addPredefined(ECFBundle.message("default.ecf.prop.lb.tspace"), properties.getOrInit(LB_TBL_SPACE_PROPERTY, ECFBundle.message("default.lb_tablespace")));
        }
        return defaults;
    }

    @Override
    public String getErrorTitle() {
        return "Cannot Create Class";
    }

    @Nullable
    @Override
    protected PsiElement doCreate(PsiDirectory directory, String className, String templateName, CreateECFComponentDlg dialog) {
        final FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(templateName);
        String defaultTemplateProperty = getDefaultTemplateProperty();
        PsiElement element;
        Project project = directory.getProject();
        try {
            Properties props = new Properties();
            props.putAll(dialog.getDefaultProperties());
            props.putAll(FileTemplateManager.getInstance().getDefaultProperties(project));
            element = FileTemplateUtil
                    .createFromTemplate(template, className, props,
                            directory);
            final PsiFile psiFile = element.getContainingFile();
            final VirtualFile virtualFile = psiFile.getVirtualFile();
            if (virtualFile != null) {
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
                if (defaultTemplateProperty != null) {
                    PropertiesComponent.getInstance(project).setValue(defaultTemplateProperty, template.getName());
                }
                return psiFile;
            }
        }
        catch (ParseException e) {
            Messages.showErrorDialog(project, "Error parsing Velocity template: " + e.getMessage(), "Create File from Template");
            return null;
        }
        catch (IncorrectOperationException e) {
            throw e;
        }
        catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String name, String templateName, CreateECFComponentDlg dialog) {
        return "Create ECF component " + name;
    }

    @Override
    protected void elementCreated(CreateECFComponentDlg dialog, PsiElement createdElement) {
        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String value;

        value = dialog.getTextName().getText();
        if (value != null) {
            propertiesComponent.setValue(NAME_PROPERTY, value);
        }
        value = dialog.getTextDescription().getText();
        if (value != null) {
            propertiesComponent.setValue(DESC_PROPERTY, value);
        }
        value = dialog.getTextSDescription().getText();
        if (value != null) {
            propertiesComponent.setValue(SHORT_DESC_PROPERTY, value);
        }
        value = dialog.getTextModel().getText();
        if (value != null) {
            propertiesComponent.setValue(MODEL_PROPERTY, value);
        }
        value = dialog.getTextDBTSpace().getText();
        if (value != null) {
            propertiesComponent.setValue(DB_TBL_SPACE_PROPERTY, value);
        }
        value = dialog.getTextIXTSpace().getText();
        if (value != null) {
            propertiesComponent.setValue(IX_TBL_SPACE_PROPERTY, value);
        }
    }
}
