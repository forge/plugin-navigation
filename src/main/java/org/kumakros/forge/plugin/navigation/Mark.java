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
package org.kumakros.forge.plugin.navigation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.Plugin;
import org.kumakros.forge.plugin.navigation.bookmark.GlobalBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.ProjectBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.api.Bookmark;
import org.kumakros.forge.plugin.navigation.bookmark.api.BookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.exception.NonExistsBookmarkException;
import org.kumakros.forge.plugin.navigation.bookmark.exception.OverwriteBookmarkException;

/**
 *
 */
@Alias("mark")
public class Mark implements Plugin
{
   @Inject
   private ShellPrompt prompt;

   @Inject
   private Shell shell;

   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   ProjectBookmarkCache projectBookmarkCache;

   @Command("add")
   public void add(
            @Option(required = true, description = "Bookmark name for this resource") String mark,
            @Option(name = "global", description = "Add to global bookmarks list?", flagOnly = true, defaultValue = "false") boolean global)
   {
      BookmarkCache bookmarkCache = getBookmarkCache(global);
      Resource<?> currentResource = shell.getCurrentResource();
      try
      {
         bookmarkCache.addBookmark(mark, currentResource.getFullyQualifiedName());
      }
      catch (OverwriteBookmarkException e)
      {
         boolean promptBoolean = prompt
                  .promptBoolean("This bookmark exists in this bookmark list, Do you want to overwrite it?");
         if (promptBoolean)
         {
            bookmarkCache.overrideBookmark(mark, currentResource.getFullyQualifiedName());
         }
      }
   }

   @Command("del")
   public void del(
            @Option(required = true, description = "Bookmark name to delete") String mark,
            @Option(name = "global", description = "From global bookmark list?", flagOnly = true, defaultValue = "false") boolean global)
   {
      BookmarkCache bookmarkCache = getBookmarkCache(global);
      try
      {
         bookmarkCache.delBookmark(mark);
      }
      catch (NonExistsBookmarkException e)
      {
         ShellMessages.error(shell, "This bookmark doesn't exists in this bookmark list");
      }
   }

   @Command("list")
   public void list(
            @Option(name = "global", description = "From global bookmark list?", flagOnly = true, defaultValue = "false") boolean global)
   {
      BookmarkCache bookmarkCache = getBookmarkCache(global);
      List<Bookmark> listBookmarks = bookmarkCache.listBookmarks();
      for (Bookmark bm : listBookmarks)
      {
         ShellMessages.info(shell, String.format("%1$s50 -> %2$s", bm.getMark(), bm.getPath()));
      }
   }

   private BookmarkCache getBookmarkCache(boolean global)
   {
      if (global)
      {
         return globalBookmarkCache;
      }
      return projectBookmarkCache;
   }
}
