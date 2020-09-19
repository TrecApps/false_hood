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

## Available Endpoints

#### Retrieve list of falsehoods

* Purpose: Retrieves list of falsehoods according to parameters
* Method: POST
* Endpoint: /Falsehood/list
* Token required: No
* Example Input:

```
{
	"terms": "Russiagate",
	"to": "2020-01-01T18:25:43.511Z",
	"from": null,
	"outlets": ["Fox News", "MSNBC"],
	"numberOfEntries": 5,
	"minimum": null,
	"maximum": null,
	"authors": ["author1"]
}
```

#### Retrieve falsehood by ID

* Purpose: retireves 1 falsehood
* Method: GET
* Endpoint: /Falsehood/id/{id}
* Token Required: No

#### Add a new falsehood

* Purpose: Submits a new falsehood to the service
* Method: POST
* Endpoint: /Update/Falsehood/Insert
* Token Required: Yes
* Example Input:

```
{
    "metadata": {
        "outlet": {
            "outletId": 9
        },
        "mediaType": 0,
        "severity": 4,
        "author1": "",
        "source": ""
    },
    "contents": ""
}
```