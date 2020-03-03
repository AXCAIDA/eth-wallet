package io.acrosafe.wallet.eth.web.rest.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateEnterpriseAccountResponse extends Response
{
    @JsonProperty("id")
    private String id;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("label")
    private String label;

    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("address")
    private String address;

    @JsonProperty("created_date")
    private Instant createdDate;

    /**
     * Returns the wallet id.
     * 
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the wallet id.
     * 
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Returns the coin symbol.
     * 
     * @return
     */
    public String getSymbol()
    {
        return symbol;
    }

    /**
     * Sets the coin symbol.
     * 
     * @param symbol
     */
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public Instant getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate)
    {
        this.createdDate = createdDate;
    }
}
