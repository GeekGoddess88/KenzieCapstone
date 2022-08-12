# ATA-Capstone-Project

Follow the instructions in the course for completing the group Capstone project.

### To create the Lambda Example table in DynamoDB:

You must do this for the ServiceLambda to work!

```
aws cloudformation create-stack --stack-name lambda-table --template-body file://LambdaExampleTable.yml --capabilities CAPABILITY_IAM
```

### To deploy the CI/CD Pipeline

Fill out `setupEnvironment.sh` with your Github Repo name.

Run `./createPipeline.sh`

To teardown the pipeline, run `./cleanupPipeline.sh`


### To deploy the Development Environment

Run `./deployDev.sh`

To tear down the deployment then run `./cleanupDev.sh`