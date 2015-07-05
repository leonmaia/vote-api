package com.mystique.service.tracing

import com.mystique.server.ApiConfig
import com.twitter.finagle.RequestTimeoutException
import com.twitter.finagle.tracing.{Annotation, Trace}
import com.twitter.finagle.util.DefaultTimer
import com.twitter.logging.Logger
import com.twitter.util.Duration._
import com.twitter.util.{Duration, Future}

trait Tracing {

  val log = Logger(getClass)

  def withTrace[T](id: String, protocol: String = "custom", timeout: Duration = fromSeconds(1)) (block: => Future[T]) = {
    if (ApiConfig.isDev) Trace.traceService(id, protocol, Option.empty) {
      Trace.record(Annotation.ClientSend())
      val time = System.currentTimeMillis()
      withTimeout(id, timeout, block) map { res =>
        log.info(s"$id - Mystique API took ${System.currentTimeMillis() - time}ms to respond")
        Trace.record(Annotation.ClientRecv())
        res
      }
    } else block
  }

  private[tracing] def withTimeout[T](id: String = s"${getClass.getSimpleName}", duration: Duration, block: => Future[T]): Future[T] = {
    block.within(
    DefaultTimer.twitter,
    duration, {
      Trace.record(s"$id.timeout")
      new RequestTimeoutException(duration, s"Timeout exceed while accessing using $id")
    }
    )
  }
}

