# false_hood

## Environment Variables

In order to avoid leaking credentials the Spring Application Properties file utilizes Environment variables to configure the app.

* ${DBUrl} - URL to a Database
* ${DBUsername} - the username for the Database
* ${DBPassword} - the password for the database

## Building

While it may be possible to build the project using Eclipse or Intelli-J, I personally use the command line:

    gradle clean build
    
For reference, my version of gradle is 6.6.

## Preparing Database Tables

There are two ways to do this. Here is one way:

#### Have the application create the tables

in _application.properties_ change the following field from

    spring.jpa.hibernate.ddl-auto=none
    
to

    spring.jpa.hibernate.ddl-auto=create
    
and change it back after the first run.

## Adding data

Once your application runs, it should have some data to work with.

#### 1. Add to the Falsehood table

    Insert into ADMIN.KEYWORD (WORD) values ('2016');
    Insert into ADMIN.KEYWORD (WORD) values ('Christopher Steele');
    Insert into ADMIN.KEYWORD (WORD) values ('Coronavirus');
    Insert into ADMIN.KEYWORD (WORD) values ('Covid-19');
    Insert into ADMIN.KEYWORD (WORD) values ('Democrats');
    Insert into ADMIN.KEYWORD (WORD) values ('Dossier');
    Insert into ADMIN.KEYWORD (WORD) values ('Facebook');
    Insert into ADMIN.KEYWORD (WORD) values ('France');
    Insert into ADMIN.KEYWORD (WORD) values ('Hydroxyclorin');
    Insert into ADMIN.KEYWORD (WORD) values ('Kompromat');
    Insert into ADMIN.KEYWORD (WORD) values ('Mueller');
    Insert into ADMIN.KEYWORD (WORD) values ('Robert Mueller');
    Insert into ADMIN.KEYWORD (WORD) values ('Russiagate');
    Insert into ADMIN.KEYWORD (WORD) values ('Sanders');
    Insert into ADMIN.KEYWORD (WORD) values ('coup');
    Insert into ADMIN.KEYWORD (WORD) values ('election');
    Insert into ADMIN.KEYWORD (WORD) values ('impeachment');
    Insert into ADMIN.KEYWORD (WORD) values ('left');
    Insert into ADMIN.KEYWORD (WORD) values ('police');
    Insert into ADMIN.KEYWORD (WORD) values ('police brutality');
    Insert into ADMIN.KEYWORD (WORD) values ('the left');
    Insert into ADMIN.KEYWORD (WORD) values ('ukraine');

#### 2. Add to the Media Outlet Table

    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (0,1999,'Me');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (2,1996,'MSNBC');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (3,1989,'CNBC');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (4,1980,'CNN');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (5,1996,'Fox News');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (6,1851,'The New York Times');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (7,1877,'The Washington Post');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (8,1881,'The Los Angelos Times');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (9,1943,'ABC');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (10,1927,'CBS');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (11,1969,'PBS');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (12,1965,'TBS');
    Insert into ADMIN.MEDIA_OUTLET (OUTLET_ID,FOUNDATION_YEAR,NAME) values (13,2013,'One America News Network');
    
#### 3. Add to the Falsehood Table

    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (7,'Rachel Maddow',null,'7-Maddow Report',to_timestamp('13-JUN-17 09.42.47.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,0,'Maddow Report',0,2);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (8,'Sean Hannity',null,'8-Hannity',to_timestamp('29-JAN-19 09.42.47.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,2,'Hannity',0,5);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (9,'bee',null,'9-Dossier',to_timestamp('04-DEC-16 09.42.47.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),0,4,'Dossier',0,10);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (10,'SNL',null,'10-SNL',to_timestamp('10-DEC-16 09.42.47.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,4,'SNL',0,2);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (0,'Chuck Todd',null,'0-Chucky',to_timestamp('04-AUG-20 03.27.27.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,2,'Chucky',0,2);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (1,'Joy Anne Reid',null,'1-hill',to_timestamp('04-AUG-20 03.38.38.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,3,'hill',0,2);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (2,'Chris Matthews',null,'2-over',to_timestamp('02-AUG-20 03.51.05.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,4,'over',0,2);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (3,'Sean Hannity',null,'3-covid',to_timestamp('08-JUN-20 03.55.14.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,0,'covid',0,5);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (4,'Laura Ingraham',null,'4-c',to_timestamp('02-JUN-20 03.55.14.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,1,'c',0,5);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (5,'Tomi Lahren',null,'5-LeftLaw',to_timestamp('01-JUN-20 03.55.14.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,3,'LeftLaw',0,5);
    Insert into ADMIN.FALSEHOOD (ID,AUTHOR1,AUTHOR2,CONTENT_ID,DATE_MADE,MEDIA_TYPE,SEVERITY,SOURCE,STATUS,OUTLET_OUTLET_ID) values (6,'Tucker Carlson',null,'6-impeach',to_timestamp('04-JUN-20 03.55.14.000000000 PM','DD-MON-RR HH.MI.SSXFF AM'),1,2,'impeach',0,5);

#### 4. Add to the Keywords, falsehood table

    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Russiagate',7);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Democrats',7);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('2016',7);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Russiagate',8);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Democrats',8);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('2016',8);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Robert Mueller',8);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Mueller',8);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Russiagate',9);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Dossier',9);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Kompromat',9);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Christopher Steele',9);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Russiagate',10);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Facebook',10);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('election',10);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('2016',10);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('France',2);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Sanders',2);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Covid-19',3);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Coronavirus',3);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Covid-19',4);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Coronavirus',4);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('Hydroxyclorin',4);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('the left',5);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('police',5);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('police brutality',5);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('left',5);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('impeachment',6);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('coup',6);
    Insert into ADMIN.KEYWORD_FALSEHOODS (KEYWORD_WORD,FALSEHOODS_ID) values ('ukraine',6);

**Note: if you use a name other than _admin_ for ${DBUsername}, you may want to replace _ADMIN_ in the above _Insert_ statements accordingly.**

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

* Field: firstName (String)
* Purpose: The first name of the User
* Mandatory: Yes

* Field: lastName (String)
* Purpose: The Last Name of the User
* Mandatory: Yes

* Field: username (String)
* Purpose: the user name of the user (used throughout all of Trec-Apps)
* Mandatory: Yes

* Field: mainEmail (String)
* Purpose: The main Email to contact, used to provide a token with which the user can verify the account (i.e. not a bot)
* Mandatory: Yes

* Field: trecEmail (String)
* Purpose: The eventual Trec Account should Trec-Apps launch it's own email service)
* Mandatory: No. Leave blank for now

* Field: backupEmail (String)
* Purpose: The back-up email to use
* Mandatory: Yes

* Field: password (Protected String)
* Purpose: token the user can use to log in later
* Mandatory: Yes

* Field: birthday (Date)
* Purpose: When was the user born. Can verify the age and manage content-restriction
* Mandatory: Yes

* Field: passwordMonthReset (1 byte number)
* Purpose: How many months can pass before the User needs to reset his/her password
* Mandatory: No

* Field: timeForValidToken (1 byte number)
* Purpose: How long after each successful login that the token should be valid for
* Mandatory: No

* Field: validTimeFromActivity (1 byte number)
* Purpose: Whether the Service should update the token after some activity by the user (expiration is pushed back)
* Mandatory: No

* Field: maxLoginAttempts (1 byte number)
* Purpose: How many times Per hour the user can fail to log-in before the account is locked
* Mandatory: No

* Field: lockTime (1 byte number)
* Purpose: How long (by ten minutes, to lock the account for)
* Mandatory: No

Note: also comes with a "clientId" but is not supposed to be known by the user

##### ReturnObj

Object returned by a successful attempt to authenticate

```
    "token": ""liuerhfnx23y40923y5t",
    "username": "username",
    "firstname": "User's first name",
    "lastname": "User's last name",
    "color": "info about the user's prefered style"
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
