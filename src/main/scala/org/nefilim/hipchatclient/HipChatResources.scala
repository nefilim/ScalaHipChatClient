package org.nefilim.hipchatclient

import org.json4s.{CustomSerializer, NoTypeHints, DefaultFormats}
import spray.http.HttpEntity
import spray.httpx.marshalling.Marshaller
import org.json4s.jackson.Serialization._
import spray.http.ContentTypes._
import org.json4s.jackson.Serialization
import org.json4s.JsonAST.{JString, JObject}

/**
 * Created by peter on 4/30/14.
 */
object HipChatResources {
  sealed trait MessageColour { val value: String }
  case object Yellow extends MessageColour { override val value = "yellow" }
  case object Red extends MessageColour { override val value = "red" }
  case object Green extends MessageColour { override val value = "green" }
  case object Purple extends MessageColour { override val value = "purple" }
  case object Gray extends MessageColour { override val value = "gray" }
  case object Random extends MessageColour { override val value = "random" }

  case class MessageStatus(status: String)

  case class NotificationRequest(message: String, color: MessageColour = Yellow, notification: Boolean = false, message_format: String = "html")


  class MessageColourSerializer extends CustomSerializer[MessageColour](format => (
    {
      case _ => Yellow // TODO no support for deserializing now
    },
    {
      case x: MessageColour =>
        JString(x.value)
    }
    ))

  implicit val formats = Serialization.formats(NoTypeHints) + new MessageColourSerializer

  implicit val notificationRequestMarshaller = Marshaller.of[NotificationRequest](`application/json`) {
    (value, ct, ctx) => ctx.marshalTo(HttpEntity(ct, write(value)))
  }


}
