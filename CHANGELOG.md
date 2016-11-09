## Version 0.0.8:

- Do not response with a 404 for "route not found" if the request caused an error. The response status is now correctly 500.
- Provide unique IDs for nobt and expenses when using the In-Memory database.

### Breaking changes

- `WRITE_STACKTRACE_TO_RESPONSE` config value has been removed.

## Version 0.0.7:

- Correctly respond with a 404 if a nobt is requested whose identifier could not be decoded to a database id. ([Issue 30](https://gitlab.com/nobt-io/api/issues/30))
- Do not leak the decoded database ID to the outside world if a nobt with a given ID could not be found in the database. ([Issue 31](https://gitlab.com/nobt-io/api/issues/31))

## Version 0.0.6:

This version drops the support for profiles and instead introduces specific environment variables for configuration.
This allows for a more fine-grained configuration of the API.

For backwards-compatibility reasons, a shell and a batch-script are located under the `bin/` folder that configure the application in the same way the `STANDALONE` profile did before.

## Version 0.0.5:

It is now possible to define a currency for a nobt. This allows the client to store expenses with different currencies.

### Breaking changes

- `POST /nobts/{id}`
    - `currency` field added.

## Version 0.0.4:

You can now delete expenses from a Nobt.

## Version 0.0.3:

When creating an expense, you now have to specify a date. This date indicates, when the user made the expense.
Additionally, a timestamp is generated for nobts and expenses, as soon as they are persisted.

### Breaking changes

- `POST /nobts/{id}/expenses`
    - `date` field was added.

## Version 0.0.2:

The main change in this version is the introduction of `shares` per expense that allows the client to split an expense into arbitrary parts. Previously an expense was always evenly split among all participants. Due to this change, two endpoints required breaking changes.

### Breaking changes

- `POST /nobts/{id}/expenses`
    - `amount` field removed.
    - `debtors` field removed.
    - `splitStrategy` field added.
    - `shares` field added.
    - A check has been added that rejects requests with non-unique names inside the `shares` field.
    
- `GET /nobts/{id}`  
    - `expenses` field semantics changed. (All changes above apply to each element of the expenses array.)
    
For detailed information, please consult the updated documentation in the distribution package.