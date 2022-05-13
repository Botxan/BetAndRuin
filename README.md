<h1 align="center">BetAndRuin - 2022</h1>
<div align="center">
  <img width="200" src="https://user-images.githubusercontent.com/33251573/163246224-a9a85206-77a9-4b7c-9187-eb3089c3c108.png">
  <p align="center"><i>BetAndRuin is a betting application developed for the Software Engineering (SE) course.</i></p>
</div>

---

## Authors
#### Josefinators
- [Leire Insausti González](https://github.com/LeireInsausti)
- [Oihan Irastorza Carrasco](https://github.com/Botxan)
- [Eneko Pizarro Liberal](https://github.com/itsNko)
- [Aritz Plazaola Cortabarria](https://github.com/Poxito)
- [Pablo Tagarro Melón](https://github.com/pablobec93)

---

## Instalation
### Prerequisites
- In order to fetch football matches, you have to register in an external Rest API like [this one](https://www.football-data.org/), and use the token provided by the API.
- Copy the config file located in the root of the repository to your user.home directory. Otherwise you may have problems connecting to the database and fetching some avatar images.

You can download the latest version [here]().


---

## Project development
- [0<sup>th</sup> Iteration](#0th-iteration-project-setup)
- [1<sup>st</sup> Iteration](#1st-iteration-authentication-and-initial-use-cases)
- [2<sup>nd</sup> Iteration](#2nd-iteration-migration-to-javafx-and-more-use-cases)
- [3<sup>rd</sup> Iteration](#3rd-iteration-advanced-design-dashboards-and-rest-api)

### 0<sup>th</sup> Iteration. Project setup
The initial scenario proposed to us is a menu where any user can both create questions and query them.
<div align="center">
  <img width="500" src="https://user-images.githubusercontent.com/33251573/158075109-c6d28f47-cb27-455d-aec2-c570f4de6e66.PNG">
</div>
At this starting point, there is still no distinction between users, there are no use cases defined beyond CreateQuestion and BrowseQuestions. The application is composed by 3 different interfaces, MainMenuGUI, CreateQuestionGUI and BrowseQuesionsGUI.

### 1<sup>st</sup> Iteration. Authentication and initial use cases
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

**List of tasks carried out during iteration:**

Compulsory
- [x] [Created the use case diagram and flow of events, including the four use cases aforementioned](https://github.com/Botxan/betandruin2022/blob/main/doc/Sprint_1_-_UML.mdj).
- [x] Updated the domain model with the necessary objects to manage users (User object) and forecasts (Forecast object). See [User](https://github.com/Botxan/betandruin2022/commit/d0b0a829f9ede27986d7ec740cbf4d76e5640549) and [Forecast](https://github.com/Botxan/betandruin2022/commit/c5d42a82c7fc00e596abebded994a0c8d1e9bb3b).
- [x] Designed the [mockups](https://github.com/Botxan/betandruin2022/blob/main/doc/mockupGUIs.png) for the entire first iteration.
- [x] Implementation of the four use cases in form of GUIs, data accesses to the database and business logic intermediary methods. See: 
  * [Register](https://github.com/Botxan/betandruin2022/commit/d08abeb3d4dba5080d03f6503a69fefe40543b06)
  * [Login](https://github.com/Botxan/betandruin2022/commit/7117dd65d6d19a75a483e712881cb7962674dd3a)
  * [Create event](https://github.com/Botxan/betandruin2022/commit/64fac94b11fd676e30d456bdceb07e0c8cd915f2)
  * [Set a fee](https://github.com/Botxan/betandruin2022/commit/9ca3db2bbdd0fe8590cf121f3f4f92e7ea87bd43)

Optional/Extras
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
- [x] [Responsive design](https://github.com/Botxan/betandruin2022/commit/6a2c5f840648a32a78991ab4a117534fd23b592d).

---

### 2<sup>nd</sup> Iteration. Migration to JavaFX and more use cases
 <p float="left">
  <img width="200" src="https://user-images.githubusercontent.com/33251573/163243866-fa74e9d0-9b2a-48f4-b23e-82016f70d0cc.gif">
  <img width="436" src="https://user-images.githubusercontent.com/33251573/163243824-a9462083-2456-4d87-976a-9a02c8dd15f8.gif">
  <img width="800" src="https://user-images.githubusercontent.com/33251573/163265724-1c8e3fb7-35dd-43ed-9ba2-1618eed147ec.gif">
 </p> 
 
With the proposed migration to JavaFX, we have invested much of the time spent on the project in [redesign](https://github.com/Botxan/betandruin2022/issues/25) each and every one of the old interfaces using JavaFX instad of Swing.

The end user can now enjoy a UI decorated with a selective [color palette](https://github.com/Botxan/betandruin2022/blob/main/src/main/resources/css/colors.css) and a wide range of new components that facilitate and improve navigation, as well as a set of new utilities and use cases for an optimal application experience.

**List of tasks carried out during iteration:**

Compulsory
- [x] General:
  * Update of the domain model and use case diagram.
- [x] Redesigns:
  * [Welcome page](https://github.com/Botxan/betandruin2022/tree/WelcomeLoginRegister)
  * [Login](https://github.com/Botxan/betandruin2022/tree/WelcomeLoginRegister)
  * [Register](https://github.com/Botxan/betandruin2022/tree/RegisterImplementation)
  * [User menu](https://github.com/Botxan/betandruin2022/tree/UserMenuUI)
  * [Admin menu](https://github.com/Botxan/betandruin2022/tree/adminMenuUI)
  * [Browse events](https://github.com/Botxan/betandruin2022/tree/BrowseEvents) (old Browse questions)
  * [Create event](https://github.com/Botxan/betandruin2022/tree/createEvent)
  * [Create question](https://github.com/Botxan/betandruin2022/commit/566b6dc1338d110990919ac2d35504892f55fe5e)
  * [Create forecast](https://github.com/Botxan/betandruin2022/tree/CreateForecastRedesign)
- [x] Use cases:
  * [Deposit money](https://github.com/Botxan/betandruin2022/tree/depositMoney)
  * [Place a bet](https://github.com/Botxan/betandruin2022/tree/PlaceBet)
  * [Remove a bet](https://github.com/Botxan/betandruin2022/tree/removeBet)
- [x] Sequence diagrams:
  * [Login](https://github.com/Botxan/betandruin2022/blob/main/doc/Sprint_2_UML.mdj)
  * [Register](https://github.com/Botxan/betandruin2022/blob/main/doc/Sprint_2_UML.mdj)
  
Optional/Extras
  - [x] [Custom navigation bar](https://github.com/Botxan/betandruin2022/tree/NavBar). We have removed the default top bar and designed a bar with a more modern look and more utilities, such has [two-way navigation](https://github.com/Botxan/betandruin2022/commit/1efa6e26d555c15238084a5f0b7f6263a12016af) and [dynamic language changer](https://github.com/Botxan/betandruin2022/commit/6ce2f1cdd99e52a736dd69f638d7ab567bd0bcbe). Also, the new bar has a nested sub-bar that will show the [buttons to login and register](https://github.com/Botxan/betandruin2022/commit/d636dc7f84ba6a0b59729c35304358a1ff62c57f) if the user is not logged in, and [user's avatar[^avatar] as well and a dropdown menu](https://github.com/Botxan/betandruin2022/commit/69e34eea5df37cc5061974202b5924bfc02e0649) with several utilities if the user is authenticated.
  - [x] [Earth globe event searcher](https://github.com/Botxan/betandruin2022/commit/d3f92d8fcbd6c676b0938510d1ef93f1d68eccba). Although it is part of Browse Events GUI redesign, we believe that the new function to search for events is remarkable. The user can now select events by country by clicking on the earth globe on the left side of the interface. Similarly, if the user selects an event from the events table, the ball will automatically rotate to the country where the event takes place.
  - [x] Linking fonts from Google Fonts. In our case, we are using Roboto.
  - [x] Integration of libraries:
    * [FontAwesome](https://github.com/Jerady/fontawesomefx-jigsaw-modules). An icon library widely used by developers.
    * [JFoenix](https://github.com/sshahine/JFoenix). JavaFX material design library.
  - [x] String input input validation in the new interfaces, now also with the use of [observators](https://github.com/Botxan/betandruin2022/commit/2af68f771f05277e04bda543688229a5e7e2d03d).

[^avatar]: Users cannot upload avatar's yet, but the platform is already prepared for that situation.


---


### 3<sup>rd</sup> Iteration. Advanced design, dashboards and Rest API
<p align="center">
  <img width="700" src="https://user-images.githubusercontent.com/33251573/168394111-f7c6e5b9-8c6f-487c-abbb-355e84f2d038.gif">
  <img width="700" src="https://user-images.githubusercontent.com/33251573/168394145-c14b96ff-a312-4a67-8f61-00ea7afcd9f5.gif">
 </p>
For this latest iteration, we have tried to improve the user and administrator experience as much as possible.

To do this, we have separated the application in terms of a gambler, an administrator and a common area for both of them and anonymous users, the portal for betting and viewing events.

**List of tasks carried out during iteration:**
Compulsory
- [x] [Class diagram](https://github.com/Botxan/betandruin2022/commit/77366d4798071671c4d9d7d4b9efbc2a8d8e5128)
- [x] [Sequence diagram for Publish Results use case](https://github.com/Botxan/betandruin2022/commit/77366d4798071671c4d9d7d4b9efbc2a8d8e5128)
- [x] Use cases:
  * [Show money movements](https://github.com/Botxan/betandruin2022/commit/b2dacb239165df7556763b068a2bf9862e29160a)
  * [Publish results and update balances](https://github.com/Botxan/betandruin2022/commit/42cfacdf4acdc78f2d3f54090bbad9bcd143ac0d)
  * [Remove events: remove questions, forecast and bet refunds in cascade] (https://github.com/Botxan/betandruin2022/commit/e8664ffbe86555b9d644bbaf15137322d63bd827)
- [x] [Updated domain model and event flows](https://github.com/Botxan/betandruin2022/commit/77366d4798071671c4d9d7d4b9efbc2a8d8e5128)

Optional/Extras
- [x] Use cases (admin)
  * [Consult admin overview](https://github.com/Botxan/betandruin2022/commit/6bd19c0e17c4f7a78b267cdd2ad61cb0176b18ae)
  * [Consult and delete questions](https://github.com/Botxan/betandruin2022/commit/a9b009b1f1362176e4b029adc9aec18838ade930)
  * [Consult and delete forecasts](https://github.com/Botxan/betandruin2022/commit/5212efef65deca8f79844d936e109012829b8ba5)
  * [Consult and ban users](https://github.com/Botxan/betandruin2022/commit/06034b43578bd8f6f5669184b8066ccd4e7fa7e6)

The administrator can now manage events, quotas and forecasts in a much simpler way. The interfaces are [interconnected](https://github.com/Botxan/betandruin2022/commit/853c921df0157e48f3376000104d001c18cbee14), so that the administrator can access the queries associated with an event with a single click, just as he can access the forecasts associated with a question with another click. We have aimed to create a simple, but very powerful and intuitive interface, so that the administrator can perform his tasks efficiently. In addition, we offer the administrator an overview of the application, which can let him know at a glance the balance of the last month, the upcoming events...

- [x] Use cases (user)
  * [Consult user overview](https://github.com/Botxan/betandruin2022/commit/595f464fcd51c1ef7badf6e2dbe4f39dff90680a)
  * [Consult profile](https://github.com/Botxan/betandruin2022/commit/d5145750b3e4cb3b5bce55e5018f0c3cdef91d68)
  * [Edit profile](https://github.com/Botxan/betandruin2022/commit/d5145750b3e4cb3b5bce55e5018f0c3cdef91d68)
  * [Change avatar](https://github.com/Botxan/betandruin2022/commit/d5145750b3e4cb3b5bce55e5018f0c3cdef91d68)
  * [Change password (by email)](https://github.com/Botxan/betandruin2022/commit/91cc87d27283bbb50171d08a434e6beaa48ce5b7)
  * [Delete account](https://github.com/Botxan/betandruin2022/commit/592fea180e3c336391800c8f37cff1bd4df92abf)
  * [Withdraw money](https://github.com/Botxan/betandruin2022/commit/b2dacb239165df7556763b068a2bf9862e29160a)
  
  The user now enjoys a pleasant and easy-to-use interface, with a wide range of components and animations that will enhance the experience significantly. The overview offered by the main panel of the allows the user to know his/her track record in the application, the bets placed, the money won and even the fluctuations of his/her wallet over the last month.

- [x] Redesign and UI creation
  * [Admin dashboard](https://github.com/Botxan/betandruin2022/commit/06034b43578bd8f6f5669184b8066ccd4e7fa7e6). Includes all the aforementioned use cases.
  * [User dashboard](https://github.com/Botxan/betandruin2022/commit/2eaf34bee1f96d992a5dc92b2deffc133f1f8a7f). Includes all the aforementioned use cases.
  * Plenty of new custom componentes, such as transaction panels, deposit/withdraw money widget, 3d flipping credit card, animated dialogs...

- [x] Email
  * [Email code sender for password change authorization](https://github.com/Botxan/betandruin2022/commit/91cc87d27283bbb50171d08a434e6beaa48ce5b7). If the user wants to change his/her password, he/she will receive a 5-digit code in his/her personal e-mail that must be entered in the application to authorize the operation. As an extra, the email is customized with some HTML and CSs, and since it is multipart, the end user can download our logo.

- [x] [Builds](https://github.com/Botxan/betandruin2022/commit/a34b8220a81194cef9845a93a76b4b1f5dc3bc58). We have generated installers for Windows, MacOS and Linux. You can find them here.

---
