package sample.reliable

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

object Main {

  def main(args: Array[String]): Unit = {

    args.headOption match {
      case Some(portString) if portString.matches("""\d+""") =>
        val cats = portString.toInt
        val httpPort = ("80" + portString.takeRight(2)).toInt
        startNode(cats, httpPort)
      case None =>
        throw new IllegalArgumentException("port number required")
    }
  }

  def startNode(port: Int, httpPort: Int): Unit = {
    val system = ActorSystem[Nothing](Guardian(), "Reliable", config(port, httpPort))
  }

  def config(port: Int, httpPort: Int): Config =
    ConfigFactory.parseString(
      s"""
      akka.remote.artery.canonical.port = $port
      shopping.http.port = $httpPort
       """).withFallback(ConfigFactory.load())
}

object Guardian {
  def apply(): Behavior[Nothing] = {
    Behaviors.setup[Nothing] { context =>
      val system = context.system
      Behaviors.empty
    }
  }
}
