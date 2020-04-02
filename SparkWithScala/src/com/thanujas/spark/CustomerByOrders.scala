package com.thanujas.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

object CustomerByOrders {
  
  /** A function that splits a line of input into (age, numFriends) tuples. */
  def parseLine(line: String) = {
      // Split by commas
      val fields = line.split(",")
      // Extract the id and price fields, and convert to integer and float
      val id = fields(0).toInt
      val price = fields(2).toFloat
      // Create a tuple that is our result.
      (id, price)
  }
  
  /** Our main function where the action happens */
  def main(args: Array[String]) {
   
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
     // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "CustomerByOrders")   
    
    // Load each line of my book into an RDD
    val input = sc.textFile("../customer-orders.csv")
    
    // Use our parseLines function to convert to (id, price) tuples
    val rdd = input.map(parseLine)
    
    val totalsAmount = rdd.reduceByKey((x,y)=> x+y)
    
    
     val finalResults = totalsAmount.map( x => (x._2, x._1) )
     val sortedResults=finalResults.sortByKey()
     val results=sortedResults.collect()
     
      // Sort and print the final results.
    results.foreach(println)
    
  }
  
}