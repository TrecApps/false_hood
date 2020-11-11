# Public Figure

Stored in it's own packages as it is used by both the Falsehoods and PublicFalsehoods package. Public Figure can refer to any individual considered a "Public Figure".


## Objects

#### PublicFigure

```
{
    "firstname": "First Name",
    "middleNames": "Middle Name[s]",
    "lastName": "Last Name",
}
```

Also features fields such as "id", "submitter" (the user who submitted it), and "approved" will be managed by the falsehood service.

#### PublicFigureEntry

```
{
    "figure": {
        // Contents of "PublicFigure" above
    },
    "text": "Information about the Public Figure"
```