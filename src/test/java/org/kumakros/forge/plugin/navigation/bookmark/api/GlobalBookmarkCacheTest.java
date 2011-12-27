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

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.arquillian.api.Deployment;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.kumakros.forge.plugin.navigation.bookmark.GlobalBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.exception.NonExistsBookmarkException;
import org.kumakros.forge.plugin.navigation.bookmark.exception.OverwriteBookmarkException;

public class GlobalBookmarkCacheTest extends AbstractShellTest
{
   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   Shell shell;

   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment()
               .addPackages(true, GlobalBookmarkCache.class.getPackage());
   }

   @Test
   public void cleanAllAndRestoreTest()
   {
      Set<Entry<String, String>> entrySet = globalBookmarkCache.getCache().entrySet();
      globalBookmarkCache.cleanAll();
      for (Entry<String, String> entry : entrySet)
      {
         globalBookmarkCache.overrideBookmark(entry.getKey(), entry.getValue());
      }
   }

   @Test
   public void addAndRemoveTest() throws OverwriteBookmarkException, NonExistsBookmarkException
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      DirectoryResource currentDirectory = shell.getCurrentDirectory();
      globalBookmarkCache.addBookmark(marktest, currentDirectory.getFullyQualifiedName());

      globalBookmarkCache.delBookmark(marktest);
   }

   @Test
   public void addAndOverwriteTest() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      DirectoryResource currentDirectory = shell.getCurrentDirectory();
      globalBookmarkCache.addBookmark(marktest, currentDirectory.getFullyQualifiedName());
      String path1 = globalBookmarkCache.getBookmark(marktest);

      shell.execute("cd ..");

      currentDirectory = shell.getCurrentDirectory();

      globalBookmarkCache.overrideBookmark(marktest, currentDirectory.getFullyQualifiedName());

      String path2 = globalBookmarkCache.getBookmark(marktest);

      assert !path1.contentEquals(path2);

      globalBookmarkCache.delBookmark(marktest);
   }

   @Test
   public void listAllTest() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      DirectoryResource currentDirectory = shell.getCurrentDirectory();

      List<Bookmark> list1 = globalBookmarkCache.listBookmarks();

      globalBookmarkCache.addBookmark(marktest, currentDirectory.getFullyQualifiedName());

      List<Bookmark> list2 = globalBookmarkCache.listBookmarks();

      globalBookmarkCache.delBookmark(marktest);

      assert list1.size() + 1 == list2.size();

      globalBookmarkCache.cleanAll();
   }

   @Test
   public void preffixSearchTest() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      DirectoryResource currentDirectory = shell.getCurrentDirectory();

      List<Bookmark> list1 = globalBookmarkCache.preffixSearch("forge");

      globalBookmarkCache.addBookmark(marktest, currentDirectory.getFullyQualifiedName());

      List<Bookmark> list2 = globalBookmarkCache.preffixSearch("forge");

      globalBookmarkCache.delBookmark(marktest);

      assert list1.size() + 1 == list2.size();

      globalBookmarkCache.cleanAll();
   }

}
