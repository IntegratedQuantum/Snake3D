# Snake3D
In Snake3D you can play snake on the surface of a 3d-cube.

### Background

At some point in high school [@blackedout01](https://github.com/blackedout01) and I challenged each other to make a 3D snake game over the weekend because we had already mastered the 2D snake game. Even before showing each other the results in class, we talked a bit about our controls and were both left confused. The presentation was quite a surprise. Turns out, we had totally different ideas of what 3D snake actually means. [@blackedout01](https://github.com/blackedout01) made the snake move inside of a cube by extending the game field into the third dimension (you can find his version [here](https://github.com/blackedout01/Snake3D)) and I made the snake move on the surface of a cube:

![Screenshot from 2019-07-24 23-08-18](https://user-images.githubusercontent.com/43880493/61828980-3c1ee400-ae68-11e9-8c69-db7f646239a4.png)

### Features
→ two gamemodes:
1. normal(like the old snake, but on a cube)
2. hard(like normal + every time you eat a radom border appears somewhere on the cube, so be careful)

→ two levels

→ no external libraries needed(Snake3D has already everything on board it needs.)

### How to build
1. Download the source code here on github.
2. Extract the .zip file.
3. Compile the content of src. This can be done with `javac *.java` in the terminal **OR** by importing the code to any IDE you want and compile it from there.

### How to play
#### Self-compiled:
Press run in your IDE **OR** type `java main` in your terminal
#### Official version:
Go to [releases](https://github.com/IntegratedQuantum/Snake3D/releases) and download any version of Snake3D. Double click the .jar file and you are good to go.
#### Game-play:
You can change game mode/level with `g` and `l` or using your mouse, start the game with `space` or click on `Start`. You can turn the cube with `wasd` and move the snake with `←` and `→`
