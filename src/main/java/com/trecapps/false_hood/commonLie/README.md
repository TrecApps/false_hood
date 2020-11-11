# Common Lie

The Common Lie package is provided to provide a means to track falsehoods that are repeated multiple times. If a given falsehood is repeated multiple times, why should those submitting those falsehoods have to repeat the explanation multiple times. It would be easier to find a Common Lie, reference that, and explain why this falsehood is an instance of the common lie.

## Objects

#### CommonLie

Basic Object detailing the lie

Long id: an identifier for the system to refer to
String title: basic description of the lie, should be easily to reference

#### CommonLieSubmission

```
{
    "lie": {
        "title": "unique title for common lie"
    },
    "falsehoods": [
        { "id": 1 },
        { "id": 2 }
    ],
    "publicFalsehoods": [
        { "id": 3 },
        { "id": 4 }
    ],
    "contents": "Explanation for the Common Lie"
}
```

Note: you can provide either Media Falsehoods "falsehoods" or Public Falsehoods "publicFalsehoods" but the total number of Falsehoods provided must be at least five.