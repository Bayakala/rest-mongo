package com.datatech.rest.mongo
import org.mongodb.scala._
import com.datatech.sdp.mongo.engine._
import MGOClasses._

object MongoModels {

  case class Person(
                   userid: String = "",
                   name: String = "",
                   age: Option[Int] = None,
                   dob: Option[MGODate] = None,
                   address: Option[String] = None
                   ) extends ModelBase[Document] {
    import org.mongodb.scala.bson._

    override def to: Document = {
      var doc = Document(
      "userid" -> this.userid,
      "name" -> this.name)


      if (this.age != None)
        doc = doc + ("age" -> this.age.get)

      if (this.dob != None)
        doc = doc + ("dob" -> this.dob.get)

      if (this.address != None)
        doc = doc + ("address" -> this.address.getOrElse(""))

      doc
    }

  }
  object Person {
    val fromDocument: Document => Person = doc => {
      val keyset = doc.keySet
      Person(
        userid = doc.getString("userid"),
        name = doc.getString("name"),
        age = mgoGetIntOrNone(doc,"age").asInstanceOf[Option[Int]],

        dob =  {if (keyset.contains("dob"))
          Some(doc.getDate("dob"))
        else None },

        address =  mgoGetStringOrNone(doc,"address")
      )
    }
  }

  case class Photo (
                     id: String,
                     loc: Option[String],
                     size: Option[Int],
                     credt: Option[MGODate],
                     photo: Option[MGOBlob]
                   ) extends ModelBase[Document] {
    override def to: Document = {
      var doc = Document("id" -> this.id)
      if (loc != None)
        doc = doc + ("loc" -> this.loc.get)
      if (size != None)
        doc = doc + ("size" -> this.size.get)
      if (credt != None)
        doc = doc + ("credt" -> this.credt.get)
      if (photo != None)
        doc = doc + ("photo" -> this.photo)
      doc
    }
  }

  object Photo {
    def fromDocument: Document => Photo = doc => {
      Photo(
        id = doc.getString("id"),
        loc = mgoGetStringOrNone(doc,"loc"),
        size = mgoGetIntOrNone(doc,"size").asInstanceOf[Option[Int]],
        credt = mgoGetDateOrNone(doc,"credt"),
        photo = mgoGetBlobOrNone(doc, "photo")
      )
    }
  }


}

