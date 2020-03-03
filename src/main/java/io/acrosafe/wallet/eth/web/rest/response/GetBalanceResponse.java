package io.acrosafe.wallet.eth.web.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetBalanceResponse extends Response
{
    @JsonProperty("balance")
    private String balance;

    @JsonProperty("balance_in_wei")
    private String balanceInWei;

    public String getBalance()
    {
        return balance;
    }

    public void setBalance(String balance)
    {
        this.balance = balance;
    }

    public String getBalanceInWei()
    {
        return balanceInWei;
    }

    public void setBalanceInWei(String balanceInWei)
    {
        this.balanceInWei = balanceInWei;
    }
}
