# dynamodb-crud-api
AWS dynamodb crud api based on low-lewel ddb api

### Prerequisites

This api works with AWS DynamoDB thus you should have
1) AWS account set up and configured
2) .aws/credentials file in your file system OR env variables for access key/secret configured.
  Pls refer to [documentation](https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html)
  #### Explanation: 
  Tests use DefaultAWSCredentialsProviderChain which looks for credentials in this order:
  
  * Environment Variables - AWS_ACCESS_KEY_ID/AWS_SECRET_ACCESS_KEY (recognized by all the AWS SDKs and CLI),
    or AWS_ACCESS_KEY/AWS_SECRET_KEY(only recognized by Java SDK)
  * Java System Properties - aws.accessKeyId and aws.secretKey
  * Credential profiles file at the default location (~/.aws/credentials)
3) dynamoDB storage service activated for your account
4) for test purposes pls [create table](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SampleData.CreateTables.html) ShopProducts with simple key id (string)

### Build & Test
This is a regular, Maven based project.
Just run `mvn clean package`

### Usage

1. Create crud wrapper api which will be working with your dynamoDB table:
1.1 Create amazon dynamodb client instance using desired credentials provider and aws region the db resides in:
```java
 AmazonDynamoDB amazonClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.US_EAST_1)
                .build();
```

1.2 Create crud wrapper api extending from AbstractDynamoDBTableCRUDApi passing amazon dynamodb client, table name, key function and (optionally) supplier for "not found" exception:

```java
public class ProductsTableCRUDApi extends AbstractDynamoDBTableCRUDApi {

    public ProductsTableCRUDApi(AmazonDynamoDB amazonDynamoDBClient) {
       super("ShopProducts", id -> Key.builder().hashKeyName("id").hashKeyValue(id).build(), amazonDynamoDBClient, () -> new RuntimeException("Product can't be found"));
    }

}
```
here in example above we create crud api to ShopProducts ddb table with simple hash key "id"
