package Workers;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;


public class Worker_sqsOPS {

    public String queueUrl = null;

    public Worker_sqsOPS() {
    }

    public String createSQS(String QUEUE_NAME) {

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
        try {
            CreateQueueRequest request = CreateQueueRequest.builder()
                    .queueName(QUEUE_NAME)
                    .build();
            CreateQueueResponse createResult = sqsClient.createQueue(request);

            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(QUEUE_NAME)
                    .build();

            queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

        } catch (QueueNameExistsException e) {
            throw e;
        }

        return queueUrl;

    }

    public void SendMessage(String message) {
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(this.queueUrl)
                .messageBody(message)
                .build();
        sqsClient.sendMessage(sendMsgRequest);
        System.out.println("sent message");

    }
}