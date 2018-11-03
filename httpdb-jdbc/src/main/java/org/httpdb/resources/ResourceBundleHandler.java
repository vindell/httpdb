package org.httpdb.resources;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public final class ResourceBundleHandler {

    /** Used to synchronize access */
    private static final Object mutex = new Object();

    /** The Locale used internally to fetch resource bundles. */
    private static Locale locale = Locale.getDefault();

    /** Map:  Integer object handle => <code>ResourceBundle</code> object. */
    private static ConcurrentMap<String, Integer> bundleHandleMap = new ConcurrentHashMap<String, Integer>();

    /** List whose elements are <code>ResourceBundle</code> objects */
    private static List<ResourceBundle> bundleList = new ArrayList<ResourceBundle>();

    /**
     * The resource path prefix of the <code>ResourceBundle</code> objects
     * handled by this class.
     */
    private static final String prefix = "org.hsqldb.resources.";

    /** JDK 1.1 compliance */
    private static final Method newGetBundleMethod = getNewGetBundleMethod();

    /** Pure utility class: external construction disabled. */
    private ResourceBundleHandler() {}

    /**
     * Getter for property locale. <p>
     *
     * @return Value of property locale.
     */
    public static Locale getLocale() {

        synchronized (mutex) {
            return locale;
        }
    }

    /**
     * Setter for property locale. <p>
     *
     * @param l the new locale
     * @throws IllegalArgumentException when the new locale is null
     */
    public static void setLocale(Locale l) throws IllegalArgumentException {

        synchronized (mutex) {
            if (l == null) {
                throw new IllegalArgumentException("null locale");
            }

            locale = l;
        }
    }

    /**
     * Retrieves an <code>int</code> handle to the <code>ResourceBundle</code>
     * object corresponding to the specified name and current
     * <code>Locale</code>, using the specified <code>ClassLoader</code>. <p>
     *
     * @return <code>int</code> handle to the <code>ResourceBundle</code>
     *        object corresponding to the specified name and
     *        current <code>Locale</code>, or -1 if no such bundle
     *        can be found
     * @param cl The ClassLoader to use in the search
     * @param name of the desired bundle
     */
    public static int getBundleHandle(String name, ClassLoader cl) {

        Integer        bundleHandle;
        ResourceBundle bundle;
        String         bundleName;
        String         bundleKey;

        bundleName = prefix + name;

        synchronized (mutex) {
            bundleKey    = locale.toString() + bundleName;
            bundleHandle = (Integer) bundleHandleMap.get(bundleKey);

            if (bundleHandle == null) {
                bundle = getBundle(bundleName, locale, cl);

                bundleList.add(bundle);

                bundleHandle = Integer.valueOf(bundleList.size() - 1);

                bundleHandleMap.put(bundleKey, bundleHandle);
            }
        }

        return bundleHandle.intValue();
    }

    /**
     * Retrieves, from the <code>ResourceBundle</code> object corresponding
     * to the specified handle, the <code>String</code> value corresponding
     * to the specified key.  <code>null</code> is retrieved if either there
     *  is no <code>ResourceBundle</code> object for the handle or there is no
     * <code>String</code> value for the specified key. <p>
     *
     * @param handle an <code>int</code> handle to a
     *      <code>ResourceBundle</code> object
     * @param key A <code>String</code> key to a <code>String</code> value
     * @return The String value corresponding to the specified handle and key.
     */
    public static String getString(int handle, String key) {

        ResourceBundle bundle;
        String         s;

        synchronized (mutex) {
            if (handle < 0 || handle >= bundleList.size() || key == null) {
                bundle = null;
            } else {
                bundle = (ResourceBundle) bundleList.get(handle);
            }
        }

        if (bundle == null) {
            s = null;
        } else {
            try {
                s = bundle.getString(key);
            } catch (Exception e) {
                s = null;
            }
        }

        return s;
    }

    /**
     * One-shot initialization of JDK 1.2+ ResourceBundle.getBundle() method
     * having ClassLoader in the signature.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static Method getNewGetBundleMethod() {

        Class   clazz;
        Class[] args;

        clazz = ResourceBundle.class;
        args  = new Class[] {
            String.class, Locale.class, ClassLoader.class
        };

        try {
            return clazz.getMethod("getBundle", args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a resource bundle using the specified base name, locale, and
     * class loader. This is a JDK 1.1 compliant substitution for the
     * ResourceBundle method with the same name and signature. If there
     * is a problem using the JDK 1.2 functionality (the class loader is
     * specified non-null and the underlying method is not available or there
     * is a security exception, etc.), then the behaviour reverts to that
     * of JDK 1.1.
     *
     * @param name the base name of the resource bundle, a fully
     *      qualified class name
     * @param locale the locale for which a resource bundle is desired
     * @param cl the class loader from which to load the resource bundle
     */
    public static ResourceBundle getBundle(String name, Locale locale,
                                           ClassLoader cl)
                                           throws NullPointerException,
                                               MissingResourceException {

        if (cl == null) {
            return ResourceBundle.getBundle(name, locale);
        } else if (newGetBundleMethod == null) {
            return ResourceBundle.getBundle(name, locale);
        } else {
            try {
                return (ResourceBundle) newGetBundleMethod.invoke(null,
                        new Object[] {
                    name, locale, cl
                });
            } catch (Exception e) {
                return ResourceBundle.getBundle(name, locale);
            }
        }
    }
}
