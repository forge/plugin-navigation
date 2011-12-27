package org.kumakros.forge.plugin.navigation.bookmark;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.forge.env.Configuration;
import org.jboss.forge.env.ConfigurationScope;
import org.jboss.forge.shell.events.ProjectChanged;
import org.kumakros.forge.plugin.navigation.bookmark.api.BookmarkCacheImpl;

@ApplicationScoped
public class ProjectBookmarkCache extends BookmarkCacheImpl
{

   @Inject
   Configuration configuration;

   @PostConstruct
   public void initialize()
   {
      setForgeConfig(configuration.getScopedConfiguration(ConfigurationScope.PROJECT));
      recoveryBookmarks();
   }

   public void changeProject(@Observes ProjectChanged projectChanged)
   {
      if (projectChanged.getNewProject() != null)
      {
         initialize();
      }
      else
      {
         clearBookmarks();
      }

   }
}
