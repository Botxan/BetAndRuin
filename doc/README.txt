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
- Sign-up
- Login
- Create a new event
- Set a fee

For each of these use cases, we have to:
- Create the use case diagram, including flow of events
- Update the domain model
- Draw the mockup GUI
- Implement the use case

List of task carried out during iteration:

**Compulsory**
- [x] Created the use case diagram and flow of events, including the four use cases aforementioned.
- [x] Updated the domain model with the necessary objects to manage users (User object) and forecasts (Forecast object).
- [x] Designed the mockups for the entire first iteration.
- [x] Implementation of the four use cases in form of GUIs, data accesses to the database and business logic intermediary methods. 

**Optional/Extras**
- [x] Project converted to Maven project.
- [x] Moved project to the repository root.
- [x] Design of our custom logo and favicon.
- [x] Added a welcome GUI. When the application is launched, this is the first window to be displayed. From here, the user can either login, register or directly browse questions.
- [x] Added navigation menu. This includes:
  * A button to go back to the previous window.
  * A menu to select the current language.
- [x] Security enhancement. This includes
  * String input validation. Use of regex and value formatters to force user to introduce valid data.
  * Hashed password. Passwords are stored using SHA-512 hashing algorithm + salt.