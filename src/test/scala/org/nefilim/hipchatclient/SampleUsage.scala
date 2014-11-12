package org.nefilim.hipchatclient

import com.typesafe.scalalogging.slf4j.LazyLogging
import spray.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, Matchers}
import scala.util.{Failure, Success}
import scala.concurrent.Await
import scala.language.postfixOps
import scala.concurrent.duration._

/**
 * Created by peter on 4/30/14.
 */
class SampleUsageSpec extends FunSpec with Matchers with ScalatestRouteTest with LazyLogging {
  def actorRefFactory = system

  describe("sample usage") {
    it("should work") {
      val hipChatClient = HipChatClient(system, "sekretkeyhere")

      val f2 = hipChatClient.sendRoomNotification("DevOps", "this is a test")
      f2.onComplete {
        case Success(result) =>
          logger.info("notification succeeded {}", result)
        case Failure(f) =>
          logger.error("failed to send notification", f)
      }

      Await.result(f2, (30 seconds))
    }
  }


}
