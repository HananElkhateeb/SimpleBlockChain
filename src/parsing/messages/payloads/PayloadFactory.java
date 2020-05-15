package parsing.messages.payloads;

import parsing.messages.payloads.types.PayloadTypes;
import parsing.messages.payloads.types.PublicKeyPayload;
import parsing.messages.payloads.types.TransactionPayload;
import parsing.messages.payloads.types.VotePayload;

public class PayloadFactory {

    public Payload getPayload(String payloadType){
        if(payloadType == null){ return null; }

        if(payloadType.equalsIgnoreCase(PayloadTypes.VOTE_PAYLOAD.toString())) {
            return new VotePayload();
        }
        else if(payloadType.equalsIgnoreCase(PayloadTypes.PUBLIC_KEY_PAYLOAD.toString())) {
            return new PublicKeyPayload();
        }
        else if(payloadType.equalsIgnoreCase(PayloadTypes.TRANSACTION_PAYLOAD.toString())) {
            return new TransactionPayload();
        }
        else {
            return null;
        }
    }
}
