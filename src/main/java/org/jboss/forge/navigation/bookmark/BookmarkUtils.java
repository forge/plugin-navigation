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
package org.jboss.forge.navigation.bookmark;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.forge.navigation.bookmark.api.BookmarkCache;
import org.jboss.forge.project.Project;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.Shell;

@Singleton
public class BookmarkUtils
{
   @Inject
   Shell shell;

   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   ProjectBookmarkCache projectBookmarkCache;

   public BookmarkCache getBookmarkCache(boolean global)
   {
      if (global)
      {
         return globalBookmarkCache;
      }
      return projectBookmarkCache;
   }

   public String getPath(Resource<?> currentResource, boolean global)
   {
      if (!global)
      {
         // Relative path
         Project currentProject = shell.getCurrentProject();
         if (currentProject != null)
         {
            String rootDir = currentProject.getProjectRoot().getFullyQualifiedName();
            String absPath = currentResource.getFullyQualifiedName();
            if (rootDir.contentEquals(absPath))
               return "/";
            return absPath.replace(rootDir, "");
         }
         return "";
      }
      return currentResource.getFullyQualifiedName();
   }

   public String relativePath(String relativePath)
   {
      if (shell.getCurrentProject() != null)
      {
         String root = shell.getCurrentProject().getProjectRoot().getFullyQualifiedName();
         if (relativePath.contentEquals("/"))
            return root;
         return root + relativePath;
      }
      else
         return relativePath;
   }
}
