package ecf.pycharm.plugin.templates;

import com.intellij.ide.fileTemplates.CreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;

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
 * Time: 1:03 PM
 */
public class ECFCreateFromTemplateHandler implements CreateFromTemplateHandler {

    public static PsiElement CreateClassOrPackage(Project project,
                                            PsiDirectory directory,
                                            String content,
                                            boolean reformat,
                                            String extension) throws IncorrectOperationException {
        return null;

    }

    @Override
    public boolean handlesTemplate(FileTemplate fileTemplate) {
        return false;
    }

    @Override
    public PsiElement createFromTemplate(Project project, PsiDirectory psiDirectory, String s, FileTemplate fileTemplate, String s2, Map<String, Object> stringObjectMap) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean canCreate(PsiDirectory[] psiDirectories) {
        return false;
    }

    @Override
    public boolean isNameRequired() {
        return false;
    }

    @Override
    public String getErrorMessage() {
        return "Cannot Create File";
    }

    @Override
    public void prepareProperties(Map<String, Object> stringObjectMap) {
        String packageName = (String) stringObjectMap.get(FileTemplate.ATTRIBUTE_PACKAGE_NAME);
        if (packageName == null || packageName.length() == 0) {
            stringObjectMap.put(FileTemplate.ATTRIBUTE_PACKAGE_NAME, FileTemplate.ATTRIBUTE_PACKAGE_NAME);
        }
    }
}
