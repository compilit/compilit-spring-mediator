package com.compilit.mediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.core.GenericTypeResolver.resolveTypeArguments;

final class GenericTypeParameterResolver {

  private GenericTypeParameterResolver() {
  }

  public static <H> List<Class<?>> resolveGenericParameters(H handler, Class<?> handlerClass, String name) {
    var generics = resolveTypeArguments(handler.getClass(), handlerClass);
    if (generics == null) {
      throw new MediatorException(ExceptionMessages.unresolvableParameterTypeMessage(name));
    }
    return new ArrayList<>(Arrays.asList(generics));
  }

}
