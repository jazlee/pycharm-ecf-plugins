package ecf.pycharm.plugin;

import com.intellij.CommonBundle;
import com.intellij.reference.SoftReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.util.ResourceBundle;

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
 * Date: 11/4/13
 * Time: 12:31 AM
 */
public class ECFBundle {
    private static Reference<ResourceBundle> ourBundle;

    @NonNls
    private static final String BUNDLE = "ecf.pycharm.plugin.ECFBundle";

    private ECFBundle() {
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE)String key, Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    // Cached loading
    private static ResourceBundle getBundle() {
        ResourceBundle bundle = null;
        if (ourBundle != null) bundle = ourBundle.get();
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE);
            ourBundle = new SoftReference<ResourceBundle>(bundle);
        }
        return bundle;
    }
}
