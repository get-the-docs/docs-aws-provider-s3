# Document engine AWS S3 provider

[![Build](https://github.com/get-the-docs/docs-aws-provider-s3/actions/workflows/build.yml/badge.svg)](https://github.com/get-the-docs/docs-aws-provider-s3/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=get-the-docs_docs-aws-provider-s3&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=get-the-docs_docs-aws-provider-s3)
[![codecov](https://codecov.io/gh/get-the-docs/docs-aws-provider-s3/graph/badge.svg?token=3AI64GBX2M)](https://codecov.io/gh/get-the-docs/docs-aws-provider-s3)

AWS S3 provider library to provide
- Template repository: see AwsS3TemplateRepository
- Result store: see AwsS3ResultStore
- Document structure repository: see AwsS3DocumentStructureRepository

## Usage

### Getting Started

Add the dependency via **Maven**:

```xml
<dependency>
  <groupId>org.getthedocs.documentengine</groupId>
  <artifactId>docs-core</artifactId>
  <version>0.1.0</version>
</dependency>
```

If not using spring, add the SpEL library of your needs (you can lookup the version the engine was tested with in the docs-core project):

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-expression</artifactId>
  <version>6.0.8</version>
</dependency>
```

#### document-engine.properties

```properties
# Template repository provider class
repository.template.provider=org.getthedocs.documentengine.core.provider.templaterepository.aws.s3.AwsS3TemplateRepository
repository.template.provider.aws.s3.bucketname=<your_bucket_name>
repository.template.provider.aws.s3.region=<bucket_region>
repository.template.provider.aws.s3.prefix=<prefix_to_use_objects_from>
# Result repository provider class
repository.result.provider=org.getthedocs.documentengine.core.provider.resultstore.aws.s3.AwsS3ResultStore
repository.result.provider.aws.s3.bucketname=<your_bucket_name>
repository.result.provider.aws.s3.region=<bucket_region>
repository.result.provider.aws.s3.prefix=<prefix_to_use_objects_from>
# Document structure repository provider
repository.documentstructure.provider=org.getthedocs.documentengine.core.provider.documentstructure.repository.aws.s3.AwsS3DocumentStructureRepository
repository.documentstructure.builder=org.getthedocs.documentengine.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder
repository.documentstructure.provider.aws.s3.bucketname=<your_bucket_name>
repository.documentstructure.provider.aws.s3.region=<bucket_region>
repository.documentstructure.provider.aws.s3.prefix=<prefix_to_use_objects_from>

```

#### Environment variables

Add the environment variables below to the project configuration or shell:

| Name                                           | Description                                                       |
|------------------------------------------------|-------------------------------------------------------------------|
| AWS_ACCESS_KEY_ID                              | The AWS access key id for a user having S3 object RW permissions. |
| AWS_SECRET_ACCESS_KEY                          | The secret key for the access key id                              |


## Local build

To compile the project locally some configuration settings are needed:

- **AWS S3 template repository**:

  What you will need:
  an AWS account and an S3 bucket.

  Add the environment variables below to the project configuration or shell:

| Name                                           | Description                                                       |
|------------------------------------------------|-------------------------------------------------------------------|
| GETTHEDOCS_REPO_TEMPLATE_AWS_S3_BUCKETNAME     | Your test bucket's name                                           | 
| GETTHEDOCS_REPO_DOCSTRUCTURE_AWS_S3_BUCKETNAME | Your test bucket's name                                           |
| GETTHEDOCS_REPO_RESULT_AWS_S3_BUCKETNAME       | Your test bucket's name                                           |
| AWS_ACCESS_KEY_ID                              | The AWS access key id for a user having S3 object RW permissions. |
| AWS_SECRET_ACCESS_KEY                          | The secret key for the access key id                              |


## Project tooling
Issue tracking: [Get-the-docs project @ Atlassian Jira](https://getthedocs.atlassian.net/jira/software/c/projects/GD/boards/1)
