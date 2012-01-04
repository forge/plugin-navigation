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
import org.jboss.forge.navigation.Go;
import org.jboss.forge.navigation.bookmark.GlobalBookmarkCache;
import org.jboss.forge.navigation.bookmark.ProjectBookmarkCache;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import static org.junit.Assert.fail;

public class GoTest extends AbstractShellTest
{
   @Inject
   Shell shell;

   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   ProjectBookmarkCache projectBookmarkCache;

   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment()
               .addPackages(true, Go.class.getPackage());
   }

   @Test
   public void globalGoTest() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      String path = shell.getCurrentDirectory().getFullyQualifiedName();
      globalBookmarkCache.addBookmark(marktest, path);

      shell.execute("cd ..");

      String path2 = shell.getCurrentDirectory().getFullyQualifiedName();

      assert !path.contentEquals(path2);

      shell.execute("go " + marktest);

      String path3 = shell.getCurrentDirectory().getFullyQualifiedName();

      assert path.contains(path3);

      globalBookmarkCache.delBookmark(marktest);

   }

   @Test
   public void projectGoTest() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      String path = shell.getCurrentDirectory().getFullyQualifiedName();
      projectBookmarkCache.addBookmark(marktest, path);

      shell.execute("cd ..");

      String path2 = shell.getCurrentDirectory().getFullyQualifiedName();

      assert !path.contentEquals(path2);

      shell.execute("go " + marktest);

      String path3 = shell.getCurrentDirectory().getFullyQualifiedName();

      assert path2.contains(path3);

      shell.execute("cd " + path);

      projectBookmarkCache.delBookmark(marktest);
   }

   @Test
   public void globalGoNotFoundTest() throws Exception
   {
      String marktest = "forge" + RandomStringUtils.randomAlphanumeric(8);
      String path = shell.getCurrentDirectory().getFullyQualifiedName();
      globalBookmarkCache.addBookmark(marktest,
               "/" + RandomStringUtils.randomAlphanumeric(8) + "/" + RandomStringUtils.randomAlphanumeric(8));

      shell.execute("cd ..");

      String path2 = shell.getCurrentDirectory().getFullyQualifiedName();

      assert !path.contentEquals(path2);

      try
      {
         shell.execute("go " + marktest);
         fail();
      }
      catch (Exception e)
      {
      }

      String path3 = shell.getCurrentDirectory().getFullyQualifiedName();

      assert path2.contains(path3);

   }

}
