package ecf.pycharm.plugin.actions;

import com.intellij.facet.ui.FacetEditorValidator;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.ide.actions.ElementCreator;
import com.intellij.ide.actions.TemplateKindCombo;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.ui.DocumentAdapter;
import ecf.pycharm.plugin.ECFBundle;
import ecf.pycharm.plugin.templates.ECFTemplateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
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
 * Time: 9:14 AM
 */
public class CreateECFComponentDlg extends ECFBaseDialog {
    private JTextField textName;
    private JTextField textDescription;
    private JTextField textSDescription;
    private JTextField textModel;
    private JTextField textDBTSpace;
    private JTextField textIXTSpace;
    private TemplateKindCombo textKindCombo;
    private JTextField textBaseDir;
    private JPanel ecfComponentPanel;
    private JTextField textLOBSpace;
    private String initialPath;
    private UpperCaseDocumentFilter filter;

    private ElementCreator elementCreator;
    private InputValidator inputValidator;

    private final Properties myDefaultProperties;

    protected CreateECFComponentDlg(@NotNull Project project, @NotNull final PsiDirectory directory, @Nullable AttributesDefaults defaults) {
        super(project);
        myDefaultProperties = defaults != null ? defaults.getDefaultProperties() :
                FileTemplateManager.getInstance().getDefaultProperties();
        setupDialog(project, directory);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return ecfComponentPanel;
    }

    public Properties getDefaultProperties() {
        return myDefaultProperties;
    }

    private void setupDialog(Project project, PsiDirectory directory) {
        initialPath = "";
        init();
        setOKActionEnabled(false);
        if (project != null) {
            final VirtualFile baseDir = directory.getVirtualFile();
            initialPath = baseDir.getPath();
        }
        filter = new UpperCaseDocumentFilter(this);
        setTitle("Create New ECF Component");
        textBaseDir.setEditable(false);
        ((AbstractDocument) textName.getDocument()).setDocumentFilter(filter);
        ((AbstractDocument) textModel.getDocument()).setDocumentFilter(filter);
        ((AbstractDocument) textDBTSpace.getDocument()).setDocumentFilter(filter);
        ((AbstractDocument) textIXTSpace.getDocument()).setDocumentFilter(filter);
        ((AbstractDocument) textLOBSpace.getDocument()).setDocumentFilter(filter);

        textBaseDir.setText(initialPath);
        textDescription.setText("");
        textSDescription.setText("");
        textModel.setText(myDefaultProperties.getProperty(ECFBundle.message("default.ecf.prop.model"), ""));
        textDBTSpace.setText(myDefaultProperties.getProperty(ECFBundle.message("default.ecf.prop.db.tspace"), "TECFSD"));
        textIXTSpace.setText(myDefaultProperties.getProperty(ECFBundle.message("default.ecf.prop.ix.tspace"), "TECFSI"));
        textLOBSpace.setText(myDefaultProperties.getProperty(ECFBundle.message("default.ecf.prop.lb.tspace"), "TECFSB"));

        registerValidators(new FacetValidatorsManager() {
            public void registerValidator(FacetEditorValidator validator, JComponent... componentsToWatch) {
            }

            public void validate() {
                checkValid();
            }
        });
        ecfComponentPanel.setPreferredSize(new Dimension(300, 100));
        checkValid();
    }

    private void checkValid() {
        final String selectedName = getTextKindCombo().getSelectedName();
        String objectName = textName.getText();
        String value;
        boolean OKEnabled = !StringUtil.isEmptyOrSpaces(objectName);
        setOKActionEnabled(OKEnabled);
        if (selectedName != null) {
            if (selectedName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_MODEL)) {
                textSDescription.setEditable(false);
                textModel.setEditable(false);
                textDBTSpace.setEditable(true);
                textIXTSpace.setEditable(true);
                textLOBSpace.setEditable(true);

                if (OKEnabled) {
                    objectName = textDBTSpace.getText();
                    OKEnabled = !StringUtil.isEmptyOrSpaces(objectName);
                }
                if (OKEnabled) {
                    objectName = textIXTSpace.getText();
                    OKEnabled = !StringUtil.isEmptyOrSpaces(objectName);
                }
                if (OKEnabled) {
                    objectName = textLOBSpace.getText();
                    OKEnabled = !StringUtil.isEmptyOrSpaces(objectName);
                }
                setOKActionEnabled(OKEnabled);

                textSDescription.setText("");
                textModel.setText("");
            } else if (selectedName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_API)) {
                textSDescription.setEditable(true);
                textModel.setEditable(false);
                textDBTSpace.setEditable(false);
                textIXTSpace.setEditable(false);
                textLOBSpace.setEditable(false);

                textModel.setText("");
            } else if (selectedName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_BOBJ)) {
                textSDescription.setEditable(false);
                textModel.setEditable(false);
                textDBTSpace.setEditable(false);
                textIXTSpace.setEditable(false);
                textLOBSpace.setEditable(false);

                textSDescription.setText("");
                textModel.setText("");
            } else if (selectedName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_CLASS)) {
                textSDescription.setEditable(false);
                textModel.setEditable(false);
                textDBTSpace.setEditable(false);
                textIXTSpace.setEditable(false);
                textLOBSpace.setEditable(false);

                textSDescription.setText("");
                textModel.setText("");
            } else if (selectedName.equalsIgnoreCase(ECFTemplateUtil.TEMPLATE_ECF_JOB)) {
                textSDescription.setEditable(false);
                textModel.setEditable(false);
                textDBTSpace.setEditable(false);
                textIXTSpace.setEditable(false);
                textLOBSpace.setEditable(false);

                textSDescription.setText("");
                textModel.setText("");
            } else {
                textSDescription.setEditable(true);
                textModel.setEditable(true);
                textDBTSpace.setEditable(false);
                textIXTSpace.setEditable(false);
                textLOBSpace.setEditable(false);

                if (OKEnabled) {
                    objectName = textModel.getText();
                    OKEnabled = !StringUtil.isEmptyOrSpaces(objectName);
                    setOKActionEnabled(OKEnabled);
                }

                value = textDescription.getText();
                if (value != null)
                    textSDescription.setText(value);
            }

        }

        value = textName.getText();
        myDefaultProperties.setProperty(ECFBundle.message("default.ecf.prop.object.name"), value == null ? "" : value);
        value = textDescription.getText();
        myDefaultProperties.setProperty(ECFBundle.message("default.ecf.prop.desc"), value == null ? "" : value);
        value = textSDescription.getText();
        myDefaultProperties.setProperty(ECFBundle.message("default.ecf.prop.short.desc"), value == null ? "" : value);
        value = textModel.getText();
        myDefaultProperties.setProperty(ECFBundle.message("default.ecf.prop.model"), value == null ? "" : value);
        value = textDBTSpace.getText();
        myDefaultProperties.setProperty(ECFBundle.message("default.ecf.prop.db.tspace"), value == null ? "" : value);
        value = textIXTSpace.getText();
        myDefaultProperties.setProperty(ECFBundle.message("default.ecf.prop.ix.tspace"), value == null ? "" : value);
        value = textLOBSpace.getText();
        myDefaultProperties.setProperty(ECFBundle.message("default.ecf.prop.lb.tspace"), value == null ? "" : value);
    }

    private void registerValidators(final FacetValidatorsManager validatorsManager) {
        textName.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                validatorsManager.validate();
            }
        });
        textModel.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                validatorsManager.validate();
            }
        });
        textDBTSpace.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                validatorsManager.validate();
            }
        });
        textIXTSpace.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                validatorsManager.validate();
            }
        });
        textLOBSpace.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                validatorsManager.validate();
            }
        });
        textDescription.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                validatorsManager.validate();
            }
        });
        textKindCombo.getComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validatorsManager.validate();
            }
        });
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (inputValidator != null) {
            final String text = textName.getText();
            final boolean canClose = inputValidator.canClose(text);
            if (!canClose) {
                String sErrorText = "Incorrect Name";
                if (inputValidator instanceof InputValidatorEx) {
                    String sMessage = ((InputValidatorEx) inputValidator).getErrorText(text);
                    if (sMessage != null) {
                        sErrorText = sMessage;
                    }
                }
                return new ValidationInfo(sErrorText, textName);
            }
        }
        return super.doValidate();
    }

    protected JTextField getTextName() {
        return textName;
    }

    protected TemplateKindCombo getTextKindCombo() {
        return textKindCombo;
    }

    protected JTextField getTextDescription() {
        return textDescription;
    }

    protected JTextField getTextSDescription() {
        return textSDescription;
    }

    protected JTextField getTextModel() {
        return textModel;
    }

    protected JTextField getTextDBTSpace() {
        return textDBTSpace;
    }

    protected JTextField getTextIXTSpace() {
        return textIXTSpace;
    }

    protected JTextField getTextLBSpace() { return textLOBSpace; }

    protected JTextField getTextBaseDir() {
        return textBaseDir;
    }

    private String getTrimmedText(JTextField field) {
        final String text = field.getText().trim();
        field.setText(text);
        return text;
    }

    private String getEnteredName() {
        final JTextField nameField = getTextName();
        return getTrimmedText(nameField);
    }

    private String getDBTSpaceName() {
        final JTextField nameField = getTextDBTSpace();
        return getTrimmedText(nameField);
    }

    private String getIXTSpaceName() {
        final JTextField nameField = getTextIXTSpace();
        return getTrimmedText(nameField);
    }

    private String getLOBSpaceName(){
        final JTextField nameField = getTextLBSpace();
        return getTrimmedText(nameField);
    }

    private String getModelName() {
        final JTextField nameField = getTextModel();
        return getTrimmedText(nameField);
    }

    @Override
    protected void doOKAction() {
        if (elementCreator.tryCreate(getEnteredName()).length == 0) {
            return;
        }
        super.doOKAction();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return getTextName();
    }

    public static Builder createDialog(@NotNull final Project project, @NotNull final PsiDirectory directory, @Nullable AttributesDefaults defaults) {
        final CreateECFComponentDlg dialog = new CreateECFComponentDlg(project, directory, defaults);
        return new BuilderImpl(dialog, project);
    }

private static class BuilderImpl implements Builder {

    private final CreateECFComponentDlg oDialog;
    private final Project oProject;

    public BuilderImpl(CreateECFComponentDlg dialog, Project project) {
        oDialog = dialog;
        oProject = project;
    }

    public CreateECFComponentDlg getDialog() {
        return oDialog;
    }

    @Override
    public Builder setTitle(String s) {
        oDialog.setTitle(s);
        return this;
    }

    @Override
    public Builder setValidator(InputValidator inputValidator) {
        oDialog.inputValidator = inputValidator;
        return this;
    }

    @Override
    public Builder addKind(@NotNull String name, @Nullable Icon icon, @NotNull String templateName) {
        oDialog.getTextKindCombo().addItem(name, icon, templateName);
        return this;
    }

    @Nullable
    @Override
    public <T extends PsiElement> T show(@NotNull String errorTitle, @Nullable String selectedTemplateName,
                                         @NotNull final FileCreator<T> creator) {
        final Ref<T> created = Ref.create(null);
        oDialog.getTextKindCombo().setSelectedName(selectedTemplateName);
        oDialog.elementCreator = new ElementCreator(oProject, errorTitle) {
            @Override
            protected PsiElement[] create(String newName) throws Exception {
                final T element = creator.createFile(oDialog.getEnteredName(), oDialog);
                created.set(element);
                if (element != null) {
                    return new PsiElement[]{element};
                }
                return PsiElement.EMPTY_ARRAY;
            }

            @Override
            protected String getActionName(String newName) {
                return creator.getActionName(newName, oDialog);
            }
        };

        oDialog.show();
        if (oDialog.getExitCode() == OK_EXIT_CODE) {
            return created.get();
        }
        return null;
    }

    @Nullable
    @Override
    public Map<String, String> getCustomProperties() {
        return null;
    }
}

public interface Builder {
    Builder setTitle(java.lang.String s);

    CreateECFComponentDlg getDialog();

    Builder setValidator(InputValidator inputValidator);

    Builder addKind(@NotNull java.lang.String kind, @Nullable javax.swing.Icon icon, @NotNull java.lang.String templateName);

    @Nullable
    <T extends PsiElement> T show(@NotNull java.lang.String s, @Nullable String s1, @NotNull FileCreator<T> creator);

    @Nullable
    Map<String, String> getCustomProperties();
}

public interface FileCreator<T> {
    @Nullable
    T createFile(@NotNull String name, @NotNull CreateECFComponentDlg dialog);

    @NotNull
    String getActionName(@NotNull String name, @NotNull CreateECFComponentDlg dialog);
}

}
