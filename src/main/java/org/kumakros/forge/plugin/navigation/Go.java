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

import java.util.Arrays;

import javax.inject.Inject;

import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.Plugin;
import org.kumakros.forge.plugin.navigation.bookmark.BookmarkUtils;
import org.kumakros.forge.plugin.navigation.bookmark.GlobalBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.ProjectBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.exception.NonExistsBookmarkException;

/**
 *
 */
@Alias("go")
public class Go implements Plugin
{
   @Inject
   private Shell shell;

   @Inject
   private ShellPrompt prompt;

   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   ProjectBookmarkCache projectBookmarkCache;

   @Inject
   BookmarkUtils bookmarkUtils;

   @DefaultCommand
   public void defaultCommand(
            @Option(required = true, completer = BookmarkAutocompleter.class, description = "Name of bookmark for go") final String mark)
            throws Exception
   {
      String pathProject = null;
      String pathGlobal = null;
      String path = null;
      if (shell.getCurrentProject() != null)
      {
         try
         {
            String bookmark = mark.replace(BookmarkAutocompleter.PROJECT_SUFFIX, "");
            pathProject = projectBookmarkCache.getBookmark(bookmark);
            pathProject = bookmarkUtils.relativePath(pathProject);
         }
         catch (NonExistsBookmarkException e)
         {
            // No pasa nada
         }
      }
      try
      {
         String bookmark = mark.replace(BookmarkAutocompleter.GLOBAL_SUFFIX, "");
         pathGlobal = globalBookmarkCache.getBookmark(bookmark);
      }
      catch (NonExistsBookmarkException e1)
      {
         // No pasa nada
      }
      if (pathProject != null && pathGlobal != null)
      {
         // To choose between project and global option
         int promptChoice = prompt.promptChoice("You have 2 paths with the same mark\nGlobal path: " + pathGlobal
                  + "\nProject path " + pathProject, Arrays.asList("global", "project"));
         if (promptChoice == 0)
         {
            path = pathGlobal;
         }
         else
         {
            path = pathProject;
         }
      }
      else if (pathProject != null)
      {
         path = pathProject;
      }
      else if (pathGlobal != null)
      {
         path = pathGlobal;
      }

      if (path != null)
      {
         shell.execute("cd " + path);
      }
   }
}
