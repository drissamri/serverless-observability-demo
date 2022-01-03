import os
import json
import boto3

sqs_client = boto3.client('sqs', region_name=os.environ['AWS_REGION'])

def handler(event, context):
    message = {"name": "hello"}

    response = sqs_client.send_message(
                    QueueUrl=os.environ.get('QUEUE_URL'),
                    MessageBody=json.dumps(message))

    body = {
        "message": "Go Serverless v2.0! Your function executed successfully!",
        "input": event,
    }

    return {
        "statusCode": 200,
        "body": json.dumps(body)
    }
