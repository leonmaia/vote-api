package com.mystique.models

case class Candidate(id: Int,
                     name: String,
                     avatar: Option[String] = None) {

  require(name.nonEmpty, "name is required")
  require(name.length > 2 && name.length < 11, "name is maximum 10 and minimum 3")

  require(avatar.nonEmpty, "name is required")
  require(avatar.getOrElse("").length < 256, "avatar should be maximum 255")
}

