package io.acrosafe.wallet.eth.exception;

public class InvalidCoinSymbolException extends Exception
{
    /**
     * Constructs new InvalidCoinSymbolException instance.
     */
    public InvalidCoinSymbolException()
    {
        super();
    }

    /**
     * Constructs new InvalidCoinSymbolException.
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidCoinSymbolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Constructs new InvalidCoinSymbolException.
     * 
     * @param message
     * @param cause
     */
    public InvalidCoinSymbolException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs new InvalidCoinSymbolException.
     * 
     * @param message
     */
    public InvalidCoinSymbolException(String message)
    {
        super(message);
    }

    /**
     * Constructs new InvalidCoinSymbolException.
     * 
     * @param cause
     */
    public InvalidCoinSymbolException(Throwable cause)
    {
        super(cause);
    }
}
