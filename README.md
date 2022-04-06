## Description of project

This application is a quiz game about energy consumption that allows users to increase their knowledge about the energy usage of different activities. It was created collaboratively for the course 'CSE1105 OOP Project' in association with the [Delft Energy Initiative](https://www.tudelft.nl/en/energy) that aims to educate about energy usage to encourage more sustainable lifestyles.

The quiz consists of a single player and a multiplayer game mode in which 20 questions are generated that test players about the consumption of activities in the database. In the single player mode, 20 questions are asked and the player has 10 seconds time to answer the question. Then the correct answer is shown and the user receives a score based on whether their answer was correct and how fast they answered. After 20 rounds, the player receives a final score that is displayed in the single player leaderbord.

In the multiplayer game, multiple players can participate in one game by entering the IP address of the server they want to play on. This game works like the single player game, but additionally emotes, jokers and further leaderboards are available. The emotes can be used to share emotions with other players and the jokers are used to gain advantages throughout the game. There are three available jokers: a joker that doubles the player's points, a joker that halves the time and a joker that removes a wrong answer. After playing 10 rounds the players see their current scores in an intermediate leaderboard and after finishing the 20 rounds, the players can compare their final scores in a final leaderboard.

Enjoy the game!

## Group members

| Profile Picture | Name | Email                            |
|---|---|----------------------------------|
| ![](https://secure.gravatar.com/avatar/b4b2377134a5db565b9ee30727daa206?s=180&d=identicon&size=50) | Per Skullerud | P.M.Skullerud@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/5ec558ba962f139a04572dbb90051cf9?s=180&d=identicon&size=50) | Krzysztof Muniak | K.Muniak@student.tudelft.nl      |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4762/avatar.png?width=400" height=50 width=50> | Irina-Ioana Marinescu | I.I.Marinescu@student.tudelft.nl |
|<img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4522/avatar.png?width=400" width="50" height="50"> | Andrei Visoiu | A.I.Visoiu@student.tudelft.nl    |
| <img src="https://secure.gravatar.com/avatar/ecda8f54079e20a5f22fc7b4c1dae20f?s=180&d=identicon" width="50" height="50"> | Yannick Zuurbier | Y.R.Zuurbier@student.tudelft.nl  |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4626/avatar.png?width=400" width="50" height="50"> | Mirella Günther | M.Gunther-1@student.tudelft.nl   |

## Software requirements

* Java 11+
* Postman
* Gradle 7.3+
* IDE of your choice

## How to run it

To run the game, you first need to download the repository, which can be found at https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/team-repositories/oopp-group-26/repository-template, as a zip file. You should then unzip the file and navigate to the location where the downloaded file is located via the terminal. 
Alternatively, if you want to use the git command line, you can do, if you want to clone with SSH:

`
git clone git@gitlab.ewi.tudelft.nl:cse1105/2021-2022/team-repositories/oopp-group-26/repository-template.git
`

Or with HTTPS:
`
https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/team-repositories/oopp-group-26/repository-template.git
`
From here, you can use the commands `gradle build` to build the project, `gradle bootRun` to start the server-side and `gradle run` to start the client-side.

Before you can really start to play, you need to import the activities and the images that are to be used in the application. We recommend downloading the zip file 'ZIP archive with activities and images' found at `https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/activity-bank/-/releases`. Make sure that the server is running in the background by using the `gradle bootRun` command. Whilst the server is running, use Postman and run it as POST with `localhost:8080/api/activities/import` as the link and the downloaded activities.json file as a raw body. This will import the activities including their titles, their consumptions, their source and their image pathways into the database. Additionally, the images have to be imported separately: the numbered folders in the downloaded zip folder have to manually be imported into the directory with the path 'server/images'.

Now the game is ready to be run. Ideally, it is run on a display with a 1600 x 900 resolution, as this allows the full screens to be shown. To then run game itself, you can again use the `gradle bootRun` and `gradle run` commands.

## How to contribute to it

To contribute to the application, you can clone the Git repository, create a branch and then create a merge request to merge the changes into main. This merge request has to be approved by two team members, to ensure that the changes are useful and implemented well.
