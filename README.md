# Document engine AWS S3 provider

[![Build](https://github.com/get-the-docs/docs-aws-provider-s3/actions/workflows/build.yml/badge.svg)](https://github.com/get-the-docs/docs-aws-provider-s3/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=get-the-docs_docs-aws-provider-s3&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=get-the-docs_docs-aws-provider-s3)
[![codecov](https://codecov.io/gh/get-the-docs/docs-aws-provider-s3/graph/badge.svg?token=3AI64GBX2M)](https://codecov.io/gh/get-the-docs/docs-aws-provider-s3)

AWS S3 provider library for the GetTheDocs document engine to provide
- Template repository: see AwsS3TemplateRepository
- Result store: see AwsS3ResultStore
- Document structure repository: see AwsS3DocumentStructureRepository

See the GetTheDocs project documentation at [www.getthedocs.tech](https://www.getthedocs.tech) for the main engine functionality.

## Usage

### Getting Started

Add the dependency via **Maven**:

```xml
<dependency>
  <groupId>org.getthedocs.documentengine</groupId>
  <artifactId>docs-core-api</artifactId>
  <version>${document-engine.version}</version>
</dependency>
<dependency>
  <groupId>org.getthedocs.documentengine</groupId>
  <artifactId>docs-core</artifactId>
  <version>0.1.0</version>
</dependency>

<dependency>
  <groupId>org.getthedocs.provider</groupId>
  <artifactId>docs-aws-provider-s3</artifactId>
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
repository.template.provider.aws.s3.bucketname=<your-template-bucket-name>
repository.template.provider.aws.s3.region=<your-template-bucket-region>
repository.template.provider.aws.s3.prefix=<S3-prefix-for-templates>

# Document structure repository provider
repository.documentstructure.provider=org.getthedocs.documentengine.core.provider.documentstructure.repository.aws.s3.AwsS3DocumentStructureRepository
repository.documentstructure.builder=org.getthedocs.documentengine.core.provider.documentstructure.builder.yaml.YmlDocStructureBuilder
repository.documentstructure.provider.aws.s3.bucketname=<your-template-bucket-name>
repository.documentstructure.provider.aws.s3.region=<your-template-bucket-region>
repository.documentstructure.provider.aws.s3.prefix=<S3-prefix-for-document-structures>

# Result repository provider class
repository.result.provider=org.getthedocs.documentengine.core.provider.resultstore.aws.s3.AwsS3ResultStore
repository.result.provider.aws.s3.bucketname=<your-results-bucket-name>
repository.result.provider.aws.s3.region=<your-results-bucket-region>
repository.result.provider.aws.s3.prefix=<S3-prefix-for-results>

# InputTemplate processors
# -----------------------------
processors.docx=org.getthedocs.documentengine.core.processor.input.docx.DocxStamperInputTemplateProcessor
processors.xlsx=org.getthedocs.documentengine.core.processor.input.xlsx.JxlsInputTemplateProcessor


```

#### Environment variables

Add the environment variables below to the project configuration or shell:

| Name                                           | Description                                                       |
|------------------------------------------------|-------------------------------------------------------------------|
| AWS_ACCESS_KEY_ID                              | The AWS access key id for a user having S3 object RW permissions. |
| AWS_SECRET_ACCESS_KEY                          | The secret key for the access key id                              |

### Single document generation

To create a simple document

```java
    public OutputStream generateContractDocument() {
        MyDTO dto = generateData();
        return TemplateServiceRegistry.getInstance().fill("MyTemplate.docx", dto, OutputFormat.PDF);
    }
```

### Document structure

Place the document structures you need in the `template_bundle_descriptors/documentstructures` directory
(when using the above example), and use the following YAML format to define them:

```yaml
# contract_v02.yml
---
documentStructureId: "109a562d-8290-407d-98e1-e5e31c9808b7"
elements:
  - templateElementId:
      id: "cover"
    templateNames:
      hu_HU: "/full-example/01-cover_v03.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "contract"
    templateNames:
      en: "/full-example/02-contract_v09_en.docx"
      hu: "/full-example/02-contract_v09_hu.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "terms"
    templateNames:
      hu: "/full-example/03-terms_v02.docx"
    defaultLocale: "hu_HU"
    count: 1
  - templateElementId:
      id: "conditions"
    templateNames:
      hu: "/full-example/04-conditions_eco_v11.xlsx"
    defaultLocale: "hu_HU"
    count: 1
resultMode: "SEPARATE_DOCUMENTS"
outputFormat: "UNCHANGED"
copies: 1

```
then invoke the template engine with the document structure ID:

```java
    public OutputStream generateContractDocuments() {
        TemplateService templateService = TemplateServiceRegistry.getInstance();
    
        ValueSet dto = new ValueSet(transactionId);
        dto
                .addContext("cover", getCoverData(transactionId))
                .addContext("contract", getContractTestData())
                .addDefaultContext("terms", null)
                .addContext("conditions", getContractTestData());
    
        final StoredGenerationResult result = templateService
                .fillAndSaveDocumentStructureByName("contract/vintage/contract-vintage_v02-separate.yml", dto);
    }
```



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
