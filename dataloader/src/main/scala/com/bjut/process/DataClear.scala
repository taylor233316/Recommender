package com.bjut.process

import org.apache.spark
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}


case class Movie(mid: Int, title: String, timelong: String, shoot: String,
                 language: String, genres: String, actors: String, directors: String, desc: String)

object DataClear {
  def main(args: Array[String]): Unit = {

    // 1. 连接spark
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("DataLoader")
    // 创建一个SparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext

    import spark.implicits._

    // 2.加载movie-en/movie-ch和rating数据
    val MOVIE_CH_DATA_PATH = "F:\\1-project\\offline\\src\\main\\resources\\file\\movie.csv"
    val MOVIE_EN_DATA_PATH = "F:\\1-project\\offline\\src\\main\\resources\\file\\movies-en.csv"
    val RATING_DATA_PATH = "F:\\1-project\\offline\\src\\main\\resources\\file\\rating.csv"

    val movieCHRDD = sc.textFile(MOVIE_CH_DATA_PATH)
    val movieENRDD = sc.textFile(MOVIE_EN_DATA_PATH)
    val ratingRDD = sc.textFile(RATING_DATA_PATH)


    val movieCHDF = movieCHRDD.map(
      item => {
        val attr = item.split("\\^")
        // id,title,desc,minute,year,year,language,geners,actors,director
        Movie(attr(0).toInt, attr(1).trim, attr(3).trim, attr(4).trim, attr(6).trim, attr(7).trim, attr(8).trim, attr(9).trim,attr(2).trim)
      }
    )


    spark.createDataFrame(movieCHDF.take(2791)).toDF().repartition(1)
        .write.format("csv")
        .option("header", "false")
        .save("F:\\1-project\\offline\\src\\main\\resources\\file\\movie-ch1.csv")

    sc.stop()



  }

}
