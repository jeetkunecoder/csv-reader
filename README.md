# Scala CSV File Reader

Sample application to read CSV files and upload data to PostgreSQL. This project relies on the Alpakka library for Data Transformations and uses Play Framework to expose a REST API and Slick for DB queries.

## How do I get setup?

Install PostgreSQL. (This process applies for Mac, for installing in Linux
or Windows you can refer to ​http://postgresguide.com/setup/install.html​ ) 

```bash
$ b​rew install postgresql
```
 
Start PostgreSQL:

```bash
​$ brew services start postgres
```

Run psql:

```bash
$ psql postgres
```

Create a database, for example:
```bash
​postgres=#​ CREATE DATABASE mydb;
```

Check the database owner:
```bash
p​ostgres=#​ \l
```

Exit psql by typing ​exit;​ in the terminal

- Clone the project to a local directory 

- Open a terminal and ​navigate to the root of the ​csv-reader project previously downloaded.
Execute the 1.sql Postgres script to create the table. Substitute <db-owner> ​with the DB owner user found in previous steps.
```bash
$ psql -U <db-owner> -d mydb postgres -f resources/1.sql
```

- Open the project with IntelliJ and then open the application.conf file and change the DB user for your <​ db-owner>​ from previous steps.

Alternatively, you can also change this line with Vim or with any text editor.

Compile the project:
```bash
$ sbt compile
```

Run the tests:
```bash
$ sbt test
```

Run the application
```bash
$ sbt run
```

The application will start running in ​http://localhost:9000 and we can make a request to the ​/v1/banks/import endpoint to upload the rows from the .csv file in the database. This file is stored in the resources folder of the project. Invoke the csv file import by running the following cURL command in a different terminal:

```bash
$ curl -X POST h​ttp://localhost:9000/v1/banks/import
```

The name of the bank identifier will be printed in the other terminal (where we invoked the application) and the csv file rows will be imported into the Postgres database.
