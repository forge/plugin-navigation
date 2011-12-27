package org.kumakros.forge.plugin.navigation.bookmark;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.forge.env.Configuration;
import org.jboss.forge.env.ConfigurationScope;
import org.kumakros.forge.plugin.navigation.bookmark.api.BookmarkCacheImpl;

@ApplicationScoped
public class GlobalBookmarkCache extends BookmarkCacheImpl
{

   @Inject
   Configuration configuration;

   @PostConstruct
   public void initialize()
   {
      setForgeConfig(configuration.getScopedConfiguration(ConfigurationScope.USER));
      recoveryBookmarks();
   }

}
