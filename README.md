# Accounting for small business

## Objectives
* A spring boot and angular learning exercise for myself.
* To use as an invoicing and accounting application 
* Complying with Belgian tax and accounting rules.
* To do double entry accounting
* Automate the accounting process


## Build and run without local development environment
To build and run the application and a postgress database inside docker containers:

    docker compose -f docker-compose.yml build
    docker compose -f docker-compose.yml up

The application can now be accessed on http://localhost:8000

## Status
This project is still very much in an early stage.

* Accounting is not actively used yet and needs to be re-factored using the MAR structure (https://www.cbn-cnc.be/nl/node/2250). 
* Create and update companies is implemented, company is an abstraction for suppliers, customers and owner company but some extra improvements are still required.
* Invoice creation (an owner company must have been created before), the invoice is intentionally generated only once however the process still needs to be separated in discrete steps, the generated invoice itself is currently only in Dutch.
* CORS and CSRF (reason why both angular and spring are bundled in the same container)
* Proper security and user management still needs to be implemented.
* Many missing features.
* Overall UI improvements needs to be made.
* Basic form-based authentication, default user name/password: uts/uts


## User story

## Accounting

### Invoice booking

The invoice is posted:
* Invoice amount including VAT debit account 400 (debtor account)
* Invoice amount excluding VAT debit account 704 (profit)
* VAT credit account 451 (owed to VAT)

The invoice is paid:
* Invoice amount inc VAT credit 400 (debtor account)
* Invoice amount inc VAT debit 5500 (bank account), implicit

