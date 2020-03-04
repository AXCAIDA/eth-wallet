package io.acrosafe.wallet.eth.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.7.
 */
@SuppressWarnings("rawtypes")
public class InternalAddress extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506105c48061005e6000396000f300606060405260043610610061576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168062821de3146101525780633ef13367146101a75780636b9f96ea146101e05780639b08e8fc146101f5575b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc349081150290604051600060405180830381858888f1935050505015156100c257600080fd5b7f6f64e4e5328485620ab88ba6c20b533410fb14c6be8fe9842d1fa7852cb7226a3334600036604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200180602001828103825284848281815260200192508082843782019150509550505050505060405180910390a1005b341561015d57600080fd5b61016561022e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156101b257600080fd5b6101de600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610253565b005b34156101eb57600080fd5b6101f3610480565b005b341561020057600080fd5b61022c600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506104fa565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156102b357600080fd5b8392503091508273ffffffffffffffffffffffffffffffffffffffff166370a08231836000604051602001526040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b151561035c57600080fd5b6102c65a03f1151561036d57600080fd5b50505060405180519050905060008114156103875761047a565b8273ffffffffffffffffffffffffffffffffffffffff1663a9059cbb6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16836000604051602001526040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b151561045357600080fd5b6102c65a03f1151561046457600080fd5b50505060405180519050151561047957600080fd5b5b50505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc3073ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f1935050505015156104f857600080fd5b565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561055557600080fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505600a165627a7a723058200d8c0c288aa12ac6c945e4e937fb9f7af0ff93f5055fa8a35a9746eb02e940f40029";

    public static final String FUNC_PARENTADDRESS = "parentAddress";

    public static final String FUNC_FLUSHTOKENS = "flushTokens";

    public static final String FUNC_FLUSH = "flush";

    public static final String FUNC_CHANGEPARENT = "changeParent";

    public static final Event INTERNALADDRESSDEPOSITED_EVENT = new Event("InternalAddressDeposited", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    @Deprecated
    protected InternalAddress(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected InternalAddress(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected InternalAddress(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected InternalAddress(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> parentAddress() {
        final Function function = new Function(FUNC_PARENTADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> flushTokens(String tokenContractAddress) {
        final Function function = new Function(
                FUNC_FLUSHTOKENS, 
                Arrays.<Type>asList(new Address(160, tokenContractAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> flush() {
        final Function function = new Function(
                FUNC_FLUSH, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> changeParent(String newParentAddress) {
        final Function function = new Function(
                FUNC_CHANGEPARENT, 
                Arrays.<Type>asList(new Address(160, newParentAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<InternalAddressDepositedEventResponse> getInternalAddressDepositedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(INTERNALADDRESSDEPOSITED_EVENT, transactionReceipt);
        ArrayList<InternalAddressDepositedEventResponse> responses = new ArrayList<InternalAddressDepositedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            InternalAddressDepositedEventResponse typedResponse = new InternalAddressDepositedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<InternalAddressDepositedEventResponse> internalAddressDepositedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, InternalAddressDepositedEventResponse>() {
            @Override
            public InternalAddressDepositedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(INTERNALADDRESSDEPOSITED_EVENT, log);
                InternalAddressDepositedEventResponse typedResponse = new InternalAddressDepositedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<InternalAddressDepositedEventResponse> internalAddressDepositedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(INTERNALADDRESSDEPOSITED_EVENT));
        return internalAddressDepositedEventFlowable(filter);
    }

    @Deprecated
    public static InternalAddress load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new InternalAddress(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static InternalAddress load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new InternalAddress(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static InternalAddress load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new InternalAddress(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static InternalAddress load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new InternalAddress(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<InternalAddress> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(InternalAddress.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<InternalAddress> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(InternalAddress.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<InternalAddress> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(InternalAddress.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<InternalAddress> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(InternalAddress.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class InternalAddressDepositedEventResponse extends BaseEventResponse {
        public String from;

        public BigInteger value;

        public byte[] data;
    }
}
