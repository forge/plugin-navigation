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
            updateCandidates(state.getCandidates(), projectBookmarkCache, peek, "");
         }
         updateCandidates(state.getCandidates(), globalBookmarkCache, peek, " (global)");
      }
   }

   private void updateCandidates(List<String> candidates, BookmarkCache bookmarkCache, String peek, String suffix)
   {
      List<Bookmark> preffixSearch = bookmarkCache.preffixSearch(peek);
      for (Bookmark marks : preffixSearch)
      {
         candidates.add(marks.getMark() + suffix);
      }
   }

}
