# Public Falsehoods

This Package supplies functionality related to Public Falsehoods, Falsehoods told by Public Figures that fall outside purview of any Media Outlet.

## Objects

#### Institution

Fields :

Long id: the Identifier in the DB
String name: the Name of the Institution as recognizable by humans

Note: when submitting the Region for the first time, the id should be null

#### InstitutionEntry

```
{
    "instutution": {
        "name": "institution name"
    },
    "contents": "Information about the institution"
}
```

#### Region

More or less the same fields as Institution

#### RegionEntry

```
{
    "region": {
        "name": "region name"
    },
    "contents": "Information about the region"
}
```

#### PublicFalsehood

```
{
    "commonLie": {
        "id": common_Lie_id
    },
    "severity": byte indicating the type of falsehood,
    "official": {
        // id field of the PublicFigure, see package on "publicFigure"
    },
    "officialType": byte indicating the type of official the public figure was at the time,
    "region": {
        "id": id of region
    },
    "institution": {
        "id": id of institution
    }
    "dateMade": date indicating when the falsehood was made,
    "tags": "Keywords separated by new line or by '|'"
}
```

Also contains "status" and "id" fields, but will be set by the Service upon submission

#### FullPublicFalsehood


```
{
    "contents": "Detailed explanation on why this is a Falsehood",
    "metadata": {
        // the PubilcFalsehood seen above
    },
    "verdicts": [{
        "approve": true/false,
        "userId": number indicating Relevant User,
        "made": Date Documented,
        "explaination": "Comment indicating what this event is or why it was made"
    },...],
    "events": [{
        "approve": true/false,
        "userId": number indicating Relevant User,
        "made": Date Documented,
        "explaination": "Comment indicating what this event is or why it was made"
    },...]
}
```

Note: For "approve" in verdict objects, true equates to the string "approve" and false equates to "reject". In event Objects, true equates to "Created" and false to "Updated". 

#### SearchPublicFalsehood

Used to perform searches of the Public Falsehoods

```
{
    "terms": "search terms \"terms that are together\""
    "to": "2020-01-01T18:25:43.511Z",
    "from": null (same format as "to" if provided),
    "numberOfEntries": number of entries to retrieve
    "minimum": "OBJECTIVE_FALSEHOOD",
    "maximum": null,
    "official": { "id": 5}
}
```

Note: "Region" and "Institution" might be removed soon to reduce SQL Search complexity and thus risk of a DoS attack

Update: Region and Institution HAS been removed to reduce complexity in Public Falsehoods search
