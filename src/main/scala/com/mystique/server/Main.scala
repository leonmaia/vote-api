package com.mystique.server

import java.net.InetSocketAddress

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.{Request, _}
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
import com.twitter.util.Await

object Server extends Router {

  def main() {
    HttpMuxer.addRichHandler("/", router)

    ServerBuilder()
      .codec(RichHttp[Request](Http.get().enableTracing(false)))
      .bindTo(new InetSocketAddress("0.0.0.0", 8088))
      .name("Mystique API")
      .build(router)

    Await.all(adminHttpServer)
  }
}

