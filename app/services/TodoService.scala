package services

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future
import scala.language.postfixOps

@javax.inject.Singleton
class TodoService @Inject() (dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple = {
    get[Option[Long]]("todo.id") ~
    get[String]("todo.name") map {
      case id ~ name => Todo(id,name)
    }
  }

  def list(): Seq[Todo] = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select * from todo
        """
      ).as(simple *)
    }
  }

  def insert(todo: Todo) = {
    db.withConnection { implicit connection =>
      SQL(
        """
      insert into todo values ((select next value for todo_seq), {name})
        """
      ).on(
        'name -> todo.name
      ).executeUpdate()
    }
  }

  def findById(id: Long): Todo = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select * from todo where id = {id}
        """
      ).on(
        'id -> id
      ).as(simple.single)
    }
  }

  def update(todo: Todo) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          update todo set name = {name} where id = {id}
        """
      ).on(
        'name -> todo.name,
        'id -> todo.id
      ).executeUpdate()
    }
  }

  def delete(todo: Todo) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          delete from todo where id = {id}
        """
      ).on(
        'id -> todo.id
      ).executeUpdate()
    }
  }
}