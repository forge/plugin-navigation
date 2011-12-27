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

import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.completer.CommandCompleter;
import org.jboss.forge.shell.completer.CommandCompleterState;
import org.kumakros.forge.plugin.navigation.bookmark.GlobalBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.ProjectBookmarkCache;
import org.kumakros.forge.plugin.navigation.bookmark.api.Bookmark;
import org.kumakros.forge.plugin.navigation.bookmark.api.BookmarkCache;

public class BookmarkAutocompleter implements CommandCompleter
{
   public static final String GLOBAL_SUFFIX = "(global)";
   public static final String PROJECT_SUFFIX = "";

   @Inject
   GlobalBookmarkCache globalBookmarkCache;

   @Inject
   ProjectBookmarkCache projectBookmarkCache;

   @Inject
   Shell shell;

   @Override
   public void complete(CommandCompleterState state)
   {
      String peek = state.getTokens().peek();
      if (state.getTokens().size() <= 1)
      {
         if (shell.getCurrentProject() != null)
         {
            updateCandidates(state, projectBookmarkCache, peek, PROJECT_SUFFIX);
         }
         updateCandidates(state, globalBookmarkCache, peek, GLOBAL_SUFFIX);
      }
   }

   private void updateCandidates(CommandCompleterState state, BookmarkCache bookmarkCache, String peek,
            String suffix)
   {
      List<Bookmark> preffixSearch = bookmarkCache.preffixSearch(peek);
      for (Bookmark marks : preffixSearch)
      {
         state.getCandidates().add(marks.getMark() + suffix);
         state.setIndex(state.getOriginalIndex() - (peek == null ? 0 : peek.length()));
      }
   }

}
