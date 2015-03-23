package ecf.pycharm.plugin.actions;

import ecf.pycharm.plugin.templates.ECFTemplateUtil;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

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

public class UpperCaseDocumentFilter extends DocumentFilter {

    private CreateECFComponentDlg dlg;

    public UpperCaseDocumentFilter(CreateECFComponentDlg dialog) {
        super();
        dlg = dialog;
    }

    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
                             AttributeSet attr) throws BadLocationException {
        boolean isClass = dlg == null ? false : dlg.getTextKindCombo().getSelectedName() == ECFTemplateUtil.TEMPLATE_ECF_CLASS;
        if (!isClass) {
            fb.insertString(offset, text.toUpperCase(), attr);
        } else {
            fb.insertString(offset, text, attr);
        }

    }

    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        boolean isClass = dlg == null ? false : dlg.getTextKindCombo().getSelectedName() == ECFTemplateUtil.TEMPLATE_ECF_CLASS;
        if (!isClass) {
            fb.replace(offset, length, text.toUpperCase(), attrs);
        } else {
            fb.replace(offset, length, text, attrs);
        }
    }
}
