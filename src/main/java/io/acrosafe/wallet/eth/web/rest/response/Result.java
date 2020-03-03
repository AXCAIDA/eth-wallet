package io.acrosafe.wallet.eth.web.rest.response;

public enum Result
{
    SUCCESS(2000),

    // HTTP 4xx error
    ACCOUNT_ALREADY_EXISTED(4001),
    ACCOUNT_NOT_FOUND(4002),
    INVALID_PASSPHRASE(4003),
    INVALID_WALLET_NAME(4004),
    INVALID_COIN_SYMBOL(4005),
    INVALID_ENTERPRISE_ACCOUNT(4006),
    WALLET_NOT_FOUND(4007),

    // HTTP 5xx error
    UNKNOWN_ERROR(5000),
    SERVICE_NOT_READY(5001),
    INVALID_CRYPTO_OPERATION(5002),
    INSUFFICIENT_BALANCE(5003),
    INVALID_CREDENTIALS(5004),
    GET_BALANCE_FAILED(5005),
    CREATE_CONTRACT_FAILED(5006),
    TOKEN_NOT_SUPPORTED(5007);

    private int code;

    /**
     * Constructs new Result enum instance.
     *
     * @param code
     */
    Result(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public static Result valueOf(int code)
    {
        for (Result response : Result.values())
        {
            if (response.code == code)
            {
                return response;
            }
        }

        return UNKNOWN_ERROR;
    }
}
