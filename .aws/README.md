# AWS configuration

We use AWS ElasticBeanstalk to host the application, however, everything else apart from the actual EC2 instance is setup separately.
This means that the EB configuration is very lightweight and can be thrown away and re-created without destroying all the attached resources.

This however, imposes a few things that need to be thought of whenever the AWS configuration is changed.

1. The endpoint to the database instance needs to be manually set correctly because the AWS EB setup is not aware of any database. (`DATABASE_CONNECTION_STRING` env variable.)
2. The EC2 instance needs to be part of the target group of the load balancer because the AWS EB setup is not aware of the load balancer.

For some configuration changes, EB replaces the EC2 instance. In that case, the EC2 needs to be re-attached to the load balancer's target group manually.

The actual route (api.nobt.io) is configured to point to the load balancer.

Also take a look at the notes of the security groups, to get an idea about what they do.