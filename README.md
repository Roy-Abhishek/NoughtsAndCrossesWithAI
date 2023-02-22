# NoughtsAndCrossesWithAI

This is my first big project as a Java developer that I made in November of 2022. I had been coding in Java for only the past 5 months but I guess the experience from Python and other languages helped. All you have to do to play is run the Java file. Everything else has been done for the user (I focused on making a good UI).

The algorithm used to make the AI is a Mini-Max Algorithm. This algorithm works recursively to assign a number to every way that the game can happen. The path with the number most in favor of the player (Nought or Cross) playing as the AI will be the path the AI chooses to take. Since details of the Mini-Max algorithm are difficult to explain, here's the YouTube video I used to get a start to making the algorithm - https://youtu.be/l-hh51ncgDI.

Of course, there is still room to grow. You may notice that the code lacks enough comments. The reason for that is I was in a hurry when writing the code and never really had the time to add the necessary comments. Another area I can improve upon would be the efficiency of the algorithm itself. As the code is right now, the AI does the calculations for all possible variations of the game every time it's the AI's turn to play. The way to improve upon this would be to store the variations in memory the first time the calculations are made. Then, the AI can reference that memory instead of calculating all the possible variations every time. I will try and make commits to the repository to fix these errors when I can.


(PS: There are certain K-Pop easter eggs hidden in the code and the game itself. Try and find them.)
