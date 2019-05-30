import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class EventDescription(tickets: Int) {
  require(tickets > 0)
}
case class Error(message: String)

trait EventMarshalling extends SprayJsonSupport with DefaultJsonProtocol {
 
  implicit val eventDescFormat = jsonFormat1(EventDescription) 
  implicit val errorFormat = jsonFormat1(Error) 
  implicit val eventFormat = jsonFormat2(BoxOffice.Event)
  implicit val eventsFormat = jsonFormat1(BoxOffice.Events) 
}
