package org.nefilim.hipchatclient

import com.typesafe.scalalogging.slf4j.LazyLogging
import spray.http._
import scala.concurrent.{ExecutionContext, Future}
import spray.client.pipelining._
import akka.actor.{ActorRefFactory, ActorContext, ActorSystem}
import spray.http.Uri.Query
import spray.http.HttpRequest
import scala.Some
import spray.http.HttpResponse
import org.nefilim.hipchatclient.HipChatResources._
import spray.httpx.marshalling.Marshaller

/**
 * Created by peter on 4/30/14.
 */

object HipChatClient {
  val serviceBasePath = "/v2"

  case class HipChatClientFailedResult(response: HttpResponse, exception: Option[Throwable] = None, message: Option[String] = None)

  def apply(context: ActorRefFactory, apiToken: String, host: String = "api.hipchat.com"): HipChatClient = new ConfiguredHipChatClient(apiToken, host, context)
}

import HipChatClient._

trait HipChatClient {
  def sendRoomNotification(room: String, message: String, notify: Boolean = true): Future[Either[HipChatClientFailedResult, MessageStatus]]
}

class ConfiguredHipChatClient(apiToken: String, host: String, implicit val context: ActorRefFactory) extends HipChatClient with LazyLogging {

  import context.dispatcher

  private val includedQueryParameters = Map[String, String](("format" -> "json"), ("auth_token" -> apiToken))

  private[hipchatclient] def logTheRequest(request: HttpRequest) {
    logger.debug("the HTTP request {}", request)
  }

  private[hipchatclient] def logServerResponse(response: HttpResponse) {
    logger.debug("the HTTP response {}", response)
  }

  private[hipchatclient] lazy val pipeline: HttpRequest => Future[HttpResponse] = (
      logRequest(logTheRequest _)
      ~> sendReceive
    )

  def sendRoomNotification(room: String, message: String, notify: Boolean = true): Future[Either[HipChatClientFailedResult, MessageStatus]] = {
    val futureResponse = fireRequest[NotificationRequest](s"/room/$room/notification", body = Some(NotificationRequest(message, notification = notify)), method = HttpMethods.POST)
    futureResponse.map { response =>
      response match {
        case r if r.status.isSuccess =>
          Right(MessageStatus(r.status.reason))
        case _ =>
          logger.error("the request failed {}", response)
          Left(HipChatClientFailedResult(response, None))
      }
    }
  }

  private[hipchatclient] def uri(scheme: String = "https", path: String, port: Int = 443, query: Query) = {
    Uri.from(
      scheme = scheme,
      host = host,
      port = port,
      path = path,
      query = query
    )
  }

  private[hipchatclient] def fireRequest[T: Marshaller](
      requestPath: String,
      query: Map[String, String] = Map.empty[String, String],
      body: Option[T] = None,
      method: HttpMethod = HttpMethods.GET): Future[HttpResponse] = {

    method match {
      case HttpMethods.GET =>
        pipeline(Get(uri(path = s"$serviceBasePath$requestPath", query = Query(query ++ includedQueryParameters))))
      case HttpMethods.POST =>
        pipeline(Post(uri(path = s"$serviceBasePath$requestPath", query = Query(query ++ includedQueryParameters)), body))
    }
  }
}

