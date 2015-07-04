package com.mystique.models

case class Contest(slug: String,
                   name: String,
                   description: Option[String],
                   startDate: String,
                   endDate: String ) {}


