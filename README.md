# Accounting for small business

## Objectives
* A spring boot and angular learning exercise for myself.
* To use as an invoicing and accounting application 
* Complying with Belgian tax and accounting rules.
* To do double entry accounting
* Automate the accounting process


## Build and run without local development enviroment
To build and run the application and a postgress database inside docker containers:

    docker compose -f docker-compose.yml build
    docker compose -f docker-compose.yml up

The application can now be accessed on http://localhost:8000

## Status
This project is still very much in an early stage.

* Accounting is not actively used yet and needs to be refactored using the MAR structure (https://www.cbn-cnc.be/nl/node/2250). 
* Create and update companies is implemented, company is an abstraction for suppliers, customers and owner company but some extra improvements are still required.
* Invoice creation (an owner company must have been created before), the invoice is intentionally generated only once however the process still needs to be separated in descrete steps, the generated invoice itself is currently only in Dutch.
* CORS and CSRF (reason why both angular and spring are bundled in the same container)
* Proper security and user management still needs to be implemented.
* Many missing features.
* Overal UI improvements needs to be made.

