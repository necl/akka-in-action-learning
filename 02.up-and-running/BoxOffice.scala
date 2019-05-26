import akka.actor.{Actor, Progs}

object BoxOffice {
  def progs(): Progs = Progs(new BoxOffice())
}

class BoxOffice extends Actor {
}

