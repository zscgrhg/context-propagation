/*
 * Copyright (C) 2016 Talsma ICT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package nl.talsmasoftware.context;

import nl.talsmasoftware.context.threadlocal.AbstractThreadLocalContext;

import java.util.Optional;

/**
 * A context can be anything that needs to be maintained on the 'current thread' level.
 * <p>
 * Implementations are typically maintained within a static {@link ThreadLocal} variable.<br>
 * A context has a very simple life-cycle: they can be created and {@link #close() closed}.
 * A well-behaved <code>Context</code> implementation will make sure that thread-local state is restored
 * to the way it was before when the context gets {@link #close() closed} again.<br>
 * There is an {@link AbstractThreadLocalContext abstract implementation}
 * available that can be extended, that takes care of random-depth nested contexts and restoring the 'previous'
 * context state.
 * <p>
 * <center><img src="Context.svg" alt="Context interface"></center>
 *
 * @author Sjoerd Talsma
 */
public interface Context<T> extends AutoCloseable {

    /**
     * Returns the value associated with this context.
     * <p>
     * Implementors should explicitly document the behaviour of this method <em>after {@link #close()}</em> was called.
     * For example, it may be useful to always return <code>empty</code> after a {@link Context} has been
     * {@link #close() closed}.
     * Contrary, it may in some cases be useful to retain the existing <code>value</code> after the context is closed,
     * so clients that have kept a reference can still have access to it.
     * <p>
     * Normally, for security-related contexts, it is wise to always return <code>null</code> from closed contexts.
     *
     * @return The value associated with this context.
     */
    Optional<T> getValue();

    /**
     * Closes this context and restores any context changes made by this object to the way things were before it
     * got created.
     * <p>
     * It must be possible to call this method multiple times.
     * It is the responsibility of the implementor of this context to make sure that closing an already-closed context
     * has no unwanted side-effects.
     * A simple way to achieve this is by using an {@link java.util.concurrent.atomic.AtomicBoolean} to make sure the
     * 'closing' transition is executed only once.
     * <p>
     *
     * @throws RuntimeException if an error occurs while restoring the context.
     */
    void close();

}
