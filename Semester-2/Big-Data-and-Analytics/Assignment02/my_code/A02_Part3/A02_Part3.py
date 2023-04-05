# --------------------------------------------------------
#
# PYTHON PROGRAM DEFINITION
#
# The knowledge a computer has of Python can be specified in 3 levels:
# (1) Prelude knowledge --> The computer has it by default.
# (2) Borrowed knowledge --> The computer gets this knowledge from 3rd party libraries defined by others
#                            (but imported by us in this program).
# (3) Generated knowledge --> The computer gets this knowledge from the new functions defined by us in this program.
#
# When launching in a terminal the command:
# user:~$ python3 this_file.py
# our computer first processes this PYTHON PROGRAM DEFINITION section of the file.
# On it, our computer enhances its Python knowledge from levels (2) and (3) with the imports and new functions
# defined in the program. However, it still does not execute anything.
#
# --------------------------------------------------------

# ------------------------------------------
# IMPORTS
# ------------------------------------------
import pyspark
import pyspark.sql.functions


# ------------------------------------------
# FUNCTION my_main
# ------------------------------------------
def my_main(spark, my_dataset_dir, bike_id):
    # 1. We define the Schema of our DF.
    my_schema = pyspark.sql.types.StructType(
        [pyspark.sql.types.StructField("start_time", pyspark.sql.types.StringType(), False),
         pyspark.sql.types.StructField("stop_time", pyspark.sql.types.StringType(), False),
         pyspark.sql.types.StructField("trip_duration", pyspark.sql.types.IntegerType(), False),
         pyspark.sql.types.StructField("start_station_id", pyspark.sql.types.IntegerType(), False),
         pyspark.sql.types.StructField("start_station_name", pyspark.sql.types.StringType(), False),
         pyspark.sql.types.StructField("start_station_latitude", pyspark.sql.types.FloatType(), False),
         pyspark.sql.types.StructField("start_station_longitude", pyspark.sql.types.FloatType(), False),
         pyspark.sql.types.StructField("stop_station_id", pyspark.sql.types.IntegerType(), False),
         pyspark.sql.types.StructField("stop_station_name", pyspark.sql.types.StringType(), False),
         pyspark.sql.types.StructField("stop_station_latitude", pyspark.sql.types.FloatType(), False),
         pyspark.sql.types.StructField("stop_station_longitude", pyspark.sql.types.FloatType(), False),
         pyspark.sql.types.StructField("bike_id", pyspark.sql.types.IntegerType(), False),
         pyspark.sql.types.StructField("user_type", pyspark.sql.types.StringType(), False),
         pyspark.sql.types.StructField("birth_year", pyspark.sql.types.IntegerType(), False),
         pyspark.sql.types.StructField("gender", pyspark.sql.types.IntegerType(), False),
         pyspark.sql.types.StructField("trip_id", pyspark.sql.types.IntegerType(), False)
         ])

    # 2. Operation C1: 'read' to create the DataFrame from the dataset and the schema
    inputDF = spark.read.format("csv") \
        .option("delimiter", ",") \
        .option("quote", "") \
        .option("header", "false") \
        .schema(my_schema) \
        .load(my_dataset_dir)

    # ------------------------------------------------
    # START OF YOUR CODE:
    # ------------------------------------------------

    # Type all your code here. Use auxiliary functions if needed.
    # Row(start_time, start_station_name, stop_time, stop_station_name)
    f = pyspark.sql.functions
    truck_station = f.lag('stop_station_name', 1).over(pyspark.sql.Window.orderBy('start_time'))
    truck_time = f.lag('stop_time', 1).over(pyspark.sql.Window.orderBy('start_time'))
    solutionDF = inputDF.where(f.col('bike_id') == bike_id)
    solutionDF = solutionDF.withColumn('truck_station', truck_station).withColumn('truck_time', truck_time)
    solutionDF = solutionDF.select(f.col('start_time'), f.col('start_station_name'), f.col('stop_time'), f.col('stop_station_name'), f.col('truck_station'), f.col('truck_time'))
    solutionDF = solutionDF.where(f.col('start_station_name') != f.col('truck_station'))
    solutionDF = solutionDF.select(f.col('truck_time'), f.col('truck_station'), f.col('start_time'), f.col('start_station_name'))\
        .withColumnRenamed('start_time', 'stop_time')\
        .withColumnRenamed('start_station_name', 'stop_station_name')\
        .withColumnRenamed('truck_time', 'start_time')\
        .withColumnRenamed('truck_station', 'start_station_name')

    # ------------------------------------------------
    # END OF YOUR CODE
    # ------------------------------------------------

    # Operation A1: 'collect' to get all results
    resVAL = solutionDF.collect()
    with open('../../my_results/Student_Solutions/A02_Part3/result.txt', 'w') as file:
        for item in resVAL:
            print(item)
            file.write(str(item)+'\n')


# --------------------------------------------------------
#
# PYTHON PROGRAM EXECUTION
#
# Once our computer has finished processing the PYTHON PROGRAM DEFINITION section its knowledge is set.
# Now its time to apply this knowledge.
#
# When launching in a terminal the command:
# user:~$ python3 this_file.py
# our computer finally processes this PYTHON PROGRAM EXECUTION section, which:
# (i) Specifies the function F to be executed.
# (ii) Define any input parameter such this function F has to be called with.
#
# --------------------------------------------------------
if __name__ == '__main__':
    # 1. We use as many input arguments as needed
    bike_id = 35143

    # 2. Local or Databricks
    local_False_databricks_True = False

    # 3. We set the path to my_dataset and my_result
    my_dataset_dir = "../../my_datasets/my_dataset/"
    if local_False_databricks_True == True:
        my_dataset_dir = "/FileStore/tables/Assignments/NYC/my_dataset/"

    # 4. We configure the Spark Session
    spark = pyspark.sql.SparkSession.builder.getOrCreate()
    spark.sparkContext.setLogLevel('WARN')

    # 5. We call to our main function
    my_main(spark, my_dataset_dir, bike_id)
