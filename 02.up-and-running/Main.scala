import scala.concurrent.{Future, ExecutionContext }
import scala.util.{Try, Success, Failure}

import com.typesafe.config.{ConfigFactory, Config}

import akka.actor.{ActorSystem}

import akka.stream.ActorMaterializer

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.{Route}



//object TicketSeller
//class  TicketSeller

//class RestApi extends RestRoutes
//trait RestRoutes extends BoxOfficeApi

object Main extends App {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher

  val route = (new RestApi(system)).routes

  implicit val materializer = ActorMaterializer()
  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(route, host, port)

  bindingFuture onComplete {
    case Success(x) => println(s"Success: $x")
    case Failure(e) => println(s"Exception: $e")
  }


}
