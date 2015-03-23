package ecf.pycharm.plugin.templates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

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
 * Date: 11/6/13
 * Time: 12:52 AM
 */
public class ECFTemplateProvider implements DefaultLiveTemplatesProvider {

    @Override
    public String[] getDefaultLiveTemplateFiles() {
        return new String[]{"liveTemplates/ECF"};
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return null;
    }
}
