package org.kumakros.forge.plugin.navigation.bookmark.api;

public class Bookmark
{

   private String mark;
   private String path;

   public Bookmark()
   {

   }

   public Bookmark(String mark, String path)
   {
      this.mark = mark;
      this.path = path;
   }

   public String getMark()
   {
      return mark;
   }

   public void setMark(String mark)
   {
      this.mark = mark;
   }

   public String getPath()
   {
      return path;
   }

   public void setPath(String path)
   {
      this.path = path;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((mark == null) ? 0 : mark.hashCode());
      result = prime * result + ((path == null) ? 0 : path.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      Bookmark other = (Bookmark) obj;
      if (mark == null)
      {
         if (other.mark != null)
         {
            return false;
         }
      }
      else if (!mark.equals(other.mark))
      {
         return false;
      }
      if (path == null)
      {
         if (other.path != null)
         {
            return false;
         }
      }
      else if (!path.equals(other.path))
      {
         return false;
      }
      return true;
   }

}
