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

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.arquillian.api.Deployment;
import org.jboss.forge.shell.completer.BaseCommandCompleterState;
import org.jboss.forge.shell.completer.CommandCompleterState;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.kumakros.forge.plugin.navigation.bookmark.GlobalBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.ProjectBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.api.Bookmark;
import org.kumakros.forge.plugin.navigation.bookmark.api.BookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.exception.NonExistsBookmarkException;
import org.kumakros.forge.plugin.navigation.bookmark.exception.OverwriteBookmarkException;

import static org.junit.Assert.assertEquals;

public class BookmarkAutocompleterTest extends AbstractShellTest
{

   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   ProjectBookmarkCache projectBookmarkCache;

   @Inject
   BookmarkAutocompleter bookmarkAutocompleter;

   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment()
               .addPackages(true, BookmarkAutocompleter.class.getPackage());
   }

   @Test
   public void foundOnlyGlobalTest() throws OverwriteBookmarkException, NonExistsBookmarkException
   {
      List<Bookmark> globalmarks = getAndClean(globalBookmarkCache);
      List<Bookmark> projectmarks = getAndClean(projectBookmarkCache);

      String markglobal = "global" + RandomStringUtils.randomAlphanumeric(8);
      String path = getShell().getCurrentDirectory().getFullyQualifiedName();
      globalBookmarkCache.addBookmark(markglobal, path);

      String markproject = "project" + RandomStringUtils.randomAlphanumeric(8);
      String pathProject = getShell().getCurrentDirectory().getFullyQualifiedName();
      projectBookmarkCache.addBookmark(markproject, pathProject);

      CommandCompleterState state = new BaseCommandCompleterState("global", "global", 6);

      bookmarkAutocompleter.complete(state);

      globalBookmarkCache.delBookmark(markglobal);
      projectBookmarkCache.delBookmark(markproject);

      restore(globalBookmarkCache, globalmarks);
      restore(projectBookmarkCache, projectmarks);

      assertEquals(1, state.getCandidates().size());
   }

   @Test
   public void foundOnlyProjectTest() throws OverwriteBookmarkException, NonExistsBookmarkException
   {
      List<Bookmark> globalmarks = getAndClean(globalBookmarkCache);
      List<Bookmark> projectmarks = getAndClean(projectBookmarkCache);

      String markglobal = "global" + RandomStringUtils.randomAlphanumeric(8);
      String path = getShell().getCurrentDirectory().getFullyQualifiedName();
      globalBookmarkCache.addBookmark(markglobal, path);

      String markproject = "project" + RandomStringUtils.randomAlphanumeric(8);
      String pathProject = getShell().getCurrentDirectory().getFullyQualifiedName();
      projectBookmarkCache.addBookmark(markproject, pathProject);

      CommandCompleterState state = new BaseCommandCompleterState("project", null, 7);

      bookmarkAutocompleter.complete(state);

      globalBookmarkCache.delBookmark(markglobal);
      projectBookmarkCache.delBookmark(markproject);

      restore(globalBookmarkCache, globalmarks);
      restore(projectBookmarkCache, projectmarks);

      assertEquals(1, state.getCandidates().size());
   }

   @Test
   public void foundBothTest() throws OverwriteBookmarkException, NonExistsBookmarkException
   {
      List<Bookmark> globalmarks = getAndClean(globalBookmarkCache);
      List<Bookmark> projectmarks = getAndClean(projectBookmarkCache);

      String markglobal = "forge23global" + RandomStringUtils.randomAlphanumeric(8);
      String path = getShell().getCurrentDirectory().getFullyQualifiedName();
      globalBookmarkCache.addBookmark(markglobal, path);

      String markproject = "forge23project" + RandomStringUtils.randomAlphanumeric(8);
      String pathProject = getShell().getCurrentDirectory().getFullyQualifiedName();
      projectBookmarkCache.addBookmark(markproject, pathProject);

      CommandCompleterState state = new BaseCommandCompleterState("forge23", null, 7);

      bookmarkAutocompleter.complete(state);

      globalBookmarkCache.delBookmark(markglobal);
      projectBookmarkCache.delBookmark(markproject);

      restore(globalBookmarkCache, globalmarks);
      restore(projectBookmarkCache, projectmarks);

      assertEquals(2, state.getCandidates().size());
   }

   @Test
   public void notFoundTest() throws OverwriteBookmarkException, NonExistsBookmarkException
   {
      List<Bookmark> globalmarks = getAndClean(globalBookmarkCache);
      List<Bookmark> projectmarks = getAndClean(projectBookmarkCache);

      String markglobal = "forgeglobal" + RandomStringUtils.randomAlphanumeric(8);
      String path = getShell().getCurrentDirectory().getFullyQualifiedName();
      globalBookmarkCache.addBookmark(markglobal, path);

      String markproject = "forgeproject" + RandomStringUtils.randomAlphanumeric(8);
      String pathProject = getShell().getCurrentDirectory().getFullyQualifiedName();
      projectBookmarkCache.addBookmark(markproject, pathProject);

      CommandCompleterState state = new BaseCommandCompleterState(RandomStringUtils.randomAlphanumeric(8), null, 8);

      bookmarkAutocompleter.complete(state);

      globalBookmarkCache.delBookmark(markglobal);
      projectBookmarkCache.delBookmark(markproject);

      restore(globalBookmarkCache, globalmarks);
      restore(projectBookmarkCache, projectmarks);

      assertEquals(0, state.getCandidates().size());
   }

   private List<Bookmark> getAndClean(BookmarkCache bookmarkCache)
   {
      List<Bookmark> marks = bookmarkCache.listBookmarks();
      bookmarkCache.cleanAll();
      return marks;
   }

   private void restore(BookmarkCache bookmarkCache, List<Bookmark> bookmarks)
   {
      for (Bookmark bookmark : bookmarks)
      {
         bookmarkCache.overrideBookmark(bookmark.getMark(), bookmark.getPath());
      }
   }

}
