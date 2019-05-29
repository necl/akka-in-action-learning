import scala.concurrent.{Future}
import scala.util.{Success, Failure}

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import java.util.concurrent.TimeUnit

import akka.http.scaladsl.server.{Route}
import akka.http.scaladsl.server.Directives._

class RestApi(system: ActorSystem) extends BoxOfficeApi  {
  def routes: Route = 
      pathPrefix("events") {
        pathEndOrSingleSlash {
          get{
            onComplete(getEvents()) { 
              case Success(x) => complete(s"result: ${x}\n")
              case Failure(e) => complete(s"exception: ${e}\n")
            }
          }
        }
      } ~
      pathPrefix("events" / Segment ) { event => 
        pathEndOrSingleSlash {
          post {
              complete(s"post $event  to be implement...\n")
          } ~
          get {
              complete(s"get $event  to be implement...\n")
          } ~
          delete {
              complete(s"delete $event  to be implement...\n")
          }
        }
      } ~
      pathPrefix("events" / Segment / "tickets" ) { event =>
        pathEndOrSingleSlash {
          post {
              complete(s"post $event tickets  to be implement...\n")
          }
        }
      }
    def createBoxOffice(): ActorRef  = system.actorOf(BoxOffice.props())
    val requestTimeout = Timeout(2, TimeUnit.SECONDS)
}

trait BoxOfficeApi {
  import BoxOffice._

  def createBoxOffice(): ActorRef
  lazy val boxOffice: ActorRef = createBoxOffice()
  implicit def requestTimeout: Timeout  //ask 需要

  def getEvents(): Future[String] = boxOffice.ask(GetEvents).mapTo[String]
}

