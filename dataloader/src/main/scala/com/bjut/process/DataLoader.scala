package com.bjut.process

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Movie 数据集
 *
 * 260                                         电影ID，mid
 * Star Wars: Episode IV - A New Hope (1977)   电影名称，name
 * 121 minutes                                 时长，timelong
 * September 21, 2004                          发行时间，issue
 * English                                     语言，language
 * Action|Adventure|Sci-Fi                     类型，genres
 * Mark Hamill|Harrison Ford|Carrie Fisher     演员表，actors
 * George Lucas                                导演，directors
 * Princess Leia is captured and held hostage  详情描述，descri
 */


/**
 * Rating数据集
 *
 * 1,31,2.5,1260759144
 */
case class Rating(uid: Int, mid: Int, score: Double, timestamp: Int )

/**
 *
 * @param uri MongoDB连接
 * @param db  MongoDB数据库
 */
case class MongoConfig(uri:String, db:String)

object DataLoader {

  val MONGODB_MOVIE_COLLECTION = "Movie"
  val MONGODB_RATING_COLLECTION = "Rating"
  val mgo_host = "root"
  val config = Map(
    "spark.cores" -> "local[*]",
    "mongo.uri" -> "mongodb://root:123456@62.234.220.250:27017/recommender",
    "mongo.db" -> "recommender"
  )

  val MOVIE_DATA_PATH = "F:\\1-project\\offline\\src\\main\\resources\\file\\movie.csv"
  val RATING_DATA_PATH = "F:\\1-project\\offline\\src\\main\\resources\\file\\ratings.csv"

  def storeDataInMongoDB(movieDF: DataFrame, ratingDF: DataFrame)(implicit mongoConfig: MongoConfig): Unit ={
    // 新建一个mongodb的连接
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))

    //    // 如果mongodb中已经有相应的数据库，先删除
        mongoClient(mongoConfig.db)(MONGODB_MOVIE_COLLECTION).dropCollection()
        mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).dropCollection()

    // 将DF数据写入对应的mongodb表中
    movieDF.write
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_MOVIE_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    ratingDF.write
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_RATING_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()


    //对数据表建索引
    mongoClient(mongoConfig.db)(MONGODB_MOVIE_COLLECTION).createIndex(MongoDBObject("mid" -> 1))
    mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).createIndex(MongoDBObject("uid" -> 1))
    mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).createIndex(MongoDBObject("mid" -> 1))

    mongoClient.close()

  }


  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("DataLoader")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._
    val movieRDD = spark.sparkContext.textFile(MOVIE_DATA_PATH)  // 加载数据
    val movieDF = movieRDD.map(
      item => {
        val attr = item.split(",")
        Movie(attr(0).toInt, attr(1).trim, attr(2).trim, attr(3).trim, attr(4).trim, attr(5).trim, attr(6).trim, attr(7).trim, attr(8).trim)
      }
    ).toDF()

    val ratingRDD = spark.sparkContext.textFile(RATING_DATA_PATH)

    val ratingDF = ratingRDD.map(item => {
      val attr = item.split(",")
      Rating(attr(0).toInt,attr(1).toInt,attr(2).toDouble,attr(3).toInt)
    }).toDF()

    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))
    // 将数据保存到MongoDB
    storeDataInMongoDB(movieDF, ratingDF)

    spark.stop()

  }




}
