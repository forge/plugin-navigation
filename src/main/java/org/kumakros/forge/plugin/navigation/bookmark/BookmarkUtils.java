package org.kumakros.forge.plugin.navigation.bookmark;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.forge.project.Project;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.Shell;
import org.kumakros.forge.plugin.navigation.bookmark.api.BookmarkCache;

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
