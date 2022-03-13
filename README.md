<h1 align="center">BetAndRuin - 2022</h1>
<div align="center">
  <img width="200" src="https://user-images.githubusercontent.com/33251573/158073590-35b9dca0-3d7f-44ae-b712-48a151511763.jpg">
</div>
<p align="center"><i>BetAndRuin is a betting application developed for the Software Engineering (SI) course.</i></p>

---

## Authors
#### Josefinators
- [Leire Insausti González](https://github.com/LeireInsausti)
- [Oihan Irastorza Carrasco](https://github.com/Botxan)
- [Eneko Pizarro Liberal](https://github.com/itsNko)
- [Aritz Plazaola Cortabarria](https://github.com/Poxito)
- [Pablo Tagarro Melón](https://github.com/pablobec93)

---

## Project development
- [0<sup>th</sup> Iteration. Project setup](#0th-iteration-project-setup)
- [1<sup>st</sup> Iteration](#1st-iteration)

### 0<sup>th</sup> Iteration. Project setup
The initial scenario proposed to us is a menu where any user can both create questions and query them.
<div align="center">
  <img width="500" src="https://user-images.githubusercontent.com/33251573/158075109-c6d28f47-cb27-455d-aec2-c570f4de6e66.PNG">
</div>
At this starting point, there is still no distinction between users, there are no use cases defined beyond CreateQuestion and BrowseQuestions. The application is composed by 3 different interfaces, MainMenuGUI, CreateQuestionGUI and BrowseQuesionsGUI.

### 1<sup>st</sup> Iteration.
For this first iteration, we are asked to define the requirements of the application, based on 4 initial use cases:
- [Register](../../issues/1)
- [Login](../../issues/7)
- [Create a new event](../../issues/3)
- [Set a fee](../../issues/2)

For each of these use cases, we have to:
- Create the use case diagram, including flow of events
- Update the domain model
- Draw the mockup GUI
- Implement the use case

List of task carried out during iteration:

**Compulsory**
- [x] [Created the use case diagram and flow of events, including the four use cases aforementioned](https://github.com/Botxan/betandruin2022/blob/main/doc/Sprint_1_-_UML.mdj).
- [x] Updated the domain model with the necessary objects to manage users (User object) and forecasts (Forecast object). See [User](https://github.com/Botxan/betandruin2022/commit/d0b0a829f9ede27986d7ec740cbf4d76e5640549) and [Forecast](https://github.com/Botxan/betandruin2022/commit/c5d42a82c7fc00e596abebded994a0c8d1e9bb3b).
- [x] Designed the [mockups](https://github.com/Botxan/betandruin2022/blob/main/doc/mockupGUIs.png) for the entire first iteration.
- [x] Implementation of the four use cases in form of GUIs, data accesses to the database and business logic intermediary methods. See: 
  * [Register](https://github.com/Botxan/betandruin2022/commit/d08abeb3d4dba5080d03f6503a69fefe40543b06)
  * [Login](https://github.com/Botxan/betandruin2022/commit/7117dd65d6d19a75a483e712881cb7962674dd3a)
  * [Create event](https://github.com/Botxan/betandruin2022/commit/64fac94b11fd676e30d456bdceb07e0c8cd915f2)
  * [Set a fee](https://github.com/Botxan/betandruin2022/commit/9ca3db2bbdd0fe8590cf121f3f4f92e7ea87bd43)

**Optional/Extras**
- [x] [Project converted to Maven project](https://github.com/Botxan/betandruin2022/commit/19da679b707f0cdbf59fd913d4af5fc80516877d).
- [x] [Moved project to the repository root](https://github.com/Botxan/betandruin2022/commit/e3ec7b7e072f721992d54f9e9cb267e6a4d69bb9).
- [x] Design of our custom logo and favicon. See [logo](https://github.com/Botxan/betandruin2022/blob/main/resources/final_logo.png) and [favicon](https://github.com/Botxan/betandruin2022/blob/main/resources/favicon.png).
- [x] Added a [welcome GUI](https://github.com/Botxan/betandruin2022/commit/43366fb3ab0e3f47a38b00df7e77b3e35f295681). When the application is launched, this is the first window to be displayed. From here, the user can either login, register or directly browse questions.
- [x] Added navigation menu. This includes:
  * A [button to go back to the previous window](https://github.com/Botxan/betandruin2022/commit/6e5fe69237f2b31b8b3dc09385dd4ff6c482f415).
  * A [menu to select the current language](https://github.com/Botxan/betandruin2022/commit/585ca03d16bd487f0408b5a83e3e5879268e0022).
- [x] Security enhancement. This includes
  * String input validation. Use of [regex](https://github.com/Botxan/betandruin2022/commit/f95ac922f7d3c06421327b9052e07b08fafa72d6) and value formatters to force user to introduce valid data.
  * [Hashed password](https://github.com/Botxan/betandruin2022/commit/e988ffead8451d1d4eeb8cb9aa445fe67ca05fbd). Passwords are stored using SHA-512 hashing algorithm + salt.
