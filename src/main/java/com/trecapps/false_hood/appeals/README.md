# Falsehood Appeals

The purpose behind this package is to provide functionality related to the appeals Process of managing Falsehoods. For instance, if you believe that a certain conformed Falsehood should not be considered a Falsehood (or new information emerge rendering the "falsehood" to be true, you can challenge it. Or if you feel that the falsehood is more severe than it's severity rating implies, you can request that it be upgraded.

## Objects 

#### FalsehoodAppeal

Used to manage the Appeal itself:

JSON input:

```
{
    "falsehood": {
        "id": xxxx
    },
    "pFalsehood": {
        "id": yyyy
    },
    "desiredState": "[Upgrade/Challenge]"
}
```

Also comes with the fields "id", "petitioner", and "ratified", but the system will set those fields for you.

Note: Set either the "falsehood" (for Media falsehood) field **OR** the "pFalsehood" (for Public Figure Falsehood) attribute. Setting both of them (or neither of them will cause your appeal to be rejected by the system.

#### FalsehoodAppealSignature

Used by other users to sign the Appeal. Needs at least fifty signatures according to the System before the target Falsehood is truly challenged.

```
{
    "appeal": {
        "id": xxxx
    }
    "verificationString": "some token sent by email",
    "grantAnon": 0 or 1
}
```

"appeal": represents the Falsehood Appeal to sign
"grantAnon": signifies whether your info shows up on the list of signers. if everything one is anonymous (1 or above), then it takes 100 signatures. Otherwise, it takes 50 non-anonymous signers.

#### FalsehoodAppealEntry

```
{
    "reason": "Reasoning behind the eppeal that should convince other users that your appeal is valid and should be approved",
    "appeal": {
        // FalsehoodAppeal data (see above)
    }
}
```

This is what is expected by the controller for appeals