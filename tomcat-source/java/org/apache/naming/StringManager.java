/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.naming;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * An internationalization / localization helper class which reduces
 * the bother of handling ResourceBundles and takes care of the
 * common cases of message formating which otherwise require the
 * creation of Object arrays and such.
 *
 * <p>The StringManager operates on a package basis. One StringManager
 * per package can be created and accessed via the getManager method
 * call.
 *
 * <p>The StringManager will look for a ResourceBundle named by
 * the package name given plus the suffix of "LocalStrings". In
 * practice, this means that the localized information will be contained
 * in a LocalStrings.properties file located in the package
 * directory of the classpath.
 *
 * <p>Please see the documentation for java.util.ResourceBundle for
 * more information.
 *
 * @author James Duncan Davidson [duncan@eng.sun.com]
 * @author James Todd [gonzo@eng.sun.com]
 * @author Mel Martinez [mmartinez@g1440.com]
 * @see java.util.ResourceBundle
 */
/**
 * 
 * @author Administrator
 * Tomcat处理错误的消息的方法是把我们的错误消息都存储在一个大的properties文件当中，便于读取和编辑，但是我们的
 * Tomcat中有几百个类，要就把所有类使用的错误信息都放入一个大的Properties属性文件当中，那么维护这个文件会是一个
 * 噩梦，所以为了避免这种情况的发生，那么则我们tomcat就properties文件划分到不同人包当中，所以我们到每个包下都会有
 * LocalStrings为前辍的properties文件，如LocalStrings_ja.properties,但是我们每个Properties文件都
 * 就用StringManager类人一个实例来处理，当我们Tomcat运行时，会产生StringManager类人很多实例，每个实例都
 * 会去读取某个包下指定的Properties文件，所以的创建这个实例时候就要传入指定包名
 * 
 * 同时还有因为我们的Tomcat相当人受欢迎，所以对错误信息做国际化也是有心要的，目前我们的tomcat就支持三种语言的国际化
 * 支持（分别就英语，日语，西班牙语），英语是存放的LocalStrings.properties文件当中，日语就存放的
 * LocalStrings_ja.properties,西班牙语是存放的LocalStrings_es.properties
 * 
 * ============================================
 * 因为我们每个包下就有很多类，当我们这个包下类要想去properties当中查找错误信息时，就要先获取一个StringManager
 * 实例，但是，在同一个包下的许多人类会使用同一个StringManager实例，那么则如果为每个要查找错误信息的类创建一个
 * StringManager实例，那么则就是在浪费资源，所以，我们把这个StringManager实例为单例，以便StringManager
 * 实例被所有的实例共享
 *
 */
public class StringManager {

    /**
     * The ResourceBundle for this StringManager.
     */
    private final ResourceBundle bundle;
    private final Locale locale;

    /**
     * Creates a new StringManager for a given package. This is a
     * private method and all access to it is arbitrated by the
     * static getManager method call so that only one StringManager
     * per package will be created.
     *
     * @param packageName Name of package to create StringManager for.
     */
    /**
     * 是一个私有的构造方法，同时使用的是一个单例的设计模式,要传入包名，然后去创建对应包下的
     * StringManager实例，然后这个实例就去读取这个文件
     * @param packageName
     */
    private StringManager(String packageName) {
    	//我们的包名如www.hailong.com
        String bundleName = packageName + ".LocalStrings";
        ResourceBundle tempBundle = null;
        try {
        	//会组成这样子的文件名LocalStrings.properperties的配置文件
        	//ResourceBundle会根据运行该运用程序的服务器的语言环境来选择使用那一个文件Locale.getDefault()
        	//es/ja...
            tempBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());
        } catch( MissingResourceException ex ) {
            // Try from the current loader (that's the case for trusted apps)
            // Should only be required if using a TC5 style classloader structure
            // where common != shared != server
        	//得到当前运用的类加载器
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if( cl != null ) {
                try {
                    tempBundle = ResourceBundle.getBundle(
                            bundleName, Locale.getDefault(), cl);
                } catch(MissingResourceException ex2) {
                    // Ignore
                }
            }
        }
        // Get the actual locale, which may be different from the requested one
        if (tempBundle != null) {
            locale = tempBundle.getLocale();
        } else {
            locale = null;
        }
        bundle = tempBundle;
    }

    /**
        Get a string from the underlying resource bundle or return
        null if the String is not found.

        @param key to desired resource String
        @return resource String matching <i>key</i> from underlying
                bundle or null if not found.
        @throws IllegalArgumentException if <i>key</i> is null.
     */
    
    /**
     * 根据key得到错误信息
     * @param key 是一个错误码
     * @return
     */
    public String getString(String key) {
        if(key == null){
            String msg = "key may not have a null value";

            throw new IllegalArgumentException(msg);
        }

        String str = null;

        try {
            str = bundle.getString(key);
        } catch(MissingResourceException mre) {
            //bad: shouldn't mask an exception the following way:
            //   str = "[cannot find message associated with key '" + key + "' due to " + mre + "]";
            //     because it hides the fact that the String was missing
            //     from the calling code.
            //good: could just throw the exception (or wrap it in another)
            //      but that would probably cause much havoc on existing
            //      code.
            //better: consistent with container pattern to
            //      simply return null.  Calling code can then do
            //      a null check.
            str = null;
        }

        return str;
    }

    /**
     * Get a string from the underlying resource bundle and format
     * it with the given set of arguments.
     *
     * @param key  The key for the required message
     * @param args The values to insert into the message
     *
     * @return The request string formatted with the provided arguments or the
     *         key if the key was not found.
     */
    public String getString(final String key, final Object... args) {
        String value = getString(key);
        if (value == null) {
            value = key;
        }

        MessageFormat mf = new MessageFormat(value);
        mf.setLocale(locale);
        return mf.format(args, new StringBuffer(), null).toString();
    }

    // --------------------------------------------------------------
    // STATIC SUPPORT METHODS
    // --------------------------------------------------------------
    //这个hashTable是一个线程安全的方法，但是效率不高，可以改用CurrentHashMap
    private static final Hashtable<String, StringManager> managers =
            new Hashtable<String, StringManager>();

    /**
     * Get the StringManager for a particular package. If a manager for
     * a package already exists, it will be reused, else a new
     * StringManager will be created and returned.
     *
     * @param packageName The package name
     *
     * @return The instance associated with the given package
     */
    
    /**
     * 这个类是一个单例的设计模式，他的构造方法是private的，但是这个在这个类的初始化的
     * 时候就创建了一个好一个StringManager对象，同时
     * @param packageName
     * @return
     */
    public static final synchronized StringManager getManager(String packageName) {
        StringManager mgr = managers.get(packageName);
        if (mgr == null) {
            mgr = new StringManager(packageName);
            managers.put(packageName, mgr);
        }
        return mgr;
    }


    public static final StringManager getManager(Class<?> clazz) {
        return getManager(clazz.getPackage().getName());
    }
}
