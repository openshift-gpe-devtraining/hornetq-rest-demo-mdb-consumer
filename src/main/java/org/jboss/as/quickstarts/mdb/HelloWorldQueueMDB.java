/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.mdb;

import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.BytesMessage;

/**
 * <p>
 * A simple Message Driven Bean that asynchronously receives and processes the messages that are sent to the queue.
 * </p>
 * 
 * @author Serge Pagop (spagop@redhat.com)
 * 
 */
@MessageDriven(name = "HelloWorldQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class HelloWorldQueueMDB implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(HelloWorldQueueMDB.class.toString());

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message rcvMessage) {
        try {
            String data = null;

            if (rcvMessage instanceof TextMessage) {
                TextMessage message = (TextMessage) rcvMessage;
                data = message.getText();
            } else if (rcvMessage instanceof BytesMessage) {
                BytesMessage message = (BytesMessage) rcvMessage;
                data = convert(message);
            }
            else {
                data = rcvMessage.toString();
            }

            LOGGER.info("Received Message from queue: " + data);

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method to convert byte message to string
     */
    private String convert(BytesMessage byteMessage) throws JMSException {

            // read bytes from message
            byte[] byteArr = new byte[(int)byteMessage.getBodyLength()];
            byteMessage.readBytes(byteArr);

            // create String based on byte array
            String msg = new String(byteArr); 

            return msg;
    }
}
