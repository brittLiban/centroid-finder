Liban 5.19

The plan

1 Find the right video library (openCV)
-   It needs to be able to process videos frame by frame
-   We need to be able to process our logic on each frame

2 Take the video in and process it in the main file - This will also contain a way to pass in command line args such as java -jar videoprocessor.jar inputPath outputCsv targetColor threshold 
2.1 Pass it along to a helper class that will than process it down frame by frame
2.2 Pass it along to another helper class that will run the logic to analyze the biggest salamender then produce the infromation of "The first column will be the number of seconds since the beginning of the video, and the second and third columns will be the x and y coordinates of the largest found centroid at that time. " all to a csv file

3. Done? 