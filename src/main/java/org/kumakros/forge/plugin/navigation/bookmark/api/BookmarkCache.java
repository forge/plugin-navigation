package org.kumakros.forge.plugin.navigation.bookmark.api;

import java.util.List;

import org.kumakros.forge.plugin.navigation.bookmark.exception.NonExistsBookmarkException;
import org.kumakros.forge.plugin.navigation.bookmark.exception.OverwriteBookmarkException;

public interface BookmarkCache
{
   public void addBookmark(String name, String path) throws OverwriteBookmarkException;

   public void overrideBookmark(String name, String path);

   public void delBookmark(String name) throws NonExistsBookmarkException;

   public String getBookmark(String name) throws NonExistsBookmarkException;

   public List<Bookmark> listBookmarks();

   public void cleanAll();

}
