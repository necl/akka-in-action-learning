
import akka.actor.ActorRef

import akka.http.scaladsl.server.{Route}
import akka.http.scaladsl.server.Directives._

class RestApi extends BoxOfficeApi  {
  def routes: Route = 
      pathPrefix("events") {
        pathEndOrSingleSlash {
          get{
            complete("to be implement...\n")
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
    def createBoxOffice(): ActorRef  = context.actorOf(BoxOffice.progs())
}

trait BoxOfficeApi {
  def createBoxOffice(): ActorRef
  lazy val boxOffice: ActorRef = createBoxOffice()

  //def getEvents = 
}
