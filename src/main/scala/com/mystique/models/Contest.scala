package com.mystique.models

import com.mystique.util.DateSupport._

case class Contest(slug: String,
                   name: String,
                   description: Option[String],
                   startDate: String,
                   endDate: String ) {
  
  require(slug.nonEmpty, "slug is required")
  require(slug.length > 2 && slug.length < 21, "slug is maximum 20 and minimum 3")

  require(name.nonEmpty, "name is required")
  require(name.length > 2 && name.length < 11, "name is maximum 10 and minimum 3")

  require(description.getOrElse("").length < 256, "description should be maximum 255")

  require(startDate.nonEmpty, "start_date is required")
  require(isValidDate(startDate), "start_date should be a date")

  require(endDate.nonEmpty, "end_date is required")
  require(isValidDate(endDate), "end_date should be a date")

  require(fmt.parse(startDate).before(fmt.parse(endDate)), "start_date should be before end_date")
}

