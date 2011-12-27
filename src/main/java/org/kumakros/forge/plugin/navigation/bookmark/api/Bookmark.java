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
