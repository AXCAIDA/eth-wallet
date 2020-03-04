/**
 * MIT License
 *
 * Copyright (c) 2020 acrosafe technologies
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.acrosafe.wallet.eth.web.rest;

import io.acrosafe.wallet.core.eth.exception.AccountNotFoundException;
import io.acrosafe.wallet.core.eth.exception.CryptoException;
import io.acrosafe.wallet.core.eth.exception.InvalidCredentialException;
import io.acrosafe.wallet.eth.domain.WalletRecord;
import io.acrosafe.wallet.eth.exception.InvalidCoinSymbolException;
import io.acrosafe.wallet.eth.exception.InvalidEnterpriseAccountException;
import io.acrosafe.wallet.eth.exception.InvalidPassphraseException;
import io.acrosafe.wallet.eth.exception.InvalidWalletNameException;
import io.acrosafe.wallet.eth.service.WalletService;
import io.acrosafe.wallet.eth.web.rest.request.CreateWalletRequest;
import io.acrosafe.wallet.eth.web.rest.response.CreateWalletResponse;
import io.acrosafe.wallet.eth.web.rest.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/eth/wallet")
public class WalletResources
{
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(WalletResources.class);

    @Autowired
    private WalletService service;

    @PostMapping("/new")
    public ResponseEntity<CreateWalletResponse> createWallet(@RequestBody CreateWalletRequest request)
    {
        CreateWalletResponse response = new CreateWalletResponse();
        try
        {
            WalletRecord walletRecord = service.createWallet(request.getSymbol(), request.getName(), request.getLabel(),
                    request.getPassphrase(), request.getEnterpriseId());
            response.setCreatedDate(walletRecord.getCreatedDate());
            response.setId(walletRecord.getId());
            response.setEnabled(walletRecord.isEnabled());
            response.setName(walletRecord.getName());

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        catch (InvalidPassphraseException e)
        {
            response.setResultCode(Result.INVALID_PASSPHRASE.getCode());
            response.setResult(Result.INVALID_PASSPHRASE);
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
        catch (InvalidWalletNameException e)
        {
            response.setResultCode(Result.INVALID_WALLET_NAME.getCode());
            response.setResult(Result.INVALID_WALLET_NAME);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (InvalidEnterpriseAccountException | AccountNotFoundException e)
        {
            response.setResultCode(Result.INVALID_ENTERPRISE_ACCOUNT.getCode());
            response.setResult(Result.INVALID_ENTERPRISE_ACCOUNT);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (InvalidCoinSymbolException e)
        {
            response.setResultCode(Result.INVALID_COIN_SYMBOL.getCode());
            response.setResult(Result.INVALID_COIN_SYMBOL);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (Throwable t)
        {
            logger.error("failed to create account.", t);
            response.setResultCode(Result.UNKNOWN_ERROR.getCode());
            response.setResult(Result.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
