## Version 0.0.2:

The main change in this version is the introduction of `shares` per expense that allows the client to split an expense into arbitrary parts. Previously an expense was always evenly split among all participants. Due to this change, two endpoints required breaking changes.

### Breaking changes

- `POST /nobts/{id}/expenses`
    - `amount` field removed.
    - `debtors` field removed.
    - `splitStrategy` field added.
    - `shares` field added.
    
- `GET /nobts/{id}`  
    - `expenses` field semantics changed. (All changes above apply to each element of the expenses array.)
    
For detailed information, please consult the updated documentation in the distribution package.