# Structure

application-layer:
	- rest-api
	- log4j-gitlab-issue-appender
	- profiles

domain-layer:
	- core
	- test
	- utils

infrastructure-layer:
	- persistence
		- production
		- in-memory
		- test
		- hibernate-postgres-json-support
		- migrations