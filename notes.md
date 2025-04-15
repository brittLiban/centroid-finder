- Liban Notes 
4/14
Image Summary App
    -   main

            if(args.length < 3)
        This is going to be reading in the args - meaning the command in the console from the user, if it is less then the length 3 it will output something

            - the main has 3 main arguments its looking for
                - The input image location
                - the hex target color it is looking for
                - an integer for binarization - idk what that means 

        It is going to create 2 string variables 
        -   The first is going to be to store the image path
            inputImagePath
        - The second is going to be to store the target color
            hexTargetColor
        - It is going to create a third int variable for the threshold which again is going to be the 3rd argument from the command line via a try catch 

        It then goes on to make a BufferedImage variable
            -   From my understading, this is making the picture into a plane where each pixel or some portion of the picture is broken down into coordinates. 
        
        With the new BufferedImage var called inputImage
            It goes onto read the new image from the inputImagePath variable we defined which is sourced from the user input in the argument command. This is also done in a try catch 
    
        int target is created and generated our hexacode Number
            This is done by doing a parseInt passing in the hexTargetColor variable which is sourced from the user input and a arguemnt and as a second parameter the radix of 16.
            
            A radix is essentially defining what numbering system or base we want to convert it to. Since we want hexacode, we will use a radix of 16. If we wanted a binary base we would use a number like 2

        It creates a new ColorDistanceFinder variable called distanceFinder that is build on the EuclideanColorDistance

ColorDistanceFinder
    This is a interface that defines the distance between two different colors. They are taken in as parameters
EuclideanColorDistance
    This will be implementing ColorDistanceFinder and overides the inherited method

    This is going to be taking in TWO hexacodes and doing some logic to ID the difference between two different hexacode colors using bitwise operations?


