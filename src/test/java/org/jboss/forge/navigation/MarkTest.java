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
package org.jboss.forge.navigation;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.arquillian.api.Deployment;
import org.jboss.forge.navigation.Mark;
import org.jboss.forge.navigation.bookmark.GlobalBookmarkCache;
import org.jboss.forge.navigation.bookmark.ProjectBookmarkCache;
import org.jboss.forge.navigation.bookmark.exception.NonExistsBookmarkException;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import static org.junit.Assert.fail;

public class MarkTest extends AbstractShellTest
{
   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   ProjectBookmarkCache projectBookmarkCache;

   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment()
               .addPackages(true, Mark.class.getPackage());
   }

   @Test
   public void testAddGlobalMark() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      getShell().execute("mark add " + marktest + " --global");
      globalBookmarkCache.getBookmark(marktest);

      try
      {
         projectBookmarkCache.getBookmark(marktest);
         fail();
      }
      catch (NonExistsBookmarkException e)
      {

      }

      globalBookmarkCache.delBookmark(marktest);
   }

   @Test
   public void testAddProjectMark() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      getShell().execute("mark add " + marktest);
      projectBookmarkCache.getBookmark(marktest);

      try
      {
         globalBookmarkCache.getBookmark(marktest);
         fail();
      }
      catch (NonExistsBookmarkException e)
      {

      }
      projectBookmarkCache.delBookmark(marktest);

   }

   @Test
   public void testAddGlobalOverwriteMark() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      queueInputLines("y", "y");

      getShell().execute("mark add " + marktest + " --global");
      String path1 = globalBookmarkCache.getBookmark(marktest);

      getShell().execute("mark add " + marktest + " --global");
      String path2 = globalBookmarkCache.getBookmark(marktest);
      assert path1.contentEquals(path2);

      getShell().execute("cd ..");
      getShell().execute("mark add " + marktest + " --global");

      String path3 = globalBookmarkCache.getBookmark(marktest);
      assert !path1.contentEquals(path3);

      globalBookmarkCache.delBookmark(marktest);

   }

   @Test
   public void testAddGlobalNonOverwriteMark() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      queueInputLines("n");
      getShell().execute("mark add " + marktest + " --global");
      String path1 = globalBookmarkCache.getBookmark(marktest);

      getShell().execute("cd ..");

      getShell().execute("mark add " + marktest + " --global");
      String path2 = globalBookmarkCache.getBookmark(marktest);
      assert path1.contentEquals(path2);

      globalBookmarkCache.delBookmark(marktest);
   }

   @Test
   public void testAddProjectOverwriteMark() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      queueInputLines("y");

      getShell().execute("mark add " + marktest);
      String path1 = projectBookmarkCache.getBookmark(marktest);

      getShell().execute("mark add " + marktest);
      String path2 = projectBookmarkCache.getBookmark(marktest);
      assert path1.contentEquals(path2);

      projectBookmarkCache.delBookmark(marktest);

   }

   @Test
   public void testAddProjectNonOverwriteMark() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      queueInputLines("n");
      getShell().execute("mark add " + marktest);
      String path1 = projectBookmarkCache.getBookmark(marktest);

      DirectoryResource currentDirectory = getShell().getCurrentDirectory();
      getShell().execute("cd ..");

      try
      {
         getShell().execute("mark add " + marktest);
         fail();
      }
      catch (IllegalStateException e)
      {

      }

      getShell().execute("cd " + currentDirectory.getFullyQualifiedName());

      projectBookmarkCache.delBookmark(marktest);
   }

   @Test
   public void testProjectOutside() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      DirectoryResource currentDirectory = getShell().getCurrentDirectory();
      getShell().execute("cd ..");

      try
      {
         getShell().execute("mark add " + marktest);
         fail();
      }
      catch (IllegalStateException e)
      {

      }

      getShell().execute("cd " + currentDirectory.getFullyQualifiedName());
   }

   @Test
   public void testList() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      DirectoryResource currentDirectory = getShell().getCurrentDirectory();

      projectBookmarkCache.cleanAll();

      getShell().execute("mark add " + marktest);

      getShell().execute("mark list");

      getShell().execute("mark del " + marktest);

      getShell().execute("mark list");

      projectBookmarkCache.cleanAll();

   }

   // @Test
   // public void testPrompt() throws Exception
   // {
   // queueInputLines("y");
   // getShell().execute("mark prompt foo bar");
   // }
}
