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
- Database: [mySQL](https://www.mysql.com/products/workbench/)
- Server: [Apache Tomcat 10.1](https://tomcat.apache.org/download-10.cgi): Under Core -> 'zip' option
- IDE: [Eclipse EE](https://www.eclipse.org/downloads/packages/release/2023-03/r/eclipse-ide-enterprise-java-and-web-developers)
- Tests: JUnit 5

## Installation
### Tomcat 10.1 Server Installation:
- Before creating/importing a new project, you must add tomcat to the servers list
- After downloading the zip for Apache Tomcat from linked website above ('zip' option under Core), extract the folder
- Store this folder where you like, and click on 'Servers' which shoud be tiled at the bottom window of eclipse
- Select 'No servers are available. Click this link to create a new server...'
- Click on 'Apache' and scroll through the options until you find 'Tomcat v10.1 Server'; Select it and hit next
- Under 'Tomcaat installation directory:' hit browse and select the unzipped Tomcat folder previously downloaded and hit finish
- Right click on the new server and hit 'Start'; The server should now be Started and running.
### Project Setup:
- Select 'Create a Dyanmic Web Project' (this should be seen, if not please download eclipse EE)
- Name the project and make sure 'Target runtime' has Apache Tomcat v10.1 selected. 
- Generate a web.xml, and hit 'Finish'
- (I have tried directly importing the project, but eclipse does not recoginze any of the files when it is done this way)
- Copy the files from the repository and paste it into the newly created project. (Just select the contents and paste it into the project folder)
  - If Junit 5 is not installed yet, go ahead and configure it.
- Right click on the project name in 'Project Explorer' and hover over 'Run' then click on 'Run on Server'
- Select Tomcat and hit finish. A new window should now pop up with the website loaded
