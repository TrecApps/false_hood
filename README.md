# false_hood

## Environment Variables

In order to avoid leaking credentials the Spring Application Properties file utilizes Environment variables to configure the app.

* ${DBUrl} - URL to a Database
* ${DBUsername} - the username for the Database
* ${DBPassword} - the password for the database
* ${DB_DRIVER} - the Driver Class used by JDBC as a DB Driver (For MySQL, "com.mysql.cj.jdbc.Driver" is recommended) 
* ${DB_DIALECT} - the Hibernate Dialect to use  (For MySQL, "org.hibernate.dialect.MySQL57Dialect" is recommended)

* ${TREC_PUBLIC_KEY} - File Path for the public Key used to verify JWT Authorization tokens
* ${FALSEHOOD_CLIENT_ID} - the ID of this instance of the Service (can be anything for now, but when using a production version of the trec-apps-user-service should be what string the service gave you when registering)
* ${USER_SERVICE_URL} - the URL of the Falsehood Service to connect to

* ${EM_USERNAME} - the Email Address to send (currently expects a gmail address)
* ${EM_PASSWORD} - The Password for the Email Address (For Gmail, this would be a special app password, not the user password)

## Building

While it may be possible to build the project using Eclipse or Intelli-J, I personally use the command line:

    gradle build
    
For reference, my version of gradle is 6.6.
You might have to run this command twice

## Preparing Database Tables

There are two ways to do this. Here is one way:

#### Have the application create the tables

in _application.properties_ change the following field from

    spring.jpa.hibernate.ddl-auto=none
    
to

    spring.jpa.hibernate.ddl-auto=create
    
and change it back after the first run.

## Http Objects available

These are objects that can be transferred over to the Falsehoods service using HTTP. You can create representations of them on the Client side (likely using JSON) and send then as part of the request body

### Account Objects

These Objects are focused on managing Login and user authentication. They are used by the falsehoods service to communicate with the User Service.

##### LogIn

Used to manage The LogIn Process, part of a Request

```
{
    "username":"some_username",
    "email":"some_email@outlook.com",
    "password":"S0M3_P455word"
}
```

Note that none of these values are real values, just used to provide an example.
Note: Either the "Username" or the "email" is used. "password" is always needed.
There is technically a fourth field called "clientId" but end user's aren't supposed to know what it is.

##### NewUser

Used in the event the User wishes to create a new account in Trec-Apps

Fields:

Field: firstName (String)
* Purpose: The first name of the User
* Mandatory: Yes

Field: lastName (String)
* Purpose: The Last Name of the User
* Mandatory: Yes

Field: username (String)
* Purpose: the user name of the user (used throughout all of Trec-Apps)
* Mandatory: Yes

Field: mainEmail (String)
* Purpose: The main Email to contact, used to provide a token with which the user can verify the account (i.e. not a bot)
* Mandatory: Yes

Field: trecEmail (String)
* Purpose: The eventual Trec Account should Trec-Apps launch it's own email service)
* Mandatory: No. Leave blank for now

Field: backupEmail (String)
* Purpose: The back-up email to use
* Mandatory: Yes

Field: password (Protected String)
* Purpose: token the user can use to log in later
* Mandatory: Yes

Field: birthday (Date)
* Purpose: When was the user born. Can verify the age and manage content-restriction
* Mandatory: Yes

Field: passwordMonthReset (1 byte number)
* Purpose: How many months can pass before the User needs to reset his/her password
* Mandatory: No

Field: timeForValidToken (1 byte number)
* Purpose: How long after each successful login that the token should be valid for
* Mandatory: No

Field: validTimeFromActivity (1 byte number)
* Purpose: Whether the Service should update the token after some activity by the user (expiration is pushed back)
* Mandatory: No

Field: maxLoginAttempts (1 byte number)
* Purpose: How many times Per hour the user can fail to log-in before the account is locked
* Mandatory: No

Field: lockTime (1 byte number)
* Purpose: How long (by ten minutes, to lock the account for)
* Mandatory: No

Note: also comes with a "clientId" but is not supposed to be known by the user

##### ReturnObj

Object returned by a successful attempt to authenticate

```
{
    "token": ""liuerhfnx23y40923y5t",
    "username": "username",
    "firstname": "User's first name",
    "lastname": "User's last name",
    "color": "info about the user's prefered style"
}
```

Note: the "token" field is meant to be a JWT token unique to that user and specific to the TrecApp's Falsehood Service.

### Other Objects

Most other objects are explained in various READMEs in the sub folders of this Repository. There is one worth mentioning here though:

##### Verdict Submission

```
{
    "comment": "Explanation for the verdict",
    "id": ID of the Falsehood or Public Falsehood
}
```

## Available Endpoints

These Endpoints are supplied by various "Controllers" in the Falsehoods Service. Each Endpoint has the following fields:

* Full Endpoint: the full path after the base URL to the endpoint
* Method: the HTTP method used (GET, POST, PUT, etc.)
* Authentication: Whether the JWT token detailing the user is required
* Credibility: The Falsehoods Service stores information on the "Credibility" rating of the user. Some actions require the user to obtain a certain credibility score
* Request Body: object type expected in the Request
* Response Body: object type expected in the Response

Note: for Request or Response Body types not labeled as "String", "Integer", "BigInteger", or "Long", assume the formatting is JSON unless specified otherwise

### Account Controller ("/account")

Provides basic account management (Creating an account and logging in)

##### "/CreateUser"

Allows User to Create a Trec-Apps Account through the Falsehoods service

* Full Endpoint: "/account/CreateUser"
* Method: POST
* Authentication: No
* Credibility: N/A
* Request Body: NewUser
* Response Body: ReturnObj

##### "/LogIn"

Allows the user to log-in and get a token

* Full Endpoint: "/account/LogIn"
* Method: POST
* Authentication: No
* Credibility: N/A
* Request Body: LogIn
* Response Body: ReturnObject

### Public Figure Controller ("/PublicFigure")

Manages Public Figure information held by the service

##### "/Add"

User can Add a Public Figure to be reviewed by peers

* Full Endpoint: "/PublicFigure/Add"
* Method: POST
* Authentication: Yes
* Credibility: 40
* Request Body: PublicFigureEntry (see README in the Public Figure package)
* Response Body: String (if error occurs, no content if successful)

##### "/Approve"

Users can Signal the approval of a public figure not currently approved for use

* Full Endpoint: "/PublicFigure/Approve"
* Method: PUT
* Authentication: Yes
* Credibility: 60
* Request Body: Long
* Response Body: String (if error occurs, no content if successful)

##### "/Reject"

Users can reject the use of a certain Public Figure Entry

* Full Endpoint: "/PublicFigure/Reject"
* Method: PUT
* Authentication: Yes
* Credibility: 60
* Request Body: Long
* Response Body:String (if error occurs, no content if successful)

##### "/list"

* Full Endpoint: "/PublicFigure/list"
* Method: GET
* Authentication: No
* Credibility: N/A
* Request Body: N/A
* Response Body: List<PublicFigure>

### Authenticated Falsehood Controller ("/Update/Falsehood")

Allows operations on Media Falsehoods and Outlets, most requiring Authentication

##### "/Insert"

Add a new Falsehood for review

* Full Endpoint: "/Update/Falsehood/Insert"
* Method: POST
* Authentication: Yes
* Credibility: 5
* Request Body: FullFalsehood (See README in the Falsehoods package)
* Response Body: String (if error occurs, no content if successful)

##### "/Approve"

Signals your approval of the Target Falsehood

* Full Endpoint: "/Update/Falsehood/Approve"
* Method: PUT
* Authentication: Yes
* Credibility: 60
* Request Body: VerdictSubmission
* Response Body: String (if error occurs, no content if successful)

##### "/Reject"

Signals your disappointment in a given Falsehood Submission

* Full Endpoint: "/Update/Falsehood/Reject"
* Method: PUT
* Authentication: Yes
* Credibility: 60
* Request Body: VerdictSubmission
* Response Body: String (if error occurs, no content if successful)

##### "/Update"

Allows you to update the contents of the Information about the Falsehood

* Full Endpoint: "/Update/Falsehood/Update"
* Method: PUT
* Authentication: Yes
* Credibility: 60
* Request Body: FullFalsehood
* Response Body: String (if error occurs, no content if successful)

##### "/AddOutlet"

Adds an Outlet for use in referenceing by falsehoods

* Full Endpoint: "/Update/Falsehood/AddOutlet"
* Method: POST
* Authentication: Yes
* Credibility: 40
* Request Body: MediaOutletEntry (see README in the Falsehoods package
* Response Body: String (if error occurs, no content if successful)

##### "/ApproveOutlet"

Signals your approval of an Outlet Submission

* Full Endpoint: "/Update/Falsehood/ApproveOutlet"
* Method: PUT
* Authentication: YES
* Credibility: 60
* Request Body: Integer
* Response Body: String (if error occurs, no content if successful)

##### "/RejectOutlet"

Signals your rejection of an Outlet Submission

* Full Endpoint: "/Update/Falsehood/RejectOutlet"
* Method: PUT
* Authentication: YES
* Credibility: 60
* Request Body: Integer
* Response Body:String (if error occurs, no content if successful)

### Falsehood Controller ("/Falsehood")

Provides Basic Search for Falsehoods

##### "/id/{id}"

* Full Endpoint: "/Falsehood/id/{id}"
* Method: GET
* Authentication: No
* Credibility: N/A
* Request Body: N/A ("{id}" in the endpoint is a Large Integer referencing the Desired Falsehood)
* Response Body: Falsehood

##### "/list"

* Full Endpoint: "/Falsehood/list"
* Method: POST
* Authentication: No
* Credibility: N/A
* Request Body: SearchFalsehood (See README in the Falsehoods Package)
* Response Body: List<Falsehood>

### Authenticated Public Falsehood Controller ("/Update/PublicFalsehood"

Operations to perform on Public Falsehoods (most of which require authentication)

##### "/Insert"

* Full Endpoint: "/Update/PublicFalsehood/Insert"
* Method: POST
* Authentication: Yes
* Credibility: 5
* Request Body: FullPublicFalsehood (See README in the "Public Falsehoods" package)
* Response Body: String (if error occurs, no content if successful)

##### "/Update"

* Full Endpoint: "/Update/PublicFalsehood/Update"
* Method: PUT
* Authentication: Yes
* Credibility: 60
* Request Body: FullPublicFalsehood
* Response Body: String (if error occurs, no content if successful)

##### "/AddRegion"

* Full Endpoint: "/Update/PublicFalsehood/AddRegion"
* Method: POST
* Authentication: Yes
* Credibility: 70
* Request Body: RegionEntry (See README in the "Public Falsehoods" package)
* Response Body: String (if error occurs, no content if successful)

##### "/AddInstitution"

* Full Endpoint: "/Update/PublicFalsehood/AddInstitution"
* Method: POST
* Authentication: Yes
* Credibility: 70
* Request Body: InstitutionEntry (See README in the "Public Falsehoods" package)
* Response Body: String (if error occurs, no content if successful)

### Public Falsehood Controller ("/PublicFalsehood")

##### "/id/{id}"

* Full Endpoint: "/PublicFalsehood/id/{id}"
* Method: GET
* Authentication: No
* Credibility: N/A
* Request Body: N/A ("{id}" in endpoint references large Integer, id of the Object)
* Response Body: PublicFalsehood

##### "/list"

* Full Endpoint: "/PublicFalsehood/list"
* Method: POST
* Authentication: No
* Credibility: N/A
* Request Body: SearchPublicFalsehood (See README in the "Public Falsehoods" package)
* Response Body: List<PublicFalsehoods>

### Common Lie Controller ("/CommonLie")

Manages Common Lies, or falsehoods told so frequently that it's better to group them.

##### "/insert"

* Full Endpoint: "/CommonLie/insert"
* Method: POST
* Authentication: Yes
* Credibility: 40
* Request Body: CommonLieSubmission (See README in the "Common Lie" Package)
* Response Body: String (if error occurs, no content if successful)

### Falsehood Appeal Controller ("/Appeal")

Controller Allowing Users to Submit appeals against falsehoods (either overturn or upgrade severity)

##### "/List"

Get List of Active Appeals

* Full Endpoint: "/Appeal/List"
* Method: GET
* Authentication: No
* Credibility: N/A
* Request Body: N/A
* Response Body: List<FalsehoodAppeal> (See README in the "Appeals" package)

##### "/entry"

Retrieves Information about a Specific Appeal

* Full Endpoint: "/Appeal/entry"
* Method: GET
* Authentication: No
* Credibility: N/A
* Request Body: BigInteger
* Response Body: FalsehoodAppealEntry (See README in the "Appeals" package)

##### "/Add"

Allows User to submit a certain appeal

* Full Endpoint: "/Appeal/Add"
* Method: POST
* Authentication: Yes
* Credibility: 25
* Request Body: FalsehoodAppealEntry
* Response Body: String (if error occurs, no content if successful)

##### "/Petition"

This Requests the Service to Provide a Validation Token (via email) which which you can use to sign the petition for the Falsehoods Service (Ratify the Appeal)

* Full Endpoint: "/Appeal/Petition"
* Method: POST
* Authentication: Yes
* Credibility: 15
* Request Body: FalsehoodAppealSignature (See README in the "Appeals" package)
* Response Body: String (if error occurs, no content if successful)

##### "/Petition"

This Endpoint takes in the validation token to formally sign the petition for the appeal. Distinguished from the above endpoint by HTTP method

* Full Endpoint: "/Appeal/Petition"
* Method: PUT
* Authentication: Yes
* Credibility: 15
* Request Body: Application form with an "appealId" number field and a "validation" String field
* Response Body: String (if error occurs, no content if successful)
