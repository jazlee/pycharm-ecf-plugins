package ecf.pycharm.plugin.actions;

import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import ecf.pycharm.plugin.ECFBundle;
import ecf.pycharm.plugin.templates.ECFTemplateUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
 * Time: 11:47 AM
 */
public abstract class CreateECFTemplateInPackageAction<T extends PsiElement> extends CreateFromECFTemplateAction<T> {

    protected CreateECFTemplateInPackageAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    @Override
    @Nullable
    protected T createFile(String name, String templateName, CreateECFComponentDlg dialog, PsiDirectory psiDirectory) {
        FileTemplateUtil.fillDefaultProperties(dialog.getDefaultProperties(), psiDirectory);
        return checkOrCreate(name, dialog, psiDirectory);
    }

    @Nullable
    private T checkOrCreate(String name, CreateECFComponentDlg dialog, PsiDirectory psiDirectory) {
        PsiDirectory directory = psiDirectory;
        String className = name;
        String templateName = dialog.getTextKindCombo().getSelectedName();
        if (templateName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_API)) {
            className = ECFBundle.message("default.ecf.api.package") + "." + className + "." + "__init__";
        } else if (templateName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_CONTROLLER)) {
            className = ECFBundle.message("default.ecf.mvc.package") + "." + className + "." + "__init__";
        } else if (templateName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_JOB)) {
            className = ECFBundle.message("default.ecf.job.package") + "." + className + "." + "__init__";
        } else if (templateName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_BOBJ)) {
            className = ECFBundle.message("default.ecf.bo.package") + "." + className;
        } else if (templateName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_MODEL)) {
            className = ECFBundle.message("default.ecf.tbl.package") + "." + className;
        } else if (templateName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_DM)) {
            className = "ecf.dm" + "." + className;
        } else if (templateName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_RPC)) {
            className = "ecf.rpc" + "." + className;
        }
        final  String sExtension = StringUtil.getShortName(templateName);
        if (StringUtil.isNotEmpty(sExtension)) {
            className = StringUtil.trimEnd(className, "." + sExtension);
        }
        if (className.contains(".")) {
            String[] names = className.split("\\.");
            for (int i = 0; i < names.length -1; i++) {
                String sName = names[i];
                PsiDirectory subdirectory = directory.findSubdirectory(sName);
                if (subdirectory == null) {
                    if (sName.equalsIgnoreCase("ecf")) {
                        subdirectory = directory;
                    } else {
                        subdirectory = directory.createSubdirectory(sName);
                    }
                }
                directory = subdirectory;
            }

            className = names[names.length - 1];
        }
        return doCreate(directory, className, templateName, dialog);
    }

    @Nullable
    protected abstract T doCreate(PsiDirectory directory, String className, String templateName, CreateECFComponentDlg dialog);

    @Nullable
    protected String getDefaultTemplateProperty() {
        return null;
    }

}
