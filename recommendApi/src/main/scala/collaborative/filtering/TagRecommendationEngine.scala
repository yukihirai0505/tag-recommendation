package collaborative.filtering

import java.util.Scanner

import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class TagRecommendationEngine {

  val conf = new SparkConf().setAppName("Recommendation App").setMaster("local")
  val sc = new SparkContext(conf)
  val directory = "src/main/resources"
  val scanner = new Scanner(System.in)
  val numPartitions = 20
  val topTenTags = getRatingFromUser

  def getRatingRDD: RDD[String] = {

    sc.textFile(directory + "/ratings.csv")
  }

  def getTagRDD: RDD[String] = {

    sc.textFile(directory + "/tags.csv")
  }

  def getRDDOfRating: RDD[(Int, Rating)] = {

    getRatingRDD.map { line =>
      val fields = line.split(",")
      (fields(2).toInt % 2, Rating(fields(0).toInt, fields(1).toInt, fields(2).toInt))
    }
  }

  def getTagsMap: Map[Int, String] = {

    getTagRDD.map { line =>
      val fields = line.split(",")

      (fields(0).toInt, fields(1))
    }.collect().toMap
  }

  def getRatingFromUser: RDD[Rating] = {
    val tagList = (1 to 10).map { _ =>
      println("Please Enter hash tag")
      val hashTag = scanner.next()
      Rating(0, getTagsMap.find(v => v._2.equals(hashTag)).fold(0)(x => x._1), 100)
    }
    sc.parallelize(tagList)
  }

}

object TagRecommendationEngine extends App {

  val tagRecommendationHelper = new TagRecommendationEngine
  val sc = tagRecommendationHelper.sc
  // Load and parse the data
  val ratings = tagRecommendationHelper.getRatingRDD.map(_.split(",") match {
    case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toInt)
  })
  val tags = tagRecommendationHelper.getTagRDD.map { str =>
    val data = str.split(",")
    (data(0), data(1))
  }.map { case (tagId, tagName) => (tagId.toInt, tagName) }

  val myRatingsRDD = tagRecommendationHelper.topTenTags
  val training = ratings.filter { case Rating(userId, tagId, rating) => (userId * tagId) % 10 <= 3 }.persist
  val test = ratings.filter { case Rating(userId, tagId, rating) => (userId * tagId) % 10 > 3 }.persist

  val model = ALS.train(training.union(myRatingsRDD), rank = 10, 10, 0.01)

  val TagsIHaveSeen = myRatingsRDD.map(x => x.product).collect().toList

  val TagsIHaveNotSeen = tags.filter { case (tagId, name) => !TagsIHaveSeen.contains(tagId) }.map(_._1)

  val predictedRates =
    model.predict(test.map { case Rating(user, item, rating) => (user, item) }).map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }.persist()

  val ratesAndPreds = test.map { case Rating(user, product, rate) =>
    ((user, product), rate)
  }.join(predictedRates)

  val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) => Math.pow(r1 - r2, 2) }.mean()

  println("Mean Squared Error = " + MSE)

  val recommendedTagsId = model.predict(TagsIHaveNotSeen.map { product =>
    (0, product)
  }).map { case Rating(user, tag, rating) => (tag, rating) }
    .sortBy(x => x._2, ascending = false).take(20).map(x => x._1)

  val recommendTag = tagRecommendationHelper.getTagRDD.map { str =>
    val data = str.split(",")
    (data(0).toInt, data(1), data(2).toInt)
  }.filter { case (id, _, _) => recommendedTagsId.contains(id) }

  recommendTag.collect().toList.foreach(println)
  tagRecommendationHelper.sc.stop()
}