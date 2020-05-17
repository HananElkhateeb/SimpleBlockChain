package com.parsing.messages.payloads;

import com.parsing.messages.payloads.types.*;

public class PayloadFactory {

    public Payload getPayload(PayloadTypes payloadType){
        if(payloadType == null){ return null; }

        switch (payloadType) {
            case VOTE_PAYLOAD:
                return new VotePayload();
            case GET_PEERS_PAYLOAD:
                return new GetPeersPayload();
            case PEER_ACK_PAYLOAD:
                return new PeerAckPayload();
            case BLOCK_PAYLOAD:
                return new BlockPayload();
            case TRANSACTION_PAYLOAD:
                return new TransactionPayload();
            default:
                return null;
        }

    }
}
