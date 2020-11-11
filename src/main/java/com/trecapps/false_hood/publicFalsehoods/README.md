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
    "dateMade": date indicating when the falsehood was made
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
    "keywords": "Terms;;that;;can;;be;;used;;to;;search;;for;;the;;falsehood"
}
```

Note: "keywords" are separated by a double semicolon ";;"

#### SearchPublicFalsehood

Used to perform searches of the Public Falsehoods

```
{
    "terms": "search terms \"terms that are together\""
    "to": "2020-01-01T18:25:43.511Z",
    "from": null (same format as "to" if provided),
    "regions": [
        {"id": 1},
        {"id": 2}
    ],
    "institutions": [
        {"id": 7},
        {"id": 8}
    ],
    "numberOfEntries": number of entries to retrieve
    "minimum": "OBJECTIVE_FALSEHOOD",
    "maximum": null,
    "authors": [
    	{ "id": 5},
    	{ "id": 7}
    ]
}
```