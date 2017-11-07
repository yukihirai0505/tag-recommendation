package services

import models.SampleEntity

import scala.concurrent.{ExecutionContextExecutor, Future}

class SampleService(implicit ec: ExecutionContextExecutor) {

  def getSample: Future[Seq[SampleEntity]] = {
    Future successful Seq(
      SampleEntity(
        username = "hoge",
        email = "hoge@xxx.com"
      ),
      SampleEntity(
        username = "sage",
        email = "sage@xxx.com"
      )
    )
  }

}