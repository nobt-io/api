# Structure

application-layer:
	- rest-api
	- log4j-gitlab-issue-appender
	- application-config

domain-layer:
	- core
	- domain-test-support
	- util

infrastructure-layer:
	- persistence
		- persistence
		- inmemory-persistence
		- persistence-test-support
		- hibernate-postgres-json-support
		- db-migrations