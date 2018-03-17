/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.catalina;

/**
 * The list of valid states for components that implement {@link Lifecycle}.
 * See {@link Lifecycle} for the state transition diagram.
 * 
 * servlet的生命周期的状态如下
 * NEW
 * BEFORE_INIT_EVENT    ==》before_init
 * AFTER_INIT_EVENT     ==》after_init
 * BEFORE_START_EVENT   ==》before_start
 * START_EVENT          ==》start
 * AFTER_START_EVENT    ==》after_start
 * BEFORE_STOP_EVENT    ==》before_stop
 * STOP_EVENT           ==》stop
 * AFTER_STOP_EVENT     ==》after_stop
 * BEFORE_DESTROY_EVENT ==》before_destroy
 * AFTER_DESTROY_EVENT  ==》after_destroy
 * 
 * 上面的这些状态，对应的是我们Servlet的生命的周期的方法，但是这些方法对应的是我们生命周期的事件
 * 因为可以通过我们的LifeCycle接口来达到统一启动/关闭这些组件的效果
 * 我们tomcat的组件实现LifeCycle接口，那么则这个组件可以触发一个或多个上面的事件，当组件启动的时候
 * ，会触发start的事件，我们编写相应的事件监听器来对这些事件响应，事件监听器的接口为org.apache.catalina.LifecycleListener
 */
public enum LifecycleState {
    NEW(false, null),
    INITIALIZING(false, Lifecycle.BEFORE_INIT_EVENT),
    INITIALIZED(false, Lifecycle.AFTER_INIT_EVENT),
    STARTING_PREP(false, Lifecycle.BEFORE_START_EVENT),
    STARTING(true, Lifecycle.START_EVENT),
    STARTED(true, Lifecycle.AFTER_START_EVENT),
    STOPPING_PREP(true, Lifecycle.BEFORE_STOP_EVENT),
    STOPPING(false, Lifecycle.STOP_EVENT),
    STOPPED(false, Lifecycle.AFTER_STOP_EVENT),
    DESTROYING(false, Lifecycle.BEFORE_DESTROY_EVENT),
    DESTROYED(false, Lifecycle.AFTER_DESTROY_EVENT),
    FAILED(false, null),
    /**
     * @deprecated Unused. Will be removed in Tomcat 9.0.x. The state transition
     *             checking in {@link org.apache.catalina.util.LifecycleBase}
     *             makes it impossible to use this state. The intended behaviour
     *             can be obtained by setting the state to
     *             {@link LifecycleState#FAILED} in
     *             <code>LifecycleBase.startInternal()</code>
     */
    @Deprecated
    MUST_STOP(true, null),
    /**
     * @deprecated Unused. Will be removed in Tomcat 9.0.x. The state transition
     *             checking in {@link org.apache.catalina.util.LifecycleBase}
     *             makes it impossible to use this state. The intended behaviour
     *             can be obtained by implementing {@link Lifecycle.SingleUse}.
     */
    @Deprecated
    MUST_DESTROY(false, null);

    private final boolean available;
    private final String lifecycleEvent;

    private LifecycleState(boolean available, String lifecycleEvent) {
        this.available = available;
        this.lifecycleEvent = lifecycleEvent;
    }

    /**
     * May the public methods other than property getters/setters and lifecycle
     * methods be called for a component in this state? It returns
     * <code>true</code> for any component in any of the following states:
     * <ul>
     * <li>{@link #STARTING}</li>
     * <li>{@link #STARTED}</li>
     * <li>{@link #STOPPING_PREP}</li>
     * <li>{@link #MUST_STOP}</li>
     * </ul>
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     *
     */
    public String getLifecycleEvent() {
        return lifecycleEvent;
    }
}
