# Falsehoods

This is more or less the root reason why this service exists, to track the Falsehoods made by media outlets. Within this package are Falsehoods, MediaOutlets, and a Search Object to easily enable users to search falsehoods documented by the system.

## Objects

#### MediaOutlet

```
{
    "foundationYear": year,
    "name": "name of the outlet"
}
```

Object also comes with 

* Integer outletId - id of the outlet
* byte approved - has the entry been approved by a moderator
* FalsehoodUser submitter - the user that submitted the outlet

#### MediaOutletEntry

```
{
    "outlet": {
        // Info about MediaOutlet (see above)
    },
    "text": "Information about the Media Outlet"
}
```

#### Falsehood

```
{
    "outlet": {
        "outletId": id
    },
    "commonLie": {
        "id": common_Lie_id
    },
    "mediaType": 0 for video or 1 for Article,
    "severity": byte indicating the type of falsehood,
    "author1": {
        // id field of the PublicFigure, see package on "publicFigure"
    },
    "author2": {
    
    },
    "source": "Name of the Article or Name of the Show",
    "dateMade": date indicating when the falsehood was made,
    "contentId": (might become depricated),
    "tags": "Keywords separated by new line or by '|'"
}
```

Note: the "commonLie" and "author2" can be null (commonLie might not exist yet). "author2" would be the host of the show so if someone else makes the falsehood, that person would be "author1" and the show host would be "author2" assuming the host fails to quickly correct the record.

#### FullFalsehood

```
{
    "contents": "Detailed explanation on why this is a Falsehood",
    "metadata": {
        // the Falsehood seen above
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

#### SearchFalsehood

Used to perform searches of the Media Falsehoods

```
{
    "terms": "search terms \"terms that are together\""
    "to": "2020-01-01T18:25:43.511Z",
    "from": null (same format as "to" if provided),
    "outlet": {"outletId": 1},
    "numberOfEntries": number of entries to retrieve
    "minimum": "OBJECTIVE_FALSEHOOD",
    "maximum": null,
    "authors": { "id": 5}
}
```
