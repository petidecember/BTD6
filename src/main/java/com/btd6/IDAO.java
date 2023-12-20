package com.btd6;

import java.util.List;

public interface IDAO<T> {

   void createTable();

   void removeTable();

   // CREATE
   boolean insert(T o);

   // READ
   T findById(String uuid);

   // READ
   List<? extends T> getAll();

   // UPDATE
   boolean update(T o);


   // DELETE
   boolean delete(String uuid);

   // DELETE ALL
   void truncate();
}
