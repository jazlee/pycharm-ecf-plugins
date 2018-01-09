package ecf.pycharm.plugin.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
 */

public abstract class ECFBaseDialog extends DialogWrapper {

    protected ECFBaseDialog(Project project) {
        super(project, true);
    }

    protected ECFBaseDialog(Component parent) {
        super(parent, true);
    }

    private void updateOkButton() {
        initValidation();
    }

    private class MyDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent documentEvent) {
            updateOkButton();
        }

        public void removeUpdate(DocumentEvent documentEvent) {
            updateOkButton();
        }

        public void changedUpdate(DocumentEvent documentEvent) {
            updateOkButton();
        }
    }

    protected void addUpdater(JTextField field) {
        field.getDocument().addDocumentListener(new MyDocumentListener());
    }

    protected void addUpdater(JToggleButton check) {
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateOkButton();
            }
        });
    }

}
