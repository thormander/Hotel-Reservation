# Hotel-Reservation
## Project Requirements:
- As a hotel guest:
  - Create a guest account, and log in/log out.
  - Modify their own guest account.
  - Search available rooms for reservation and browse the information of a selected room.
  - Make a new reservation and change/cancel an existing reservation.
- As a hotel clerk:
  - Log in to the system using a username and a password.
  - Modify their own profile information including password.
  - Enter and modify the information of all rooms.
  - View the status of all the rooms in the hotel.
  - Modify a reservation.
  - Process check-in/check-out of a guest.
  - Generate billing information for any guest.
  - Cancel any reservation prior to the reservation start date.
- As an admin user:
  - Log in to the system using a username and a password.
  - Create a hotel clerk account which contains a username and a default password.
  - Reset the user account password.
- Extra:
  - Hotel guest membership with points earning/redeem system.
  - Email notification system.

## Technologies Used:
- Database: [mySQL](https://dev.mysql.com/downloads/mysql/)
- Server: [Apache Tomcat 10.1](https://tomcat.apache.org/download-10.cgi): Under Core -> 'zip' option
- IDE: [Eclipse EE](https://www.eclipse.org/downloads/packages/release/2023-03/r/eclipse-ide-enterprise-java-and-web-developers) (Make sure to use EE!)
- Tests: JUnit 5

## Installation Guide
### MySQL:
- Ensure the name of the admin user is 'root' and the password is '12345678'
- [Mac Installation Tutorial](https://www.youtube.com/watch?v=nj3nBCwZaqI&t=237s)
  - If you are using MySQL workbench and it repeatedly crashes, downgrade to the previous version [8.0.31](https://downloads.mysql.com/archives/workbench/)
- [Windows Installation Tutorial](https://www.youtube.com/watch?v=u96rVINbAUI&t=97s)
- Run the following commands in mySQL: 
  - `CREATE DATABASE hotel;`
  - `USE hotel;`
- Run the following commands from the .txt files below in order:
  - [SqlCreate.txt](https://github.com/thormander/Hotel-Reservation/files/11256004/SqlCreate.txt)
  - [40roomsINSERT.txt](https://github.com/thormander/Hotel-Reservation/files/11224624/40roomsINSERT.txt)
- Insert the admin user
  - ```INSERT INTO `hotel`.`account` (`user_name`, `email_id`, `password`, `type`) VALUES ('admin', 'admin@test.com', 'admin', 'admin');``` 
### Tomcat 10.1 Server Installation:
- Before creating/importing a new project, you must add tomcat to the servers list
- After downloading the zip for Apache Tomcat from linked website above ('zip' option under Core), extract the folder
- Store this folder where you like, and click on 'Servers' which shoud be tiled at the bottom window of eclipse
- Select 'No servers are available. Click this link to create a new server...'
- Click on 'Apache' and scroll through the options until you find 'Tomcat v10.1 Server'; Select it and hit next
- Under 'Tomcat installation directory:' hit browse and select the unzipped Tomcat folder previously downloaded and hit finish

https://user-images.githubusercontent.com/71967190/231578521-df3b2076-b767-4a54-9a8f-da0a570c1a12.mov


- Right click on the new server and hit 'Start'; The server should now be Started and running.
### Project Setup:
- Select 'Create a Dynamic Web Project' (this should be seen, if not please download eclipse EE)
- Name the project and make sure 'Target runtime' has Apache Tomcat v10.1 selected. Select '5.0' for Dynamic Web Module
- Generate a web.xml, and hit 'Finish'

https://user-images.githubusercontent.com/71967190/231578675-f542a1ab-8227-4ded-9555-55e2ec9a4005.mov


- (I have tried directly importing the project, but eclipse does not recoginze any of the files when it is done this way)
- Copy the files from the repository and paste it into the newly created project. (Just select the contents and paste it into the project folder); I would recommend cloning the repository then just copy pasting from there.
  - If Junit 5 is not installed yet, add it to the library by right clicking on project -> 'Build Path' -> 'Configure Build path'
  - Go to 'Libraries' -> click on 'Classpath' -> 'Add Library...' -> 'Junit 5'
  
  https://user-images.githubusercontent.com/71967190/231580148-7e22b0e1-12fb-4f41-90f6-9e0429771a96.mov


- Right click on the project name in 'Project Explorer' and hover over 'Run' then click on 'Run on Server'
- Select Tomcat and hit finish. A new window should now pop up with the website loaded
### Troubleshooting:
- If you right click on the project hover over 'Run On', and it does not show 'Run on Server'; You need to change the version of your Dynamic Web Module.

https://user-images.githubusercontent.com/71967190/231577937-123e9b52-ddec-499a-9cab-16f4f0c6b6f9.mov


