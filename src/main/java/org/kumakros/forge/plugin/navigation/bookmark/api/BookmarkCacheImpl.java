package org.kumakros.forge.plugin.navigation.bookmark.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.forge.env.Configuration;
import org.kumakros.forge.plugin.navigation.bookmark.exception.NonExistsBookmarkException;
import org.kumakros.forge.plugin.navigation.bookmark.exception.OverwriteBookmarkException;

public class BookmarkCacheImpl implements BookmarkCache
{
   private final HashMap<String, String> cache;
   private final String bookmarPreffix = "bookmark";
   private Configuration forgeConfig;

   public BookmarkCacheImpl()
   {
      this.cache = new HashMap<String, String>();
   }

   public void addBookmark(String name, String path) throws OverwriteBookmarkException
   {
      checkInitializedCache();
      if (!cache.containsKey(name))
      {
         cache.put(name, path);
         forgeConfig.addProperty(bookmarPreffix + "." + name, path);
      }
      else
      {
         throw new OverwriteBookmarkException();
      }
   }

   public void overrideBookmark(String name, String path)
   {
      checkInitializedCache();
      try
      {
         delBookmark(name);
      }
      catch (NonExistsBookmarkException e)
      {

      }
      try
      {
         addBookmark(name, path);
      }
      catch (OverwriteBookmarkException e)
      {
      }

   }

   public void delBookmark(String name) throws NonExistsBookmarkException
   {
      checkInitializedCache();
      if (cache.containsKey(name))
      {
         cache.remove(name);
         forgeConfig.clearProperty(bookmarPreffix + "." + name);
      }
      else
      {
         throw new NonExistsBookmarkException();
      }

   }

   public String getBookmark(String name) throws NonExistsBookmarkException
   {
      if (cache.containsKey(name))
      {
         return cache.get(name);
      }
      else
      {
         throw new NonExistsBookmarkException();
      }
   }

   public List<Bookmark> listBookmarks()
   {
      List<Bookmark> bookmarks = new ArrayList<Bookmark>();
      for (Entry<String, String> e : cache.entrySet())
      {
         bookmarks.add(new Bookmark(e.getKey(), e.getValue()));
      }
      return bookmarks;
   }

   protected void recoveryBookmarks()
   {
      checkInitializedCache();
      cache.clear();
      Iterator<?> keys = forgeConfig.getKeys(bookmarPreffix);
      while (keys.hasNext())
      {
         String key = String.valueOf(keys.next());
         key = key.replace(bookmarPreffix + ".", "");
         cache.put(key, forgeConfig.getString(key));
      }
   }

   protected void clearBookmarks()
   {
      setForgeConfig(null);
      cache.clear();
   }

   protected void setForgeConfig(Configuration forgeConfig)
   {
      this.forgeConfig = forgeConfig;
   }

   /**
    * Check if Configuration is set
    * 
    * @throws IllegalStateException when forgeConfig isn't set
    */
   public void checkInitializedCache()
   {
      if (this.forgeConfig == null)
      {
         throw new IllegalStateException("You must setForgeConfig before use this bookmarks cache");
      }
   }

   public void cleanAll()
   {
      checkInitializedCache();
      Set<String> keySet = cache.keySet();
      for (String key : keySet)
      {
         forgeConfig.clearProperty(bookmarPreffix + "." + key);
      }
      cache.clear();
   }

   protected HashMap<String, String> getCache()
   {
      return cache;
   }

}
