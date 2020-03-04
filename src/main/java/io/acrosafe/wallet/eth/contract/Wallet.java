package io.acrosafe.wallet.eth.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
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
public class Wallet extends Contract {
    private static final String BINARY = "60606040526000600160006101000a81548160ff021916908315150217905550341561002a57600080fd5b604051610f86380380610f86833981016040528080518201919050506003815114151561005657600080fd5b806000908051906020019061006c929190610073565b5050610140565b8280548282559060005260206000209081019282156100ec579160200282015b828111156100eb5782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555091602001919060010190610093565b5b5090506100f991906100fd565b5090565b61013d91905b8082111561013957600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600101610103565b5090565b90565b610e378061014f6000396000f30060606040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630dcd7a6c146101285780632079fb9a146101de57806339125215146102415780635ef680df1461031b5780637df73e2714610373578063a0b7967b146103c4578063abe3219c146103ed578063fc0f392d1461041a575b6000341115610126577f6e89d517057028190560dd200cf6bf792842861353d1173761dfa362e1c133f03334600036604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200180602001828103825284848281815260200192508082843782019150509550505050505060405180910390a15b005b341561013357600080fd5b6101dc600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803590602001909190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190505061042f565b005b34156101e957600080fd5b6101ff6004808035906020019091905050610608565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561024c57600080fd5b610319600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001909190803590602001909190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610647565b005b341561032657600080fd5b610371600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610946565b005b341561037e57600080fd5b6103aa600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610a12565b604051808215151515815260200191505060405180910390f35b34156103cf57600080fd5b6103d7610ab6565b6040518082815260200191505060405180910390f35b34156103f857600080fd5b610400610b10565b604051808215151515815260200191505060405180910390f35b341561042557600080fd5b61042d610b23565b005b60008061043b33610a12565b151561044657600080fd5b878787878760405180807f45524332300000000000000000000000000000000000000000000000000000008152506005018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c010000000000000000000000000281526014018581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c0100000000000000000000000002815260140183815260200182815260200195505050505050604051809103902091506105298883858888610bb6565b508590508073ffffffffffffffffffffffffffffffffffffffff1663a9059cbb89896000604051602001526040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b15156105d857600080fd5b6102c65a03f115156105e957600080fd5b5050506040518051905015156105fe57600080fd5b5050505050505050565b60008181548110151561061757fe5b90600052602060002090016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008061065333610a12565b151561065e57600080fd5b878787878760405180807f45544845520000000000000000000000000000000000000000000000000000008152506005018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c0100000000000000000000000002815260140185815260200184805190602001908083835b60208310151561070757805182526020820191506020810190506020830392506106e2565b6001836020036101000a03801982511681845116808217855250505050505090500183815260200182815260200195505050505050604051809103902091506107538883858888610bb6565b90508773ffffffffffffffffffffffffffffffffffffffff16878760405180828051906020019080838360005b8381101561079b578082015181840152602081019050610780565b50505050905090810190601f1680156107c85780820380516001836020036101000a031916815260200191505b5091505060006040518083038185876187965a03f19250505015156107ec57600080fd5b7f59bed9ab5d78073465dd642a9e3e76dfdb7d53bcae9d09df7d0b8f5234d5a8063382848b8b8b604051808773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200185600019166000191681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156108fd5780820151818401526020810190506108e2565b50505050905090810190601f16801561092a5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a15050505050505050565b600061095133610a12565b151561095c57600080fd5b8290508073ffffffffffffffffffffffffffffffffffffffff16633ef13367836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b15156109f957600080fd5b6102c65a03f11515610a0a57600080fd5b505050505050565b600080600090505b600080549050811015610aab578273ffffffffffffffffffffffffffffffffffffffff16600082815481101515610a4d57fe5b906000526020600020900160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415610a9e5760019150610ab0565b8080600101915050610a1a565b600091505b50919050565b6000806000809150600090505b600a811015610b055781600282600a81101515610adc57fe5b01541115610af857600281600a81101515610af357fe5b015491505b8080600101915050610ac3565b600182019250505090565b600160009054906101000a900460ff1681565b610b2c33610a12565b1515610b3757600080fd5b60018060006101000a81548160ff0219169083151502179055507f0909e8f76a4fd3e970f2eaef56c0ee6dfaf8b87c5b8d3f56ffce78e825a9115733604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a1565b600080610bc38686610c61565b9050600160009054906101000a900460ff168015610be75750610be587610a12565b155b15610bf157600080fd5b42841015610bfe57600080fd5b610c0783610d34565b610c1081610a12565b1515610c1b57600080fd5b3373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610c5457600080fd5b8091505095945050505050565b60008060008060418551141515610c7757600080fd5b602085015192506040850151915060ff6041860151169050601b8160ff161015610ca257601b810190505b600186828585604051600081526020016040526000604051602001526040518085600019166000191681526020018460ff1660ff16815260200183600019166000191681526020018260001916600019168152602001945050505050602060405160208103908084039060008661646e5a03f11515610d2057600080fd5b505060206040510351935050505092915050565b600080610d4033610a12565b1515610d4b57600080fd5b60009150600090505b600a811015610db35782600282600a81101515610d6d57fe5b01541415610d7a57600080fd5b600282600a81101515610d8957fe5b0154600282600a81101515610d9a57fe5b01541015610da6578091505b8080600101915050610d54565b600282600a81101515610dc257fe5b0154831015610dd057600080fd5b612710600283600a81101515610de257fe5b015401831115610df157600080fd5b82600283600a81101515610e0157fe5b01819055505050505600a165627a7a7230582023a711559c73f45f44195fba96c000dd953d2adf2c2b8448c24b995f389f40700029";

    public static final String FUNC_SENDMULTISIGTOKEN = "sendMultiSigToken";

    public static final String FUNC_SIGNERS = "signers";

    public static final String FUNC_SENDMULTISIG = "sendMultiSig";

    public static final String FUNC_FLUSHTOKENS = "flushTokens";

    public static final String FUNC_ISSIGNER = "isSigner";

    public static final String FUNC_GETNEXTSEQUENCEID = "getNextSequenceId";

    public static final String FUNC_SAFEMODE = "safeMode";

    public static final String FUNC_ACTIVATESAFEMODE = "activateSafeMode";

    public static final Event DEPOSITED_EVENT = new Event("Deposited", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event SAFEMODEACTIVATED_EVENT = new Event("SafeModeActivated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event TRANSACTED_EVENT = new Event("Transacted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    @Deprecated
    protected Wallet(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Wallet(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Wallet(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Wallet(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> sendMultiSigToken(String toAddress, BigInteger value, String tokenContractAddress, BigInteger expireTime, BigInteger sequenceId, byte[] signature) {
        final Function function = new Function(
                FUNC_SENDMULTISIGTOKEN, 
                Arrays.<Type>asList(new Address(160, toAddress),
                new Uint256(value),
                new Address(160, tokenContractAddress),
                new Uint256(expireTime),
                new Uint256(sequenceId),
                new DynamicBytes(signature)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> signers(BigInteger param0) {
        final Function function = new Function(FUNC_SIGNERS, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> sendMultiSig(String toAddress, BigInteger value, byte[] data, BigInteger expireTime, BigInteger sequenceId, byte[] signature) {
        final Function function = new Function(
                FUNC_SENDMULTISIG, 
                Arrays.<Type>asList(new Address(160, toAddress),
                new Uint256(value),
                new DynamicBytes(data),
                new Uint256(expireTime),
                new Uint256(sequenceId),
                new DynamicBytes(signature)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> flushTokens(String forwarderAddress, String tokenContractAddress) {
        final Function function = new Function(
                FUNC_FLUSHTOKENS, 
                Arrays.<Type>asList(new Address(160, forwarderAddress),
                new Address(160, tokenContractAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isSigner(String signer) {
        final Function function = new Function(FUNC_ISSIGNER, 
                Arrays.<Type>asList(new Address(160, signer)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> getNextSequenceId() {
        final Function function = new Function(FUNC_GETNEXTSEQUENCEID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> safeMode() {
        final Function function = new Function(FUNC_SAFEMODE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> activateSafeMode() {
        final Function function = new Function(
                FUNC_ACTIVATESAFEMODE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<DepositedEventResponse> getDepositedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DEPOSITED_EVENT, transactionReceipt);
        ArrayList<DepositedEventResponse> responses = new ArrayList<DepositedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DepositedEventResponse typedResponse = new DepositedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DepositedEventResponse> depositedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, DepositedEventResponse>() {
            @Override
            public DepositedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DEPOSITED_EVENT, log);
                DepositedEventResponse typedResponse = new DepositedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DepositedEventResponse> depositedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DEPOSITED_EVENT));
        return depositedEventFlowable(filter);
    }

    public List<SafeModeActivatedEventResponse> getSafeModeActivatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SAFEMODEACTIVATED_EVENT, transactionReceipt);
        ArrayList<SafeModeActivatedEventResponse> responses = new ArrayList<SafeModeActivatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SafeModeActivatedEventResponse typedResponse = new SafeModeActivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.msgSender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SafeModeActivatedEventResponse> safeModeActivatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, SafeModeActivatedEventResponse>() {
            @Override
            public SafeModeActivatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(SAFEMODEACTIVATED_EVENT, log);
                SafeModeActivatedEventResponse typedResponse = new SafeModeActivatedEventResponse();
                typedResponse.log = log;
                typedResponse.msgSender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SafeModeActivatedEventResponse> safeModeActivatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SAFEMODEACTIVATED_EVENT));
        return safeModeActivatedEventFlowable(filter);
    }

    public List<TransactedEventResponse> getTransactedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTED_EVENT, transactionReceipt);
        ArrayList<TransactedEventResponse> responses = new ArrayList<TransactedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransactedEventResponse typedResponse = new TransactedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.msgSender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.otherSigner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.toAddress = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransactedEventResponse> transactedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransactedEventResponse>() {
            @Override
            public TransactedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSACTED_EVENT, log);
                TransactedEventResponse typedResponse = new TransactedEventResponse();
                typedResponse.log = log;
                typedResponse.msgSender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.otherSigner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.operation = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.toAddress = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(5).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransactedEventResponse> transactedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSACTED_EVENT));
        return transactedEventFlowable(filter);
    }

    @Deprecated
    public static Wallet load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Wallet(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Wallet load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Wallet(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Wallet load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Wallet(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Wallet load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Wallet(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Wallet> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, List<String> allowedSigners) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(allowedSigners, Address.class))));
        return deployRemoteCall(Wallet.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Wallet> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, List<String> allowedSigners) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(allowedSigners, Address.class))));
        return deployRemoteCall(Wallet.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Wallet> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, List<String> allowedSigners) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(allowedSigners, Address.class))));
        return deployRemoteCall(Wallet.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Wallet> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, List<String> allowedSigners) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Address>(
                        Address.class,
                        org.web3j.abi.Utils.typeMap(allowedSigners, Address.class))));
        return deployRemoteCall(Wallet.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class DepositedEventResponse extends BaseEventResponse {
        public String from;

        public BigInteger value;

        public byte[] data;
    }

    public static class SafeModeActivatedEventResponse extends BaseEventResponse {
        public String msgSender;
    }

    public static class TransactedEventResponse extends BaseEventResponse {
        public String msgSender;

        public String otherSigner;

        public byte[] operation;

        public String toAddress;

        public BigInteger value;

        public byte[] data;
    }
}
