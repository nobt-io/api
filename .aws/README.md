# AWS configuration

We use AWS EB to host the application, however, everything else apart from the actual EC2 instance is setup separately.
This means that the EB configuration is very lightweight and can be thrown away and re-created without destroying all the attached resources.

This however, imposes a few things that need to be thought of whenever the AWS configuration is changed.

1. The endpoint to the database instance needs to be set correctly.
2. The EC2 instance needs to be part of the target group of the load balancer.

For some configuration changes, EB replaces the EC2 instance. In that case, the EC2 needs to be re-attached to the load balancer's target group manually.