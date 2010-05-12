package plexus.clojure.factory;

import clojure.lang.RT;
import clojure.lang.Var;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.factory.AbstractComponentFactory;
import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

import java.io.IOException;

public class ClojureComponentFactory
    extends AbstractComponentFactory
{
    public Object newInstance( ComponentDescriptor componentDescriptor,
                               ClassRealm classRealm,
                               PlexusContainer container )
        throws ComponentInstantiationException
    {
      ClassLoader currentContextLoader =
        Thread.currentThread().getContextClassLoader();

      try
      {
        Thread.currentThread().setContextClassLoader(
          classRealm.getClassLoader());

        Var require = RT.var("clojure.core", "require");
        Var symbol = RT.var("clojure.core", "symbol");
        require.invoke(
          symbol.invoke(
            "plexus.clojure.factory.component-factory"));
        Var instantiate = RT.var(
          "plexus.clojure.factory.component-factory",
          "instantiate");

        return instantiate.invoke(componentDescriptor.getImplementation());
        }
        catch ( Exception e )
        {
            throw new ComponentInstantiationException(
              "Failed to extract Clojure component for: "
              + componentDescriptor.getHumanReadableKey(), e );
        }
 	finally
        {
          Thread.currentThread()
            .setContextClassLoader(currentContextLoader);
        }
    }

    public String getId()
    {
        return "clojure-mojo";
    }

}
