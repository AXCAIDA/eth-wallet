package io.acrosafe.wallet.eth.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.web3j.utils.Convert;

import io.acrosafe.wallet.core.eth.exception.AccountNotFoundException;
import io.acrosafe.wallet.core.eth.exception.CryptoException;
import io.acrosafe.wallet.core.eth.exception.GetBalanceException;
import io.acrosafe.wallet.core.eth.exception.InvalidCredentialException;
import io.acrosafe.wallet.eth.domain.EnterpriseAccountRecord;
import io.acrosafe.wallet.eth.exception.InvalidCoinSymbolException;
import io.acrosafe.wallet.eth.service.EnterpriseAccountService;
import io.acrosafe.wallet.eth.web.rest.request.CreateEnterpriseAccountRequest;
import io.acrosafe.wallet.eth.web.rest.response.CreateEnterpriseAccountResponse;
import io.acrosafe.wallet.eth.web.rest.response.GetAddressResponse;
import io.acrosafe.wallet.eth.web.rest.response.GetBalanceResponse;
import io.acrosafe.wallet.eth.web.rest.response.Result;

@Controller
@RequestMapping("/api/v1/eth/enterprise/account")
public class EnterpriseAccountResources
{
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseAccountResources.class);

    @Autowired
    private EnterpriseAccountService service;

    @PostMapping("/new")
    public ResponseEntity<CreateEnterpriseAccountResponse>
            createEnterpriseAccount(@RequestBody CreateEnterpriseAccountRequest request)
    {
        CreateEnterpriseAccountResponse response = new CreateEnterpriseAccountResponse();
        try
        {
            EnterpriseAccountRecord record = service.createEnterpriseAccount(request.getLabel(), request.getSymbol());

            response.setAddress(record.getAddress());
            response.setCreatedDate(record.getCreatedDate());
            response.setEnabled(record.isEnabled());
            response.setId(record.getId());
            response.setLabel(record.getLabel());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (InvalidCoinSymbolException e)
        {
            response.setResultCode(Result.INVALID_COIN_SYMBOL.getCode());
            response.setResult(Result.INVALID_COIN_SYMBOL);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (CryptoException e)
        {
            response.setResultCode(Result.INVALID_CRYPTO_OPERATION.getCode());
            response.setResult(Result.INVALID_CRYPTO_OPERATION);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (InvalidCredentialException e)
        {
            response.setResultCode(Result.INVALID_CREDENTIALS.getCode());
            response.setResult(Result.INVALID_CREDENTIALS);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Throwable t)
        {
            logger.error("failed to create new enterprise account.", t);
            response.setResultCode(Result.UNKNOWN_ERROR.getCode());
            response.setResult(Result.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<GetBalanceResponse> getAccountBalance(@PathVariable String accountId)
    {
        GetBalanceResponse response = new GetBalanceResponse();
        try
        {
            String balance = this.service.getBalance(accountId);
            response.setBalanceInWei(balance);
            response.setBalance(Convert.fromWei(balance, Convert.Unit.ETHER).toString());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (AccountNotFoundException e)
        {
            response.setResultCode(Result.ACCOUNT_NOT_FOUND.getCode());
            response.setResult(Result.ACCOUNT_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (GetBalanceException e)
        {
            logger.error("failed to get enterprise account balance.", e);
            response.setResultCode(Result.GET_BALANCE_FAILED.getCode());
            response.setResult(Result.GET_BALANCE_FAILED);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Throwable t)
        {
            logger.error("failed to get enterprise account balance.", t);
            response.setResultCode(Result.UNKNOWN_ERROR.getCode());
            response.setResult(Result.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}/address")
    public ResponseEntity<GetAddressResponse> getAccountAddress(@PathVariable String accountId)
    {
        GetAddressResponse response = new GetAddressResponse();
        try
        {
            String address = this.service.getAccountAddress(accountId);
            response.setAddress(address);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (AccountNotFoundException e)
        {
            response.setResultCode(Result.ACCOUNT_NOT_FOUND.getCode());
            response.setResult(Result.ACCOUNT_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (Throwable e)
        {
            response.setResultCode(Result.UNKNOWN_ERROR.getCode());
            response.setResult(Result.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
