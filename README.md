# deep-dive
A repository for marine monitoring data.

# Getting Started

## Before Starting

1. Clone the repository

Since the development status is pre-alpha, use the [development branch](https://github.com/atware/deep-dive).

2. Have a [Postgres database](http://www.postgresql.org/) ready to go. Specifically, a 9.4.+ version of Postgres.

By default Deep-Dive has [Play Evolutions](https://www.playframework.com/documentation/2.3.x/Evolutions) enabled. So you should add your
database credentials to the [conf/application.conf configuration file](https://www.playframework.com/documentation/2.3.x/Configuration).
Then start the app- evolutions will load the example data automatically.

3. Install Activator

## Starting

From the command line, in the root of the project directory, run 'activator'. Once it has loaded, 'run'.


## Testing

To run all tests both functional and unit, run 'test'.

To run only functional tests, 'test-only funspec.*'
