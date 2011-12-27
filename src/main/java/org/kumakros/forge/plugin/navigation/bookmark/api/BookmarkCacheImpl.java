/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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

   @Override
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

   @Override
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

   @Override
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

   @Override
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

   @Override
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
         String path = forgeConfig.getString(key);
         key = key.replace(bookmarPreffix + ".", "");
         cache.put(key, path);
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

   @Override
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

   @Override
   public List<Bookmark> preffixSearch(String preffix)
   {
      Set<String> keySet = cache.keySet();
      List<Bookmark> bookmarks = new ArrayList<Bookmark>();
      for (String key : keySet)
      {
         if (preffix == null || preffix.contentEquals("") || key.startsWith(preffix))
         {
            String path = cache.get(key);
            bookmarks.add(new Bookmark(key, path));
         }
      }
      return bookmarks;
   }
}
