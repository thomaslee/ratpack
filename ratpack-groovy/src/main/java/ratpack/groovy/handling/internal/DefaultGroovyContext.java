/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.groovy.handling.internal;

import groovy.lang.Closure;
import ratpack.api.NonBlocking;
import ratpack.api.Nullable;
import ratpack.block.Blocking;
import ratpack.groovy.block.GroovyBlocking;
import ratpack.groovy.block.internal.DefaultGroovyBlocking;
import ratpack.groovy.handling.GroovyContext;
import ratpack.groovy.internal.Util;
import ratpack.handling.ByContentHandler;
import ratpack.handling.ByMethodHandler;
import ratpack.handling.Handler;
import ratpack.http.Request;
import ratpack.http.Response;
import ratpack.parse.Parse;
import ratpack.path.PathTokens;
import ratpack.registry.NotInRegistryException;
import ratpack.registry.Registry;
import ratpack.render.NoSuchRendererException;
import ratpack.server.BindAddress;
import ratpack.util.Action;
import ratpack.util.Factory;
import ratpack.util.ResultAction;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class DefaultGroovyContext implements GroovyContext {

  private final ratpack.handling.Context delegate;

  public DefaultGroovyContext(ratpack.handling.Context delegate) {
    this.delegate = delegate;
  }

  @Override
  public <T> GroovyBlocking.GroovySuccessOrError<T> blocking(Closure<T> operation) {
    return new DefaultGroovyBlocking(this, getBlocking()).block(operation);
  }

  @Override
  public void byMethod(Closure<?> closure) {
    ByMethodHandler handler = getByMethod();
    DefaultGroovyByMethodHandler groovyHandler = new DefaultGroovyByMethodHandler(this, handler);
    Util.configureDelegateFirst(groovyHandler, closure);
    handler.handle(this);
  }

  @Override
  public Request getRequest() {
    return delegate.getRequest();
  }

  @Override
  public Response getResponse() {
    return delegate.getResponse();
  }

  @Override
  @NonBlocking
  public void next() {
    delegate.next();
  }

  @Override
  @NonBlocking
  public void insert(List<Handler> handlers) {
    delegate.insert(handlers);
  }

  @Override
  @NonBlocking
  public void insert(List<Handler> handlers, Registry registry) {
    delegate.insert(handlers, registry);
  }

  @Override
  @NonBlocking
  public <T> void insert(List<Handler> handlers, Class<T> publicType, Factory<? extends T> factory) {
    delegate.insert(handlers, publicType, factory);
  }

  @Override
  @NonBlocking
  public <P, T extends P> void insert(List<Handler> handlers, Class<P> publicType, T implementation) {
    delegate.insert(handlers, publicType, implementation);
  }

  @Override
  @NonBlocking
  public void insert(List<Handler> handlers, Object object) {
    delegate.insert(handlers, object);
  }

  @Override
  @NonBlocking
  public void respond(Handler handler) {
    delegate.respond(handler);
  }

  @Override
  public ByMethodHandler getByMethod() {
    return delegate.getByMethod();
  }

  @Override
  public ByContentHandler getByContent() {
    return delegate.getByContent();
  }

  @Override
  @NonBlocking
  public void error(Exception exception) throws NotInRegistryException {
    delegate.error(exception);
  }

  @Override
  @NonBlocking
  public void clientError(int statusCode) throws NotInRegistryException {
    delegate.clientError(statusCode);
  }

  @Override
  public void withErrorHandling(Runnable runnable) {
    delegate.withErrorHandling(runnable);
  }

  @Override
  public <T> ResultAction<T> resultAction(Action<T> action) {
    return delegate.resultAction(action);
  }

  @Override
  public PathTokens getPathTokens() throws NotInRegistryException {
    return delegate.getPathTokens();
  }

  @Override
  public PathTokens getAllPathTokens() throws NotInRegistryException {
    return delegate.getAllPathTokens();
  }

  @Override
  public File file(String path) throws NotInRegistryException {
    return delegate.file(path);
  }

  @Override
  @NonBlocking
  public void render(Object object) throws NoSuchRendererException {
    delegate.render(object);
  }

  @Override
  public Blocking getBlocking() {
    return delegate.getBlocking();
  }

  @Override
  public <T> Blocking.SuccessOrError<T> blocking(Callable<T> blockingOperation) {
    // Only need this because Groovy picks this method over the closure overload
    if (Closure.class.isInstance(blockingOperation)) {
      return blocking((Closure<T>) blockingOperation);
    }
    return delegate.blocking(blockingOperation);
  }

  @Override
  public void redirect(String location) throws NotInRegistryException {
    delegate.redirect(location);
  }

  @Override
  public void redirect(int code, String location) throws NotInRegistryException {
    delegate.redirect(code, location);
  }

  @Override
  @NonBlocking
  public void lastModified(Date date, Runnable runnable) {
    delegate.lastModified(date, runnable);
  }

  @Override
  public BindAddress getBindAddress() {
    return delegate.getBindAddress();
  }

  @Override
  public <T> T parse(Parse<T> parse) {
    return delegate.parse(parse);
  }

  @Override
  public <O> O get(Class<O> type) throws NotInRegistryException {
    return delegate.get(type);
  }

  @Override
  @Nullable
  public <O> O maybeGet(Class<O> type) {
    return delegate.maybeGet(type);
  }

  @Override
  public <O> List<O> getAll(Class<O> type) {
    return delegate.getAll(type);
  }

}
