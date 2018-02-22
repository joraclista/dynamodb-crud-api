# dynamodb-crud-api
AWS dynamodb crud api based on low-lewel ddb api

### Prerequisites

This api works with AWS DynamoDB thus you should have
1) AWS account set up and configured
2) .aws/credentials file in your file system (aws_access_key/aws_secret_access_key) OR env variables for access key/secret configured.
  Pls refer to [documentation](https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html)
3) dynamoDB storage service activated for your account
4) for test purposes pls [create table](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SampleData.CreateTables.html) ShopProducts with simple key id (string)

### Build & Test
This is a regular, Maven based project.
Just run `mvn clean package`
